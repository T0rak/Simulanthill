package ch.hearc.simulanthill;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.MathUtils;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

import ch.hearc.simulanthill.actors.Ant;
import ch.hearc.simulanthill.actors.Asset;
import ch.hearc.simulanthill.actors.ElementActor;
import ch.hearc.simulanthill.actors.ElementActorType;
import ch.hearc.simulanthill.actors.Pheromone;
import ch.hearc.simulanthill.actors.PheromoneType;
import ch.hearc.simulanthill.actors.Resource;
import ch.hearc.simulanthill.map.WorldMap;

/**
 * This class contains all element (actor) of simulation (implements Stage from Libgdx)
 */
public class Ecosystem extends Stage
{
    private static Ecosystem instance = null;

    private WorldMap worldMap;

    private List<Ant> ants;
    private int nbAntMax;

    private boolean isPlaying;

    /**
     * Constructor
     * @param _viewport location where the stage can be drawed
     */
    private Ecosystem(Viewport _viewport)
    {
        super(_viewport);
        ants = new ArrayList<>();
        isPlaying = false;
        nbAntMax = 500;
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
        ants.add(_ant);
        addActor(_ant);
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

        if(worldMap.reset())
        {
            addElementActorGridToStage();
        }
    }

    /**
     * Generate a random map
     */
    public void loadMap()
    {
        removeAllActor();
        worldMap = new WorldMap(getViewport().getWorldWidth(), getViewport().getWorldHeight());
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
        for (ElementActor[][] elementA: worldMap.getElementActorGrid()) 
        {
            for (ElementActor[] elementB: elementA) 
            {
                for (ElementActor elementC: elementB)
                {
                    //element.setPos(element.getPosX()*size, element.getPosY()*size);
                    //element.setPos(element.getPosX(), element.getPosY());
                    //Gdx.app.log("X TEST", String.valueOf(element.getPosX()));
                    //Gdx.app.log("Y TEST", String.valueOf(element.getPosX()));
                    //element.setSize(size, size);
                    if (elementC != null)
                    {
                        addActor(elementC);
                    }
                }
            }
		}
    }

    /**
     * Remove all acotr from stage
     */
    private void removeAllActor()
    {
        ants.clear();
        this.clear();
    }

    /**
     * Checks the presence of an element at a given position
     * @param _x position x of point (scene coordinates)
     * @param _y position y of point (scene coordinates)
     * @param _type type of element to check
     * @return actor in the case, null if has not an element of this type
     */
    public ElementActor isElement(float _x, float _y, ElementActorType _type)
    {
        int xCase = castInCase(_x);
        int yCase = castInCase(_y);
        return worldMap.getElementActorGrid()[yCase][xCase][_type.getValue()];
    }

    /**
     * Checks the presence of an element at a given position
     * @param _x position x of point (case coordinate)
     * @param _y position y of point (case coordinate)
     * @param _type type of element to check
     * @return actor in the case, null if has not an element of this type
     */
    private ElementActor isElement(int _x, int _y, ElementActorType _type)
    {
        return worldMap.getElementActorGrid()[_y][_x][_type.getValue()];
    }

    /**
     * Checks if the given position is in the given actor
     * @param _x position x of point (scene coordinates)
     * @param _y position y of point (scene coordinates)
     * @param _actor actor
     * @return true if are on the same case
     */
    public Boolean isOnElement(float _x, float _y, ElementActor _actor)
    {
        return (castInCase(_x) == castInCase(_actor.getX())) && (castInCase(_y) == castInCase(_actor.getY()));
    }

