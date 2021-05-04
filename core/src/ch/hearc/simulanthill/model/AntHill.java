package ch.hearc.simulanthill.model;

import ch.hearc.simulanthill.model.ant.Ant;

import java.util.List;

public class AntHill extends MapTile {

	private int id;

	private int antCapacity;

	private int ressourceCapacity;

	private List<Ant> antList;

	public AntHill(int _posX, int _posY) 
	{
		this.id = idGen;
		idGen++; 
		this.posX = _posX;
		this.posY = _posY;
		this.antCapacity = 10000000;
		this.ressourceCapacity = 1000;

		//We need now to create the ants:
		for (int i = 0; i < antCapacity; i++) 
		{
			new Ant(this.posX, this.posY);
		}
		
	}

	@Override
	public int getId()
	{
		return this.id;
	}

}
