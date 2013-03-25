import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
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
		open.addActionListener(
				new ActionListener(){
						public void actionPerformed(ActionEvent e){
							int returnVal = fc.showOpenDialog(open);
							 if (returnVal == JFileChooser.APPROVE_OPTION) {
						            File currentFile = fc.getSelectedFile();
						            String filename = currentFile.getName();
						            String extension = filename.substring(filename.lastIndexOf(".") + 1, filename.length());

						            String mp3extension = "mp3";
						            if (extension != mp3extension) {
						                JOptionPane.showMessageDialog(null, "Currently We only support MP3 file");
						            }else{
							        	Main_Frame.fileVector.add(currentFile);
							        	Main_Frame.insertNew(currentFile);
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
	}
}
