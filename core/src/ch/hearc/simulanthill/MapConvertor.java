package ch.hearc.simulanthill;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

	private List<Character> acceptedCharList;

	private List<List<Character>> mapCharList;
	List<Character> lineCharList;

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
		acceptedCharList = new ArrayList<Character>();
		mapCharList = new ArrayList<List<Character>>();

		acceptedCharList.add(Character.toUpperCase(' ')); 
		acceptedCharList.add(Character.toUpperCase('#')); 
		acceptedCharList.add(Character.toUpperCase('R')); 
		acceptedCharList.add(Character.toUpperCase('\r')); //Carriage return
		acceptedCharList.add(Character.toUpperCase('O'));
		acceptedCharList.add(Character.toUpperCase('\n')); //New line

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

		Button generateButton = new Button("Generate a random map");

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

		generateButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				random(117, 53);	//Will just launch the random method
				//Defined a fixed width and height.
 
			   statusLabel.setText("Map generated"); //Outputs an informational message
			}
		});
  
		controlPanel.add(showFileDialogButton);
		controlPanel.add(generateButton);
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

				Character character = ' ';

				int c = 0;             
				while((c = br.read()) != -1)         //Read char by Char
				{
						 
					character = Character.toUpperCase((char) c);           //converting integer to char
					System.out.print(character);        //Display the Character

					if (!acceptedCharList.contains(character))
					{
						System.out.println("\n\nInvalid character : " + character );
						//TODO : Throw a character message error
						br.close();
						fr.close();
						return;
					}
					else
					{
						//TODO : add the chars in a List then loop on it to put it in the main List
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
	private void random(int _width, int _height) 
	{
		
		int width = _width;		
		int height = _height;

		Random r = new Random();

		String acceptedChars = "";

		boolean hasAntHill = false;

		char pickedChar;

		for (int index = 0 ; index < acceptedCharList.size() ; index ++)
		{
			acceptedChars += acceptedCharList.get(index);
		}

		//We want a higher probability of having spaces:
		for (int index = 0 ; index < 50 ; index ++)
		{
			acceptedChars += ' ';
		}

		mapCharList.clear();

		for(int i = 0 ; i < height ; i++ )
		{
			lineCharList = new ArrayList<Character>();
	
			for(int j = 0 ; j < width ; j++ )
			{
				if(i == 0 
				|| i == height - 1
				|| j == 0
				|| j == width - 1)  //this means that it's the first line or the last one or an edge
				{
					lineCharList.add('#');
				}
				else
				{
					do
					{
					pickedChar = acceptedChars.charAt(r.nextInt(acceptedChars.length()));

					if (hasAntHill && pickedChar == 'O')
					{
						pickedChar = '\n';
					}

					} while (pickedChar == '\n' || pickedChar == '\r' );

					if (pickedChar == 'O')
					{
						hasAntHill = true;
					}

					lineCharList.add(pickedChar);	
				}
				
			}

			lineCharList.add('\n');

			mapCharList.add(lineCharList);
		}

		for (List<Character> lines : mapCharList)
		{
			for (int i = 0 ; i < lines.size() ; i++)
			{
				System.out.print(lines.get(i));
			}
		}
		

	}

}
