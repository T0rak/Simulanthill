package ch.hearc.simulanthill.ecosystem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

import ch.hearc.simulanthill.ecosystem.actors.Ant;
import ch.hearc.simulanthill.ecosystem.actors.Anthill;
import ch.hearc.simulanthill.ecosystem.actors.ElementActor;
import ch.hearc.simulanthill.ecosystem.actors.ElementActorType;
import ch.hearc.simulanthill.ecosystem.actors.Obstacle;
import ch.hearc.simulanthill.ecosystem.actors.Pheromone;
import ch.hearc.simulanthill.ecosystem.actors.PheromoneType;
import ch.hearc.simulanthill.ecosystem.actors.Resource;
import ch.hearc.simulanthill.ecosystem.map.WorldMap;
import ch.hearc.simulanthill.screen.gui.MapListener;
import ch.hearc.simulanthill.tools.Asset;
import ch.hearc.simulanthill.tools.ColorManagement;

/**
 * This class contains all element (actor) of simulation (implements Stage from Libgdx)
 */
public class Ecosystem extends Stage
{
    private static Ecosystem instance = null;

    private WorldMap worldMap;

    //private List<Ant> ants;
    
    private List<Anthill> anthills;

    private Group mapTiles;
    private Group ants;
    private Group pheromones;
    
    private int nbAntMax;

    private boolean isPlaying;

    private List<MapListener> MapListeners;

    private Map<Integer, Pheromone[][][]> pheromoneGridMap;

    

    
    //reset
    //pheromoneGrid = null;

    /**
     * Constructor
     * @param _viewport location where the stage can be drawed
     */
    private Ecosystem(Viewport _viewport)
    {
        super(_viewport);

        mapTiles = new Group();
        addActor(mapTiles);

        ants = new Group();
        addActor(ants);

        pheromones = new Group();
        addActor(pheromones);

        //ants = new ArrayList<Ant>();
        anthills = new ArrayList<Anthill>();
        isPlaying = false;
        nbAntMax = 500;
        MapListeners = new LinkedList<MapListener>();
        pheromoneGridMap =  new TreeMap<Integer, Pheromone[][][]>();
    }

    /**
     * Create a new instance of Ecosystem if not exist
     * @param _viewport location where the stage can be drawed
     * @return ecosystem instance
     */
    public static Ecosystem getInstance(Viewport _viewport)
    {
        if(instance == null)
        {
            instance = new Ecosystem(_viewport);
        }
        return instance;
        
    }

    /**
     * Get Eccosystem instance
     * @return ecosystem instance (null if not exists)
     */
    public static Ecosystem getCurrentEcosystem()
    {
        return instance;
    }

    /**
     * add a ant in ecosystem
     * @param _ant ant
     */
    public void addAnt(Ant _ant)
    {
        ants.addActor(_ant);
    }

    /**
     * Change the ecosystem from play to pause or pause to play
     */
    public void swicthIsPlaying()
    {
        isPlaying = !isPlaying;
    }

    /**
     * Get if ecosystem is playing
     * @return true if is playing
     */
    public boolean getIsPlaying()
    {
        return isPlaying;
    }

    /**
     * reset Ecosystem at the initial state
     */
    public void reset()
    { 
        removeAllActor();

        if (worldMap.reset())
        {
            addElementActorGridToStage();
        }
    }

    /**
     * Generate a random map
     */
    public void loadMap(int _width, int _height)
    {
        removeAllActor();
        worldMap = new WorldMap(getViewport().getWorldWidth(), getViewport().getWorldHeight(), _width, _height);
        addElementActorGridToStage();
    }

    /**
     * Load a map from File .txt
     * @param _filepath file path
     */
    public void loadMap(String _filepath)
    {
        removeAllActor();
        worldMap = new WorldMap(_filepath, getViewport().getWorldWidth(), getViewport().getWorldHeight());
        addElementActorGridToStage();
    }

    /**
     * draw stage Ecosystem
     */
    @Override
    public void draw() 
    {
        getBatch().begin();
        if (worldMap == null)
        {
            getBatch().draw(Asset.backgound(), 0, 0, getViewport().getWorldWidth(), getViewport().getWorldHeight());            
        }
        else
        {
            getBatch().draw(Asset.backgound(), 0, 0, worldMap.getWidth() * worldMap.getCaseSize(), worldMap.getHeight() * worldMap.getCaseSize());
        }
        getBatch().end();
        super.draw();
    }

    /**
     * add all actor from worldMap grid to stage
     */
    private void addElementActorGridToStage()
    {
        ColorManagement.reset();
        for (ElementActor[] elementA: worldMap.getmapTileGrid()) 
        {
    
            for (ElementActor elementB: elementA)
            {
                if (elementB != null)
                {
                    mapTiles.addActor(elementB);
                }
            }
		}
        refreshAnthills();
    }


