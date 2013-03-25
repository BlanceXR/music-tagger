// /* This class builds the Table_Panel for music selections, it also contains functions for updating music tag info
//  *
//  * Methods:
//  *
//  */

// import java.awt.BorderLayout;
// import javax.swing.JPanel;
// import javax.swing.JScrollPane;
// import javax.swing.JTable;


// public class Table_Panel
// {

// 	// objects
// 	JPanel panel;
// 	JScrollPane scrollPane;
// 	JTable table;
// 	String[] tag_names = { "Title", "Artist", "Album", "Release Year", "Comment" };
// 	Object[][] data = { { "Jack", "is", "Demonstrating", "enjoy", "it" }, { "Jack", "is", "Demonstrating", "enjoy", "it" } };


// 	// constructor
// 	public Table_Panel()
// 	{
// 		table = new JTable( data, tag_names );

// 		scrollPane = new JScrollPane( table );

// 		panel = new JPanel( new BorderLayout() );
// 		panel.add( scrollPane, BorderLayout.CENTER );
// 	}

// }
/* This class builds the Table_Panel for music selections, it also contains functions for updating music tag info
 *
 * Methods:
 *
 */

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JPopupMenu;
import java.io.File;
import javax.swing.table.DefaultTableModel;

public class Table_Panel extends MouseAdapter
{

	// objects
	JPanel panel;
	JScrollPane scrollPane;
	JTable table;
	String[] tag_names = { "Title", "Artist", "Album", "Release Year", "Comment" };
	//Object[][] data = { { "Jack", "is", "Demonstrating", "enjoy", "it" }, { "Jack", "is", "Demonstrating", "enjoy", "it" } };
	Object[][] data = { };
	//popup
	JPopupMenu popup;
	JMenuItem fixall;
	JMenuItem tag, delete, select, clean;

	// constructor
	public Table_Panel(){
		table = new JTable(new DefaultTableModel( data, tag_names) ){
		    public boolean isCellEditable(int rowIndex, int colIndex) {
		        return false;   //Disallow the editing of any cell
		    }
		};
		table.setCellSelectionEnabled(false);
		table.setRowSelectionAllowed(true);
		
		
		scrollPane = new JScrollPane( table );

		panel = new JPanel( new BorderLayout() );
		panel.add( scrollPane, BorderLayout.CENTER );
		//pop up
		popup=new JPopupMenu();
		fixall=new JMenuItem("Fix All");
		tag = new JMenuItem("Fix Tag");
		delete = new JMenuItem("Delete");
		select = new JMenuItem("Select All");
		clean = new JMenuItem("Clean");
		popup.add(fixall);
		popup.add(tag);
		popup.add(delete);
		popup.add(select);
		popup.add(clean);

		tag.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							//TODO: fix tag here
							
						}
				});
		table.addMouseListener( new MouseAdapter()
		{

			public void mousePressed(MouseEvent e)
			{

				if (e.isPopupTrigger())
				{

					JTable source = (JTable)e.getSource();
					int row = source.rowAtPoint( e.getPoint() );
					int column = source.columnAtPoint( e.getPoint() );

					if (! source.isRowSelected(row)){
						source.changeSelection(row, column, false, false);
					}
					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			}
			public void mouseReleased(MouseEvent e)
			{
				if (e.isPopupTrigger())
				{
					JTable source = (JTable)e.getSource();
					int row = source.rowAtPoint( e.getPoint() );
					int column = source.columnAtPoint( e.getPoint() );

					if (! source.isRowSelected(row)){
						source.changeSelection(row, column, false, false);
					}
					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
	}
	//used to insert table content when new file is added
	public void insert(MP3FILE file) {
		
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.addRow(new Object[]{file.title,file.artist, file.album, file.release_Date , file.length});
	}
}
