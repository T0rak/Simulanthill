package ch.hearc.simulanthill.model;


public class Ressources extends MapTile 
{

	private int id;
	private int capacity;

	protected int width;
	protected int height;

	public void setRessource(int _value) 
	{
		this.capacity = _value;
	}

	public void decreaseCapacity(int _value) 
	{
		//TODO : of how much we would like to decrease ?

		this.capacity -= 1;

	}

	public Ressources(int _posX, int _posY) 
	{
		//TODO : surround with synchonized in case of Threads ?
		//TODO : define values from panel
		this.id = idGen;
		this.posX = _posX;
		this.posY = _posY;
		this.width = 5;
		this.height = 5;
		this.capacity = 10;

		//rgb(225,177,0)
		this.color = Integer.parseInt("e1b100", 16); //Converts Hex to int

		idGen++;

	}

	@Override
	public int getId()
	{
		return this.id;
	}

}
