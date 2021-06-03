package ch.hearc.simulanthill;

import java.util.ArrayList;
import java.util.List;
import java.util.spi.CurrencyNameProvider;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.viewport.Viewport;

import ch.hearc.simulanthill.actors.Ant;
import ch.hearc.simulanthill.actors.ElementActor;
import ch.hearc.simulanthill.actors.ElementActorType;
import ch.hearc.simulanthill.actors.Pheromone;
import ch.hearc.simulanthill.actors.PheromoneType;
import ch.hearc.simulanthill.actors.Resource;
import ch.hearc.simulanthill.map.MapConvertor;

public class Ecosystem extends Stage
{
    private static Ecosystem instance = null;
    private ElementActor[][][] elementActorGrid;
    private float caseSize;

    private List<Ant> ants;

    private Ecosystem(Viewport viewport)
    {
        super(viewport);
        ants = new ArrayList<>();
		

        //loadMap(filepath, 1600, 900);

    }

    private Ecosystem()
    {
        super();
        

        //loadMap(filepath, 1600, 900);

    }

    public static Ecosystem getInstance(Viewport viewport)
    {
        if(instance == null)
        {
            instance = new Ecosystem(viewport);
        }
        return instance;
        
    }

    public static Ecosystem getCurrentEcosystem()
    {
        return instance;
    }

    public void addAnt(Ant _ant)
    {
        /*int nbAnts = 5000;
		Ant tab[] = new Ant[nbAnts];
		
		for (int i = 0; i < nbAnts; i++) {
			tab[i] = new Ant(MathUtils.random((float)Gdx.graphics.getWidth()), MathUtils.random((float)Gdx.graphics.getHeight()), 12, 12);
		}

		for (int i = 0; i < nbAnts; i++) {
			addActor(tab[i]);
		}*/
        ants.add(_ant);
        addActor(_ant);

    }

