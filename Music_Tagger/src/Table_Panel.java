

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
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import java.io.File;
import java.util.Vector;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class Table_Panel //extends //MouseAdapter
{
	
	JPanel buttonpanel;
	
	// objects
	JPanel panel;
	JScrollPane scrollPane;
	static JTable table;
	String[] tag_names = { "Title", "Artist", "Album", "Release Year", "Comment" };
	
	Object[][] data = { };
	//popup
	JPopupMenu popup;
	JMenuItem fixall, tag, delete, selectall, clean;

	static int rowsSelected[];
	static int rowsSelectedCount;
	
	
	// constructor
	public Table_Panel(  )
	{
		
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
		selectall = new JMenuItem("Select All");
		clean = new JMenuItem("Clean");
		popup.add(fixall);
		popup.add(tag);
		popup.add(delete);
		popup.add(selectall);
		popup.add(clean);

		fixall.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//TODO: fix all tag here
				for( MP3FILE file : Main_Frame.filevector){
					try{
						file.get_correct_tag();
						//file.output_tags();
						file.save_id3v1_tag();
						//System.out.print("===");
						file.save_id3v2_tag();
						//System.out.print("===");
					}catch( Exception E ){
						
						E.printStackTrace();
						
					}

				}
				
				int totalRows = table.getRowCount();
				for ( int row = 0; row < totalRows; row++ )
					update( row );
				Main_Frame.clp.refresh(Main_Frame.clp.currentindex);
			}
			
	});
		delete.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//TODO: delete tag here
				
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				for(int i=rowsSelectedCount-1;i>=0;i--){
					model.removeRow(rowsSelected[i]);
					Main_Frame.filevector.removeElementAt(rowsSelected[i]);
				}
				
				System.out.println("delete");
			}
	});
		selectall.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//TODO: select all tag here
				DefaultTableModel model = (DefaultTableModel) table.getModel();
					
				table.addRowSelectionInterval(0,model.getRowCount()-1);
				System.out.println("select all");
			}
	});
		clean.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//TODO:  clean tag here
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				for(int i=model.getRowCount()-1;i>=0;i--){
				model.removeRow(i);
				Main_Frame.filevector.removeElementAt(i);
				}
				System.out.println("clean");
			}
	});
		
		tag.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
							//TODO: fix tag here
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				MP3FILE temp;
				for(int i = 0 ; i < rowsSelectedCount ; i++){
					temp = Main_Frame.filevector.get(rowsSelected[i]);
					try{
						temp.get_correct_tag();
						temp.save_id3v1_tag();
						temp.save_id3v2_tag();
					}catch( Exception E ){
						E.printStackTrace();
					}
				}
			}
	});
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				ListSelectionModel model = (ListSelectionModel)e.getSource();
				int index = model.getAnchorSelectionIndex();
				Main_Frame.clp.refresh(index);
			}
			
		});
		table.addMouseListener( new MouseAdapter()
		{

			public void mousePressed(MouseEvent e)
			{
				JTable source = (JTable)e.getSource();
				rowsSelectedCount=source.getSelectedRowCount();
				rowsSelected=new int[rowsSelectedCount];
				rowsSelected=source.getSelectedRows();
				Side_Panel.currentindex = rowsSelected[0];
				if (e.isPopupTrigger())
				{
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
				/*
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
				*/
			}
		});
	}
	//used to insert table content when new file is added
	public void insert(MP3FILE file) {
		
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.addRow(new Object[]{file.tag.title ,file.tag.artist, file.tag.album, file.tag.release_Date , ""});
	}
	
	public void update ( int row )
	{
		int column = 0;
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setValueAt( Main_Frame.filevector.get( row ).tag.title, row , column++ );
		model.setValueAt( Main_Frame.filevector.get( row ).tag.artist, row , column++ );
		model.setValueAt( Main_Frame.filevector.get( row ).tag.album, row , column++ );
		model.setValueAt( Main_Frame.filevector.get( row ).tag.release_Date, row , column++ );
		model.setValueAt( Main_Frame.filevector.get( row ).tag.comment, row , column++ );
	}
}