    private void refreshAnthills()
    {
        pheromoneGridMap.clear();
        anthills.clear();
        for (ElementActor[] elementA: worldMap.getmapTileGrid()) 
        {
            for (ElementActor elementB: elementA) 
            {
                if (elementB != null && elementB.getClass() == Anthill.class)
                {
                    anthills.add((Anthill)elementB);
                    pheromoneGridMap.put(((Anthill)elementB).getId(), new Pheromone[worldMap.getWidth()][worldMap.getHeight()][2]);
                }
            }
		}
        informChangeMap();
    }

    public List<Anthill> getAnthills()
    {
        return anthills;
    }

    /**
     * Remove all actor from stage
     */
    private void removeAllActor()
    {
        mapTiles.clear();
        anthills.clear();
        pheromones.clear();
        ants.clear();

        pheromoneGridMap.clear();
        //this.clear();
    }


    /**
     * Checks the presence of an element at a given position
     * @param _x position x of point (scene coordinates)
     * @param _y position y of point (scene coordinates)
     * @param _type type of element to check
     * @return actor in the case, null if has not an element of this type
     */
    public ElementActor getElementFrom(float _x, float _y)
    {
        int xCase = mouseToGrid(_x);
        int yCase = mouseToGrid(_y);
        return getElement(xCase, yCase);
    }

    /**
     * Checks the presence of an element at a given position
     * @param _x position x of point (case coordinate)
     * @param _y position y of point (case coordinate)
     * @param _type type of element to check
     * @return actor in the case, null if has not an element of this type
     */
    public ElementActor getElement(int _x, int _y)
    {
        return worldMap.getmapTileGrid()[_x][_y];
    }

    public boolean isObstacle(ElementActor actor)
    {
        return actor != null && actor.getClass() == Obstacle.class;
    }

    public boolean isResource(ElementActor actor) 
    {
        return actor != null && actor.getClass() == Resource.class;
    }

    public boolean isAnthill(ElementActor actor) 
    {
        return actor != null && actor.getClass() == Anthill.class;
    }

    
    public boolean isInstanceOf(ElementActor actor, Class<?> _class) 
    {
        return actor != null && actor.getClass() == _class;
    }

    /**
     * Checks the presence of an element at a given position
     * @param _x position x of point (scene coordinates)
     * @param _y position y of point (scene coordinates)
     * @param _type type of element to check
     * @return actor in the case, null if has not an element of this type
     */
    public Pheromone isPheromoneFrom(float _x, float _y, Anthill _anthill, PheromoneType _type)
    {
        int xCase = mouseToGrid(_x);
        int yCase = mouseToGrid(_y);
        return isPheromone(xCase, yCase, _anthill, _type);
    }

    /**
     * Checks the presence of an element at a given position
     * @param _x position x of point (case coordinate)
     * @param _y position y of point (case coordinate)
     * @param _type type of element to check
     * @return actor in the case, null if has not an element of this type
     */
    public Pheromone isPheromone(int _x, int _y, Anthill anthill, PheromoneType _type)
    {
        return pheromoneGridMap.get(anthill.getId())[_x][_y][_type.getValue()];
    }

    
    
    

    /**
     * Take a resource
     * @param _x start y position of point (scene coordinates)
     * @param _y start x position of point (scene coordinates)
     * @param _quantity quantity of resource
     * @return quantity that was available
     */
    public int takeResource(float _x, float _y, int _quantity) 
    {
        int xCase = mouseToGrid(_x);
        int yCase = mouseToGrid(_y);
        ElementActor actor = getElement(xCase, yCase);
        
        if (actor != null && actor.getClass() == Resource.class)
        {
            return ((Resource)actor).decrease(_quantity);

        }
        return 0;
        
    }

    /**
     * Take a resource
     * @param _x start y position of point (scene coordinates)
     * @param _y start x position of point (scene coordinates)
     */
    public void removeResource(float _x, float _y) 
    {
        int xCase = mouseToGrid(_x);
        int yCase = mouseToGrid(_y);
        if (worldMap.getmapTileGrid()[xCase][yCase].getClass() == Resource.class)
        {
            worldMap.getmapTileGrid()[xCase][yCase] = null;
        }

    }

    /**
     * Add a pheromone at the given position 
     * @param _x start y position of point (scene coordinates)
     * @param _y start x position of point (scene coordinates)
     * @param _type type of pheromone to add
     * @param _ant that added the pheromone
     * @param _stepFrom distance from the last goal of the ant
     */
    public void addPheromone(Pheromone _pheromone) 
    {
        
        int i = _pheromone.getType().getValue();
        int caseX = mouseToGrid(_pheromone.getX());
        int caseY = mouseToGrid(_pheromone.getY());
        Pheromone currentPheromoneCase = pheromoneGridMap.get(_pheromone.getAnthill().getId())[caseX][caseY][i];
        if (currentPheromoneCase != null) 
        {
            if (currentPheromoneCase.getStepFrom() > _pheromone.getStepFrom())
            {
                currentPheromoneCase.remove();
                pheromoneGridMap.get(_pheromone.getAnthill().getId())[caseX][caseY][i] = _pheromone;
                pheromones.addActor(_pheromone);
            }
            else
            {
                currentPheromoneCase.reinforce();
            }
            
        }
        else
        {
            pheromoneGridMap.get(_pheromone.getAnthill().getId())[caseX][caseY][i] = _pheromone;
            pheromones.addActor(_pheromone);
        }
    }

