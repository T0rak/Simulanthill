
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.awt.*;
import java.awt.event.*;


/**
 * 
 */
public class MapConvertor 
{

	private String fileName;
	private Frame mainFrame;
	private Label headerLabel;
    private Label statusLabel;
    private Panel controlPanel;

	public static void main(String[] args) 
	{
		//MapConvertor aMap = new MapConvertor("C:\\Users\\Luce\\Documents\\LESGITSDUCOURS\\P2JAVA\\simulanthill\\maps\\testmap1.txt");
		MapConvertor aMap = new MapConvertor();
		aMap.showFileDialogDemo();
	}

	/**
	 * Constructor
	 */
	public MapConvertor() 
	{
		prepareGUI();
	}

	/**
	 * Generates the graphical interface
	 */
	private void prepareGUI()
	{
		mainFrame = new Frame("Test FileDialog (AWT)");	//Window Title
		mainFrame.setSize(400,400);
		mainFrame.setLayout(new GridLayout(3, 1));

		mainFrame.addWindowListener(new WindowAdapter() 
		{
		   public void windowClosing(WindowEvent windowEvent)
		   {
			  System.exit(0);
		   }        
		}); 
		
		headerLabel = new Label();
		headerLabel.setAlignment(Label.CENTER);

		statusLabel = new Label();        
		statusLabel.setAlignment(Label.CENTER);
		statusLabel.setSize(350,100);

		controlPanel = new Panel();
		controlPanel.setLayout(new FlowLayout());

		mainFrame.add(headerLabel);
		mainFrame.add(controlPanel);
		mainFrame.add(statusLabel);

		mainFrame.setVisible(true);  
	 }

	 /**
	  * Opens the fileDialog
	  */
	 private void showFileDialogDemo()
	 {
		headerLabel.setText("Control in action: FileDialog"); 
  
		final FileDialog fileDialog = new FileDialog(mainFrame,"Select file");
		fileDialog.setFile("*.txt");
		Button showFileDialogButton = new Button("Open File");

		showFileDialogButton.addActionListener(new ActionListener() // annonymus function : event on click --> opens the fileDialog
		{
		   @Override
		   public void actionPerformed(ActionEvent e) 
		   {
			  fileDialog.setVisible(true);

			  if (fileDialog.getFile().length()!=0)
			  {
				validate(fileDialog.getDirectory()+fileDialog.getFile());
			  }

			  statusLabel.setText("File Selected :"  + fileDialog.getDirectory() + fileDialog.getFile()); //Outputs the file selected.
		   }
		});
  
		controlPanel.add(showFileDialogButton);
		mainFrame.setVisible(true);  
	 }

	 /**
	  * Checks whether a map is valid or not. 
	  * The map is selected through a fileDialog.
	  * @param _fileName //absolute path fileName
	  */
	private void validate(String _fileName) 
	{
		this.fileName = _fileName;

		File f = new File(fileName);
		if(f.exists() && !f.isDirectory() && fileName.substring(fileName.length()-3, fileName.length()).equalsIgnoreCase("txt")) 
		{ 
    		System.out.println("The file exists");

			//We need to check if all characters are valid : provided by https://www.candidjava.com/tutorial/program-to-read-a-file-character-by-character/ 
			try 
			{
				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);

				int c = 0;             
				while((c = br.read()) != -1)         //Read char by Char
				{
						char character = (char) c;          //converting integer to char
						System.out.print(character);        //Display the Character
						if (Character.toUpperCase(character) != 'R' 
						 && Character.toUpperCase(character) != ' '
						 && Character.toUpperCase(character) != 'O'
						 && Character.toUpperCase(character) != '#'
						 && Character.toUpperCase(character) != '\r' //Carriage return
						 && character != '\n' //New Line
						)
						{

							System.out.println("\n\nInvalid character : " + character );
							//TODO : Throw a character message error
							br.close();
							fr.close();
							return;
						}
				}

				System.out.println("Map is valid ! :) ");

				br.close();
				fr.close();

			} 
			catch (IOException e)	//Unuseful but needed...
			{
				//TODO : Throw a IO error message
				e.printStackTrace();
				System.out.println("Unable to read the file");
			}   

		}
		else
		{
			System.out.println("The file does NOT exists");
			//TODO : Throw a no file message or extension error
		}
	}

	/**
	 * Generates a valid random map 
	 */
	public void random() 
	{

	}

}
