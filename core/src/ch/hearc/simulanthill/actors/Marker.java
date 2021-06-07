package ch.hearc.simulanthill.actors;

public class Marker extends ElementActor 
{

	public Marker(float _posX, float _posY, float _width, float _height)
	{
        super(_posX, _posY, Asset.obstacle(), "marker");
    
        setSize(_width, _height);
        this.sprite.setOrigin(_width / 2, _height / 2);
    }

    public Marker(float _x, float _y)
    {
        this(_x, _y, 30, 30);
    }

    public Marker() 
    {
        this(0, 0);
    }

}