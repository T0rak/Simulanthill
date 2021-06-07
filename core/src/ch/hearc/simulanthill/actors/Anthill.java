package ch.hearc.simulanthill.actors;
import ch.hearc.simulanthill.Ecosystem;

public class Anthill extends ElementActor
{
    private int nbLimit = 1000;
    private int nbAnts;

    public Anthill(float _x, float _y, float _width, float _height) 
    {
        super(_x, _y, Asset.anthill(), "anthill");

        setSize(_width, _height);
        this.sprite.setOrigin(_width/2, _height/2);
    }

    public Anthill(float _x, float _y) 
    {
        this(_x, _y, 30, 30);
    }

    public Anthill() 
    {
        this(0, 0);
    }

    public void act(float _delta)
    {
        super.act(_delta);
        if (nbAnts < nbLimit)
        {
            int nbCreated = 1;
            for (int i = 0; i < nbCreated; i++) 
            {
                createAnt();
            }
            
        }
    }

    public void createAnt()
    {
        nbAnts++;
        Ecosystem.getCurrentEcosystem().addAnt(new Ant(this.getX(), this.getY(), 12, 12, this));
    }
}
