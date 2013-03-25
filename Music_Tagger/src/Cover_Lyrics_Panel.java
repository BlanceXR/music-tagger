import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class Cover_Lyrics_Panel
{
	BufferedImage cover;
	ImageIcon icon;
	JPanel panel;
	JLabel label;
	JScrollPane scrollPane;
	JTextArea lyrics_field;
	String lyrics;

	public Cover_Lyrics_Panel()
	{
		lyrics = new String();
		cover = null;
		icon = new ImageIcon();
		label = new JLabel("Album Arts\n",icon,JLabel.CENTER);
		
		lyrics_field = new JTextArea(lyrics);
		lyrics_field.setEditable(false);
		scrollPane = new JScrollPane(lyrics_field);
		scrollPane.setPreferredSize(new Dimension(250,300));
		
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(scrollPane,BorderLayout.SOUTH);
		panel.add(label,BorderLayout.NORTH);
   }
	
	//display cover image
	public int set_cover( BufferedImage image ){
		//todo
		return 0;
	}
	
	//display cover image
	public int set_cover ( File cover_file ){
		//todo
		return 0;
	}
	
	//undisplay cover image
	public int unset_cover(){
		//todo
		return 0;
	}

	//save image to file
	public int save_cover( File cover_file ){
		
		return 0;
	}
	
	//display lyrics
	public int set_lyrics( String lyric ){
		//todo
		return 0;
	}
	
	//display lyrics
	public int set_lyrics( File lyrics_file ){
		//todo
		return 0;
	}
	
	//undisplay lyrics
	public int unset_lyrics(){
		
		return 0;
	}
	
	//save lyrics to file
	public int save_lyrice( File lyrics_file ){
		
		return 0;
	}
}
