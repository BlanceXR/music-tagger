/* This class builds the Table_Panel for music selections, it also contains functions for updating music tag info 
 * 
 * Methods:
 * 		
 */

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;


public class Table_Panel
{
	
	// objects
	JPanel panel;
	JScrollPane scrollPane;
	JTable table;
	String[] tag_names = { "Title", "Artist", "Album", "Release Year", "Comment" };
	Object[][] data = { { "Jack", "is", "Demonstrating", "enjoy", "it" }, { "Jack", "is", "Demonstrating", "enjoy", "it" } };

	
	// constructor
	public Table_Panel()
	{   
		table = new JTable( data, tag_names );
		
		scrollPane = new JScrollPane( table );
		
		panel = new JPanel( new BorderLayout() );
		panel.add( scrollPane, BorderLayout.CENTER );
	}
	
}
