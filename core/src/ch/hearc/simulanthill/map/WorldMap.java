package ch.hearc.simulanthill.map;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

import ch.hearc.simulanthill.actors.ElementActor;
import ch.hearc.simulanthill.actors.Anthill;
import ch.hearc.simulanthill.actors.Obstacle;
import ch.hearc.simulanthill.actors.Resource;

/**
 * Class that links a .txt file map to SimulantHill.
 * Can Handle Imported files or random generated maps.
 */
public class WorldMap 
{
	public int width;
	public int height;
	private float worldWidth;
	private float worldHeight;
	public float size;
	public String filename;

	private ElementActor[][][] elementActorGrid;
	private Character[][][] elementActorCharGrid;
	
	private static final Character[] ACCEPTED_CHAR_LIST = 
	{
		Character.toUpperCase(' '),
		Character.toUpperCase('#'),
		Character.toUpperCase('R'),
		Character.toUpperCase('\r'),
		Character.toUpperCase('O'),
		Character.toUpperCase('\n')
	};

	private static final String[] MAP_RESOURCES_STRUCTURES = 
	{
		"R\n  RR\n   R\nRRR\n  RR\nRR\nRR\nRR\nRRRRR\n  RRR\n  RRR\n  RR\n",
		"  RRR\nRRRRRR\n  RRRRRRRR\n  RRRRR\n  RRR\n  R\n",
		"RRRRR RRRRRR RRRRR           RRRRRRRR\n    RRRRRRRRRRRRRRRRRRRRRRRRRRRRRR\n                      RRRR\n",
		"        RRRRRR\n       RRRRR RR  RRRR\n  RRRRRRRRRRRRR\n    RRRRRRRRR\n    RRRRR RRRRR\n        RR\n",
		"RRRRRR\n      RRR  RRR\n        RR\n          RRRR\n            R\n          RR RRR\n                RRRRR\n",
		"           R  RRR\n              RR\n  RRR       RRRR\n          RR  R\n          RR R\n      RR R\n      RRRRRRRR\n RRRR\n",
		"    RRRRRRRRRRRR\n    RRR    RR    RRRR\nRRR  RRR          RR\nRR      RR R    RR\nRRRRR         RRRRR\n    RRRRRRRR\n",
		"RRRRRRRRRRRR\n      RRRRRRRRRRRRRRRR\n",
		" RRR\n  RR\n  RR\n  R\n  RRRR\n    R\n    RR\n    R  R R\n   R    R\n",
		"   RRRR\nRRRRR\n  RRRRRR\n    RRRR\n    RR  RRRR\n",
	};

	private static final String[] MAP_OBSTACLES_STRUCTURES = 
	{
		"#####################\n",
		"#\n#\n#\n#\n#\n#\n#\n",
		"#### ###### #####           ########\n    ##############################\n                      ####\n",
		"#\n  ##\n   #\n ###\n  ##\n##\n##\n##\n#####\n  ###\n  ###\n  ##\n",
		"  ###\n######\n  ########\n  #####\n  ###\n  #\n"
	};

	
	/**
	 * Constructor for importer files
	 * @param _filename the map filename to import
	 * @param _worldWidth the width in pixel wanted of the map
	 * @param _worldHeight the height in pixel wanted of the map
	 */
	public WorldMap(String _filename, float _worldWidth, float _worldHeight) 
	{
		width = 0;
		height = 0;

		filename = _filename;

		worldWidth = _worldWidth;
		worldHeight = _worldHeight;
		
		if (validate())
		{
			
			convert();
		} 
		else 
		{
			filename = "../../maps/mapLoad.txt";
			System.out.println("ERROR : unable to convert map : map is not valid");
			validate();
			convert();
			
		}

	}

	/**
	 * Constructor for random maps.
	 * @param _worldWidth the width in pixel wanted of the map
	 * @param _worldHeight the height in pixel wanted of the map
	 */
	public WorldMap(float _worldWidth, float _worldHeight)
	{
		width = 0;
		height = 0;
		filename = "";
		worldWidth = _worldWidth;
		worldHeight = _worldHeight;

		random(40);
	}

	/**
	 * Getter of the map width (number on cases)
	 * @return the map width
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * Getter of the map height (number on cases)
	 * @return the map height
	 */
	public int getHeight()
	{
		return height;
	}

