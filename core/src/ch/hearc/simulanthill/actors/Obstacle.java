package ch.hearc.simulanthill.actors;

import ch.hearc.simulanthill.Asset;

/**
 * The class representing the obstacles
 */
public class Obstacle extends ElementActor
{
    /**
    * Main constructor
    * @param _posX the initial x position
    * @param _posY the initial y position 
    * @param _width the width 
    * @param _height the height
    */
	public Obstacle(float _posX, float _posY, float _width, float _height)
	{
		//TODO : change values fron pannel input ? 
        super(_posX, _posY, Asset.obstacle(), "obstacle");
    
        setSize(_width, _height);
        this.sprite.setOrigin(_width / 2, _height / 2);
    }
    /**
    * Constructor
    * @param _posX the initial x position
    * @param _posY the initial y position 
    */
    public Obstacle(float _posX, float _posY) 
    {
        this(_posX, _posY, 30, 30);
    }

    /**
    * Constructor
    */
    public Obstacle() 
    {
        this(0, 0);
    }
}