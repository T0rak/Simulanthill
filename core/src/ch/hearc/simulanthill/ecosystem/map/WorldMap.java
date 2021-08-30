package ch.hearc.simulanthill.ecosystem.map;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import ch.hearc.simulanthill.ecosystem.actors.Anthill;
import ch.hearc.simulanthill.ecosystem.actors.ElementActor;
import ch.hearc.simulanthill.ecosystem.actors.Obstacle;
import ch.hearc.simulanthill.ecosystem.actors.Resource;

/**
 * Class that links a .txt file map to SimulantHill.
 * Can Handle Imported files or random generated maps.
 */
public class WorldMap 
{
	private int width;
	private int height;

	private float worldWidth;
	private float worldHeight;

	private float caseSize;

	public String filename;
	private ArrayList<String> mapStringLines;

	//Contains Anthills, Obstacles and Resources
	private ElementActor[][] mapTileGrid;

	private static final int errorMapWidth = 125 ;
	private static final int errorMapHeight = 58 ;
	
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
		"     RRR\n   RRRRRRR\nRRRRRRRRRRRRRRR\n RRRRRRRRRRRR\n     RRRRR\n      RR\n",
		"      RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR\n   RRRRRRRRRRRRRRRRRRRRRRRRRR\n     RRRRRRRRRRRRRRRRRRRRRRRRR"
	};

	private static final String[] MAP_OBSTACLES_STRUCTURES = 
	{
		"#####################\n",
		"#\n#\n#\n#\n#\n#\n#\n",
		"#### ###### #####           ########\n    ##############################\n                      ####\n",
		"#\n  ##\n   #\n ###\n  ##\n##\n##\n##\n#####\n  ###\n  ###\n  ##\n",
		"  ###\n######\n  ########\n  #####\n  ###\n  #\n",
		"#######\n ######\n################\n    ########\n ####\n",
		"     ###\n   #######\n###############\n ############\n     #####\n      ##\n",
		"##########################################\n   ###########                   #################\n       ###########                            ##########\n              ###########               #############################\n########################\n",
		"##\n  ###\n  ##\n  #\n  ####\n  ##\n  ###        ###############\n  #           ####\n  ###      ###\n  #       ###\n  ############\n"
		
	};

	private static final String INVALID_MAP = "#############################################################################################################################\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                      #   #   ###   ####      #####   ###   #   #  #   #   ###  #####    ####   #####                      #\n#                      ## ##  #   #  #   #     #      #   #  ##  #  ##  #  #   #   #      #   #  #                          #\n#                      # # #  #####  ####      #      #####  # # #  # # #  #   #   #      ####   ####                       #\n#                      #   #  #   #  #         #      #   #  #  ##  #  ##  #   #   #      #   #  #                          #\n#                      #   #  #   #  #         #####  #   #  #   #  #   #   ###    #      ####   #####                      #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                         #       ###   #####  ####   #####  ####                                           #\n#                                         #      #   #  #   #  #   #  #      #   #                                          #\n#                                         #      #   #  #####  #   #  ####   #   #                                          #\n#                                         #      #   #  #   #  #   #  #      #   #                                          #\n#                                         #####   ###   #   #  ####   #####  ####                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                            O                                                              #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#                                                                                                                           #\n#############################################################################################################################";

	/**
	 * Constructor for importer files
	 * @param _filename the map filename to import
	 * @param _worldWidth the width in pixel wanted of the map
	 * @param _worldHeight the height in pixel wanted of the map
	 */
	public WorldMap(String _filename, float _worldWidth, float _worldHeight) 
	{
		worldHeight = _worldHeight;
		worldWidth = _worldWidth;

		if(!loadFile(_filename) || !validateDimensions() || !validateCharacters() || !validateStructure())
		{
			System.out.println("Error with map");
			useErrorMap();
		} else {
			System.out.println("Map is Valid !");
		}

		caseSize = Math.min(worldWidth/width, worldHeight/height);
		convertMap();
	}

	/**
	 * Constructor for random maps.
	 * @param _worldWidth the width in pixel wanted of the map
	 * @param _worldHeight the height in pixel wanted of the map
	 */
	public WorldMap(float _worldWidth, float _worldHeight, int _width, int _height)
	{
		//filename = "";

		width = _width;
		height = _height;

		worldWidth = _worldWidth;
		worldHeight = _worldHeight;
		
		random(1/5f);
		
		caseSize = Math.min(worldWidth/width, worldHeight/height);
		convertMap();
	}

	/**
	 * Convert the map from characters to objects.
	 */
	private void convertMap() 
	{
		//pheromoneGrid = new Pheromone[width][height][nbPhero];
		mapTileGrid = new ElementActor[width][height];
		int iLine = height;
		for (String line : mapStringLines) 
		{
			iLine --;
			int iColumn = 0;
			for (char ch : line.toCharArray()) 
			{
				convertElementActor(Character.toUpperCase(ch), iLine, iColumn);
				iColumn ++;
			}
		}
	}

	private void convertElementActor(char ch, int line, int col)
	{
		ElementActor actor;
		switch (ch) 
		{
			case ' ':
				actor = null;
				break;
			case '#':
				actor = new Obstacle(col * caseSize, line * caseSize, caseSize, caseSize);
				mapTileGrid[col][line] = actor;
				break;
			case 'O':
				actor = new Anthill(col * caseSize, line * caseSize, caseSize, caseSize);
				mapTileGrid[col][line] = actor;
				break;
			case 'R':
				actor = new Resource(col * caseSize, line * caseSize, caseSize, caseSize);
				mapTileGrid[col][line] = actor;
				break;
		
			default:
				actor = null;
				break;
		}		
	}

	private boolean loadFile(String _filename)
	{
		File f = new File(_filename);
		if (f.exists() && !f.isDirectory() && _filename.substring(_filename.length()-3, _filename.length()).equalsIgnoreCase("txt"))
		{
			FileReader fr;
			mapStringLines = new ArrayList<String>();
			try 
			{
				fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);
				String line;
				while ((line = br.readLine())!= null) {
					mapStringLines.add(line);
				}
				
				fr.close();
			} 
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			
			width = mapStringLines.get(0).length();
			height = mapStringLines.size();
			
			return true;
		}
		return false;
		
	}

	private boolean validateDimensions() 
	{
		for (String line : mapStringLines) 
		{
			if (line.length() != width) 
			{
				return false;
			}
		}
		return true;
	}

	private boolean validateCharacters()
	{
		for (String line : mapStringLines) 
		{
			for (char ch : line.toCharArray()) 
			{
				if (!Arrays.asList(ACCEPTED_CHAR_LIST).contains(Character.toUpperCase(ch)))
				{
					return false;
				}
			}
		}
		return true;
	}

	private boolean allObstacles(String line)
	{
		for (char ch : line.toCharArray()) 
			{
				if (ch != '#')
				{
					return false;
				}
			}
		return true;

	}

	private boolean validateStructure() {
		allObstacles(mapStringLines.get(0));
		allObstacles(mapStringLines.get(height-1));
		
		for (String l : mapStringLines) 
		{
			if (l.charAt(0) != '#' || l.charAt(width-1) != '#') 
			{
				return false;
			}
		}
		return true;
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

	private void useErrorMap()
	{
	
		mapStringLines = new ArrayList<String>(Arrays.asList(INVALID_MAP.split("\n")));
		
		width = errorMapWidth;
		height = errorMapHeight;

	}
	

	

	/**
	 * Generates a valid random map 
	 * @param _nbElements number of random elements to pu in the map
	 */
	public void random(float _factorNbElements) 
	{
		int nbElement = (int)(width * height * _factorNbElements);
		char[][] mapGrid = new char[width][height];
		
		// Generate random elements in map
		Random r = new Random();
		int count = 0;
		while (count < nbElement) {

			String newStructure;
			if (r.nextInt() % 2 == 0) {
				newStructure = MAP_RESOURCES_STRUCTURES[r.nextInt(MAP_RESOURCES_STRUCTURES.length)];
			} else {
				newStructure = MAP_OBSTACLES_STRUCTURES[r.nextInt(MAP_OBSTACLES_STRUCTURES.length)];
			}
			
			int xPos = r.nextInt(width + 20) - 10;
			int yPos = r.nextInt(height + 20) - 10;
			
			int dy = 0;
			for (String line : newStructure.split("\n")) {
				int dx = 0;
				for (char ch : line.toCharArray()) {
					int x = xPos + dx;
					int y = yPos + dy;
					if (ch != ' ' && x < width && y < height && x >= 0 && y >= 0) {
						mapGrid[x][y] = ch;
						count ++;
					}
					dx++;
				}
				dy++;
			}
		}
		int xPos;
		int yPos;
		do {	
			xPos = r.nextInt(width - 2) + 1;
			yPos = r.nextInt(height - 2) + 1;
		} while(mapGrid[xPos][yPos] == '#' || mapGrid[xPos][yPos] == 'R');

		mapGrid[xPos][yPos] = 'O';

		//The map must be rounded by obstacles.
		for (int i = 0; i < width ; i++)
		{
			mapGrid[i][0]= '#';
			mapGrid[i][height-1] = '#';
		}
		
		for (int i = 0; i < height ; i++)
		{
			mapGrid[0][i]= '#';
			mapGrid[width - 1][i] = '#';
		}
		
		// put mapGrid into mapStringLines
		mapStringLines = new ArrayList<String>();

		for (int y = 0; y < height; y++) 
		{
			StringBuilder strB = new StringBuilder();

			for (int x = 0; x < width; x++) 
			{
				strB.append(mapGrid[x][y]);
			}
			mapStringLines.add(strB.toString());
		}

	}
	
	/**
	 * Resests the map. It will bring the map to the same state as at the beginning.
	 * Can be used with both random and imported maps.
	 * @return if the reset has succeeded or not.
	 */
	public boolean reset() 
	{
		//pheromoneGrid = null;
		mapTileGrid = null;
		convertMap();
		return true;
	}

	/**
	 * Returns the Actor grid (getter) 
	 * @return the grid containing the actors
	 */
	/*public Pheromone[][][] getpheromoneGrid() 
	{
		return pheromoneGrid;
	}*/

	public ElementActor[][] getmapTileGrid() 
	{
		return mapTileGrid;
	}

	/**
	 * Returns the size of a case in the ElementActorGrid.
	 * The case is a square.
	 * @return the size of the case.
	 */
	public float getCaseSize()
	{
		return caseSize;
	}

}
