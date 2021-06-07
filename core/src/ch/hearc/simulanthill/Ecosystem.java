package ch.hearc.simulanthill;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

import ch.hearc.simulanthill.actors.Ant;
import ch.hearc.simulanthill.actors.ElementActor;
import ch.hearc.simulanthill.actors.ElementActorType;
import ch.hearc.simulanthill.actors.Pheromone;
import ch.hearc.simulanthill.actors.PheromoneType;
import ch.hearc.simulanthill.actors.Resource;
import ch.hearc.simulanthill.map.WorldMap;

public class Ecosystem extends Stage
{
    private static Ecosystem instance = null;
    //private ElementActor[][][] elementActorGrid;
    //private float caseSize;
    private WorldMap worldMap;
    private List<Ant> ants;

    private Ecosystem(Viewport _viewport)
    {
        super(_viewport);
        ants = new ArrayList<>();
    }

    private Ecosystem()
    {
        super();
    }

    public static Ecosystem getInstance(Viewport _viewport)
    {
        if(instance == null)
        {
            instance = new Ecosystem(_viewport);
        }
        return instance;
        
    }

    public static Ecosystem getCurrentEcosystem()
    {
        return instance;
    }

    public void addAnt(Ant _ant)
    {
        ants.add(_ant);
        addActor(_ant);
    }

    public void loadMap()
    {
        worldMap = new WorldMap(getViewport().getWorldWidth(), getViewport().getWorldHeight());
        addElementActorGridToStage();
    }

    public void loadMap(String _filepath)
    {
        worldMap = new WorldMap(_filepath, getViewport().getWorldWidth(), getViewport().getWorldHeight());
        addElementActorGridToStage();
    }

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

    

    

    /*public float getCaseSize()
    {
        return this.mapC.getCaseSize();
    }*/

    public ElementActor isElement(float _x, float _y, ElementActorType _type)
    {
        int xCase = castInCase(_x);
        int yCase = castInCase(_y);
        return worldMap.getElementActorGrid()[yCase][xCase][_type.getValue()];
    }

    private ElementActor isElement(int _x, int _y, ElementActorType _type)
    {
        return worldMap.getElementActorGrid()[_y][_x][_type.getValue()];
    }

    public Boolean isOnElement(float _x, float _y, ElementActor _actor)
    {
        return (castInCase(_x) == castInCase(_actor.getX())) && (castInCase(_y) == castInCase(_actor.getY()));
    }

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
                if (v != null) {
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

    public Pheromone checkRadialPheromone(float _x, float _y, int _radius, PheromoneType _type, Pheromone _pheromone)
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
                    Pheromone actor = checkRadialLinePheromone(xCase, yCase, _radius, i, j, toActorType(_type), _pheromone);
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

    public Pheromone checkRadialPheromone(float _x, float _y, int _radius, PheromoneType _type)
    {
        return checkRadialPheromone(_x, _y, _radius, _type, null);
    }


    private Pheromone checkRadialLinePheromone(int _x, int _y, int _radius, int _dx, int _dy, ElementActorType _type, Pheromone _pheromone)
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

    public int takeResource(float _x, float _y, int _quantity) {
        int xCase = castInCase(_x);
        int yCase = castInCase(_y);
        return ((Resource)worldMap.getElementActorGrid()[yCase][xCase][1]).decrease(_quantity);
    }

    public void removeResource(float _x, float _y) {
        worldMap.getElementActorGrid()[castInCase(_y)][castInCase(_x)][1] = null;

    }

    public void addPheromone(float _x, float _y, PheromoneType _type, Ant _ant, int _stepFrom) {
      
        int i = toActorType(_type).getValue();
        int caseX = castInCase(_x);
        int caseY = castInCase(_y);
        Pheromone currentPheromoneCase = (Pheromone)worldMap.getElementActorGrid()[caseY][caseX][i];
        if (currentPheromoneCase != null) {
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
        else{
            Pheromone p = new Pheromone(_x, _y, _type, _ant, _stepFrom);
            worldMap.getElementActorGrid()[caseY][caseX][i] = p;
            addActor(p);
        }
        /*if (currentPheromoneCase == null) {
            Pheromone p = new Pheromone(x, y, type, ant);
            elementActorGrid[caseY][caseX][i] = p;
            addActor(p);
        } else {
            currentPheromoneCase.reinforce();
        }*/
    }

    public void removePheromone(float _x, float _y, PheromoneType _type) {
        worldMap.getElementActorGrid()[castInCase(_y)][castInCase(_x)][toActorType(_type).getValue()] = null;

    }

    public int castInCase(float _f)
    {
        return MathUtils.round(_f / worldMap.getCaseSize());
    }

    private ElementActorType toActorType(PheromoneType _type)
    {
        if (_type == PheromoneType.HOME) {
            return ElementActorType.HOME_PHEROMONE;
        }

        if (_type == PheromoneType.RESSOURCE) {
            return ElementActorType.FOOD_PHEROMONE;
        }

        if (_type == PheromoneType.DANGER) {
            return ElementActorType.DANGER_PHEROMONE;
        }

        return null;
    }

}
