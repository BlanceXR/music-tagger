import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class Side_Panel
{
	BufferedImage cover;
	ImageIcon icon;
	JPanel panel;
	JLabel label;
	JScrollPane scrollPane;
	JTextArea lyrics_field;
	String lyrics;
	
	JLabel Label_Title_ID3V1;
	JLabel Label_Year_ID3V1;
	JLabel Label_Artist_ID3V1;
	JLabel Label_Composer_ID3V1;
	JLabel Label_Album_ID3V1;
	JLabel Label_Comment_ID3V1;
	JLabel Label_Genre_ID3V1;	
	
	JLabel Label_Title_ID3V2;
	JLabel Label_Year_ID3V2;
	JLabel Label_Artist_ID3V2;
	JLabel Label_Composer_ID3V2;
	JLabel Label_Album_ID3V2;
	JLabel Label_Comment_ID3V2;
	JLabel Label_Genre_ID3V2;	
	
	final JTextField Text_Title_ID3V1;
	final JTextField Text_Year_ID3V1;
	final JTextField Text_Artist_ID3V1;
	final JTextField Text_Composer_ID3V1;
	final JTextField Text_Album_ID3V1;
	final JTextField Text_Comment_ID3V1;
	final JTextField Text_Genre_ID3V1;
	
	final JTextField Text_Title_ID3V2;
	final JTextField Text_Year_ID3V2;
	final JTextField Text_Artist_ID3V2;
	final JTextField Text_Composer_ID3V2;
	final JTextField Text_Album_ID3V2;
	final JTextField Text_Comment_ID3V2;
	final JTextField Text_Genre_ID3V2;
	
	JButton fix_ID3V1;
	JButton fix_ID3V2;
	JButton reset_ID3V1;
	JButton reset_ID3V2;
	
	static int currentindex;
	
	public Side_Panel()
	{
		currentindex = -1;
		panel = new JPanel();
		
		panel.setLayout(new BorderLayout());
		label = new JLabel("Album Arts\n",icon,JLabel.CENTER);
		panel.add(label,BorderLayout.NORTH);
		
		
		//////////////////////////////////         tab panel        ////////////////////////////////////////
		
		JTabbedPane tabPane = new JTabbedPane();
		JPanel gridPane_ID3V1 = new JPanel( new GridLayout(7, 2) );
		JPanel boxPane_ID3V1 = new JPanel(  );
		JPanel buttonPane_ID3V1 = new JPanel(  );
		JPanel gridPane_ID3V2 = new JPanel( new GridLayout(7, 2) );
		JPanel boxPane_ID3V2 = new JPanel(  );
		JPanel buttonPane_ID3V2 = new JPanel(  );
		
		//////////////////////////////////		    ID3V1 	       ////////////////////////////////////////
		
		Label_Title_ID3V1 = new JLabel( "Title" );
		Label_Year_ID3V1 = new JLabel( "Year" );
		Label_Artist_ID3V1 = new JLabel( "Artist" );
		Label_Composer_ID3V1 = new JLabel( "Composer" );
		Label_Album_ID3V1 = new JLabel( "Album" );
		Label_Comment_ID3V1 = new JLabel( "Comment" );
		Label_Genre_ID3V1 = new JLabel( "Genre" );	
		
		Text_Title_ID3V1 = new JTextField(  );
		Text_Year_ID3V1 = new JTextField(  );
		Text_Artist_ID3V1 = new JTextField(  );
		Text_Composer_ID3V1 = new JTextField(  );
		Text_Album_ID3V1 = new JTextField(  );
		Text_Comment_ID3V1 = new JTextField(  );
		Text_Genre_ID3V1 = new JTextField(  );	
		
		final String DEFAULT_TITLE_ID3V1 ="";
		final String DEFAULT_YEAR_ID3V1;
		final String DEFAULT_ARTIST_ID3V1;
		final String DEFAULT_COMPOSER_ID3V1;
		final String DEFAULT_ALBUM_ID3V1;
		final String DEFAULT_COMMENT_ID3V1;
		final String DEFAULT_GENRE_ID3V1;
		
		fix_ID3V1 = new JButton( "Fix" );
		
		reset_ID3V1 = new JButton( "Reset" );
		reset_ID3V1.addActionListener(
				new ActionListener(){
					public void actionPerformed( ActionEvent e ){
						// TODO reset to defaults
						Text_Title_ID3V1.setText( DEFAULT_TITLE_ID3V1 );
					}
				});
		
		gridPane_ID3V1.add( Label_Title_ID3V1 );
		gridPane_ID3V1.add( Text_Title_ID3V1 );
		gridPane_ID3V1.add( Label_Year_ID3V1 );
		gridPane_ID3V1.add( Text_Year_ID3V1 );
		gridPane_ID3V1.add( Label_Artist_ID3V1 );
		gridPane_ID3V1.add( Text_Artist_ID3V1 );
		gridPane_ID3V1.add( Label_Composer_ID3V1 );
		gridPane_ID3V1.add( Text_Composer_ID3V1 );
		gridPane_ID3V1.add( Label_Album_ID3V1 );
		gridPane_ID3V1.add( Text_Album_ID3V1 );
		gridPane_ID3V1.add( Label_Comment_ID3V1 );
		gridPane_ID3V1.add( Text_Comment_ID3V1 );
		gridPane_ID3V1.add( Label_Genre_ID3V1 );
		gridPane_ID3V1.add( Text_Genre_ID3V1 );
		
		JScrollPane scrollPane_ID3V1 = new JScrollPane( gridPane_ID3V1 );
		buttonPane_ID3V1.setLayout( new BoxLayout( buttonPane_ID3V1, BoxLayout.LINE_AXIS) );
		buttonPane_ID3V1.add( fix_ID3V1 );
		buttonPane_ID3V1.add( reset_ID3V1 );
		boxPane_ID3V1.setLayout( new BoxLayout( boxPane_ID3V1, BoxLayout.PAGE_AXIS) );
		boxPane_ID3V1.add( scrollPane_ID3V1 );
		boxPane_ID3V1.add( buttonPane_ID3V1 );
		tabPane.add( "ID3V1" , boxPane_ID3V1 );

		//////////////////////////////////		    ID3V2 	       ////////////////////////////////////////
		
		final String DEFAULT_TITLE_ID3V2 = new String( "Default Title" );
		final String DEFAULT_YEAR_ID3V2;
		final String DEFAULT_ARTIST_ID3V2;
		final String DEFAULT_COMPOSER_ID3V2;
		final String DEFAULT_ALBUM_ID3V2;
		final String DEFAULT_COMMENT_ID3V2;
		final String DEFAULT_GENRE_ID3V2;
		
		Label_Title_ID3V2 = new JLabel( "Title" );
		Label_Year_ID3V2 = new JLabel( "Year" );
		Label_Artist_ID3V2 = new JLabel( "Artist" );
		Label_Composer_ID3V2 = new JLabel( "Composer" );
		Label_Album_ID3V2 = new JLabel( "Album" );
		Label_Comment_ID3V2 = new JLabel( "Comment" );
		Label_Genre_ID3V2 = new JLabel( "Genre" );	
		
		Text_Title_ID3V2 = new JTextField(  );
		Text_Year_ID3V2 = new JTextField(  );
		Text_Artist_ID3V2 = new JTextField(  );
		Text_Composer_ID3V2 = new JTextField(  );
		Text_Album_ID3V2 = new JTextField(  );
		Text_Comment_ID3V2 = new JTextField(  );
		Text_Genre_ID3V2 = new JTextField(  );	
		
		fix_ID3V2 = new JButton( "Fix" );
		
		reset_ID3V2 = new JButton( "Reset" );
		reset_ID3V2.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						// TODO reset to defaults
						Text_Title_ID3V2.setText( DEFAULT_TITLE_ID3V2 );
					}
				});
		addfixlistener();
		gridPane_ID3V2.add( Label_Title_ID3V2 );
		gridPane_ID3V2.add( Text_Title_ID3V2 );
		gridPane_ID3V2.add( Label_Year_ID3V2 );
		gridPane_ID3V2.add( Text_Year_ID3V2 );
		gridPane_ID3V2.add( Label_Artist_ID3V2 );
		gridPane_ID3V2.add( Text_Artist_ID3V2 );
		gridPane_ID3V2.add( Label_Composer_ID3V2 );
		gridPane_ID3V2.add( Text_Composer_ID3V2 );
		gridPane_ID3V2.add( Label_Album_ID3V2 );
		gridPane_ID3V2.add( Text_Album_ID3V2 );
		gridPane_ID3V2.add( Label_Comment_ID3V2 );
		gridPane_ID3V2.add( Text_Comment_ID3V2 );
		gridPane_ID3V2.add( Label_Genre_ID3V2 );
		gridPane_ID3V2.add( Text_Genre_ID3V2 );
		
		JScrollPane scrollPane_ID3V2 = new JScrollPane( gridPane_ID3V2 );
		buttonPane_ID3V2.setLayout( new BoxLayout( buttonPane_ID3V2, BoxLayout.LINE_AXIS) );
		buttonPane_ID3V2.add( fix_ID3V2 );
		buttonPane_ID3V2.add( reset_ID3V2 );
		boxPane_ID3V2.setLayout( new BoxLayout( boxPane_ID3V2, BoxLayout.PAGE_AXIS) );
		boxPane_ID3V2.add( scrollPane_ID3V2 );
		boxPane_ID3V2.add( buttonPane_ID3V2 );
		tabPane.add( "ID3V2" , boxPane_ID3V2 );
		
		panel.add(tabPane, BorderLayout.CENTER);
		
		
		////////////////////////////////     lyrics      ///////////////////////////////////////
		
		lyrics = "fuck o";
		lyrics_field = new JTextArea(lyrics);
		lyrics_field.setEditable(false);
		scrollPane = new JScrollPane(lyrics_field);
		
		scrollPane.setPreferredSize(new Dimension(250,300));
		panel.add(scrollPane,BorderLayout.SOUTH);
   }
	
	public void addfixlistener(){
		fix_ID3V1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(Side_Panel.currentindex);
				// TODO Auto-generated method stub
				MP3FILE temp = Main_Frame.filevector.get(currentindex);
				try{
					temp.tag.album = Text_Album_ID3V1.getText();
					temp.tag.artist = Text_Artist_ID3V1.getText();
					temp.tag.release_Date = Text_Year_ID3V1.getText();
					temp.tag.title = Text_Title_ID3V1.getText();
					temp.tag.comment = Text_Comment_ID3V1.getText();
					temp.save_id3v1_tag();
					Main_Frame.tp.update(currentindex);
					refresh(currentindex);
					//System.out.println(currentindex);
				}catch( Exception E ){
					
				}
			}
			
		});
		fix_ID3V2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				MP3FILE temp = Main_Frame.filevector.get(Table_Panel.rowsSelected[0]);
				try{
					temp.get_correct_tag();
					temp.save_id3v2_tag();
					refresh(Table_Panel.rowsSelected[0]);
				}catch( Exception E ){
					
				}
			}
			
		});
	}
	
	
	public int refresh( int index ){
		
		Text_Album_ID3V1.setText(Main_Frame.filevector.get(index).id3v1tag.getAlbum());
		Text_Artist_ID3V1.setText(Main_Frame.filevector.get(index).id3v1tag.getArtist());
		Text_Comment_ID3V1.setText(Main_Frame.filevector.get(index).id3v1tag.getComment());
		Text_Title_ID3V1.setText(Main_Frame.filevector.get(index).id3v1tag.getSongTitle());
		Text_Year_ID3V1.setText(Main_Frame.filevector.get(index).id3v1tag.getYear());
		
		Text_Album_ID3V2.setText(Main_Frame.filevector.get(index).id3v2tag.getAlbumTitle());
		Text_Artist_ID3V2.setText(Main_Frame.filevector.get(index).id3v2tag.getLeadArtist());
		Text_Comment_ID3V2.setText(Main_Frame.filevector.get(index).id3v2tag.getSongComment());
		Text_Title_ID3V2.setText(Main_Frame.filevector.get(index).id3v2tag.getSongTitle());
		Text_Year_ID3V2.setText(Main_Frame.filevector.get(index).id3v2tag.getYearReleased());
		return 0;
	}
	
	
	
	
}