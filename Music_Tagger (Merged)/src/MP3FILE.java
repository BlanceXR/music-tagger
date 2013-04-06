	import de.umass.lastfm.*;
	import org.farng.mp3.*;
	import org.farng.mp3.id3.*;
	import java.util.*;
	import java.util.regex.Pattern;
	import java.util.regex.Matcher;
	import java.text.*;
	import java.lang.Runtime;
	import java.lang.Process;
	import java.net.URL;
	import java.io.InputStream;
	import java.util.Scanner;
	import java.io.File;
	import java.awt.image.BufferedImage;
	import javax.imageio.ImageIO;
	import org.jmusixmatch.MusixMatch;
	import org.jmusixmatch.entity.lyrics.Lyrics;
	
	public class MP3FILE {
		public static String Fingerprint = "./FPclient";
		
		public MP3File mp3_file;
		public String mp3_path;
		public ID3v1 id3v1tag;
		public AbstractMP3Tag id3v2tag;
		public Tag tag;
		
		public MP3FILE( File file ) throws Exception{
			
			mp3_path = file.getAbsolutePath();
			mp3_file = new MP3File(file);
			tag = new Tag();
			int hasid3v2 = 0;
			int hasid3v1 = 0;
			
			//check and construct id3v2 tag
			if(mp3_file.hasID3v2Tag()){
				hasid3v2 = 1;
				id3v2tag = mp3_file.getID3v2Tag();
			}else{
				//System.out.println("asdasdasd");
				id3v2tag = new ID3v2_3();
				load_ID3v2_tag();
				mp3_file.setID3v2Tag(id3v1tag);
				mp3_file.save();
			}
			
			//check or construct id3v1 tag
			if(mp3_file.hasID3v1Tag()){
				hasid3v1 = 1;
				id3v1tag = mp3_file.getID3v1Tag();
			}else{
				id3v1tag = new ID3v1();
				load_ID3v1_tag();
				mp3_file.setID3v1Tag(id3v1tag);
				mp3_file.save();
			}
			
			if( hasid3v2 == 1 ){
				load_id3v2_tag();
			}else if( hasid3v1 == 1 ){
				load_id3v1_tag();
			}else{
				load_id3v2_tag();
			}
			
			tag.track_mbid = null;
			tag.artist_mbid = null;
			tag.album_mbid = null;
			tag.track_url = null;
			tag.image_url = null;
			tag.cover = null;
			tag.album_tags = null;
		}
		
		public int load_ID3v1_tag(){
			id3v1tag.setAlbumTitle("");
			id3v1tag.setLeadArtist("");
			id3v1tag.setSongComment("");
			//id3v1tag.setSongGenre("");
			//id3v1tag.setSongLyric("");
			id3v1tag.setSongTitle("");
			//id3v1tag.setTrackNumberOnAlbum("");
			id3v1tag.setYearReleased("");
			return 0;
		}
		public int load_ID3v2_tag(){
			id3v2tag.setAlbumTitle("");
			id3v2tag.setLeadArtist("");
			id3v2tag.setSongComment("");
			//id3v2tag.setSongGenre("");
			//id3v2tag.setSongLyric("");
			id3v2tag.setSongTitle("");
			id3v2tag.setYearReleased("");
			return 0;
		}
		
		public int load_id3v1_tag() throws Exception{
			tag.title = id3v1tag.getSongTitle();
			if( tag.title == null )
				tag.title = "";
			tag.artist = id3v1tag.getLeadArtist();
			if( tag.artist == null )
				tag.artist = "";
			tag.album = id3v1tag.getAlbumTitle();
			if( tag.album == null )
				tag.album = "";
			tag.release_Date = id3v1tag.getYearReleased();
			if( tag.release_Date == null )
				tag.release_Date = "";
			tag.track_num = id3v1tag.getTrackNumberOnAlbum();
			if( tag.track_num == null )
				tag.track_num = "";
			tag.comment = id3v1tag.getSongComment();
			if( tag.comment == null )
				tag.comment = "";
			tag.lyrics = id3v1tag.getSongLyric();
			if( tag.lyrics == null )
				tag.lyrics = "";
			tag.genre = id3v1tag.getSongGenre();
			if( tag.genre == null )
				tag.genre = "";
			return 0;
		}
		
		public int load_id3v2_tag() throws Exception{
			tag.title = id3v2tag.getSongTitle();
			if( tag.title == null )
				tag.title = "";
			tag.artist = id3v2tag.getLeadArtist();
			if( tag.artist == null )
				tag.artist = "";
			tag.album = id3v2tag.getAlbumTitle();
			if( tag.album == null )
				tag.album = "";
			tag.release_Date = id3v2tag.getYearReleased();
			if( tag.release_Date == null )
				tag.release_Date = "";
			tag.track_num = id3v2tag.getTrackNumberOnAlbum();
			if( tag.track_num == null )
				tag.track_num = "";
			tag.comment = id3v2tag.getSongComment();
			if( tag.comment == null )
				tag.comment = "";
			tag.lyrics = id3v2tag.getSongLyric();
			if( tag.lyrics == null )
				tag.lyrics = "";
			tag.genre = id3v2tag.getSongGenre();
			if( tag.genre == null )
				tag.genre = "";
			//tag.length = id3v2tag.get
			return 0;
		}
		
		public int save_id3v1_tag() throws Exception{
			id3v1tag.setSongTitle(tag.title);
			id3v1tag.setLeadArtist(tag.artist);
			id3v1tag.setAlbumTitle(tag.album);
			id3v1tag.setYearReleased(tag.release_Date);
			//id3v1tag.setTrackNumberOnAlbum(tag.track_num);
			id3v1tag.setSongComment(tag.comment);
			//id3v1tag.setSongLyric(tag.lyrics);
			mp3_file.setID3v1Tag(id3v1tag);
			mp3_file.save();
			return 0;
		}
		
		public int save_id3v2_tag() throws Exception{
			id3v2tag.setSongTitle(tag.title);
			id3v2tag.setLeadArtist(tag.artist);
			id3v2tag.setAlbumTitle(tag.album);
			id3v2tag.setYearReleased(tag.release_Date);
			//id3v2tag.setTrackNumberOnAlbum(tag.track_num);
			id3v2tag.setSongComment(tag.comment);
			//id3v2tag.setSongGenre(tag.genre);
			//id3v2tag.setSongLyric(tag.lyrics);
			mp3_file.setID3v2Tag(id3v2tag);
			mp3_file.save();
			return 0;
		}
		
		public int setTitle( String arg ){
			if( arg == null )
				return -1;
			tag.title = new String(arg);
			return 0;
		}
		public int setArtist( String arg ){
			if( arg == null )
				return -1;
			tag.artist = new String(arg);
			return 0;
		}
		public int setAlbum( String arg ){
			if( arg == null )
				return -1;
			tag.album = new String(arg);
			return 0;
		}
		public int setComment( String arg ){
			if( arg == null )
				return -1;
			tag.comment = new String(arg);
			return -1;
		}
		public int setYear( String arg ){
			if( arg == null )
				return -1;
			tag.release_Date = new String(arg);
			return 0;
		}
		public int setLyrics( String arg ){
			if( arg == null )
				return -1;
			tag.lyrics = new String(arg);
			return 0;
		}
		public int setGenre( String arg ){
			if( arg == null )
				return -1;
			tag.genre = new String(arg);
			return 0;
		}
		public int setTrackNum( String arg ){
			if( arg == null )
				return -1;
			tag.track_num = new String(arg);
			return 0;
		}
		
		
		public int get_correct_tag() throws Exception{
			if( get_track_mbid() != 0 )
				return -1;
			if( get_tags() != 0 )
				return -1;
			return 0;
		}
		
		private int get_track_mbid() throws Exception{
			
			if(!mp3_path.endsWith(".mp3"))
				return -1;
			
			String path = new String(mp3_path);
			path.replace(" ", "\\ ");
			//System.out.println(path);
			
			String cmd[] = {Fingerprint,path};
			Process process = Runtime.getRuntime().exec( cmd );
			int exitval = process.waitFor();
			if(exitval != 0)
				return -1;
			InputStream in = process.getInputStream();
			Scanner scanner = new Scanner(in);
			
			String regex = "\\<mbid\\>.*\\</mbid\\>";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher;
			String line;
			
			boolean match = false;
			while(!match){
				if( !scanner.hasNext()){
					return -1;
				}
				line = scanner.nextLine();
				matcher = pattern.matcher(line);
				//System.out.print(line);
				//System.out.println("=====");
				if( matcher.find() ){
					
					match = true;
					int start = matcher.start();
					int end = matcher.end();
					tag.track_mbid = line.substring(start+6, end-7);
				}
			}
			scanner.close();
			in.close();
			return 0;
		}
		
		private int get_tags() throws Exception{
			
			String key = "53c45c5411794035744f14e96ae64089";

			Track track = Track.getInfo("asd", tag.track_mbid , key);

			tag.title = track.getName();
			tag.artist = track.getArtist();
			tag.artist_mbid = track.getArtistMbid();
			tag.album = track.getAlbum();
			tag.album_mbid = track.getAlbumMbid();
			//ength = track.getDuration();
			tag.track_url = track.getUrl();
			//track_num = track.getUrl();
			tag.image_url = track.getImageURL(ImageSize.EXTRALARGE);
			URL url = new URL(tag.image_url);
			tag.cover = ImageIO.read(url);
			//track.getDuration();
			//Image[] images;
			//Artist.getImages(artist_mbid, key).getPageResults().toArray(images);
		
			Album album_t = Album.getInfo(tag.artist, tag.album_mbid, key);
			//album_tags = album_t.getTags().toString();
			SimpleDateFormat simpleDateformat = new SimpleDateFormat("yyyy");
			Date date = album_t.getReleaseDate();
			if(date != null)
				tag.release_Date = simpleDateformat.format(date);
			else
				tag.release_Date = "";
	
			String MusicMatchAPI = "21e1e64b9b33547ce146f673a5d5c677";
			MusixMatch music_match = new MusixMatch(MusicMatchAPI);
			int trackID = music_match.getMatchingTrack(tag.title, tag.artist).getTrack().getTrackId();
			Lyrics l =  music_match.getLyrics(trackID);
			tag.lyrics = l.getLyricsBody();
			return 0;
		}
		
		public int save_cover(){
			
			return 0;
		}
		
		public int output_tags(){
			System.out.println(mp3_path);
			System.out.println(tag.title);
			System.out.println(tag.artist);
			System.out.println(tag.album);
			System.out.println(tag.track_num);
			System.out.println(tag.release_Date);
			System.out.println(tag.comment);
			System.out.println(tag.genre);
			System.out.println(tag.lyrics);
			//System.out.println();
			//System.out.println(tag.track_mbid);
			//System.out.println(tag.artist_mbid);
			//System.out.println(tag.album_mbid);
			//System.out.println(tag.track_url);
			//System.out.println(tag.image_url);
			return 0;
		}
		/*
		public static void main( String args[] ) throws Exception{
			String filepath = "a.mp3";
			MP3FILE mp3file = new MP3FILE(new File(filepath));
			mp3file.get_correct_tag();
			mp3file.output_tags();
			//System.out.println(mp3file.getAlbum_tags());
		}
		*/
		
			
	}
	class Tag{
		public String title;
		public String artist;
		public String album;
		public String release_Date;
		public String track_num;
		public String comment;
		public String lyrics;
		public String genre;
		
		public int length;
		
		public String track_mbid;
		public String artist_mbid;
		public String album_mbid;
		public String track_url;
		public String image_url;
		public BufferedImage cover;
		public String album_tags;
	}