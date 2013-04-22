import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
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
import javax.swing.JProgressBar;
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
	//JTextArea table_log;
	JProgressBar progress_bar;
	JScrollPane table_panel_scroll;//,table_log_scroll;
	JButton autofix_button,saveimage_button,rename_button,synchronize,auto_rename;
	JFileChooser savedialog;
	JTable table_panel_table;
	String[] categories = { "File Name","Title", "Artist", "Album", "Release Year", "Comment" };
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
		
		progress_bar = new JProgressBar(0,100);
		progress_bar.setPreferredSize(new Dimension(50,35));
		//pb.setSize(200, 100);
		progress_bar.setString("Welcome To Music Tagger");
		progress_bar.setStringPainted(true);
		progress_bar.setVisible(true);
		progress_bar.setValue(0);
		table_panel.add(progress_bar,BorderLayout.SOUTH);
		//table_log = new JTextArea();
		//table_log_scroll = new JScrollPane( table_log );
		//table_panel.add(table_log_scroll,BorderLayout.SOUTH);
		
		
		autofix_button = new JButton(new ImageIcon("autofix.jpg")); 
		saveimage_button = new JButton(new ImageIcon("save.jpg"));
		rename_button = new JButton(new ImageIcon("rename.jpg"));
		synchronize = new JButton(new ImageIcon("transfer.jpg"));
		auto_rename = new JButton(new ImageIcon("auto_rename.jpg"));
		button_panel = new JPanel( new FlowLayout(FlowLayout.LEFT));
		button_panel.add(autofix_button);
		button_panel.add(saveimage_button);
		button_panel.add(rename_button);
		button_panel.add(auto_rename);
		button_panel.add(synchronize);
		table_panel.add(button_panel,BorderLayout.NORTH);
		createPopup();
		
		autofix_button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//j.setVisible(true);
				
				System.out.println("Auto Fixing....");
				
				int totalRows = table_panel_table.getRowCount();
				progress_bar.setString("auto batch fixing...");
				progress_bar.update(progress_bar.getGraphics());
				
				for ( int row = 0; row < totalRows; row++ ){
					try{
						filevector.get(row).get_correct_tag();
						update( row );
					}catch( Exception E ){
						E.printStackTrace();
					}
					System.out.println((int)((row+1)/(double)totalRows*100));
					//progress_bar.setVisible(true);
					progress_bar.setValue((int)((row+1)/(double)totalRows*100));
					progress_bar.update(progress_bar.getGraphics());
					
				}
				progress_bar.setString("Auto Fixing Done");
				progress_bar.setValue(0);
				progress_bar.update(progress_bar.getGraphics());
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
		auto_rename.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				for( int index = 0 ; index < filevector.size() ; index++ ){
					File oldfile = filevector.get(index).mp3_file.getMp3file();
					String oldname = oldfile.getName();
					String directory = oldfile.getParent();
					String newname = filevector.get(index).id3v2tag.getSongTitle();
					newname = newname + ".mp3";
					BufferedImage image = filevector.get(index).cover;
					String Ly = filevector.get(index).lyrics;
					//= JOptionPane.showInputDialog(table_panel, "rename to :", oldname,JOptionPane.QUESTION_MESSAGE);
					if( newname.compareTo(oldname) != 0 ){
						//if( newname.endsWith(".mp3") ){
							System.out.println("rename processing..");
							File newfile = new File(directory + "/"+newname);
							oldfile.renameTo(newfile);
							try{
								MP3FILE temp = temp = new MP3FILE(newfile,image,Ly);
								filevector.set(index, temp);
								refreshID3v1();
								refreshID3v2();
								refreshLyrics();
								refreshCover();
							}catch(Exception e3){	
							}
						//}
					}
					update(index);
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
					BufferedImage image = filevector.get(currentRowIndex).cover;
					String Ly = filevector.get(currentRowIndex).lyrics;
					if( newname.compareTo(oldname) != 0 ){
						if( newname.endsWith(".mp3") ){
							System.out.println("rename processing..");
							File newfile = new File(directory + "/"+newname);
							oldfile.renameTo(newfile);
							try{
								MP3FILE temp = temp = new MP3FILE(newfile,image,Ly);
								filevector.set(currentRowIndex, temp);
								refreshID3v1();
								refreshID3v2();
								refreshLyrics();
								refreshCover();
							}catch(Exception e3){	
							}
						}
					}
					update(currentRowIndex);
				}
			}
		});
		synchronize.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Iterator<MP3FILE> iterate = filevector.iterator();
				Vector<File> chosen_files = new Vector<File>();
				while( iterate.hasNext() ){
					MP3FILE m = iterate.next();
					chosen_files.add(m.mp3_file.getMp3file());
				}
				transfer_frame f = new transfer_frame(chosen_files);
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
		table_panel_table.getTableHeader().addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				table_panel_table.clearSelection();
				int col =  table_panel_table.getTableHeader().columnAtPoint(e.getPoint());
				mp3structure[] list = new mp3structure[filevector.size()];
				for( int i = 0 ; i < filevector.size() ; i++ ){
					list[i] = new mp3structure(filevector.get(i).mp3_filename,filevector.get(i).id3v2tag.getSongTitle(),filevector.get(i).id3v2tag.getLeadArtist(),filevector.get(i).id3v2tag.getAlbumTitle(),filevector.get(i).id3v2tag.getYearReleased(),filevector.get(i));
				}
				switch(col){
				case 0:
					Arrays.sort(list,new comparefilename());
					break;
				case 1:
					Arrays.sort(list,new comparetitle());
					break;
				case 2:
					Arrays.sort(list,new compareartist());
					break;
				case 3:
					Arrays.sort(list,new comparealbum());
					break;
				case 4:
					Arrays.sort(list,new compareyear());
					break;
				}
				for( int i = 0 ; i < filevector.size() ; i++ ){
					filevector.set(i, list[i].mp3);
				}
				for( int i = 0 ; i < filevector.size() ; i++ ){
					update(i);
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
										model.addRow(new Object[]{ mp3.mp3_filename,mp3.id3v2tag.getSongTitle(), mp3.id3v2tag.getLeadArtist() , mp3.id3v2tag.getAlbumTitle() , mp3.id3v2tag.getYearReleased() , mp3.id3v2tag.getSongComment() });
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
				progress_bar.setString("batch fixing...");
				progress_bar.update(progress_bar.getGraphics());
				for ( int row = 0; row < totalRows; row++ ){
					try{
						filevector.get(row).get_correct_tag();
						update( row );
					}catch( Exception E ){
						E.printStackTrace();
					}
					progress_bar.setValue((int)((row+1)/(double)totalRows*100));
					progress_bar.update(progress_bar.getGraphics());
				}
				progress_bar.setString("batch fixing done");
				progress_bar.setValue(0);
				progress_bar.update(progress_bar.getGraphics());
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
				progress_bar.setString("fixing...");
				progress_bar.update(progress_bar.getGraphics());
				for( int i = 0 ; i < selectedRowCount ; i++ ){
					try{
						filevector.get(selectedRows[i]).get_correct_tag();
						update(selectedRows[i]);
					}catch( Exception e1 ){
					}
					progress_bar.setValue((int)((i+1)/(double)selectedRowCount*100));
					progress_bar.update(progress_bar.getGraphics());
				}
				progress_bar.setString("fixing done");
				progress_bar.setValue(0);
				progress_bar.update(progress_bar.getGraphics());
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
				progress_bar.setString("Batch Fixing...");
				progress_bar.update(progress_bar.getGraphics());
				for ( int row = 0; row < totalRows; row++ ){
					try{
						filevector.get(row).get_correct_tag();
						update( row );
					}catch( Exception E ){
						E.printStackTrace();
					}
					progress_bar.setValue((int)((row+1)/(double)totalRows*100));
					progress_bar.update(progress_bar.getGraphics());
				}
				progress_bar.setString("Batch Fixing Done");
				progress_bar.setValue(0);
				progress_bar.update(progress_bar.getGraphics());
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
				progress_bar.setString("Fixing...");
				progress_bar.update(progress_bar.getGraphics());
				for( int i = 0 ; i < selectedRowCount ; i++ ){
					try{
						filevector.get(selectedRows[i]).get_correct_tag();
						update(selectedRows[i]);
					}catch( Exception e1 ){
					}
					progress_bar.setValue((int)((i+1)/(double)selectedRowCount*100));
					progress_bar.update(progress_bar.getGraphics());
				}
				progress_bar.setString("Fixing Done");
				progress_bar.setValue(0);
				progress_bar.update(progress_bar.getGraphics());
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
		model.setValueAt( filevector.get( row ).mp3_filename , row , column++ );
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
	
	private class mp3structure{
		public String FileName;
		public String Title;
		public String Artist;
		public String Album;
		public String Year;
		public MP3FILE mp3;
		public mp3structure( String filename , String title , String artist , String album , String year , MP3FILE m ){
			FileName = filename;
			Title = title;
			Artist = artist;
			Album = album;
			Year = year;
			mp3 = m;
		}
	}
	private class comparefilename<T> implements Comparator<T>{

		@Override
		public int compare(T o1, T o2) {
			// TODO Auto-generated method stub
			mp3structure m1 = (mp3structure)o1;
			mp3structure m2 = (mp3structure)o2;
			return m1.FileName.compareTo(m2.FileName);
		}
		
	}
	private class comparetitle<T> implements Comparator<T>{

		@Override
		public int compare(T o1, T o2) {
			// TODO Auto-generated method stub
			mp3structure m1 = (mp3structure)o1;
			mp3structure m2 = (mp3structure)o2;
			return m1.Title.compareTo(m2.Title)	; 
		}
		
	}
	private class compareartist<T> implements Comparator<T>{

		@Override
		public int compare(T o1, T o2) {
			// TODO Auto-generated method stub
			mp3structure m1 = (mp3structure)o1;
			mp3structure m2 = (mp3structure)o2;
			return m1.Artist.compareTo(m2.Artist);
		}
		
	}
	private class comparealbum<T> implements Comparator<T>{

		@Override
		public int compare(T o1, T o2) {
			// TODO Auto-generated method stub
			mp3structure m1 = (mp3structure)o1;
			mp3structure m2 = (mp3structure)o2;
			return m1.Album.compareTo(m2.Album);
		}
		
	}
	private class compareyear implements Comparator{

		@Override
		public int compare(Object o1, Object o2) {
			// TODO Auto-generated method stub
			mp3structure m1 = (mp3structure)o1;
			mp3structure m2 = (mp3structure)o2;
			int y1 = Integer.parseInt(m1.Year);
			int y2 = Integer.parseInt(m2.Year);
			if( y1 > y2 ) return 1;
			else if( y1 < y2 ) return -1;
			return 0;
		}
		
	}
	
	
	private class transfer_frame extends JFrame{
		private JFrame itself;
		public JTable file_list_table;
		public JScrollPane file_pane;
		public JPanel button_panel;
		public JButton browse_button,wifi_syn_button,synchronize_button,cancel_button,delete_button;
		public JLabel progress_log;
		public Vector<File> file_list;
		
		private final JFileChooser fileselector = new JFileChooser();
		public transfer_frame( Vector<File> chosen_files ){
			super();
			itself = this;
			construct_button_panel();
			construct_list_table(chosen_files);
			construct_log();
			setLayout( new BorderLayout() );
			add(file_pane,BorderLayout.CENTER);
			add(button_panel,BorderLayout.NORTH);
			add(progress_log,BorderLayout.SOUTH);
			
			setTitle("File Transfer");
			pack();
			setSize( 700,400 );
			setVisible(true);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent event){
					itself.dispose();
				}
			});
		}
		
		private void construct_button_panel(){
			fileselector.setMultiSelectionEnabled(true);
			button_panel = new JPanel();
			button_panel.setLayout(new BoxLayout(button_panel,BoxLayout.LINE_AXIS));
			browse_button = new JButton(" Browse ");
			wifi_syn_button = new JButton("WIFI Syn");
			synchronize_button = new JButton("Synchronize");
			cancel_button = new JButton(" Cancel ");
			delete_button = new JButton(" Delete ");
			browse_button.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					int returnVal = fileselector.showOpenDialog(file_list_table);
					if ( returnVal == JFileChooser.APPROVE_OPTION ) {
						File currentFile[] = fileselector.getSelectedFiles();
						int len = currentFile.length;
						String filename;
						File mp3;
						for( int i = 0; i < len; i++ ){
							//filename = currentFile[ i ].getName();
							//String extension = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
							//String mp3extension = "mp3";
							//if (extension.compareTo(mp3extension) != 0) {
							//	JOptionPane.showMessageDialog(null, "Currently We only support MP3 menu_file");
							//}else{
								file_list.add(currentFile[i]);
								DefaultTableModel model = (DefaultTableModel) file_list_table.getModel();
								model.addRow(new Object[]{ currentFile[i].getName(), currentFile[i].getParent()});        	
							//}
						}
					} else {}
				}
				
			});
			cancel_button.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					itself.dispose();
				}
				
			});
			delete_button.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					DefaultTableModel model = (DefaultTableModel) file_list_table.getModel();
					while( file_list_table.getSelectedRowCount() > 0 ){
						file_list.remove(file_list_table.getSelectedRow());
						model.removeRow(file_list_table.getSelectedRow());
					}
				}
				
			});
			wifi_syn_button.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					
					ServerSocket server = null;
					try {
						Socket clientSocket;
						int port = Integer.parseInt(JOptionPane.showInputDialog("Enter Port Number", "8000"));
						
						
						server = new ServerSocket(port);
						clientSocket = server.accept();
						int length = file_list.size();
						DataOutputStream dataoutput = new DataOutputStream(clientSocket.getOutputStream());
						dataoutput.writeInt(length);
						Iterator<File> f = file_list.iterator();
						File transfer_file;
						progress_log.setText("FILE SYNCHRONIZING PLEASE WAIT");
						while( f.hasNext() ){
							
							transfer_file = f.next();
							String name = transfer_file.getName();
							int namelength = name.length();
							long size = transfer_file.length();
							System.out.println(size);
							FileInputStream input = new FileInputStream(transfer_file);
							byte[] data = new byte[(int)size];
							input.read(data);
							dataoutput.writeInt(namelength);
							for( int i = 0 ; i < namelength ; i++ ){
								dataoutput.writeChar(name.charAt(i));
							}
							dataoutput.writeLong(size);
							for ( int j = 0 ; j < size ; j++ ){
								dataoutput.writeByte(data[j]);
							}
							
						}
						dataoutput.close();
						clientSocket.close();
						server.close();
						progress_log.setText("FILE SYNCHRONIZATION DONE");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						//e1.printStackTrace();
						System.out.println("error wifi sync");
						try{
							if(server != null)
								server.close();
						}catch(Exception e3){
							
						}
					}
				}
				
			});
			synchronize_button.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					Socket socket;
					try{	
						socket = new Socket("sslab04.cs.purdue.edu",8080);
						int length = file_list.size();
						DataOutputStream dataoutput = new DataOutputStream(socket.getOutputStream());
						dataoutput.writeInt(1);
						dataoutput.writeInt(length);
						Iterator<File> f = file_list.iterator();
						File transfer_file;
						
						progress_log.setText("FILE SYNCHRONIZING PLEASE WAIT");
						while( f.hasNext() ){
							
							transfer_file = f.next();
							String name = transfer_file.getName();
							int namelength = name.length();
							long size = transfer_file.length();
							FileInputStream input = new FileInputStream(transfer_file);
							byte[] data = new byte[(int)size];
							System.out.println(data.length);
							input.read(data);
							input.close();
							dataoutput.writeInt(namelength);
							for( int i = 0 ; i < namelength ; i++ ){
								dataoutput.writeChar(name.charAt(i));
							}
							dataoutput.writeLong(size);
							for ( int j = 0 ; j < size ; j++ ){
								dataoutput.writeByte(data[j]);
							}
							
							
						}
						dataoutput.close();
						socket.close();
						progress_log.setText("FILE SYNCHRONIZATION DONE");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			button_panel.add(wifi_syn_button);
			button_panel.add(synchronize_button);
			button_panel.add(browse_button);
			button_panel.add(cancel_button);
			button_panel.add(delete_button);
			
		}
		public void construct_list_table(Vector<File> chosen_files){
			file_list = new Vector<File>(chosen_files);
			file_list_table = new JTable(new DefaultTableModel( new Object[][]{}, new String[]{"Music Name","Directory"} ){
				public boolean isCellEditable(int rowIndex, int colIndex) {
					return false;  
				}
			});
			file_list_table.setCellSelectionEnabled(false);
			file_list_table.setRowSelectionAllowed(true);
			file_pane = new JScrollPane(file_list_table);
			DefaultTableModel model = (DefaultTableModel)file_list_table.getModel();
			Iterator<File> f = file_list.iterator();
			while( f.hasNext() ){
				File file = f.next();
				model.addRow(new Object[]{file.getName(),file.getParent()});
			}
		}
		public void construct_log(){
			progress_log = new JLabel();
			progress_log.setVisible(true);
			progress_log.setText("FILE SYNCHRONIZATION");
		}
	}
}
