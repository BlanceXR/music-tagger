#ifndef TAGGER_H
#define TAGGER_H

#include <string>
#include <taglib.h>
using namespace std;

class tagger
{
private:
    static string music_path;
    static string xml_path;
public:
    tagger();
    static int get_xml();
    static int fix( string &path );
    static int extract_music_xml( string &xmlpath , string &artist , int &duration , string &title , string &mbid );
    static int get_more_info( string &mbid );

    static string get_title(string &path);
    static string get_artist(string &path);
    static string get_album(string &path);
    static string get_comment(string &path);
    static string get_genre(string &path);
    static int get_year(string &path);
    static int get_track(string &path);
};

#endif // TAGGER_H
