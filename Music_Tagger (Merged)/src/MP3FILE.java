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
		private String track_mbid;
		public BufferedImage cover;
		public String lyrics;
		
		public MP3FILE( File file ) throws Exception{
			
			mp3_path = file.getAbsolutePath();
			mp3_file = new MP3File(file);
			//check and construct id3v2 tag
			if(mp3_file.hasID3v2Tag()){
				id3v2tag = mp3_file.getID3v2Tag();
			}else{
				id3v2tag = new ID3v2_4();
				mp3_file.setID3v2Tag(id3v2tag);
				mp3_file.save();
				id3v2tag = mp3_file.getID3v2Tag();
			}
			
			//check or construct id3v1 tag
			if(mp3_file.hasID3v1Tag()){
				id3v1tag = mp3_file.getID3v1Tag();
			}else{
				id3v1tag = new ID3v1();
				mp3_file.setID3v1Tag(id3v1tag);
				mp3_file.save();
				id3v1tag = mp3_file.getID3v1Tag();
			}
			cover = null;
			lyrics = null;
		}
		
		public int get_correct_tag() throws Exception{
			if( get_track_mbid() != 0 )
				return -1;
			if( correct_tags() != 0 )
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
					track_mbid = line.substring(start+6, end-7);
				}
			}
			scanner.close();
			in.close();
			return 0;
		}
		
		private int correct_tags() throws Exception{
			
			String key = "53c45c5411794035744f14e96ae64089";

			Track track = Track.getInfo("asd", track_mbid , key);
			String title = track.getName();
			if( title != null ){
				id3v2tag.setSongTitle(title);
				id3v1tag.setSongTitle(title);
			}else{
				id3v2tag.setSongTitle("");
				id3v1tag.setSongTitle("");
			}
			String artist = track.getArtist();
			if( artist != null ){
				id3v2tag.setLeadArtist(artist);
				id3v1tag.setArtist(artist);
			}else{
				id3v2tag.setLeadArtist("");
				id3v1tag.setArtist("");
			}
			String album = track.getAlbum();
			if( album != null ){
				id3v2tag.setAlbumTitle(album);
				id3v1tag.setAlbumTitle(album);
			}else{
				id3v2tag.setAlbumTitle("");
				id3v1tag.setAlbumTitle("");
			}
			
			Album album_t = Album.getInfo(artist, track.getAlbumMbid(), key);
			String image_url;
			URL url;
			image_url = album_t.getImageURL(ImageSize.EXTRALARGE);
			if( image_url != null ){
				url = new URL(image_url);
				cover = ImageIO.read(url);
				//File outputfile = new File("save.jpg");
				//ImageIO.write(cover,"jpg",outputfile);
				//System.out.println("image out ==== " + image_url);
			}else{
				cover = null;
			}
			//album_tags = album_t.getTags().toString();
			SimpleDateFormat simpleDateformat = new SimpleDateFormat("yyyy");
			Date date = album_t.getReleaseDate();
			if( date != null ){
				String year = simpleDateformat.format(date);
				id3v2tag.setYearReleased(year);
				id3v1tag.setYearReleased(year);
			}else{
				id3v2tag.setYearReleased("");
				id3v1tag.setYearReleased("");
			}
			
			String MusicMatchAPI = "21e1e64b9b33547ce146f673a5d5c677";
			MusixMatch music_match = new MusixMatch(MusicMatchAPI);
			int trackID = music_match.getMatchingTrack(title, artist).getTrack().getTrackId();
			Lyrics l =  music_match.getLyrics(trackID);
			String ly;
			if( l != null ){
				ly = l.getLyricsBody();
				lyrics = ly;
				//System.out.println(lyrics);
				id3v2tag.setSongLyric(ly);
			}else{
				lyrics = null;
				id3v2tag.setSongLyric("");
			}
			
			mp3_file.setID3v2Tag(id3v2tag);
			mp3_file.save();
			return 0;
		}
		/*
		public static void main( String args[] ) throws Exception{
			MP3FILE[] file = new MP3FILE[8];
			file[0] = new MP3FILE(new File("5.mp3"));
			file[1] = new MP3FILE(new File("6.mp3"));
			file[2] = new MP3FILE(new File("7.mp3"));
			file[3] = new MP3FILE(new File("8.mp3"));
			file[4] = new MP3FILE(new File("9.mp3"));
			file[0].get_correct_tag();
			file[1].get_correct_tag();
			file[2].get_correct_tag();
			file[3].get_correct_tag();
			file[4].get_correct_tag();
		}
		*/
}
