package ch.hearc.simulanthill;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

import ch.hearc.simulanthill.actors.Ant;
import ch.hearc.simulanthill.actors.ElementActor;
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

    public boolean isObstacle(int x, int y)
    {
        return (elementActorGrid[y][x][0] != null);
    }
    public boolean isRessource(int x, int y)
    {
        return (elementActorGrid[y][x][1] != null);
    }
    public boolean isAnthill(int x, int y)
    {
        return (elementActorGrid[y][x][2] != null);
    }
}
