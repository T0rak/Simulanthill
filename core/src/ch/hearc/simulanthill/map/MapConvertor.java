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
import ch.hearc.simulanthill.Ecosystem;
import ch.hearc.simulanthill.actors.Anthill;
import ch.hearc.simulanthill.actors.Obstacle;
import ch.hearc.simulanthill.actors.Resource;

/**
 * 
 */
public class MapConvertor 
{
	private static String FILE_NAME;
	private static boolean IS_VALID;
	private static int WIDTH;
	private static int HEIGHT;

	private static int PARENT_WIDTH;
	private static int PARENT_HEIGHT;

	private static float SIZE;

	private static List<Character> ACCEPTED_CHAR_LIST;
	private static List<List<Character>> MAP_CHAR_LIST;
	private static List<Character> LINE_CHAR_LIST;

	/////////////////////////////////////////////////////
	private static ElementActor[][][] ELEMENT_ACTOR_3D;
	/////////////////////////////////////////////////////

	/**
	 * Constructor
	 */
	public static void generate(String _filename, float _parentWidth, float _parentHeight) 
	{
		WIDTH = 0;
		HEIGHT = 0;
		IS_VALID = false;

		//SIZE = _parentWidth / Ecosystem.getInstance().getWidth(); //DEFAUT VALUE

		ACCEPTED_CHAR_LIST = new ArrayList<Character>();
		MAP_CHAR_LIST = new ArrayList<List<Character>>();

		ACCEPTED_CHAR_LIST.add(Character.toUpperCase(' ')); 
		ACCEPTED_CHAR_LIST.add(Character.toUpperCase('#')); 
		ACCEPTED_CHAR_LIST.add(Character.toUpperCase('R')); 
		ACCEPTED_CHAR_LIST.add(Character.toUpperCase('\r')); //Carriage return
		ACCEPTED_CHAR_LIST.add(Character.toUpperCase('O'));
		ACCEPTED_CHAR_LIST.add(Character.toUpperCase('\n')); //New line

		if(validate(_filename))
		{
			//convert();
			IS_VALID = true;
		}

		if(IS_VALID)
		{
			convert();
		}

	}
	/*
	public MapConvertor(String _filename, int _parentWidth, int _parentHeight)
	{
		this(_filename); //makes a default conversion with 30 of width & weight.
	}
	*/

	public static int getWidth()
	{
		return WIDTH;
	}

	public static int getHeight()
	{
		return HEIGHT;
	}

	 /**
	  * Checks whether a map is valid or not. 
	  * The map is selected through a fileDialog.
	  * @param _fileName //absolute path fileName
	  */
	private static boolean validate(String _fileName) 
	{
		FILE_NAME = _fileName;

		File f = new File(FILE_NAME);
		if(f.exists() && !f.isDirectory() && FILE_NAME.substring(FILE_NAME.length()-3, FILE_NAME.length()).equalsIgnoreCase("txt")) 
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
						if(WIDTH == 0)
						{
							WIDTH = column-1;
						}
						line++;
						column = -1;
					}

					System.out.print(character);        //Display the Character

					if (!ACCEPTED_CHAR_LIST.contains(character))
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
					}
				}

				System.out.println("Map is valid ! :) ");
				HEIGHT = line;
				System.out.println();
				System.out.println("width map : "+ WIDTH);
				System.out.println("height map : "+ HEIGHT);

				br.close();
				fr.close();
				return true;

			} 
			catch(IOException e)	//Unuseful but needed...
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

	/**
	 * Will convert the map from characters to objects.
	 */
	public static void convert()
	{
		if(IS_VALID)
		{
			int line = 0;
			int column = 0;

			File f = new File(FILE_NAME);
		
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

				/*  0 : Obstacle
					1 : Ressource
					2 : Anthill	
					3 : HomePheromone	
					4 : FoodPheromone
					5 : DangerPheromone
				   --> size = 6
				*/

				SIZE = Math.min(Ecosystem.getCurrentEcosystem().getWidth()/WIDTH, Ecosystem.getCurrentEcosystem().getHeight()/HEIGHT);

				ELEMENT_ACTOR_3D = new ElementActor[HEIGHT][WIDTH][6];

				Character character = ' ';

				while(!lines.empty())
				{
					currentline = lines.pop();

					for(int i = 0 ; i < currentline.length() ; i++ )        
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

						switch(character) 
						{
							case 'R':
								//actorList.add(new Resource(column*_size, line*_size, _size, _size, 10));
								ELEMENT_ACTOR_3D[line][column][1] = new Resource(column*SIZE, line*SIZE, SIZE, SIZE, 10);
								break;

							case '#':
								//actorList.add(new Obstacle(column*_size, line*_size, _size, _size));
								ELEMENT_ACTOR_3D[line][column][0] = new Obstacle(column*SIZE, line*SIZE, SIZE, SIZE);
								break;

							case 'O':
								//actorList.add(new Anthill(column*_size, line*_size, _size, _size));
								ELEMENT_ACTOR_3D[line][column][2] = new Anthill(column*SIZE, line*SIZE, SIZE, SIZE);
								break;
						
							default:
								break;
						}
						column++;
					}
					line++;

					if(WIDTH == 0)
					{
						WIDTH = column-1;
					}
					//System.out.println(); // just for the console output 

					column = 0;
				}
				Ecosystem.getCurrentEcosystem().setElementActorGrid(ELEMENT_ACTOR_3D);
				Ecosystem.getCurrentEcosystem().setCaseSize(SIZE);
				br.close();
				fr.close();

				System.out.println("Map Converted !!");

			}
			catch(IOException e)	//Unuseful but needed...
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
	public static void random(int _width, int _height) 
	{
		
		int width = _width;		
		int height = _height;

		Random r = new Random();

		String acceptedChars = "";

		boolean hasAntHill = false;

		char pickedChar;

		for(int index = 0 ; index < ACCEPTED_CHAR_LIST.size() ; index ++)
		{
			acceptedChars += ACCEPTED_CHAR_LIST.get(index);
		}

		//We want a higher probability of having spaces:
		for(int index = 0 ; index < 50 ; index ++)
		{
			acceptedChars += ' ';
		}

		MAP_CHAR_LIST.clear();

		for(int i = 0 ; i < height ; i++ )
		{
			LINE_CHAR_LIST = new ArrayList<Character>();
	
			for(int j = 0 ; j < width ; j++ )
			{
				if(i == 0 
				|| i == height - 1
				|| j == 0
				|| j == width - 1)  //this means that it's the first line or the last one or an edge
				{
					LINE_CHAR_LIST.add('#');
				}
				else
				{
					do
					{
					pickedChar = acceptedChars.charAt(r.nextInt(acceptedChars.length()));

					if(hasAntHill && pickedChar == 'O')
					{
						pickedChar = '\n';
					}

					} while(pickedChar == '\n' || pickedChar == '\r' );

					if(pickedChar == 'O')
					{
						hasAntHill = true;
					}

					LINE_CHAR_LIST.add(pickedChar);	
				}
				
			}

			LINE_CHAR_LIST.add('\n');

			MAP_CHAR_LIST.add(LINE_CHAR_LIST);
		}

		for(List<Character> lines : MAP_CHAR_LIST)
		{
			for(int i = 0 ; i < lines.size() ; i++)
			{
				System.out.print(lines.get(i));
			}
		}
		

	}

}
