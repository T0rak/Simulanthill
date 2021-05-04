package ch.hearc.simulanthill.map;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import ch.hearc.simulanthill.actors.ElementActor;
import ch.hearc.simulanthill.actors.Anthill;
import ch.hearc.simulanthill.actors.Obstacle;
import ch.hearc.simulanthill.actors.Resource;

/**
 * 
 */
public class MapConvertor 
{
	private String fileName;
	private boolean isValid;
	private int width;
	private int height;

	private List<Character> acceptedCharList;
	private List<List<Character>> mapCharList;
	private List<Character> lineCharList;

	/////////////////////////////////////////////////////
	private List<ElementActor>  actorList;
	private ElementActor[][][] elementActor3D;
	/////////////////////////////////////////////////////

	/**
	 * Constructor
	 */
	public MapConvertor(String _filename, float _parentWidth, float _parentHeight) 
	{
		this.width = 0;
		this.height = 0;
		this.isValid = false;

		acceptedCharList = new ArrayList<Character>();
		mapCharList = new ArrayList<List<Character>>();
		actorList = new ArrayList<ElementActor>();

		acceptedCharList.add(Character.toUpperCase(' ')); 
		acceptedCharList.add(Character.toUpperCase('#')); 
		acceptedCharList.add(Character.toUpperCase('R')); 
		acceptedCharList.add(Character.toUpperCase('\r')); //Carriage return
		acceptedCharList.add(Character.toUpperCase('O'));
		acceptedCharList.add(Character.toUpperCase('\n')); //New line

		if (validate(_filename))
		{
			//convert();
			this.isValid = true;
		}

	}
	/*
	public MapConvertor(String _filename, int _parentWidth, int _parentHeight)
	{
		this(_filename); //makes a default conversion with 30 of width & weight.
	}
	*/

	public int getWidth()
	{
		return this.width;
	}

	public int getHeight()
	{
		return this.height;
	}

	public List<ElementActor> getActorList()
	{
		return this.actorList;
	}

	 /**
	  * Checks whether a map is valid or not. 
	  * The map is selected through a fileDialog.
	  * @param _fileName //absolute path fileName
	  */
	private boolean validate(String _fileName) 
	{
		this.fileName = _fileName;

		File f = new File(fileName);
		if(f.exists() && !f.isDirectory() && fileName.substring(fileName.length()-3, fileName.length()).equalsIgnoreCase("txt")) 
		{ 
    		System.out.println("The file exists");
			int line = 0;
			int column = 0;

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

					if(character == '\n')
					{
						if(this.width == 0)
						{
							this.width = column-1;
						}
						line++;
						column = -1;
					}

					System.out.print(character);        //Display the Character

					if (!acceptedCharList.contains(character))
					{
						System.out.println("\n\nInvalid character : " + character );
						//TODO : Throw a character message error
						br.close();
						fr.close();
						return false;
					}
					else
					{
						column++;
						//TODO : add the chars in a List then loop on it to put it in the main List
					}
				}

				System.out.println("Map is valid ! :) ");
				this.height = line;
				System.out.println();
				System.out.println("width map : "+ this.width);
				System.out.println("height map : "+ this.height);

				br.close();
				fr.close();
				return true;

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
		return false;
	}

	public void convert(float _size)
	{
		if (this.isValid)
		{
			int line = 0;
			int column = 0;

			File f = new File(fileName);
		
			System.out.println("Converting...");

			try 
			{
				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);

				//We put the lines in a stack, so we can pop then un in reverse order
				Stack<String> lines = new Stack<String>();
				String currentline = br.readLine();
				while(currentline != null) 
				{
					lines.push(currentline);
					currentline = br.readLine();
				}

				/* 0 : Obstacle
				   1 : Ressource
				   2 : Anthill		
				   --> size = 3
				*/
				this.elementActor3D = new ElementActor[this.height][this.width][3];

				Character character = ' ';

				while (! lines.empty())
				{
					currentline = lines.pop();

					for (int i = 0 ; i < currentline.length() ; i++ )        
					{
						character = Character.toUpperCase(currentline.charAt(i)); 

						//System.out.print(character); //Display the Character

						/*
						if(character == '\n')
						{
							line++;
							column = -1;
						}
						*/

						switch (character) 
						{
							case 'R':
								//actorList.add(new Resource(column*_size, line*_size, _size, _size, 10));
								this.elementActor3D[line][column][1] = new Resource(column*_size, line*_size, _size, _size, 10);
								break;

							case '#':
								//actorList.add(new Obstacle(column*_size, line*_size, _size, _size));
								this.elementActor3D[line][column][0] = new Obstacle(column*_size, line*_size, _size, _size);
								break;

							case 'O':
								//actorList.add(new Anthill(column*_size, line*_size, _size, _size));
								this.elementActor3D[line][column][2] = new Anthill(column*_size, line*_size, _size, _size);
								break;
						
							default:
								break;
						}
						column++;
					}
					line++;

					if(this.width == 0)
					{
						this.width = column-1;
					}
					//System.out.println(); // just for the console output 

					column = 0;
				}

				br.close();
				fr.close();

				System.out.println("Map Converted !!");

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
			System.out.println("ERROR : unable to convert map : map is not valid");
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
