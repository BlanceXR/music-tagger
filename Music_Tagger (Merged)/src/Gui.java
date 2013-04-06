import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;


public class Gui extends JFrame
{
	
	//////////////////////  Objects  /////////////////////////
	
	//////// Menu ////////
	JMenuBar menu_bar;
	JMenu menu_file;
	JMenuItem file_open, file_exit;
	JMenu menu_edit;
	JMenuItem edit_tag, edit_delete, edit_select, edit_clean;
	final JFileChooser filechooser = new JFileChooser();
	
	//////// Table_Panel /////////
	JPanel table_panel;
	JScrollPane table_panel_scroll;
	JTable table_panel_table;
	String[] categories = { "Title", "Artist", "Album", "Release Year", "Comment" };
	Object[][] data = { };
	JPopupMenu popup;
	JMenuItem popup_fixall, popup_tag, popup_delete, popup_selectall, popup_clean;
	int rowsSelected[];
	int rowsSelectedCount;
	
	//////// Side_Panel /////////
	JPanel side_panel, album_panel, lyrics_panel;
	BufferedImage default_cover, cover;
	ImageIcon icon;
	JLabel album_title,lyrics_title;
	JLabel album_art;
	JScrollPane side_panel_scroll;
	JTextArea lyrics_field;
	String lyrics;
	static int currentindex;
	
	///// ID3V1 /////	
	JLabel Label_Title_ID3V1;
	JLabel Label_Year_ID3V1;
	JLabel Label_Artist_ID3V1;
	JLabel Label_Composer_ID3V1;
	JLabel Label_Album_ID3V1;
	JLabel Label_Comment_ID3V1;
	JLabel Label_Genre_ID3V1;
	final JTextField Text_Title_ID3V1 = new JTextField(  );
	final JTextField Text_Year_ID3V1 = new JTextField(  );
	final JTextField Text_Artist_ID3V1 = new JTextField(  );
	final JTextField Text_Composer_ID3V1 = new JTextField(  );
	final JTextField Text_Album_ID3V1 = new JTextField(  );
	final JTextField Text_Comment_ID3V1 = new JTextField(  );
	final JTextField Text_Genre_ID3V1 = new JTextField(  );
	JButton fix_ID3V1;
	JButton reset_ID3V1;
	
	///// ID3V1 /////
	JLabel Label_Title_ID3V2;
	JLabel Label_Year_ID3V2;
	JLabel Label_Artist_ID3V2;
	JLabel Label_Composer_ID3V2;
	JLabel Label_Album_ID3V2;
	JLabel Label_Comment_ID3V2;
	JLabel Label_Genre_ID3V2;		
	final JTextField Text_Title_ID3V2 = new JTextField(  );
	final JTextField Text_Year_ID3V2 = new JTextField(  );
	final JTextField Text_Artist_ID3V2 = new JTextField(  );
	final JTextField Text_Composer_ID3V2 = new JTextField(  );
	final JTextField Text_Album_ID3V2 = new JTextField(  );
	final JTextField Text_Comment_ID3V2 = new JTextField(  );
	final JTextField Text_Genre_ID3V2 = new JTextField(  );
	JButton fix_ID3V2;
	JButton reset_ID3V2;

	//////// MP3_FILE /////////
	Vector <MP3FILE> filevector;
	static boolean singleton = false; 
	
	// getter method for Frame_Gui
	public static Gui getGui()
	{
		if( singleton == true)
			return null;
		singleton = true;
		Gui temp_main = new Gui();
		return temp_main;
	}
	
	//////////////// Constructor ////////////////
	private Gui()
	{
		super();
		constructTablePanel();
		constructSidePanel();
		constructMenu();
		constructFrame();
	}
	
	//////////////// Helper Constructors ////////////////

	private void constructMenu()
	{
		menu_bar = new JMenuBar();
		createFileMenu();
		createEditMenu();
	}

