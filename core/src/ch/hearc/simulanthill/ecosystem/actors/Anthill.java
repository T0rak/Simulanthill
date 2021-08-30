package ch.hearc.simulanthill.ecosystem.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

import ch.hearc.simulanthill.ecosystem.Ecosystem;
import ch.hearc.simulanthill.tools.Asset;
/**
 * The anthill actor that makes ants spawning
 */
public class Anthill extends ElementActor
{
    
    protected final Color color = new Color(MathUtils.random(0.2f, 0.8f) , MathUtils.random(0.2f, 0.8f), MathUtils.random(0.2f, 0.8f), 1);

    private static int idGenerator = 0;

    private int nbAnts;
    private int nbResources;


    
    private int id;

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
        if (Ecosystem.getCurrentEcosystem().getNbAnt() < Ecosystem.getCurrentEcosystem().getNbAntMax())
        {
            createAnt();
        }
    }
    /**
     * Spawns an ant
     */
    public void createAnt()
    {
        nbAnts++;
        int antSize = (int)Ecosystem.getCurrentEcosystem().getMapCaseSize();
        Ecosystem.getCurrentEcosystem().addAnt(new Ant(this.getX(), this.getY(),  antSize, antSize, this));
    }
    
    /**
     * Removes an ant
     * @param ant  the ant that will removed
     */
    public void removeAnt(Ant _ant)
    {
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

    public Color getColor() {
        return color;
    }
}