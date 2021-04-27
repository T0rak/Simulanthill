package ch.hearc.simulanthill.model.ant;

import ch.hearc.simulanthill.model.Pheromone;

import ch.hearc.simulanthill.model.Entity;

public class Ant extends Entity {

	private int id;

	private int capacity;

	private State state;

	private static double speed = 10;

	private Pheromone currentPheromoneProduced;

	public Ant(int _posX, int _posY) 
	{
		//TODO : surround with synchonized in case of Threads ?
		//TODO : define values from panel
		this.id = idGen;
		this.capacity = 10;
		this.angle = 0;
		this.posX = _posX;
		this.posY = _posY;

		idGen++;

	}

	public void takeRessource(int _ressource) 
	{
		if (this.capacity < 10)
		{
			this.capacity += _ressource;
		}

	}

	public void move()
	{
		this.posX += Math.cos(this.angle) * speed;
		this.posY += Math.sin(this.angle) * speed;
	}

	public int depositRessource() 
	{
		int depositedValue = this.capacity;
		this.capacity = 0;

		return depositedValue;
	}

	public static void setSpeed(double _speed) 
	{
		speed = _speed;
	}

	public void releasePheromone() 
	{
		//TODO
		this.currentPheromoneProduced = new Pheromone(this.posX, this.posY);
	}

	@Override
	public int getId()
	{
		return this.id;
	}

}