	private void constructTablePanel()
	{
		table_panel_table = new JTable( new DefaultTableModel( data, categories ) ){
			//Disallow the editing of any cell
			public boolean isCellEditable(int rowIndex, int colIndex) {
				return false;  
			}
		};
		table_panel_table.setCellSelectionEnabled(false);
		table_panel_table.setRowSelectionAllowed(true);
		table_panel_scroll = new JScrollPane( table_panel_table );
		table_panel_scroll.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e)
			{
				if(SwingUtilities.isLeftMouseButton(e)){
					int rowcount=table_panel_table.getSelectedRowCount();
					if(rowcount>0){
						table_panel_table.clearSelection();
					}
				}
			}
		});
		table_panel = new JPanel( new BorderLayout() );
		table_panel.add( table_panel_scroll, BorderLayout.CENTER );
		createPopup();
		
		table_panel_table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				ListSelectionModel model = (ListSelectionModel)e.getSource();
				int index = model.getAnchorSelectionIndex();
				if( index != -1 )
					refresh(index);
			}

		});
		
		table_panel_table.addMouseListener( new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				JTable source = (JTable)e.getSource();
				rowsSelectedCount=source.getSelectedRowCount();
				rowsSelected=new int[rowsSelectedCount];
				rowsSelected=source.getSelectedRows();
				currentindex = rowsSelected[0];
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
	
	private void constructSidePanel()
	{
		currentindex = -1;
		side_panel = new JPanel();
		side_panel.setLayout( new GridLayout( 3 ,1 ) );
		
		//////////////////////////////////       Album  Art       ///////////////////////////////////////////
		album_panel = new JPanel();
		album_panel.setLayout( new BoxLayout( album_panel, BoxLayout.LINE_AXIS) );
//		album_title = new JLabel("Album Arts\n",JLabel.CENTER);
		cover = null;
		
		try
		{	cover = ImageIO.read(new File("default_cover.jpg"));	}
		catch (IOException e)
		{	e.printStackTrace();	}
		icon = new ImageIcon(cover);
		album_art = new JLabel( icon, JLabel.CENTER);
		
//		album_panel.add(album_title);
		album_panel.add(album_art);
		
		side_panel.add( album_panel );
		
		/////////////////////////////////         Tabbed Panel      /////////////////////////////////////////
		
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
		fix_ID3V1 = new JButton( "Fix" );
		reset_ID3V1 = new JButton( "Reset" );
		reset_ID3V1.addActionListener(
				new ActionListener(){
					public void actionPerformed( ActionEvent e ){
						// TODO reset to defaults
						Text_Title_ID3V1.setText( "" );
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
		Label_Title_ID3V2 = new JLabel( "Title" );
		Label_Year_ID3V2 = new JLabel( "Year" );
		Label_Artist_ID3V2 = new JLabel( "Artist" );
		Label_Composer_ID3V2 = new JLabel( "Composer" );
		Label_Album_ID3V2 = new JLabel( "Album" );
		Label_Comment_ID3V2 = new JLabel( "Comment" );
		Label_Genre_ID3V2 = new JLabel( "Genre" );	
		fix_ID3V2 = new JButton( "Fix" );
		reset_ID3V2 = new JButton( "Reset" );
		reset_ID3V2.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						// TODO reset to defaults
						Text_Title_ID3V2.setText( "" );
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
		side_panel.add(tabPane);	
		
		////////////////////////////////     lyrics      ///////////////////////////////////////
		lyrics = "lyrics demonstration";
		lyrics_field = new JTextArea(lyrics);
		lyrics_field.setEditable(false);
		side_panel_scroll = new JScrollPane(lyrics_field);
		side_panel_scroll.setPreferredSize(new Dimension(250,300));
		side_panel.add( side_panel_scroll );
   }
	
	public void addfixlistener(){
		fix_ID3V1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				MP3FILE temp = filevector.get(currentindex);
				try{
					temp.tag.album = Text_Album_ID3V1.getText();
					temp.tag.artist = Text_Artist_ID3V1.getText();
					temp.tag.release_Date = Text_Year_ID3V1.getText();
					temp.tag.title = Text_Title_ID3V1.getText();
					temp.tag.comment = Text_Comment_ID3V1.getText();
					temp.save_id3v1_tag();
					update( currentindex );
					refresh( currentindex );
				}catch( Exception E ){
					
				}
			}
			
		});
		fix_ID3V2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				MP3FILE temp = filevector.get( rowsSelected[0] );
				try{
					temp.tag.album = Text_Album_ID3V2.getText();
					temp.tag.artist = Text_Artist_ID3V2.getText();
					temp.tag.release_Date = Text_Year_ID3V2.getText();
					temp.tag.title = Text_Title_ID3V2.getText();
					temp.tag.comment = Text_Comment_ID3V2.getText();
					temp.save_id3v2_tag();
					update( currentindex );
					refresh( currentindex );
					
					cover = temp.tag.cover;
					icon = new ImageIcon(cover);
					album_art = new JLabel( icon, JLabel.CENTER);
					album_panel.add(album_art);
					side_panel.add( album_panel );
					
				}catch( Exception E ){ }
			}
			
		});
	}

	private void constructFrame()
	{	
		setLayout( new BoxLayout(this.getContentPane(), BoxLayout.LINE_AXIS) );
		setTitle("Music Tagger");
		pack();
		
		filevector = new Vector<MP3FILE>();
		setJMenuBar( menu_bar );
		add(table_panel);
		add(side_panel);
		
		setSize( 1350,700 );
		setVisible(true);
	}
	
	///////////////////////////////         helping methods          ///////////////////////////////////

	private void createFileMenu()
	{
		menu_file = new JMenu("File");
		menu_file.setMnemonic('F');
		file_open = new JMenuItem("Open File...");
		file_exit = new JMenuItem("Exit");
		menu_file.add(file_open);
		menu_file.add(file_exit);
		menu_bar.add(menu_file);
		filechooser.setMultiSelectionEnabled(true);
		
		file_open.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						int returnVal = filechooser.showOpenDialog(file_open);
						if ( returnVal == JFileChooser.APPROVE_OPTION ) {
							File currentFile[] = filechooser.getSelectedFiles();
							int len = currentFile.length;
							String filename;
							MP3FILE mp3;
							for( int i = 0; i < len; i++ ){
								filename = currentFile[ i ].getName();
								String extension = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
								String mp3extension = "mp3";
								if (extension.compareTo(mp3extension) != 0) {
									JOptionPane.showMessageDialog(null, "Currently We only support MP3 menu_file");
								}else{
									try {
										mp3 = new MP3FILE( currentFile[ i ] );
										filevector.add( mp3 );
										DefaultTableModel model = (DefaultTableModel) table_panel_table.getModel();
										model.addRow(new Object[]{ mp3.tag.title , mp3.tag.artist, mp3.tag.album, mp3.tag.release_Date , mp3.tag.comment });
									} catch (Exception e1) {
										e1.printStackTrace();
									}						        	
								}
							}
						} else {}
					}
				});
		
		file_exit.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						System.exit(0);
					}
				});
	}

	private void createEditMenu()
	{
		menu_edit = new JMenu("Edit");
		menu_file.setMnemonic('E');
		edit_tag = new JMenuItem("Fix Tag");
		edit_delete = new JMenuItem("Delete");
		edit_select = new JMenuItem("Select All");
		edit_clean = new JMenuItem("Clean");
		menu_edit.add(edit_tag);
		menu_edit.add(edit_delete);
		menu_edit.add(edit_select);
		menu_edit.add(edit_clean);
		menu_bar.add(menu_edit);
		
		edit_tag.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// TODO
			}
		});

		edit_delete.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int [] selectedRows = table_panel_table.getSelectedRows();
				int selectedRowsCount = table_panel_table.getSelectedRowCount();
				DefaultTableModel model = (DefaultTableModel) table_panel_table.getModel();
				for(int i = selectedRowsCount - 1; i >= 0; i--){
					model.removeRow( selectedRows[ i ] );
					filevector.removeElementAt( selectedRows[ i ] );
				}
			}
		});
		
		edit_select.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				DefaultTableModel model = (DefaultTableModel) table_panel_table.getModel();
				table_panel_table.addRowSelectionInterval( 0 , model.getRowCount() - 1 );
			}
		});
		
		edit_clean.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				DefaultTableModel model = (DefaultTableModel) table_panel_table.getModel();
				for(int i=model.getRowCount()-1;i>=0;i--){
					model.removeRow(i);
					filevector.removeElementAt(i);
				}
			}
		});
	}
	
	private void createPopup()
	{
		popup=new JPopupMenu();
		popup_fixall=new JMenuItem("Fix All");
		popup_tag = new JMenuItem("Fix tag");
		popup_delete = new JMenuItem("Delete");
		popup_selectall = new JMenuItem("Select All");
		popup_clean = new JMenuItem("Clean");
		popup.add(popup_fixall);
		popup.add(popup_tag);
		popup.add(popup_delete);
		popup.add(popup_selectall);
		popup.add(popup_clean);

		popup_fixall.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				for( MP3FILE file : filevector){
					try{
						file.get_correct_tag();
						file.save_id3v1_tag();
						file.save_id3v2_tag();
					}catch( Exception E ){
						E.printStackTrace();
					}

				}
				int totalRows = table_panel_table.getRowCount();
				for ( int row = 0; row < totalRows; row++ )
					update( row );
				refresh( currentindex );
			}

	});
		popup_delete.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				DefaultTableModel model = (DefaultTableModel) table_panel_table.getModel();
				for(int i=rowsSelectedCount-1;i>=0;i--){
					model.removeRow(rowsSelected[i]);
					filevector.removeElementAt(rowsSelected[i]);
				}
			}
	});
		popup_selectall.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				DefaultTableModel model = (DefaultTableModel) table_panel_table.getModel();

				table_panel_table.addRowSelectionInterval(0,model.getRowCount()-1);
			}
	});
		popup_clean.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				DefaultTableModel model = (DefaultTableModel) table_panel_table.getModel();
				for(int i=model.getRowCount()-1;i>=0;i--){
				model.removeRow(i);
				filevector.removeElementAt(i);
				}
			}
	});

		popup_tag.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				DefaultTableModel model = (DefaultTableModel) table_panel_table.getModel();
				MP3FILE temp;
				for(int i = 0 ; i < rowsSelectedCount ; i++){
					temp = filevector.get(rowsSelected[i]);
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
	}

	private void update ( int row )
	{
		int column = 0;
		DefaultTableModel model = (DefaultTableModel) table_panel_table.getModel();
		model.setValueAt( filevector.get( row ).tag.title, row , column++ );
		model.setValueAt( filevector.get( row ).tag.artist, row , column++ );
		model.setValueAt( filevector.get( row ).tag.album, row , column++ );
		model.setValueAt( filevector.get( row ).tag.release_Date, row , column++ );
		model.setValueAt( filevector.get( row ).tag.comment, row , column++ );
	}
	
	private int refresh( int index )
	{
		Text_Album_ID3V1.setText(filevector.get(index).id3v1tag.getAlbum());
		Text_Artist_ID3V1.setText(filevector.get(index).id3v1tag.getArtist());
		Text_Comment_ID3V1.setText(filevector.get(index).id3v1tag.getComment());
		Text_Title_ID3V1.setText(filevector.get(index).id3v1tag.getSongTitle());
		Text_Year_ID3V1.setText(filevector.get(index).id3v1tag.getYear());

		Text_Album_ID3V2.setText(filevector.get(index).id3v2tag.getAlbumTitle());
		Text_Artist_ID3V2.setText(filevector.get(index).id3v2tag.getLeadArtist());
		Text_Comment_ID3V2.setText(filevector.get(index).id3v2tag.getSongComment());
		Text_Title_ID3V2.setText(filevector.get(index).id3v2tag.getSongTitle());
		Text_Year_ID3V2.setText(filevector.get(index).id3v2tag.getYearReleased());
		return 0;
	}
}