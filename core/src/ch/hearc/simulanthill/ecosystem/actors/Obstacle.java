package ch.hearc.simulanthill.ecosystem.actors;

import com.badlogic.gdx.graphics.Color;

/**
 * The class representing the obstacles
 */
public class Obstacle extends ElementActor
{
    public static final Color color = new Color(40/255f, 40/255f, 40/255f, 1);
    /**
    * Main constructor
    * @param _posX the initial x position
    * @param _posY the initial y position 
    * @param _width the width 
    * @param _height the height
    */
	public Obstacle(float _posX, float _posY, float _width, float _height)
	{
        super(_posX, _posY, _width, _height, color);
    }
}