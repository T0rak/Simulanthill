package ch.hearc.simulanthill.map;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
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
	private static float SIZE;

	private static List<Character> ACCEPTED_CHAR_LIST;

	/////////////////////////////////////////////////////
	private static ElementActor[][][] ELEMENT_ACTOR_3D;
	
	//for random generation
	private static List<String> mapStructuresResources;
	private static List<String> mapStructuresObstacle;
	private static Character[][][] ELEMENT_ACTOR_3D_char_temp;
	/////////////////////////////////////////////////////

	/**
	 * Resets the map used in the simulator. Has to be call each time a new map want's to be used.
	 */
	private static void reset()
	{
		ELEMENT_ACTOR_3D = null;			//to erase an array is enough (https://stackoverflow.com/questions/15448457/deleting-an-entire-array#15448477)
		ELEMENT_ACTOR_3D_char_temp = null;
		ACCEPTED_CHAR_LIST = null;
		mapStructuresResources = null;
		mapStructuresObstacle = null;
		IS_VALID = false;

	}

	private static void init()
	{
		ACCEPTED_CHAR_LIST = new ArrayList<Character>();

		ACCEPTED_CHAR_LIST.add(Character.toUpperCase(' ')); 
		ACCEPTED_CHAR_LIST.add(Character.toUpperCase('#')); 
		ACCEPTED_CHAR_LIST.add(Character.toUpperCase('R')); 
		ACCEPTED_CHAR_LIST.add(Character.toUpperCase('\r')); //Carriage return
		ACCEPTED_CHAR_LIST.add(Character.toUpperCase('O'));
		ACCEPTED_CHAR_LIST.add(Character.toUpperCase('\n')); //New line

		//we add to map structure some patterns so that they will spanw randomly:
		mapStructuresResources = new LinkedList<String>();
		mapStructuresObstacle = new LinkedList<String>();

		mapStructuresResources.add("R\n  RR\n   R\nRRR\n  RR\nRR\nRR\nRR\nRRRRR\n  RRR\n  RRR\n  RR\n");
		mapStructuresResources.add("  RRR\nRRRRRR\n  RRRRRRRR\n  RRRRR\n  RRR\n  R\n");
		mapStructuresResources.add("RRRRR RRRRRR RRRRR           RRRRRRRR\n    RRRRRRRRRRRRRRRRRRRRRRRRRRRRRR\n                      RRRR\n");
		mapStructuresResources.add("        RRRRRR\n       RRRRR RR  RRRR\n  RRRRRRRRRRRRR\n    RRRRRRRRR\n    RRRRR RRRRR\n        RR\n");
		mapStructuresResources.add("RRRRRR\n      RRR  RRR\n        RR\n          RRRR\n            R\n          RR RRR\n                RRRRR\n");
		mapStructuresResources.add("           R  RRR\n              RR\n  RRR       RRRR\n          RR  R\n          RR R\n      RR R\n      RRRRRRRR\n RRRR\n");
		mapStructuresResources.add("    RRRRRRRRRRRR\n    RRR    RR    RRRR\nRRR  RRR          RR\nRR      RR R    RR\nRRRRR         RRRRR\n    RRRRRRRR\n");
		mapStructuresResources.add("RRRRRRRRRRRR\n      RRRRRRRRRRRRRRRR\n");
		mapStructuresResources.add(" RRR\n  RR\n  RR\n  R\n  RRRR\n    R\n    RR\n    R  R R\n   R    R\n");
		mapStructuresResources.add("   RRRR\nRRRRR\n  RRRRRR\n    RRRR\n    RR  RRRR\n");

		mapStructuresObstacle.add("#####################\n");
		mapStructuresObstacle.add("#\n#\n#\n#\n#\n#\n#\n");
		mapStructuresObstacle.add("#### ###### #####           ########\n    ##############################\n                      ####\n");
		mapStructuresObstacle.add("#\n  ##\n   #\n ###\n  ##\n##\n##\n##\n#####\n  ###\n  ###\n  ##\n");
		mapStructuresObstacle.add("  ###\n######\n  ########\n  #####\n  ###\n  #\n");

	}

	/**
	 * Constructor
	 */
	public static void generate(String _filename, float _parentWidth, float _parentHeight) 
	{
		WIDTH = 0;
		HEIGHT = 0;
		IS_VALID = false;

		init();

		if(validate(_filename))
		{
			IS_VALID = true;
		}

		if(IS_VALID)
		{
			convert();
		}

	}

	public static void generate_random(float _parentWidth, float _parentHeight)
	{
		init();
		random((int)_parentWidth, (int)_parentHeight);
	}

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
				while((c = br.read()) != -1) //Read char by Char
				{
						 
					character = Character.toUpperCase((char) c); //converting integer to char

					if(character == '\n')
					{
						if(WIDTH == 0)
						{
							WIDTH = column-1;
						}
						line++;
						column = -1;
					}

					System.out.print(character); //Display the Character

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
			catch(IOException e)
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

						switch(character) 
						{
							case 'R':
								ELEMENT_ACTOR_3D[line][column][1] = new Resource(column*SIZE, line*SIZE, SIZE, SIZE);
								break;

							case '#':
								ELEMENT_ACTOR_3D[line][column][0] = new Obstacle(column*SIZE, line*SIZE, SIZE, SIZE);
								break;

							case 'O':
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

					column = 0;
				}
				Ecosystem.getCurrentEcosystem().setElementActorGrid(ELEMENT_ACTOR_3D);
				Ecosystem.getCurrentEcosystem().setCaseSize(SIZE);
				br.close();
				fr.close();

				System.out.println("Map Converted !!");

			}
			catch(IOException e)
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

		WIDTH = _width/10;		
		HEIGHT = _height/10;
		SIZE = Math.min(Ecosystem.getCurrentEcosystem().getWidth()/WIDTH, Ecosystem.getCurrentEcosystem().getHeight()/HEIGHT);

		System.out.println("SIZE : "+SIZE);

		Random r = new Random();

		ELEMENT_ACTOR_3D_char_temp = new Character[HEIGHT][WIDTH][1];

		//The map must be rounded by obstacles.
		for(int i = 0 ; i < HEIGHT ; i++ )
		{
			for(int j = 0 ; j < WIDTH ; j++ )
			{
				if(i == 0 
				|| i == HEIGHT - 1
				|| j == 0
				|| j == WIDTH - 1)  //this means that it's the first line or the last one or an edge
				{
					ELEMENT_ACTOR_3D_char_temp[i][j][0] = '#';
				}
				else
				{
					ELEMENT_ACTOR_3D_char_temp[i][j][0] = ' ';
				}
			}

		}

		int max = 20;

		//Lets popout max spots where to add the patterns:
		for (int i = 0 ; i < max ; i++)
		{
			int xCoord = r.nextInt(WIDTH-1) + 1;
			int yCoord = r.nextInt(HEIGHT-1) + 1; 

			//We have now the x and y coords where to start the pattern

			//We need to know which pattern to draw
			int index = 0;
			String resourceStructure = "";

			//lets just say that we want half resources pattern & half obstacles pattern
			if (i < max/2)
			{
				index = r.nextInt(mapStructuresResources.size());
				resourceStructure = mapStructuresResources.get(index);

			}
			else
			{
				index = r.nextInt(mapStructuresObstacle.size());
				resourceStructure = mapStructuresObstacle.get(index);
			}

			int w = 0;
			int h = 0;
			//now, we need to put each char of the pattern in the tab
			for (char character : resourceStructure.toCharArray()) 
			{
				if (character == '\n')
				{
					w = 0;
					h++;
				}
				else
				{
					if ( (xCoord + w ) < WIDTH - 1 && (yCoord + h ) < HEIGHT - 1 )
					{
						ELEMENT_ACTOR_3D_char_temp[yCoord + h][xCoord + w][0] = character;
					}
					w++;
					
				}
			}
		}

		//Last thing to do is to place the anthill in a spot where's nothing
		int xCoord = 0 ;
		int yCoord = 0 ;
		do 
		{
			yCoord = r.nextInt(WIDTH-1) + 1;
			xCoord = r.nextInt(HEIGHT-1) + 1; 

		} while (ELEMENT_ACTOR_3D_char_temp[xCoord][yCoord][0] != ' ');

		ELEMENT_ACTOR_3D_char_temp[xCoord][yCoord][0] = 'O';

		ELEMENT_ACTOR_3D = new ElementActor[HEIGHT][WIDTH][6];
		for (int i = 0; i < HEIGHT; i++)
		{
			for (int j = 0; j < WIDTH; j++)
			{
				//System.out.print(ELEMENT_ACTOR_3D_char_temp[i][j][0]);

				switch(ELEMENT_ACTOR_3D_char_temp[i][j][0])
				{
					case '#' : 
						ELEMENT_ACTOR_3D[i][j][0] = new Obstacle(j*SIZE, i*SIZE, SIZE, SIZE);
						break;
					case 'O' :
						ELEMENT_ACTOR_3D[i][j][2] = new Anthill(j*SIZE, i*SIZE, SIZE, SIZE);
						break;
					case 'R':
						ELEMENT_ACTOR_3D[i][j][1] = new Resource(j*SIZE, i*SIZE, SIZE, SIZE);
						break;
				}

			}
			//System.out.println();
		}

		Ecosystem.getCurrentEcosystem().setElementActorGrid(ELEMENT_ACTOR_3D);
		Ecosystem.getCurrentEcosystem().setCaseSize(SIZE);

	}

}
