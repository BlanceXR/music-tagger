import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


public class Menu
{
	JMenuBar menu_bar;
	
	JMenu file;
	JMenuItem open, exit;
	
	JMenu edit;
	JMenuItem tag, delete, select, clean; 
	
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