	 /**
	  * Checks whether a map is valid or not. 
	  * The map is selected through a fileDialog.
	  * @return returns true if it's valid or false if not. 
	  */
	private boolean validate() 
	{
		File f = new File(filename);
		if(f.exists() && !f.isDirectory() && filename.substring(filename.length()-3, filename.length()).equalsIgnoreCase("txt")) 
		{ 
    		System.out.println("The file exists");
			int line = 0;
			int column = 0;

			//We need to check if all characters are valid.
			//provided by https://www.candidjava.com/tutorial/program-to-read-a-file-character-by-character/ 
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
						if(width == 0)
						{
							width = column-1;
						}
						else //column must have the same length
						{
							if (width != column -1)
							{
								System.out.println("\n\nInvalid map sizes : some lines are bigger than others");
								br.close();
								fr.close();
								return false;
							}
						}
						line++;
						column = -1;
					}

					System.out.print(character); //Display the Character

					if (!Arrays.asList(ACCEPTED_CHAR_LIST).contains(character))
					{
						System.out.println("\n\nInvalid character : " + character );
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
				height = line;
				System.out.println();
				System.out.println("width map : "+ width);
				System.out.println("height map : "+ height);

				br.close();
				fr.close();
				return true;

			} 
			catch(IOException e)
			{
				e.printStackTrace();
				System.out.println("Unable to read the file");
			}   

		}
		else
		{
			System.out.println("The file does NOT exists");
		}
		return false;
	}

	/**
	 * Will convert the map from characters to objects.
	 */
	private void convert()
	{
		int line = 0;
		int column = 0;

		File f = new File(filename);
	
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

			size = Math.min(worldWidth / width, worldHeight / height);

			elementActorGrid = new ElementActor[height][width][5];

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
							elementActorGrid[line][column][1] = new Resource(column*size, line*size, size, size);
							break;

						case '#':
							elementActorGrid[line][column][0] = new Obstacle(column*size, line*size, size, size);
							break;

						case 'O':
							elementActorGrid[line][column][2] = new Anthill(column*size, line*size, size, size);
							break;
					
						default:
							break;
					}
					column++;
				}
				line++;

				if(width == 0)
				{
					width = column-1;
				}

				column = 0;
			}
			br.close();
			fr.close();
			
			System.out.println("Map Converted !!");

		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.out.println("Unable to read the file");
		} 		
	}

	/**
	 * Generates a valid random map 
	 * @param _nbElements number of random elements to pu in the map
	 */
	public void random(int _nbElements) 
	{

		width = (int) worldWidth/10;		
		height =(int) worldHeight/10;
		size = Math.min(worldWidth/width, worldHeight/height);

		//System.out.println("SIZE : "+size);

		Random r = new Random();

		elementActorCharGrid = new Character[height][width][1];

		//The map must be rounded by obstacles.
		for(int i = 0 ; i < height ; i++ )
		{
			for(int j = 0 ; j < width ; j++ )
			{
				if(i == 0 
				|| i == height - 1
				|| j == 0
				|| j == width - 1)  //this means that it's the first line or the last one or an edge
				{
					elementActorCharGrid[i][j][0] = '#';
				}
				else
				{
					elementActorCharGrid[i][j][0] = ' ';
				}
			}

		}

		//Lets popout max spots where to add the patterns:
		for (int i = 0 ; i < _nbElements ; i++)
		{
			int xCoord = r.nextInt(width-1) + 1;
			int yCoord = r.nextInt(height-1) + 1; 

			//We have now the x and y coords where to start the pattern

			//We need to know which pattern to draw
			int index = 0;
			String resourceStructure = "";

			//lets just say that we want half resources pattern & half obstacles pattern
			if (i < _nbElements/2)
			{
				index = r.nextInt(MAP_RESOURCES_STRUCTURES.length);
				resourceStructure = MAP_RESOURCES_STRUCTURES[index];

			}
			else
			{
				index = r.nextInt(MAP_OBSTACLES_STRUCTURES.length);
				resourceStructure = MAP_OBSTACLES_STRUCTURES[index];
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
					if ( (xCoord + w ) < width - 1 && (yCoord + h ) < height - 1 )
					{
						elementActorCharGrid[yCoord + h][xCoord + w][0] = character;
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
			yCoord = r.nextInt(width-1) + 1;
			xCoord = r.nextInt(height-1) + 1; 

		} while (elementActorCharGrid[xCoord][yCoord][0] != ' ');

		elementActorCharGrid[xCoord][yCoord][0] = 'O';

		loadCharGrid();

	}
	
	/**
	 * Will create the objects found in elementActorCharGrid in the 
	 * grid used my the Ecosystem.
	 */
	public void loadCharGrid() 
	{
		elementActorGrid = new ElementActor[height][width][6];
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				//System.out.print(ELEMENT_ACTOR_3D_char_temp[i][j][0]);

				switch(elementActorCharGrid[i][j][0])
				{
					case '#' : 
						elementActorGrid[i][j][0] = new Obstacle(j*size, i*size, size, size);
						break;
					case 'O' :
						elementActorGrid[i][j][2] = new Anthill(j*size, i*size, size, size);
						break;
					case 'R':
						elementActorGrid[i][j][1] = new Resource(j*size, i*size, size, size);
						break;
				}
			}
		}
	}

	/**
	 * Resests the map. It will bring the map to the same state as at the beginning.
	 * Can be used with both random and imported maps.
	 * @return if the reset has succeeded or not.
	 */
	public boolean reset() 
	{
		elementActorGrid = null;
		if (filename.isBlank()) 
		{
			loadCharGrid();
		}
		else
		{
			if (validate())
			{
				convert();
			}
			else
			{
				filename = "../../maps/mapLoad.txt";
				System.out.println("ERROR : unable to convert map : map is not valid");
				validate();
				convert();
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns the Actor grid (getter) 
	 * @return the grid containing the actors
	 */
	public ElementActor[][][] getElementActorGrid() 
	{
		return elementActorGrid;
	}

	/**
	 * Returns the size of a case in the ElementActorGrid.
	 * The case is a square.
	 * @return the size of the case.
	 */
	public float getCaseSize()
	{
		return size;
	}
}
