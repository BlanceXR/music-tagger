import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
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
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;


public class Gui extends JFrame
{
	
	//////////////////////  Objects  /////////////////////////
	//JDialog j; 
	
	//////// Menu ////////
	JMenuBar menu_bar;
	JMenu menu_file;
	JMenuItem file_open, file_exit;
	JMenu menu_edit;
	JMenuItem edit_fix_all , edit_tag, edit_delete, edit_select_all, edit_clean;
	final JFileChooser filechooser = new JFileChooser();
	
	//////// Table_Panel /////////
	JPanel table_panel,button_panel;
	JTextArea table_log;
	JScrollPane table_panel_scroll,table_log_scroll;
	JButton autofix_button,saveimage_button,rename_button;
	JFileChooser savedialog;
	JTable table_panel_table;
	String[] categories = { "Title", "Artist", "Album", "Release Year", "Comment" };
	Object[][] data = { };
	JPopupMenu popup;
	JMenuItem popup_fixall, popup_tag, popup_delete, popup_selectall, popup_clean;
	int selectedRows[];
	int selectedRowCount;
	int currentRowIndex;
	
	//////// Side_Panel /////////
	JPanel side_panel,album_panel,lyrics_panel;
	BufferedImage default_cover, cover;
	JLabel album_title,lyrics_title;
	JLabel album_art;
	JScrollPane side_panel_scroll;
	JTextArea lyrics_field;
	String Lyrics;
	
	///// ID3V1 /////	
	JLabel Label_Title_ID3V1;
	JLabel Label_Year_ID3V1;
	JLabel Label_Artist_ID3V1;
	//JLabel Label_Composer_ID3V1;
	JLabel Label_Album_ID3V1;
	JLabel Label_Comment_ID3V1;
	JLabel Label_Genre_ID3V1;
	final JTextField Text_Title_ID3V1 = new JTextField(  );
	final JTextField Text_Year_ID3V1 = new JTextField(  );
	final JTextField Text_Artist_ID3V1 = new JTextField(  );
	//final JTextField Text_Composer_ID3V1 = new JTextField(  );
	final JTextField Text_Album_ID3V1 = new JTextField(  );
	final JTextField Text_Comment_ID3V1 = new JTextField(  );
	final JTextField Text_Genre_ID3V1 = new JTextField(  );
	JButton fix_ID3V1;
	JButton reset_ID3V1;
	
	///// ID3V1 /////
	JLabel Label_Title_ID3V2;
	JLabel Label_Year_ID3V2;
	JLabel Label_Artist_ID3V2;
	//JLabel Label_Composer_ID3V2;
	JLabel Label_Album_ID3V2;
	JLabel Label_Comment_ID3V2;
	JLabel Label_Genre_ID3V2;		
	final JTextField Text_Title_ID3V2 = new JTextField(  );
	final JTextField Text_Year_ID3V2 = new JTextField(  );
	final JTextField Text_Artist_ID3V2 = new JTextField(  );
	//final JTextField Text_Composer_ID3V2 = new JTextField(  );
	final JTextField Text_Album_ID3V2 = new JTextField(  );
	final JTextField Text_Comment_ID3V2 = new JTextField(  );
	final JTextField Text_Genre_ID3V2 = new JTextField(  );
	JButton fix_ID3V2;
	JButton reset_ID3V2;

	//////// MP3_FILE /////////
	Vector <MP3FILE> filevector;
	static boolean singleton = false; 
	static Gui gui = null;
	