    public void loadMap(String filepath, float width, float height)
    {
        System.out.println("TEST");
        //MapConvertor map = new MapConvertor(filepath, width, height);
        MapConvertor.generate(filepath, width, height);
        //map.convert(1);
        //float size = width / map.getWidth();
        //Gdx.app.log("Y TEST", String.valueOf(map.getWidth()));
        //map.convert(size);
        
		for (ElementActor[][] elementA: elementActorGrid) 
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

    public void setElementActorGrid(ElementActor[][][] elementActorGrid)
    {
        this.elementActorGrid = elementActorGrid;
    }

    public void setCaseSize(float caseSize)
    {
        this.caseSize = caseSize;
    }

    public float getCaseSize()
    {
        return this.caseSize;
    }

    public ElementActor isElement(float x, float y, ElementActorType type)
    {
        int xCase = castInCase(x);
        int yCase = castInCase(y);
        return elementActorGrid[yCase][xCase][type.getValue()];
    }

    private ElementActor isElement(int x, int y, ElementActorType type)
    {
        return elementActorGrid[y][x][type.getValue()];
    }

    public Boolean isOnElement(float x, float y, ElementActor actor)
    {
        return (castInCase(x) == castInCase(actor.getX())) && (castInCase(y) == castInCase(actor.getY()));
    }

    public ElementActor checkRadial(float x, float y, int radius, ElementActorType type)
    {
        ElementActor res = null;
        float distance = 0;
        int xCase = castInCase(x);
        int yCase = castInCase(y);
        for (int i = -1; i <= 1; i++)
        {
            for (int j = -1; j <= 1; j++)
            {
                ElementActor v = checkRadialLine(xCase, yCase, radius, i, j, type);
                if (v != null) {
                    float d = (float)(Math.pow(x - v.getX(), 2) + Math.pow(y - v.getY(), 2));
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

    private ElementActor checkRadialLine(int x, int y, int radius, int dx, int dy, ElementActorType type)
    {
        for (int i = 1; i <= radius; i++)
        {
            int xi = x + i * dx;
            int yi = y + i * dy;
            //addActor(new Pheromone(xi * caseSize, yi * caseSize, PheromoneType.HOME, null, 0));
            ElementActor actor = isElement(xi, yi, type);
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
                if (Math.abs(dx) == Math.abs(dy))
                {
                    ElementActor actor2 = isElement(xi + dx, yi, type);
                    //addActor(new Pheromone((xi +dx) * caseSize, yi * caseSize, PheromoneType.HOME, null, 0));
                    if (actor2 != null)
                    {
                        return actor2;   
                    }
                    ElementActor actor3 = isElement(xi, yi + dy, type);
                    //addActor(new Pheromone(xi * caseSize, (yi+dy) * caseSize, PheromoneType.HOME, null, 0));
                    if (actor3 != null)
                    {
                        return actor3;   
                    }               
                }
            }
        }
        return null;
    }

    public Pheromone checkRadialPheromone(float x, float y, int radius, PheromoneType type, Pheromone pheromone)
    {
        Pheromone res = null;
        int resStepFrom = Integer.MAX_VALUE;
        /*if (pheromone != null)
        {
            resStepFrom = pheromone.getStepFrom();
        }*/
        int xCase = castInCase(x);
        int yCase = castInCase(y);
        for (int i = -1; i <= 1; i++)
        {
            for (int j = -1; j <= 1; j++)
            {
                if (!(j == 0 && i == 0))
                {
                    Pheromone actor = checkRadialLinePheromone(xCase, yCase, radius, i, j, toActorType(type), pheromone);
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

    public Pheromone checkRadialPheromone(float x, float y, int radius, PheromoneType type)
    {
        return checkRadialPheromone(x, y, radius, type, null);
    }


    private Pheromone checkRadialLinePheromone(int x, int y, int radius, int dx, int dy, ElementActorType type, Pheromone pheromone)
    {
        Pheromone res = null;
        int resStepFrom = Integer.MAX_VALUE;
        /*if (pheromone != null)
        {
            resStepFrom = pheromone.getStepFrom();
        }*/

        for (int i = 1; i <= radius; i++)
        {
            int xi = x + i * dx;
            int yi = y + i * dy;
            //addActor(new Pheromone(xi * caseSize, yi * caseSize, PheromoneType.HOME));
            Pheromone actor = (Pheromone)isElement(xi, yi, type);
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

            if (Math.abs(dx) == Math.abs(dy))
            {
                Pheromone actor2 = (Pheromone)isElement(xi + dx, yi, type);
                //addActor(new Pheromone((xi +dx) * caseSize, yi * caseSize, PheromoneType.HOME, null));
                if (actor2 != null)
                {
                    if(actor2.getStepFrom() < resStepFrom)
                    {
                        res = actor2;
                        resStepFrom = actor2.getStepFrom();
                    }
                    
                }
                Pheromone actor3 = (Pheromone)isElement(xi + dx, yi, type);
                //addActor(new Pheromone((xi +dx) * caseSize, yi * caseSize, PheromoneType.HOME, null));
                if (actor3 != null)
                {
                    if(actor3.getStepFrom() < resStepFrom)
                    {
                        res = actor3;
                        resStepFrom = actor3.getStepFrom();
                    }
                    
                }
            }

        }
        return res;
    }

    public int takeResource(float x, float y, int quantity) {
        int xCase = castInCase(x);
        int yCase = castInCase(y);
        return ((Resource)elementActorGrid[yCase][xCase][1]).decrease(quantity);
    }

    public void removeResource(float x, float y) {
        elementActorGrid[castInCase(y)][castInCase(x)][1] = null;

    }

    public void addPheromone(float x, float y, PheromoneType type, Ant ant, int stepFrom) {
      
        int i = toActorType(type).getValue();
        int caseX = castInCase(x);
        int caseY = castInCase(y);
        Pheromone currentPheromoneCase = (Pheromone)elementActorGrid[caseY][caseX][i];
        if (currentPheromoneCase != null) {
            currentPheromoneCase.remove();
        }
        Pheromone p = new Pheromone(x, y, type, ant, stepFrom);
        elementActorGrid[caseY][caseX][i] = p;
        addActor(p);
        /*if (currentPheromoneCase == null) {
            Pheromone p = new Pheromone(x, y, type, ant);
            elementActorGrid[caseY][caseX][i] = p;
            addActor(p);
        } else {
            currentPheromoneCase.reinforce();
        }*/
    }

    public void removePheromone(float x, float y, PheromoneType type) {
        elementActorGrid[castInCase(y)][castInCase(x)][toActorType(type).getValue()] = null;

    }

    public int castInCase(float f)
    {
        return MathUtils.round(f / getCaseSize());
    }

    private int castFromCase(int i)
    {
        return (int) (i * getCaseSize() + getCaseSize()/2);
    }

    private ElementActorType toActorType(PheromoneType type)
    {
        if (type == PheromoneType.HOME) {
            return ElementActorType.HOME_PHEROMONE;
        }

        if (type == PheromoneType.RESSOURCE) {
            return ElementActorType.FOOD_PHEROMONE;
        }

        if (type == PheromoneType.DANGER) {
            return ElementActorType.DANGER_PHEROMONE;
        }

        return null;
    }

}
