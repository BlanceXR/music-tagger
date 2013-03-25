import de.umass.lastfm.*;
import org.farng.mp3.*;
import org.farng.mp3.id3.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.text.DateFormat;
import java.text.*;
import java.lang.Runtime;
import java.lang.Process;
import java.net.URL;
import java.io.InputStream;
import java.util.Scanner;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class MP3FILE {
	public static String Fingerprint = "../FPclient/lastfmfpclient";
	
	public File file;
	public MP3File mp3_file;
	public AbstractMP3Tag tag;
	public String mp3_path;
	
	public String title;
	public String artist;
	public String album;
	public String release_Date;
	public int size;
	public String track_num;
	public String comment;
	public String lyrics;
	public int length;
	
	public String track_mbid;
	public String artist_mbid;
	public String album_mbid;
	public String cover_path;
	public boolean fix_status;
	public String track_url;
	public String image_url;
	public BufferedImage cover;
	public BufferedImage artist_photo;
	
	public MP3FILE(File file) throws Exception{
		
//		mp3_path = path;
//		file = new File(mp3_path);
		mp3_file = new MP3File(file);
		
		if(mp3_file.hasID3v2Tag()){
			tag = mp3_file.getID3v2Tag();
			read_ID3_tag();
		}else if(mp3_file.hasID3v1Tag()){
			tag = mp3_file.getID3v1Tag();
			read_ID3_tag();
		}else{
			tag = new ID3v2_3();
			tag.setAlbumTitle("");
			tag.setLeadArtist("");
			tag.setSongTitle("");
			tag.setSongComment("");
			tag.setSongComment("");
			tag.setSongLyric("");
			tag.setSongGenre("");
			tag.setTrackNumberOnAlbum("");
			tag.setYearReleased("");
			tag.setAuthorComposer("");
			mp3_file.setID3v2Tag(tag);
			mp3_file.save();
			read_ID3_tag();
		}
		
		track_mbid = null;
		artist_mbid = null;
		album_mbid = null;
		cover_path = null;
		fix_status = false;
		track_url = null;
		image_url = null;
		cover = null;
		artist_photo = null;
	}
	
	public int setTitle( String arg ){
		if( arg == null )
			return -1;
		title = new String(arg);
		return 0;
	}
	public int setArtist( String arg ){
		if( arg == null )
			return -1;
		artist = new String(arg);
		return 0;
	}
	public int setAlbum( String arg ){
		if( arg == null )
			return -1;
		album = new String(arg);
		return 0;
	}
	public int setComment( String arg ){
		if( arg == null )
			return -1;
		comment = new String(arg);
		return -1;
	}
	public int setYear( String arg ){
		if( arg == null )
			return -1;
		release_Date = new String(arg);
		return 0;
	}
	public String getTitle(){
		if( title == null ) return "";
		return title;
	}
	public String getArtist(){
		if( artist == null ) return "";
		return title;
	}
	public String getAlbum(){
		if( album == null ) return "";
		return title;
	}
	public String getComment(){
		if( comment == null ) return "";
		return title;
	}
	public String getYear(){
		if( release_Date == null ) return "";
		return title;
	}
	public BufferedImage getCover(){
		return cover;
	}
	public BufferedImage getArtist_Photo(){
		return artist_photo;
	}
	
	private int read_ID3_tag() throws Exception{
		title = tag.getSongTitle();
		artist = tag.getLeadArtist();
		album = tag.getAlbumTitle();
		release_Date = tag.getYearReleased();
		//File temp = new File(mp3_path);
		//AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(temp);
		//AudioFormat format = audioInputStream.getFormat();
		//length = (Integer)format.getProperty("duration");
		size = tag.getSize();
		track_num = tag.getTrackNumberOnAlbum();
		comment = tag.getSongComment();
		lyrics = tag.getSongLyric();
		return 0;
	}
	public int write_tag() throws Exception{
		tag.setSongTitle(title);
		tag.setLeadArtist(artist);
		tag.setAlbumTitle(album);
		tag.setYearReleased(release_Date);
		tag.setTrackNumberOnAlbum(track_num);
		tag.setSongComment(comment);
		mp3_file.setID3v2Tag(tag);
		mp3_file.save();
		return 0;
	}

	public int convert(){	
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
				track_mbid = line.substring(start+6, end-7);
			}
		}
		scanner.close();
		in.close();
		return 0;
	}
	
	private int get_tags() throws Exception{
		
		String key = "53c45c5411794035744f14e96ae64089";
		
		
		Track track = Track.getInfo("asd", track_mbid , key);
		
		title = track.getName();
		artist = track.getArtist();
		artist_mbid = track.getArtistMbid();
		album = track.getAlbum();
		album_mbid = track.getAlbumMbid();
		//ength = track.getDuration();
		track_url = track.getUrl();
		image_url = track.getImageURL(ImageSize.EXTRALARGE);
		URL url = new URL(image_url);
		cover = ImageIO.read(url);
		
		//Image[] images;
		//Artist.getImages(artist_mbid, key).getPageResults().toArray(images);
	
		Album album_t = Album.getInfo(artist, album_mbid, key);
		SimpleDateFormat simpleDateformat = new SimpleDateFormat("yyyy");
		release_Date = simpleDateformat.format(album_t.getReleaseDate());
		
		
		
		return 0;
	}
	
	public int save_cover(){
		
		return 0;
	}
	
	public int output_tags(){
		System.out.println(mp3_path);
		System.out.println(title);
		System.out.println(track_mbid);
		System.out.println(artist);
		System.out.println(artist_mbid);
		System.out.println(album);
		System.out.println(album_mbid);
		//System.out.println(length);
		System.out.println(release_Date);
		System.out.println(track_url);
		System.out.println(image_url);
		return 0;
	}
	
	
	
		
}
