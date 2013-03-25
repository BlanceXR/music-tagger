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
		cover = null;
		try
		{	cover = ImageIO.read(new File("dokita avatar.jpeg"));	}
		catch (IOException e)
		{	e.printStackTrace();	}
	
		icon = new ImageIcon(cover);
		panel = new JPanel();
		
		panel.setLayout(new BorderLayout());
		label = new JLabel("Album Arts\n",icon,JLabel.CENTER);
		panel.add(label,BorderLayout.NORTH);
		
		lyrics = "fuck o";
		lyrics_field = new JTextArea(lyrics);
		lyrics_field.setEditable(false);
		scrollPane = new JScrollPane(lyrics_field);
		
		scrollPane.setPreferredSize(new Dimension(250,300));
		panel.add(scrollPane,BorderLayout.SOUTH);
   }
	
}