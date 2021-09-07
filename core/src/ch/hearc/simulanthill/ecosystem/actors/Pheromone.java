package ch.hearc.simulanthill.ecosystem.actors;

import com.badlogic.gdx.graphics.Color;

import ch.hearc.simulanthill.ecosystem.Ecosystem;
import ch.hearc.simulanthill.tools.Asset;

/**
 * Class that handle the pheromones in the simulator inherits form ElementActor
 * A pheromone is released by an ant during his lifetime.
 */
public class Pheromone extends ElementActor 
{
	private int lifeTime;
	private int initLifeTime;
	private PheromoneType pheromoneType;
	private int stepFrom;
	private Anthill anthill;
	public static final Color color_food = new Color(255/255f, 153/255f, 51/255f, 1);
	public static final Color color_home = new Color(102/255f, 255/255f, 102/255f, 1);

	/**
	 * Constructor of pheromone
	 * @param _posX x coordinate of the pheromone
	 * @param _posY y coordinate of the pheromone
	 * @param _type type of the pheromone
	 * @param _ant ant that releases the pheromone
	 * @param _stepFrom steps from the last goal that the ant releasing the pheromone reached.
	 */
	public Pheromone(float _posX, float _posY, PheromoneType _type, Anthill _anthill, int _stepFrom)
	{
		super(_posX, _posY, Ecosystem.getCurrentEcosystem().getMapCaseSize()/3, Ecosystem.getCurrentEcosystem().getMapCaseSize()/3, (_type == PheromoneType.HOME ? Asset.pixel(color_home) : Asset.pixel(color_food)), Ecosystem.getCurrentEcosystem());
		anthill = _anthill;
		lifeTime = _anthill.getPheromoneLifeTime();
		initLifeTime = lifeTime;
		pheromoneType = _type;
		stepFrom = _stepFrom;
		setOpacity();
	}

	/**
	 * Decreased the lifetime of the pheromone by 1 unit
	 * If the lifetime reaches 0, it will be removed.
	 */
	public void decreaseLifeTime()
	{

		lifeTime--;

		if (initLifeTime != anthill.getPheromoneLifeTime())
		{
			if (initLifeTime != 0)
			{
				lifeTime = (int)(lifeTime * anthill.getPheromoneLifeTime() / (float)initLifeTime);
			}
			else{
				lifeTime = 0;
			}
			
			initLifeTime = anthill.getPheromoneLifeTime();
		}
		
		if (lifeTime <= 0) 
		{
			remove();
		}
		setOpacity();
	}


	public void setOpacity()
	{
		sprite.setAlpha(anthill.getPheromoneOpacityFactor() * (lifeTime / (float)anthill.getPheromoneLifeTime()));
	}

	/**
	 * getter on the type of the pheromone
	 * @return the type of the pheromone
	 */
	public PheromoneType getType() 
	{
		return pheromoneType;
	}


	/**
	 * Reinforces the lifetime of a pheromone
	 * the reinforcement gives back the maximum lifetime to the pheromone. 
	 */
	public void reinforce()
	{
		lifeTime = anthill.getPheromoneLifeTime();
	}

	/**
	 * getter on the stepfrom attribute
	 * @return the step from 
	 */
	public int getStepFrom() 
	{
		return stepFrom;
	}

	/**
     * gives an act to some elements 
     * @param _delta the time elapsed since the last act.
     */
	public void act(float _delta) 
	{
		super.act(_delta);
		decreaseLifeTime();
	}
	
	/**
	 * getter on the ant that released the pheromone.
	 * @return the ant that released the pheromone.
	 */
	public Anthill getAnthill() 
	{
		return anthill;
	}
	
	/**
	 * getter on the lifetime
	 * @return the lifetime
	 */
	public int getLifeTime() 
	{
		return lifeTime;
	}

	/**
	 * Removes the pheromone.
	 */
	@Override
	public boolean remove() 
	{
		Ecosystem.getCurrentEcosystem().removePheromone(this);
		return super.remove();
	}

}
