package ch.hearc.simulanthill.ecosystem.actors;

import com.badlogic.gdx.graphics.Color;

import ch.hearc.simulanthill.ecosystem.Ecosystem;

/**
 * Class that handle the pheromones in the simulator inherits form ElementActor
 * A pheromone is released by an ant during his lifetime.
 */
public class Pheromone extends ElementActor 
{
    
    private static int INIT_LIFE_TIME = 200;
	private int lifeTime;
	private PheromoneType pheromoneType;
	private int stepFrom;
	private Ant ant;
	private static float opacityFactor= 0;
	private static final Color foodcolor = new Color(255/255f, 153/255f, 51/255f, 1);
	private static final Color homecolor = new Color(102/255f, 255/255f, 102/255f, 1);

	/**
	 * Constructor of pheromone
	 * @param _posX x coordinate of the pheromone
	 * @param _posY y coordinate of the pheromone
	 * @param _type type of the pheromone
	 * @param _ant ant that releases the pheromone
	 * @param _stepFrom steps from the last goal that the ant releasing the pheromone reached.
	 */
	public Pheromone(float _posX, float _posY, PheromoneType _type, Ant _ant, int _stepFrom)
	{
		super(_posX, _posY, 4, 4, (_type == PheromoneType.HOME ? homecolor : foodcolor));
		ant = _ant;
		lifeTime = INIT_LIFE_TIME;
		pheromoneType = _type;
		stepFrom = _stepFrom;
	}

	/**
	 * Sets the lifetime of the pheromone
	 * @param _lifeTimeInit litetime to set
	 */
	public static void setLifetimeInit(int _lifeTimeInit)
	{
		INIT_LIFE_TIME = _lifeTimeInit;
	}

	/**
	 * Decreased the lifetime of the pheromone by 1 unit
	 * If the lifetime reaches 0, it will be removed.
	 */
	public void decreaseLifeTime()
	{
		this.lifeTime--;
		if (lifeTime <= 0) 
		{
			remove();
		}
		sprite.setAlpha(opacityFactor * (lifeTime/(float)INIT_LIFE_TIME));
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
		lifeTime = INIT_LIFE_TIME;
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
	public Ant getAnt() 
	{
		return ant;
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

	/**
	 * Sets the opacity of a pheromone. This makes the pheromone visible on the scene
	 * @param _opacityFactor the opacity wanted of the pheromone.
	 */
	public static void setOpacityFactor(float _opacityFactor)
	{
		opacityFactor = _opacityFactor;
	}

}