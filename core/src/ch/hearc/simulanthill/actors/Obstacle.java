package ch.hearc.simulanthill.actors;

public class Obstacle extends ElementActor
{

	public Obstacle(float _posX, float _posY, float _width, float _height)
	{
		//TODO : change values fron pannel input ? 
        super(_posX, _posY, Asset.obstacle(), "obstacle");
    
        setSize(_width, _height);
        this.sprite.setOrigin(_width / 2, _height / 2);
    }

    public Obstacle(float _x, float _y) 
    {
        this(_x, _y, 30, 30);
    }

    public Obstacle() 
    {
        this(0, 0);
    }
}