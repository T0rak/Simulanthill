package ch.hearc.simulanthill;

import java.util.ArrayList;
import java.util.List;
import java.util.spi.CurrencyNameProvider;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
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

    public boolean isObstacle(float x, float y)
    {
        int xCase = castInCase(x);
        int yCase = castInCase(y);
        return (elementActorGrid[yCase][xCase][0] != null);
    }

    public boolean isResource(int x, int y)
    {
        return (elementActorGrid[y][x][1] != null);
    }

    public boolean isResource(float x, float y)
    {
        return (elementActorGrid[castInCase(y)][castInCase(x)][1] != null);
    }

    public boolean isAnthill(float x, float y)
    {
        int xCase = castInCase(x);
        int yCase = castInCase(y);
        return (elementActorGrid[yCase][xCase][2] != null);
    }
    

    public boolean isPheromone(int x, int y, PheromoneType type)
    {
        return (elementActorGrid[y][x][pheromoneIndex(type)] != null);
    }

    public boolean isPheromone(float x, float y, PheromoneType type)
    {
        int xCase = castInCase(x);
        int yCase = castInCase(y);
        return (elementActorGrid[yCase][xCase][pheromoneIndex(type)] != null);
    }

    public Vector2 checkRadial(float x, float y, int radius, ElementActorType type)
    {
        Vector2 res = null;
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
                    if (res == null)
                    {
                        distance = d;
                        res = new Vector2(v.getX(), v.getY());
                        
                    }
                    else if (d < distance){
                        distance = d;
                        res = new Vector2(v.getX(), v.getY());
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
            //addActor(new Pheromone(xi * caseSize, yi * caseSize, PheromoneType.HOME));
            ElementActor actor = isElement(xi, yi, type);
            if (actor != null)
            {
                return actor;
            }
            else if (isElement(xi, yi, ElementActorType.OBSTACLE) != null)
            {
                return null;
            }
        }
        return null;
    }

    public Pheromone checkRadialPheromone(float x, float y, int radius, ElementActorType type, Ant ant)
    {
        Pheromone res = null;
        float lifeTime = 0;
        int xCase = castInCase(x);
        int yCase = castInCase(y);
        for (int i = -1; i <= 1; i++)
        {
            for (int j = -1; j <= 1; j++)
            {
                Pheromone p = checkRadialLinePheromone(xCase, yCase, radius, i, j, type, ant);
                if (p != null) {
                    float lt = p.getLifeTime();
                    if (res == null)
                    {
                        lifeTime = lt;
                        res = p;
                        
                    }
                    else if (lt < lifeTime){
                        lifeTime = lt;
                        res = p;
                    }
                }
            }
        }
        return res;
        
    }

