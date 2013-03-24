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


public class Main_Frame
{
	
	// objects
	JFrame mainFrame;
	
	// constructor
	public Main_Frame()
	{
		mainFrame = new JFrame();
		mainFrame.setLayout( new BoxLayout(mainFrame.getContentPane(), BoxLayout.LINE_AXIS) );
		mainFrame.setTitle("Music Tagger");
		mainFrame.setSize( 1500,800 );
		mainFrame.pack();
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

	// main
	public static void main(String args[])
	{
		Main_Frame mf = new Main_Frame();
		Menu mn = new Menu();
		Table_Panel tp = new Table_Panel();
		Cover_Lyrics_Panel clp = new Cover_Lyrics_Panel();
		
		mf.addMenu( mn.menu_bar );
		mf.addPanel( tp.panel, new Dimension(0,0) );
		mf.addPanel( clp.panel, new Dimension(0,0) );
		mf.show();
	}

}