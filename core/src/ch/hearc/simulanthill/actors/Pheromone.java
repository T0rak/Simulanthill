package ch.hearc.simulanthill.actors;

import com.badlogic.gdx.math.Vector2;

public class Pheromone {
    
    private static int lifeTimeInit;
	private int lifeTime;
	private PheromoneType pheromoneType;
	private double range;
	private Vector2 position;
	public Pheromone(int _posX, int _posY) 
	{
		//TODO : surround with synchonized in case of Threads ?
		//TODO : define values from panel
		this.lifeTime = lifeTimeInit;
		this.pheromoneType = PheromoneType.HOME;
		this.range = 20;
		this.position = new Vector2(_posX, _posY);
	}

	public void setLifetimeInit(int _lifeTimeInit)
	{
		lifeTimeInit = _lifeTimeInit;
	}

	public void decreaseLifeTime()
	{
		this.lifeTime--;
	}

	//We don't need any id in Obstacles
	public int getId()
	{
		return -1;
	};
}