public Pheromone checkRadialPheromone(float x, float y, int radius, ElementActorType type)
    {
        float lifeTime = 0;
        int xCase = castInCase(x);
        int yCase = castInCase(y);
        Pheromone res = null;
        
        for (int i = -1; i <= 1; i++)
        {
            for (int j = -1; j <= 1; j++)
            {
                Pheromone p = checkRadialLinePheromone(xCase, yCase, radius, i, j, type);
                if (p != null) 
                {
                    float lt = p.getLifeTime();
                    if (res == null)
                    {
                        lifeTime = lt;
                        res = p;
                        
                    }
                    else if (lt < lifeTime)
                    {
                        lifeTime = lt;
                        res = p;
                    }
                }
            }
        }
        return res;
        
    }

    private Pheromone checkRadialLinePheromone(int x, int y, int radius, int dx, int dy, ElementActorType type)
    {
        for (int i = 1; i <= radius; i++)
        {
            int xi = x + i * dx;
            int yi = y + i * dy;
            //addActor(new Pheromone(xi * caseSize, yi * caseSize, PheromoneType.HOME));
            ElementActor actor = isElement(xi, yi, type);
            if (actor != null)
            {
                    return (Pheromone)actor;
                
            }
            else if (isElement(xi, yi, ElementActorType.OBSTACLE) != null)
            {
                return null;
            }
        }
        return null;
    }


    private Pheromone checkRadialLinePheromone(int x, int y, int radius, int dx, int dy, ElementActorType type, Ant ant)
    {
        for (int i = 1; i <= radius; i++)
        {
            int xi = x + i * dx;
            int yi = y + i * dy;
            //addActor(new Pheromone(xi * caseSize, yi * caseSize, PheromoneType.HOME));
            ElementActor actor = isElement(xi, yi, type);
            if (actor != null)
            {
                if (((Pheromone)actor).getAnt() == ant)
                {
                    return (Pheromone)actor;
                }
                
            }
            else if (isElement(xi, yi, ElementActorType.OBSTACLE) != null)
            {
                return null;
            }
        }
        return null;
    }

    /*
    public Vector2 checkResourceAround(float x, float y, int radius)
    {
        int xCase = castInCase(x);
        int yCase = castInCase(y);
        
        for (int i = xCase - radius; i <= xCase + radius; i++) 
        {
            if (i >= elementActorGrid[0].length || i < 0){
                break;
            }
            for (int j = yCase - radius; j <= yCase + radius; j++)
            {
                if (j >= elementActorGrid.length || j < 0){
                    break;
                }
                if (isElement(i, j, ElementActorType.RESSOURCE) == null)
                {
                    return new Vector2(castFromCase(i), castFromCase(j));
                }
            }
        }
        return null;
    }

    public Vector2 checkStrongestPheromoneAround(float x, float y, int radius, PheromoneType type)
    {
        int xCase = castInCase(x);
        int yCase = castInCase(y);
        
        int index = pheromoneIndex(type);
        Pheromone strongestPheromone = null;
        int strongestPower = 0;

        for (int i = xCase - radius; i <= xCase + radius; i++) 
        {
            if (i >= elementActorGrid[0].length || i < 0){
                break;
            }
            for (int j = yCase - radius; j <= yCase + radius; j++)
            {
                if (j >= elementActorGrid.length || j < 0){
                    break;
                }
                if (isPheromone(i, j, type))
                {
                    Pheromone p = ((Pheromone)elementActorGrid[j][i][index]);
                    if (p.getPower() > strongestPower) {
                        strongestPheromone = p;
                        strongestPower = p.getPower();
                    }
                }
            }
        }
        if (strongestPheromone == null) {
            return null;
        }
        return new Vector2(strongestPheromone.getX(), strongestPheromone.getY());
    }
*/
    public int takeResource(float x, float y, int quantity) {
        int xCase = castInCase(x);
        int yCase = castInCase(y);
        return ((Resource)elementActorGrid[yCase][xCase][1]).decrease(quantity);
    }

    public void removeResource(float x, float y) {
        elementActorGrid[castInCase(y)][castInCase(x)][1] = null;

    }

    public void addPheromone(float x, float y, PheromoneType type, Ant ant) {
      
        int i = pheromoneIndex(type);
        int caseX = castInCase(x);
        int caseY = castInCase(y);
        //Pheromone currentPheromoneCase = (Pheromone)elementActorGrid[caseY][caseX][i];
        Pheromone p = new Pheromone(x, y, type, ant);
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
        int i = pheromoneIndex(type);
        elementActorGrid[castInCase(y)][castInCase(x)][i] = null;

    }

    private int castInCase(float f)
    {
        return MathUtils.round(f / getCaseSize());
    }

    private int castFromCase(int i)
    {
        return (int) (i * getCaseSize() + getCaseSize()/2);
    }

    private int pheromoneIndex(PheromoneType type) {
        int i;
        switch (type) {
            case HOME:
                i = 3;
                break;
            case RESSOURCE:
                i = 4;
                break;
            case DANGER:
                i = 5;
                break;
            default:
            // TODO: print error
                i = -1;
                break;
        }   
        return i;
    }

}
