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

public class Main_Frame extends JFrame
{
	
	// objects
	static Menu mn; 
	static Table_Panel tp;
	static Side_Panel clp;
	static Vector<MP3FILE> filevector;
	static boolean singleton = false; 
	
	public static Main_Frame getInstance(){
		if( singleton == true)
			return null;
		singleton = true;
		Main_Frame temp_main = new Main_Frame();
		return temp_main;
	}
	// constructor
	private Main_Frame()
	{
		super();
		
		setLayout( new BoxLayout(this.getContentPane(), BoxLayout.LINE_AXIS) );
		setTitle("Music Tagger");
		
		pack();
		
		//initialize frame
		mn = new Menu();
		tp = new Table_Panel();
		clp = new Side_Panel();
		filevector = new Vector<MP3FILE>();
		
		addMenu( mn.menu_bar );
		addPanel( tp.panel, new Dimension(0,0) );
		addPanel( clp.panel, new Dimension(0,0) );
		setVisible(true);
		setSize( 1500,800 );
		
	}
	
	
	
	// add menu bar, menu items, etc.
	public void addMenu( JMenuBar bar )
	{
		setJMenuBar( bar );
	}
	
	// add a panel with a specific location
	public void addPanel( JPanel panel, Dimension dim)
	{
		add( Box.createRigidArea( dim ) );
		add( panel );
	}
	
	
	//need to update the mainFrame when new file is added or after meta info been fixed
	public static void insertNew(MP3FILE  file)
	{	
		tp.insert(file);
	}
	
	public static void updateID3display(MP3FILE file){
		
		//clp.updateID3(file);
	}
	public static void update(MP3FILE file)
	{
		//TODO: 
	}
	

}