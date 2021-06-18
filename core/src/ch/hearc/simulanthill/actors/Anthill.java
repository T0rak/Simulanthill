package ch.hearc.simulanthill.actors;
import ch.hearc.simulanthill.Asset;
import ch.hearc.simulanthill.Ecosystem;
/**
 * The anthill actor that makes ants spawning
 */
public class Anthill extends ElementActor
{
    /**
	 * Main constructor
	 * @param  _x the initial x position of the ant 
     * @param  _y the initial y position of the ant
     * @param  _width the width of the ant
     * @param  _height the height of the ant
	 */
    public Anthill(float _x, float _y, float _width, float _height) 
    {
        super(_x, _y, Asset.anthill(), "anthill");

        setSize(_width, _height);
        this.sprite.setOrigin(_width/2, _height/2);
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
        Ecosystem.getCurrentEcosystem().addAnt(new Ant(this.getX(), this.getY(), 12, 12, this));
    }
    
    /**
     * Removes an ant
     * @param ant  the ant that will removed
     */
    public void removeAnt(Ant ant)
    {
        Ecosystem.getCurrentEcosystem().removeAnt(ant);
    }
}