     /**
     * Checks around a position if there is an actor of a given type and returns it 
     * @param _x position x of point (scene coordinates)
     * @param _y position y of point (scene coordinates)
     * @param _radius the number of cases around the position
     * @param _type type of actor to find
     * @return the ElementActor if there is one else null
     */
    public ElementActor checkRadial(float _x, float _y, int _radius, ElementActorType _type)
    {
        ElementActor res = null;
        float distance = 0;
        int xCase = castInCase(_x);
        int yCase = castInCase(_y);
        for (int i = -1; i <= 1; i++)
        {
            for (int j = -1; j <= 1; j++)
            {
                ElementActor v = checkRadialLine(xCase, yCase, _radius, i, j, _type);
                if (v != null) 
                {
                    float d = (float)(Math.pow(_x - v.getX(), 2) + Math.pow(_y - v.getY(), 2));
                    if (res == null || (res != null && d < distance))
                    {
                        distance = d;
                        res = v;   
                    }
                }
            }
        }
        return res;
        
    }
    /**
     * Checks on a line if ElementActor of a given type is present
     * @param _x start y position of point (case coordinates)
     * @param _y start x position of point (case coordinates)
     * @param _dx end y position of point (case coordinates)
     * @param _dy end x position of point (case coordinates)
     * @param _type type of actor to find
     * @return the ElementActor if there is one else null
     */
    private ElementActor checkRadialLine(int _x, int _y, int _radius, int _dx, int _dy, ElementActorType _type)
    {
        for (int i = 1; i <= _radius; i++)
        {
            int xi = _x + i * _dx;
            int yi = _y + i * _dy;
            //addActor(new Pheromone(xi * caseSize, yi * caseSize, PheromoneType.HOME, null, 0));
            ElementActor actor = isElement(xi, yi, _type);
            if (actor != null)
            {
                return actor;
            }
            else if (isElement(xi, yi, ElementActorType.OBSTACLE) != null)
            {
                return null;
            }
            else
            {
                if (Math.abs(_dx) == Math.abs(_dy))
                {
                    ElementActor actor2 = isElement(xi + _dx, yi, _type);
                    //addActor(new Pheromone((xi +dx) * caseSize, yi * caseSize, PheromoneType.HOME, null, 0));
                    if (actor2 != null)
                    {
                        return actor2;   
                    }
                    ElementActor actor3 = isElement(xi, yi + _dy, _type);
                    //addActor(new Pheromone(xi * caseSize, (yi+dy) * caseSize, PheromoneType.HOME, null, 0));
                    if (actor3 != null)
                    {
                        return actor3;   
                    } 
                    
                    if (isElement(xi + _dx, yi, ElementActorType.OBSTACLE) != null)
                    {
                        return null;
                    }
                    if (isElement(xi, yi + _dy, ElementActorType.OBSTACLE) != null)
                    {
                        return null;
                    }
                }
            }
        }
        return null;
    }
    /**
     * Checks around a position if a phereomone of a given type is present
     * @param _x start y position of point (scene coordinates)
     * @param _y start x position of point (scene coordinates)
     * @param _radius the number of cases around the position
     * @param _type type of pheromone to find
     * @return the Pheromone if there is one else null
     */
    public Pheromone checkRadialPheromone(float _x, float _y, int _radius, PheromoneType _type)
    {
        Pheromone res = null;
        int resStepFrom = Integer.MAX_VALUE;
        /*if (pheromone != null)
        {
            resStepFrom = pheromone.getStepFrom();
        }*/
        int xCase = castInCase(_x);
        int yCase = castInCase(_y);
        for (int i = -1; i <= 1; i++)
        {
            for (int j = -1; j <= 1; j++)
            {
                //if (!(j == 0 && i == 0))
                {
                    Pheromone actor = checkRadialLinePheromone(xCase, yCase, _radius, i, j, toActorType(_type));
                    //Pheromone actor = (Pheromone)isElement(xi, yi, type);
                    if (actor != null)
                    {
                        if(actor.getStepFrom() < resStepFrom)
                        {
                            res = actor;
                            resStepFrom = actor.getStepFrom();
                        }
                        
                    }
                }
            }
        }
        return res;
        
    }

    

