import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.io.File;

public class Menu
{
	JMenuBar menu_bar;
	//File currentOpenFile;
	JMenu file;
	JMenuItem open, exit;
	
	JMenu edit;
	JMenuItem tag, delete, select, clean; 
	final JFileChooser fc = new JFileChooser();
	
	public Menu()
	{
		menu_bar = new JMenuBar();
		createFileMenu();
		createEditMenu();
	}
	
	public void createFileMenu()
	{
		file = new JMenu("File");
		file.setMnemonic('F');
		
		open = new JMenuItem("Open File...");
		
		exit = new JMenuItem("Exit");
		
		file.add(open);
		fc.setMultiSelectionEnabled(true);
		open.addActionListener(
				new ActionListener(){
						public void actionPerformed(ActionEvent e){
							int returnVal = fc.showOpenDialog(open);
							 if (returnVal == JFileChooser.APPROVE_OPTION) {
						            File currentFile[];
						            currentFile= fc.getSelectedFiles();
						            int len=currentFile.length;
						            String[] filename=new String[len];
						            MP3FILE []  mp3=new MP3FILE[len];
						            for(int i=0;i<len;i++){
						            
						            	filename[i]= currentFile[i].getName();
						            String extension = filename[i].substring(filename[i].lastIndexOf(".") + 1, filename[i].length());
						            String mp3extension = "mp3";
						            if (extension.compareTo(mp3extension) != 0) {
						                JOptionPane.showMessageDialog(null, "Currently We only support MP3 file");
						            }else{						        	
						            	
										try {
											mp3[i] = new MP3FILE(currentFile[i]);
											Main_Frame.fileVector.add(mp3[i]);
											Main_Frame.insertNew(mp3[i]);
										} catch (Exception e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}						        	
						            }
							 }
						            
						        } else {
						            //did not open successfully
						        }
							 
						}
				});
		file.add(exit);
		exit.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						System.exit(0);
					}
				});
		menu_bar.add(file);
	
		
	}
	
	
	public void createEditMenu()
	{
		edit = new JMenu("Edit");
		file.setMnemonic('E');
		
		tag = new JMenuItem("Fix Tag");
		delete = new JMenuItem("Delete");
		select = new JMenuItem("Select All");
		clean = new JMenuItem("Clean");
		
		edit.add(tag);
		edit.add(delete);
		edit.add(select);
		edit.add(clean);
		
		menu_bar.add(edit);
		tag.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//TODO:  ckean tag here
				System.out.println("fix");
			}
	});

		delete.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//TODO: delete tag here
				int [] selecetdRows;
				selecetdRows=Table_Panel.table.getSelectedRows();
				int selecetdRowsCount=Table_Panel.table.getSelectedRowCount();
				
				
				DefaultTableModel model = (DefaultTableModel) Table_Panel.table.getModel();
				for(int i=selecetdRowsCount-1;i>=0;i--){
					
				model.removeRow(selecetdRows[i]);
				Main_Frame.fileVector.removeElementAt(selecetdRows[i]);
				}
				System.out.println("delete");
			}
	});
		select.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//TODO: select all tag here
				DefaultTableModel model = (DefaultTableModel) Table_Panel.table.getModel();
				Table_Panel.table.addRowSelectionInterval(0,model.getRowCount()-1);
				System.out.println("select all");
			}
	});
		clean.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//TODO:  ckean tag here
				DefaultTableModel model = (DefaultTableModel) Table_Panel.table.getModel();
				for(int i=model.getRowCount()-1;i>=0;i--){
				model.removeRow(i);
				Main_Frame.fileVector.removeElementAt(i);
				}
				System.out.println("clean");
			}
	});
		
	}
}
