import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JFileChooser;
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
		file.add(exit);
		open.addActionListener(
				new ActionListener(){
						public void actionPerformed(ActionEvent e){
							int returnVal = fc.showOpenDialog(open);
							 if (returnVal == JFileChooser.APPROVE_OPTION) {
						            File currentOpenFile = fc.getSelectedFile();
						        	Main_Frame.fileVector.add(currentOpenFile);
						        	Main_Frame.insertNew(currentOpenFile);
						        } else {
						            //did not open successfully
						        }
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
