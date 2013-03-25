/* This class builds the skeleton code for Music Tagger's gui 
 * 
 * Methods:
 * 		addPanel( JPanel, Dimension )
 *			adds JPanels in horizontal direction
 *
 * 		addMenu( JMenuBar )
 * 			adds a JMenubar
 * 
 * 		show()
 * 			display the frame
 */

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import java.util.*;
import java.io.File;

public class Main_Frame
{
	
	// objects
	JFrame mainFrame;
	Menu mn; 
	static Table_Panel tp;
	static Cover_Lyrics_Panel clp;
	public static Vector<MP3FILE> fileVector = new Vector<MP3FILE>();
	
	// constructor
	public Main_Frame()
	{
		mainFrame = new JFrame();
		mainFrame.setLayout( new BoxLayout(mainFrame.getContentPane(), BoxLayout.LINE_AXIS) );
		mainFrame.setTitle("Music Tagger");
		
		mainFrame.pack();
		//initialize frame
		
		mn = new Menu();
		tp = new Table_Panel();
		clp = new Cover_Lyrics_Panel();
		addMenu( mn.menu_bar );
		addPanel( tp.panel, new Dimension(0,0) );
		addPanel( clp.panel, new Dimension(0,0) );
		show();
		mainFrame.setSize( 1500,800 );
	}
	
	// add menu bar, menu items, etc.
	public void addMenu( JMenuBar bar )
	{
		mainFrame.setJMenuBar( bar );
	}
	
	// add a panel with a specific location
	public void addPanel( JPanel panel, Dimension dim)
	{
		mainFrame.add( Box.createRigidArea( dim ) );
		mainFrame.add( panel );
	}
	
	// display the frame
	public void show()
	{	mainFrame.setVisible( true );	}
	
	//need to update the mainFrame when new file is added or after meta info been fixed
	public static void insertNew(MP3FILE file){
		tp.insert(file);
	}
	public static void update(MP3FILE file){
		//TODO: 
	}
	// main
	public static void main(String args[])
	{
		Main_Frame mf = new Main_Frame();
	}

}