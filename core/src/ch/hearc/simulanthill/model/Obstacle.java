package ch.hearc.simulanthill.model;

public class Obstacle extends MapTile {

	protected int width;
	protected int height;

	public Obstacle(int _posX, int _posY) 
	{
		//TODO : change values fron pannel input ? 
		this.posX = _posX;
		this.posY = _posY;
		this.width = 5;
		this.height = 5;

		//rgb(170,171,174)
		this.color = Integer.parseInt("aaabae", 16); //Converts Hex to int

	}

	//We don't need any id in Obstacles
	public int getId()
	{
		return -1;
	};

}
