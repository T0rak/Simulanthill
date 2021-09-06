package ch.hearc.simulanthill.ecosystem.actors;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

import ch.hearc.simulanthill.ecosystem.Ecosystem;
import ch.hearc.simulanthill.tools.Asset;
import ch.hearc.simulanthill.tools.ColorManagement;
/**
 * The anthill actor that makes ants spawning
 */
public class Anthill extends ElementActor
{ 
    private final Texture texture = Asset.ant(ColorManagement.nextColor());

    private static int idGenerator = 0;

    private int nbAnts;
    private int nbResources;
    private int id;
    private List<Ant> antList;
    private int count = MathUtils.random(0, 10);

    /**
	 * Main constructor
	 * @param  _x the initial x position of the ant 
     * @param  _y the initial y position of the ant
     * @param  _width the width of the ant
     * @param  _height the height of the ant
	 */
    public Anthill(float _x, float _y, float _width, float _height) 
    {
        super(_x, _y, _width, _height, Asset.anthill());
        antList = new ArrayList<Ant>();
        id = idGenerator ++;
    }
    
    /**
    * Constructor
    * @param _x the initial x position
    * @param _y the initial y position 
    */
    public Anthill(float _x, float _y) 
    {
        this(_x, _y, 30, 30);
    }

    /**
    * Constructor
    */
    public Anthill() 
    {
        this(0, 0);
    }

    /**
    * Spawns ants if there are not enough
	* @param _delta  the time passed since the last act call
    */
    public void act(float _delta)
    {
        super.act(_delta);

        count++;
        
        if (Ecosystem.getCurrentEcosystem().getNbAnt() < Ecosystem.getCurrentEcosystem().getNbAntMax() && count > 10)
        {
            createAnt();
            count = 0;
        }
    }
    /**
     * Spawns an ant
     */
    public void createAnt()
    {
        nbAnts++;
        int antSize = (int)Ecosystem.getCurrentEcosystem().getMapCaseSize();
        float caseSizeCenter =  Ecosystem.getCurrentEcosystem().getMapCaseSize()/2;
        Ant newAnt = new Ant(this.getX() + caseSizeCenter, this.getY() + caseSizeCenter,  antSize, antSize, this);
        antList.add(newAnt);
        Ecosystem.getCurrentEcosystem().addAnt(newAnt);
    }
    
    public void createAntAt(float _x, float _y, int _stepFrom) 
    {
        Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();
        if (!ecosystem.isObstacle(ecosystem.getElementFrom(_x, _y))) 
        {
            nbAnts++;
            int antSize = (int)Ecosystem.getCurrentEcosystem().getMapCaseSize();
            Ant newAnt = new Ant(_x, _y,  antSize, antSize, this, _stepFrom, AntState.LOST);
            antList.add(newAnt);
            Ecosystem.getCurrentEcosystem().addAnt(newAnt);
        }
    }
    /**
     * Removes an ant
     * @param ant  the ant that will removed
     */
    public void removeAnt(Ant _ant)
    {
        nbAnts--;
        Ecosystem.getCurrentEcosystem().removeAnt(_ant);
    }


    public void addRessource(int _nbResources)
    {
        nbResources += _nbResources;
    }

    public int getNbAnts()
    {
        return nbAnts;
    }
    public int getNbResource()
    {
        return nbResources;
    }

    public int getId()
    {
        return id;
    }

    public Texture getTexture() {
        return texture;
    }

    public void removeAllAnts() {
        for (Ant ant : antList) {
            ant.remove();
        }
    }
}
