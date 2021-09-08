package ch.hearc.simulanthill.ecosystem.actors;

import com.badlogic.gdx.graphics.Color;

import ch.hearc.simulanthill.ecosystem.Ecosystem;

/**
 * The class representing the obstacles
 */
public class Obstacle extends MapTile
{
    public static final Color color = new Color(40/255f, 40/255f, 40/255f, 1);
    
    /**
    * Main constructor
    * @param _caseX the initial x position
    * @param _caseY the initial y position 
    */
	public Obstacle(int _caseX, int _caseY)
	{
        super(_caseX, _caseY, color, Ecosystem.getCurrentEcosystem());
    }
}