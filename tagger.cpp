#include "tagger.h"
#include <fstream>
#include <iostream>
#include <regex.h>
#include <cstdlib>
#include <cstring>
#include <lastfm/Track.h>
#include <taglib/fileref.h>
#include <taglib/taglib.h>
#include <QDir>

#define FP_BIN "/home/wlz/workspace/Musictagger/Fingerprinter/bin/lastfm-fpclient"
#define XML_TEMP_FILE "/home/wlz/workspace/Musictagger/temp_xml/temp.xml"
#define MAX_BUFFER_SIZE 2048

using namespace lastfm;
using namespace std;

tagger::tagger()
{

};

string tagger::music_path = "";
string tagger::xml_path = "";

int tagger::get_xml(){
    string rmcommand("rm -f ");
    rmcommand.append(XML_TEMP_FILE);
    system(rmcommand.c_str());
    string command(FP_BIN);
    command.append(" \"" + music_path + "\" ");
    command.append("> " + xml_path);
    int result = system(command.c_str());
    return result;
};

int tagger::extract_music_xml( string &xmlpath , string &artist , int &duration , string &title , string &mbid ){
    ifstream input(xmlpath.c_str());
    regex_t re_mbid;
    regex_t re_name;
    regex_t re_duration;

    char * reg_mbid = (char *)"<mbid>.*</mbid>";
    char * reg_name = (char *)"<name>.*</name>";
    char * reg_duration = (char *)"<duration\>.*</duration>";

    regcomp(&re_mbid,reg_mbid,REG_EXTENDED);
    regcomp(&re_duration,reg_duration,REG_EXTENDED);
    regcomp(&re_name,reg_name,REG_EXTENDED);

    string line;
    regmatch_t rem;

    bool match = false;
    while( !match ){
        if(input.eof()){
            regfree(&re_mbid);
            regfree(&re_name);
            regfree(&re_duration);
            return -1;
        }
        getline(input,line);
//        cout << line << endl;
        int result = regexec(&re_name,line.c_str(),1,&rem,0);
        if(result != 0)
            continue;
        match = true;

        title = line.substr(rem.rm_so+6,rem.rm_eo-rem.rm_so-13);
    }

    match = false;
    while( !match ){
        if(input.eof()){
            regfree(&re_mbid);
            regfree(&re_name);
            regfree(&re_duration);
            return -1;
        }
        getline(input,line);
        int result = regexec(&re_duration,line.c_str(),1,&rem,0);
        if(result != 0)
            continue;
        match = true;
        char buffer[256];
        strncpy(buffer,&line[rem.rm_so+10],rem.rm_eo-rem.rm_so-17);
        buffer[rem.rm_eo-rem.rm_so-17] = 0;
        duration = atoi(buffer);
    }
    match = false;
    while( !match ){
        if(input.eof()){
            regfree(&re_mbid);
            regfree(&re_name);
            regfree(&re_duration);
            return -1;
        }
        getline(input,line);
        int result = regexec(&re_mbid,line.c_str(),1,&rem,0);
        if(result != 0)
            continue;
        match = true;

        mbid = line.substr(rem.rm_so+6,rem.rm_eo-rem.rm_so-13);
    }
    match = false;
    while( !match ){
        if(input.eof()){
            regfree(&re_mbid);
            regfree(&re_name);
            regfree(&re_duration);
            return -1;
        }
        getline(input,line);
        int result = regexec(&re_name,line.c_str(),1,&rem,0);
        if(result != 0)
            continue;
        match = true;

        artist = line.substr(rem.rm_so+6,rem.rm_eo-rem.rm_so-13);
    }

    regfree(&re_mbid);
    regfree(&re_name);
    regfree(&re_duration);
    return 0;
};

int tagger::get_more_info( string &mbid ){


};

int tagger::fix( string &path ){
    music_path = path;
    xml_path = XML_TEMP_FILE;
    int result;
    result = get_xml();
    if(result != 0){
        return -1;
    }
    string mbid;
    string artist;
    string title;
    int duration;
    result = extract_music_xml( xml_path , artist , duration , title , mbid );
    if(result != 0){
        return -1;
    }
    //cout << mbid << endl;
    result = get_more_info( mbid );
    if(result != 0){
        return -1;
    }

    TagLib::FileRef f(music_path.c_str());
    TagLib::Tag * tag = f.tag();
    tag->setTitle(title);
    tag->setArtist(artist);
    f.save();
    QString file_name(music_path.c_str());
    QFile file(file_name);
    file.rename((title+".mp3").c_str());
    music_path.clear();
    xml_path.clear();
    return 0;
};

string get_title( string &path ){
    TagLib::FileRef f(path.c_str());
    TagLib::Tag * tag = f.tag();
    return tag->title().toCString();
}
string get_artist( string &path ){
    TagLib::FileRef f(path.c_str());
    TagLib::Tag * tag = f.tag();
    return tag->artist().toCString();
}
string get_album( string &path ){
    TagLib::FileRef f(path.c_str());
    TagLib::Tag * tag = f.tag();
    return tag->album().toCString();
}
string get_comment( string &path ){
    TagLib::FileRef f(path.c_str());
    TagLib::Tag * tag = f.tag();
    return tag->comment().toCString();
}
string get_genre( string &path ){
    TagLib::FileRef f(path.c_str());
    TagLib::Tag * tag = f.tag();
    return tag->genre().toCString();
}
int get_year( string &path ){
    TagLib::FileRef f(path.c_str());
    TagLib::Tag * tag = f.tag();
    return tag->year();
}
int get_track( string &path ){
    TagLib::FileRef f(path.c_str());
    TagLib::Tag * tag = f.tag();
    return tag->track();
}
