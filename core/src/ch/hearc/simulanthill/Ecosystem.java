package ch.hearc.simulanthill;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

import ch.hearc.simulanthill.actors.Ant;
import ch.hearc.simulanthill.actors.ElementActor;
import ch.hearc.simulanthill.actors.Pheromone;
import ch.hearc.simulanthill.actors.PheromoneType;
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

    public boolean isObstacle(float x, float y)
    {
        int xCase = castInCase(x);
        int yCase = castInCase(y);
        return (elementActorGrid[yCase][xCase][0] != null);
    }

    public boolean isRessource(int x, int y)
    {
        return (elementActorGrid[y][x][1] != null);
    }

    public boolean isAnthill(float x, float y)
    {
        int xCase = castInCase(x);
        int yCase = castInCase(y);
        return (elementActorGrid[yCase][xCase][2] != null);
    }

    public Vector2 checkRessourceAround(float x, float y, int radius)
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
                if (isRessource(i, j))
                {
                    return new Vector2(castFromCase(i), castFromCase(j));
                }
            }
        }
        return null;
    }

    public void addPheromone(float x, float y, PheromoneType type) {
      
        int i = pheromoneIndex(type);
        Pheromone p = new Pheromone(x, y, type);
        elementActorGrid[(int) (y / caseSize)][(int) (x / caseSize)][i] = p;
        addActor(p);
    }

    public void removePheromone(Pheromone pheromone) {
        int i = pheromoneIndex(pheromone.getType());
        elementActorGrid[(int)(pheromone.getY()/caseSize)][(int)(pheromone.getX()/caseSize)][i] = null;
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
            case DANGER:
                i = 5;
                break;
            case RESSOURCE:
                i = 4;
                break;
            case HOME:
                i = 3;
                break;
            default:
            // TODO: print error
                i = -1;
                break;
        }   
        return i;
    }
}