    public void addPheromone(float _x, float _y, PheromoneType _type , Anthill _anthill, int _stepFrom) 
    {
        Pheromone pheromone = new Pheromone(_x, _y, _type, _anthill, _stepFrom);
        int i = _type.getValue();
        int caseX = mouseToGrid(_x);
        int caseY = mouseToGrid(_y);
        Pheromone currentPheromoneCase = pheromoneGridMap.get(pheromone.getAnthill().getId())[caseX][caseY][i];
        if (currentPheromoneCase != null) 
        {
            if (currentPheromoneCase.getStepFrom() > pheromone.getStepFrom())
            {
                currentPheromoneCase.remove();
                pheromoneGridMap.get(pheromone.getAnthill().getId())[caseX][caseY][i] = pheromone;
                pheromones.addActor(pheromone);
            }
            else
            {
                currentPheromoneCase.reinforce();
            }
            
        }
        else
        {
            pheromoneGridMap.get(pheromone.getAnthill().getId())[caseX][caseY][i] = pheromone;
            pheromones.addActor(pheromone);
        }
    }
    /**
     * Remove a pheromone at the given position 
     * @param _x start y position of point (scene coordinates)
     * @param _y start x position of point (scene coordinates)
     * @param _type type of pheromone to remove
     */
    public void removePheromone(Pheromone _pheromone) 
    {   
        Anthill anthill = _pheromone.getAnthill();
        int xCase = mouseToGrid(_pheromone.getX());
        int yCase = mouseToGrid(_pheromone.getY());
        int typeIndex = _pheromone.getType().getValue();
        //if(pheromoneGridMap.containsKey(anthill.getId()))
        {
            pheromoneGridMap.get(anthill.getId())[xCase][yCase][typeIndex] = null;
        }
    }

    /**
     * Cast in case from stage coordinate
     * @param _f stage coordinate
     * @return case
     */
    public int castInCase(float _f)
    {
        return MathUtils.round(_f / worldMap.getCaseSize());
    }

    public int mouseToGrid(float _f) {
        return MathUtils.floor(_f / worldMap.getCaseSize());
    }

    public void setNbAntMax(int _nbAntMax)
    {
        nbAntMax = _nbAntMax;
    }

    public int getNbAntMax()
    {
        return nbAntMax;
    }

    public int getNbAnt()
    {
        return ants.getChildren().size;
    }

    /**
	 * Deletes an ant from the ecosystem
     * @param _ant the ant to remove
	 */
    public void removeAnt(Ant _ant)
    {
        //ants.remove(_ant);
        _ant.remove();
    }

    public float getMapCaseSize() {
        return worldMap.getCaseSize();
    }


    public void addMapListener(MapListener mapListener)
    {
        MapListeners.add(mapListener);
    }

    private void informChangeMap()
    {
        for (MapListener mapListener : MapListeners) {
            mapListener.change();
        }
    }

    public void addMapTiles(int _x, int _y, ElementActorType _type)
    {
        if (isOnBorder(_x, _y))
        {
            return;
        }
    
        ElementActor actor;
        switch (_type) {
            case ANTHILL:
                actor = addAnthill(_x, _y);
                break;
            case OBSTACLE:
                actor = new Obstacle(_x * getMapCaseSize(), _y * getMapCaseSize(), getMapCaseSize(), getMapCaseSize());
                break;
            case RESOURCE:
                actor = new Resource(_x * getMapCaseSize(), _y * getMapCaseSize(), getMapCaseSize(), getMapCaseSize());
                break;
        
            default:
                return;
        }
        
        if(worldMap.getmapTileGrid()[_x][_y] != null)
        {
            if (isAnthill(worldMap.getmapTileGrid()[_x][_y]))
            {
                anthills.remove((Anthill)worldMap.getmapTileGrid()[_x][_y]);
                informChangeMap();
            }
            worldMap.getmapTileGrid()[_x][_y].remove();
        }
        worldMap.getmapTileGrid()[_x][_y] = actor;
        mapTiles.addActor(actor);
        
        
    }

    public boolean isOnBorder(int _x, int _y)
    {
        return _x == 0 || _x == worldMap.getWidth()-1 || _y == 0 || _y == worldMap.getHeight()-1;
    }

    public Anthill addAnthill(int _x, int _y)
    {
        Anthill anthill = new Anthill(_x * getMapCaseSize(), _y * getMapCaseSize(), getMapCaseSize(), getMapCaseSize());
        anthills.add(anthill);
        pheromoneGridMap.put(anthill.getId(), new Pheromone[worldMap.getWidth()][worldMap.getHeight()][2]);
        informChangeMap();
        return anthill;

    }

    public float getWorldMapWidth() {
        return worldMap.getWorldWidth();
    }

    public float getWorldMapHeight() {
        return worldMap.getWorldHeight();
    }

}