	// getter method for Frame_Gui
	public static Gui getGui()
	{
		if( !singleton ){
			singleton = true;
			gui = new Gui();
		}
		return gui;
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
		//j = new JDialog(this, "Tag Fixing.... please wait");
		table_panel_table = new JTable( new DefaultTableModel( data, categories ) ){
			//Disallow the editing of any cell
			public boolean isCellEditable(int rowIndex, int colIndex) {
				return false;  
			}
		};
		table_panel_table.setCellSelectionEnabled(false);
		table_panel_table.setRowSelectionAllowed(true);
		table_panel_scroll = new JScrollPane( table_panel_table );
		table_panel = new JPanel( new BorderLayout() );
		table_panel.add( table_panel_scroll, BorderLayout.CENTER );
		table_log = new JTextArea();
		table_log_scroll = new JScrollPane( table_log );
		table_panel.add(table_log_scroll,BorderLayout.SOUTH);
		autofix_button = new JButton(new ImageIcon("autofix.jpg")); 
		saveimage_button = new JButton(new ImageIcon("save.jpg"));
		rename_button = new JButton(new ImageIcon("rename.jpg"));
		button_panel = new JPanel( new FlowLayout(FlowLayout.LEFT));
		button_panel.add(autofix_button);
		button_panel.add(saveimage_button);
		button_panel.add(rename_button);
		table_panel.add(button_panel,BorderLayout.NORTH);
		createPopup();
		
		autofix_button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//j.setVisible(true);
				int totalRows = table_panel_table.getRowCount();
				for ( int row = 0; row < totalRows; row++ ){
					try{
						filevector.get(row).get_correct_tag();
						update( row );
					}catch( Exception E ){
						E.printStackTrace();
					}
				}
				refreshID3v1();
				refreshID3v2();
				refreshCover();
				refreshLyrics();
				//j.setVisible(false);
			}
		});
		
		savedialog = new JFileChooser();
		saveimage_button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if( default_cover != cover ){
					savedialog.setSelectedFile(new File("untitle.jpg"));
					int retval = savedialog.showSaveDialog(saveimage_button);
					if( retval == JFileChooser.APPROVE_OPTION ){
						File savefile = savedialog.getSelectedFile();
						try{
							ImageIO.write(cover,"jpg",savefile);
						}catch( IOException e1 ){
						}
					}
				}
			}
		});
		rename_button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(selectedRowCount > 1){
					JOptionPane.showMessageDialog(table_panel, "please select single file");
				}else if(selectedRowCount == 1){
					File oldfile = filevector.get(currentRowIndex).mp3_file.getMp3file();
					String oldname = oldfile.getName();
					String directory = oldfile.getParent();
					String newname = JOptionPane.showInputDialog(table_panel, "rename to :", oldname,JOptionPane.QUESTION_MESSAGE);
					if( newname.compareTo(oldname) != 0 ){
						if( newname.endsWith(".mp3") ){
							System.out.println("rename processing..");
							File newfile = new File(directory + "/"+newname);
							oldfile.renameTo(newfile);
							try{
								MP3FILE temp = temp = new MP3FILE(newfile);
								filevector.set(currentRowIndex, temp);
								refreshID3v1();
								refreshID3v2();
								refreshLyrics();
								refreshCover();
							}catch(Exception e3){	
							}
						}
					}
				}
			}
		});
		
		table_panel_scroll.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e) {
				Point point = e.getPoint();
				if( !table_panel_table.contains(point) ){
					table_panel_table.clearSelection();
				}
			}
		});		
		
		table_panel_table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("row value change");
				ListSelectionModel model = (ListSelectionModel)e.getSource();
				selectedRowCount = table_panel_table.getSelectedRowCount();
				if( selectedRowCount > 0 ){
					currentRowIndex = model.getLeadSelectionIndex();
					selectedRows = table_panel_table.getSelectedRows();
				}else{
					currentRowIndex = -1;
					selectedRows = null;
				}	
				System.out.println("lead selection:" + currentRowIndex);
				refreshID3v1();
				refreshID3v2();
				refreshCover();
				refreshLyrics();
			}

		});
		
		table_panel_table.addMouseListener( new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				System.out.println("left click");
				if (e.isPopupTrigger())
				{	
					JTable source = (JTable)e.getSource();
					int row = source.rowAtPoint( e.getPoint() );
					int column = source.columnAtPoint( e.getPoint() );
					
					//if no row is selected before right click or if the row at cursor position is not selected, then change selection
					if( selectedRowCount <= 0 || !source.isRowSelected(row) ){
						source.setRowSelectionInterval(row, row);
					}
					System.out.println("right click pressed");
					popup.show(e.getComponent(),e.getX(),e.getY());
				}
			}
		});
		
	}
	
	private void constructSidePanel()
	{
		currentRowIndex = -1;
		side_panel = new JPanel();
		side_panel.setLayout(new GridLayout(3,1));
		
		//////////////////////////////////       Album  Art       ///////////////////////////////////////////
		album_panel = new JPanel();
		album_panel.setLayout( new BoxLayout( album_panel, BoxLayout.LINE_AXIS) );
//		album_title = new JLabel("Album Arts\n",JLabel.CENTER);

		try
		{	default_cover = ImageIO.read(new File("default_cover.jpg"));	}
		catch (IOException e)
		{	e.printStackTrace();	}
		cover = default_cover; 
		album_art = new JLabel( new ImageIcon(cover) , JLabel.CENTER);
		album_panel.add(album_art);
		side_panel.add( album_panel );
		
		/////////////////////////////////         Tabbed Panel      /////////////////////////////////////////
		
		JTabbedPane tabPane = new JTabbedPane();
		JPanel gridPane_ID3V1 = new JPanel( new GridLayout(6, 2) );
		JPanel boxPane_ID3V1 = new JPanel(  );
		JPanel buttonPane_ID3V1 = new JPanel(  );
		JPanel gridPane_ID3V2 = new JPanel( new GridLayout(6, 2) );
		JPanel boxPane_ID3V2 = new JPanel(  );
		JPanel buttonPane_ID3V2 = new JPanel(  );
		
		//////////////////////////////////		    ID3V1 	       ////////////////////////////////////////
		
		Label_Title_ID3V1 = new JLabel( "Title" );
		Label_Year_ID3V1 = new JLabel( "Year" );
		Label_Artist_ID3V1 = new JLabel( "Artist" );
		//Label_Composer_ID3V1 = new JLabel( "Composer" );
		Label_Album_ID3V1 = new JLabel( "Album" );
		Label_Comment_ID3V1 = new JLabel( "Comment" );
		Label_Genre_ID3V1 = new JLabel( "Genre" );	
		fix_ID3V1 = new JButton( "Fix" );
		reset_ID3V1 = new JButton( "Reset" );
		reset_ID3V1.addActionListener(
				new ActionListener(){
					public void actionPerformed( ActionEvent e ){
						// TODO reset to defaults
						refreshID3v1();							
					}
				});
		
		gridPane_ID3V1.add( Label_Title_ID3V1 );
		gridPane_ID3V1.add( Text_Title_ID3V1 );
		gridPane_ID3V1.add( Label_Year_ID3V1 );
		gridPane_ID3V1.add( Text_Year_ID3V1 );
		gridPane_ID3V1.add( Label_Artist_ID3V1 );
		gridPane_ID3V1.add( Text_Artist_ID3V1 );
		//gridPane_ID3V1.add( Label_Composer_ID3V1 );
		//gridPane_ID3V1.add( Text_Composer_ID3V1 );
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
		//Label_Composer_ID3V2 = new JLabel( "Composer" );
		Label_Album_ID3V2 = new JLabel( "Album" );
		Label_Comment_ID3V2 = new JLabel( "Comment" );
		Label_Genre_ID3V2 = new JLabel( "Genre" );	
		
		fix_ID3V2 = new JButton( "Fix" );
		
		reset_ID3V2 = new JButton( "Reset" );
		reset_ID3V2.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						// TODO reset to defaults
						refreshID3v2();
					}
				});
		addfixlistener();
		gridPane_ID3V2.add( Label_Title_ID3V2 );
		gridPane_ID3V2.add( Text_Title_ID3V2 );
		gridPane_ID3V2.add( Label_Year_ID3V2 );
		gridPane_ID3V2.add( Text_Year_ID3V2 );
		gridPane_ID3V2.add( Label_Artist_ID3V2 );
		gridPane_ID3V2.add( Text_Artist_ID3V2 );
		//gridPane_ID3V2.add( Label_Composer_ID3V2 );
		//gridPane_ID3V2.add( Text_Composer_ID3V2 );
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
		lyrics_title = new JLabel("Lyrics\n",JLabel.CENTER);
		Lyrics = "lyris preview";
		lyrics_field = new JTextArea(Lyrics);
		lyrics_field.setEditable(false);
		side_panel_scroll = new JScrollPane(lyrics_field);
		side_panel_scroll.setPreferredSize(new Dimension(250,300));
		side_panel.add(side_panel_scroll);
		Border border = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		side_panel.setBorder(border);
   }
	
	public void addfixlistener(){
		fix_ID3V1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if( currentRowIndex >= 0 ){
					MP3FILE temp = filevector.get(currentRowIndex);
					try{
						temp.id3v1tag.setAlbumTitle(Text_Album_ID3V1.getText());
						temp.id3v1tag.setArtist(Text_Artist_ID3V1.getText());
						temp.id3v1tag.setYearReleased(Text_Year_ID3V1.getText());
						temp.id3v1tag.setTitle(Text_Title_ID3V1.getText());
						temp.id3v1tag.setSongComment(Text_Comment_ID3V1.getText());
						//temp.id3v1tag.setAuthorComposer(Text_Composer_ID3V1.getText());
						temp.id3v1tag.setSongGenre(Text_Genre_ID3V1.getText());
						temp.mp3_file.setID3v1Tag(temp.id3v1tag);
						temp.mp3_file.save();
						refreshID3v1();
					}catch( Exception E ){
					}
				}else
					return;
			}
			
		});
		fix_ID3V2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if( currentRowIndex >= 0 ){
					MP3FILE temp = filevector.get( currentRowIndex );
					try{
						temp.id3v2tag.setAlbumTitle(Text_Album_ID3V2.getText());
						temp.id3v2tag.setLeadArtist(Text_Artist_ID3V2.getText());
						temp.id3v2tag.setYearReleased(Text_Year_ID3V2.getText());
						temp.id3v2tag.setSongTitle(Text_Title_ID3V2.getText());
						temp.id3v2tag.setSongComment(Text_Comment_ID3V2.getText());
						//temp.id3v2tag.setAuthorComposer(Text_Composer_ID3V2.getText());
						temp.id3v2tag.setSongGenre(Text_Genre_ID3V2.getText());
						temp.mp3_file.setID3v2Tag(temp.id3v2tag);
						temp.mp3_file.save();
						update( currentRowIndex );
						refreshID3v2( );
					}catch( Exception E ){ 
					}
				}else
					return;
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
	
	//////////////////////////////////////////////////////////////////

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
										model.addRow(new Object[]{ mp3.id3v2tag.getSongTitle(), mp3.id3v2tag.getLeadArtist() , mp3.id3v2tag.getAlbumTitle() , mp3.id3v2tag.getYearReleased() , mp3.id3v2tag.getSongComment() });
										System.out.println("insert file " + currentFile[i].getName() + "    filevector size: " + filevector.size());
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
		menu_edit.setMnemonic('E');
		edit_fix_all = new JMenuItem("Fix All");
		edit_tag = new JMenuItem("Fix Selected");
		edit_delete = new JMenuItem("Delete");
		edit_select_all = new JMenuItem("Select All");
		edit_clean = new JMenuItem("Clean");
		menu_edit.add(edit_fix_all);
		menu_edit.add(edit_tag);
		menu_edit.add(edit_delete);
		menu_edit.add(edit_select_all);
		menu_edit.add(edit_clean);
		menu_bar.add(menu_edit);
		
		edit_fix_all.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				int totalRows = table_panel_table.getRowCount();
				for ( int row = 0; row < totalRows; row++ ){
					try{
						filevector.get(row).get_correct_tag();
						update( row );
					}catch( Exception E ){
						E.printStackTrace();
					}
				}
				refreshID3v1();
				refreshID3v2();
				refreshCover();
				refreshLyrics();
			}
		});
		
		edit_tag.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				// TODO
				for( int i = 0 ; i < selectedRowCount ; i++ ){
					try{
						filevector.get(selectedRows[i]).get_correct_tag();
						update(selectedRows[i]);
					}catch( Exception e1 ){
					}
				}
				refreshID3v1();
				refreshID3v2();
				refreshCover();
				refreshLyrics();
			}
		});

		edit_delete.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				while( table_panel_table.getSelectedRowCount() > 0 ){
					int index = table_panel_table.getSelectedRow();
					DefaultTableModel model = (DefaultTableModel)table_panel_table.getModel();
					filevector.removeElementAt( index );
					model.removeRow(index);
				}
			}
		});
		
		edit_select_all.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				table_panel_table.setRowSelectionInterval( 0 , table_panel_table.getRowCount() - 1 );
			}
		});
		
		
		//clean table entry
		edit_clean.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int rowcount;
				while( (rowcount = table_panel_table.getRowCount()) > 0 ){
					DefaultTableModel model = (DefaultTableModel)table_panel_table.getModel();
					filevector.remove(rowcount - 1);
					model.removeRow(rowcount - 1);
				}
			}
		});
	}
	
	private void createPopup()
	{
		popup=new JPopupMenu();
		popup_fixall=new JMenuItem("Fix All");
		popup_tag = new JMenuItem("Fix selected");
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
				int totalRows = table_panel_table.getRowCount();
				for ( int row = 0; row < totalRows; row++ ){
					try{
						filevector.get(row).get_correct_tag();
						update( row );
					}catch( Exception E ){
						E.printStackTrace();
					}
				}
				refreshID3v1();
				refreshID3v2();
				refreshCover();
				refreshLyrics();
			}
	});
		popup_delete.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e ){
				while( table_panel_table.getSelectedRowCount() > 0 ){
					int index = table_panel_table.getSelectedRow();
					DefaultTableModel model = (DefaultTableModel)table_panel_table.getModel();
					filevector.removeElementAt( index );
					model.removeRow(index);
				}
			}
	});
		popup_selectall.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				table_panel_table.setRowSelectionInterval( 0 , table_panel_table.getRowCount() - 1 );
			}
	});
		popup_clean.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int rowcount;
				while( (rowcount = table_panel_table.getRowCount()) > 0 ){
					DefaultTableModel model = (DefaultTableModel)table_panel_table.getModel();
					filevector.remove(rowcount - 1);
					model.removeRow(rowcount - 1);
				}
			}
	});

		popup_tag.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				// TODO
				for( int i = 0 ; i < selectedRowCount ; i++ ){
					try{
						filevector.get(selectedRows[i]).get_correct_tag();
						update(selectedRows[i]);
					}catch( Exception e1 ){
					}
				}
				refreshID3v1();
				refreshID3v2();
				refreshCover();
				refreshLyrics();
			}
	});
		
	}
	
	//////////////////// Helper Methods /////////////////////
	
	private void update ( int row )
	{
		int column = 0;
		DefaultTableModel model = (DefaultTableModel) table_panel_table.getModel();
		model.setValueAt( filevector.get( row ).id3v2tag.getSongTitle() , row , column++ );
		model.setValueAt( filevector.get( row ).id3v2tag.getLeadArtist() , row , column++ );
		model.setValueAt( filevector.get( row ).id3v2tag.getAlbumTitle() , row , column++ );
		model.setValueAt( filevector.get( row ).id3v2tag.getYearReleased() , row , column++ );
		model.setValueAt( filevector.get( row ).id3v2tag.getSongComment() , row , column );
	}
	
	
	private int refreshID3v1()
	{
		if( currentRowIndex >= 0 ){
			int index = currentRowIndex;
			Text_Album_ID3V1.setText(filevector.get(index).id3v1tag.getAlbum());
			Text_Artist_ID3V1.setText(filevector.get(index).id3v1tag.getArtist());
			Text_Comment_ID3V1.setText(filevector.get(index).id3v1tag.getComment());
			Text_Title_ID3V1.setText(filevector.get(index).id3v1tag.getSongTitle());
			Text_Year_ID3V1.setText(filevector.get(index).id3v1tag.getYear());
			//Text_Composer_ID3V1.setText(filevector.get(index).id3v1tag.getAuthorComposer());
			Text_Genre_ID3V1.setText(filevector.get(index).id3v1tag.getSongGenre());
		}else{
			Text_Album_ID3V1.setText("");
			Text_Artist_ID3V1.setText("");
			Text_Comment_ID3V1.setText("");
			Text_Title_ID3V1.setText("");
			Text_Year_ID3V1.setText("");
			//Text_Composer_ID3V1.setText("");
			Text_Genre_ID3V1.setText("");
		}
		return 0;
	}
	
	
	private int refreshID3v2()
	{
		if( currentRowIndex >= 0 ){
			int index = currentRowIndex;
			Text_Album_ID3V2.setText(filevector.get(index).id3v2tag.getAlbumTitle());
			Text_Artist_ID3V2.setText(filevector.get(index).id3v2tag.getLeadArtist());
			Text_Comment_ID3V2.setText(filevector.get(index).id3v2tag.getSongComment());
			Text_Title_ID3V2.setText(filevector.get(index).id3v2tag.getSongTitle());
			Text_Year_ID3V2.setText(filevector.get(index).id3v2tag.getYearReleased());
			//Text_Composer_ID3V2.setText(filevector.get(index).id3v2tag.getAuthorComposer());
			Text_Genre_ID3V2.setText(filevector.get(index).id3v2tag.getSongGenre());
		}else{
			Text_Album_ID3V2.setText("");
			Text_Artist_ID3V2.setText("");
			Text_Comment_ID3V2.setText("");
			Text_Title_ID3V2.setText("");
			Text_Year_ID3V2.setText("");
			//Text_Composer_ID3V2.setText("");
			Text_Genre_ID3V2.setText("");
		}
		
		return 0;
	}
	private int refreshCover(){
		if( currentRowIndex >= 0 ){
			int index = currentRowIndex;
			cover = filevector.get(currentRowIndex).cover;
			if(cover == null)
				cover = default_cover;
			ImageIcon icon = new ImageIcon(cover);
			album_art.setIcon(icon);
		}
		return 0;
	}
	private int refreshLyrics(){
		if( currentRowIndex >= 0 ){
			int index = currentRowIndex;
			if( filevector.get(currentRowIndex).lyrics != null ){
				Lyrics = new String(filevector.get(currentRowIndex).lyrics);
				lyrics_field.setText(Lyrics);
			}
			else{
				Lyrics = "lyrics preview";
				lyrics_field.setText(Lyrics);
			}				
		}else{
			Lyrics = "lyrics preview";
			lyrics_field.setText(Lyrics);
		}
		return 0;
	}
}