    /**
     * Checks on a line if a pheromone of a given type is present
     * @param _x start y position of point (case coordinates)
     * @param _y start x position of point (case coordinates)
     * @param _dx end y position of point (case coordinates)
     * @param _dy end x position of point (case coordinates)
     * @param _type type of actor to find
     * @param _pheromone type of pheromone to find
     * @return the Pheromone if there is one else null
     */
    private Pheromone checkRadialLinePheromone(int _x, int _y, int _radius, int _dx, int _dy, ElementActorType _type)
    {
        Pheromone res = null;
        int resStepFrom = Integer.MAX_VALUE;
        /*if (pheromone != null)
        {
            resStepFrom = pheromone.getStepFrom();
        }*/

        for (int i = 1; i <= _radius; i++)
        {
            int xi = _x + i * _dx;
            int yi = _y + i * _dy;
            //addActor(new Pheromone(xi * caseSize, yi * caseSize, PheromoneType.HOME));
            Pheromone actor = (Pheromone)isElement(xi, yi, _type);
            if (actor != null)
            {
                if(actor.getStepFrom() < resStepFrom)
                {
                    res = actor;
                    resStepFrom = actor.getStepFrom();
                }
                
            }
            else if (isElement(xi, yi, ElementActorType.OBSTACLE) != null)
            {
                return res;
            }

            if (Math.abs(_dx) == Math.abs(_dy))
            {
                Pheromone actor2 = (Pheromone)isElement(xi + _dx, yi, _type);
                //addActor(new Pheromone((xi +dx) * caseSize, yi * caseSize, PheromoneType.HOME, null));
                if (actor2 != null)
                {
                    if(actor2.getStepFrom() < resStepFrom)
                    {
                        res = actor2;
                        resStepFrom = actor2.getStepFrom();
                    }
                    
                }
                Pheromone actor3 = (Pheromone)isElement(xi + _dx, yi, _type);
                //addActor(new Pheromone((xi +dx) * caseSize, yi * caseSize, PheromoneType.HOME, null));
                if (actor3 != null)
                {
                    if(actor3.getStepFrom() < resStepFrom)
                    {
                        res = actor3;
                        resStepFrom = actor3.getStepFrom();
                    }
                    
                }

                if (isElement(xi + _dx, yi, ElementActorType.OBSTACLE) != null)
                {
                    return res;
                }
                if (isElement(xi, yi + _dy, ElementActorType.OBSTACLE) != null)
                {
                    return res;
                }
            }

        }
        return res;
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
        int xCase = castInCase(_x);
        int yCase = castInCase(_y);
        return ((Resource)worldMap.getElementActorGrid()[yCase][xCase][1]).decrease(_quantity);
    }

    /**
     * Take a resource
     * @param _x start y position of point (scene coordinates)
     * @param _y start x position of point (scene coordinates)
     */
    public void removeResource(float _x, float _y) 
    {
        worldMap.getElementActorGrid()[castInCase(_y)][castInCase(_x)][1] = null;

    }

    /**
     * Add a pheromone at the given position 
     * @param _x start y position of point (scene coordinates)
     * @param _y start x position of point (scene coordinates)
     * @param _type type of pheromone to add
     * @param _ant that added the pheromone
     * @param _stepFrom distance from the last goal of the ant
     */
    public void addPheromone(float _x, float _y, PheromoneType _type, Ant _ant, int _stepFrom) 
    {
      
        int i = toActorType(_type).getValue();
        int caseX = castInCase(_x);
        int caseY = castInCase(_y);
        Pheromone currentPheromoneCase = (Pheromone)worldMap.getElementActorGrid()[caseY][caseX][i];
        if (currentPheromoneCase != null) 
        {
            if (currentPheromoneCase.getStepFrom() > _stepFrom)
            {
                currentPheromoneCase.remove();
                Pheromone p = new Pheromone(_x, _y, _type, _ant, _stepFrom);
                worldMap.getElementActorGrid()[caseY][caseX][i] = p;
                addActor(p);
            }
            else
            {
                currentPheromoneCase.reinforce();
            }
            
        }
        else
        {
            Pheromone p = new Pheromone(_x, _y, _type, _ant, _stepFrom);
            worldMap.getElementActorGrid()[caseY][caseX][i] = p;
            addActor(p);
        }
    }
    /**
     * Remove a pheromone at the given position 
     * @param _x start y position of point (scene coordinates)
     * @param _y start x position of point (scene coordinates)
     * @param _type type of pheromone to remove
     */
    public void removePheromone(float _x, float _y, PheromoneType _type) 
    {
        worldMap.getElementActorGrid()[castInCase(_y)][castInCase(_x)][toActorType(_type).getValue()] = null;

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

    /**
     * Convetr PheromoneType to ElementActorType
     * @param _type PheromoneType to convert
     * @return ElementActorType
     */
    private ElementActorType toActorType(PheromoneType _type)
    {
        if (_type == PheromoneType.HOME) 
        {
            return ElementActorType.HOME_PHEROMONE;
        }

        if (_type == PheromoneType.RESSOURCE) 
        {
            return ElementActorType.FOOD_PHEROMONE;
        }

        if (_type == PheromoneType.DANGER) 
        {
            return ElementActorType.DANGER_PHEROMONE;
        }

        return null;
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
        return ants.size();
    }

    /**
	 * Deletes an ant from the ecosystem
     * @param _ant the ant to remove
	 */
    public void removeAnt(Ant _ant)
    {
        ants.remove(_ant);
        _ant.remove();
    }

}

