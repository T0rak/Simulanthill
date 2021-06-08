package ch.hearc.simulanthill.actors;
import ch.hearc.simulanthill.Ecosystem;

public class Anthill extends ElementActor
{

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
        if (Ecosystem.getCurrentEcosystem().getNbAnt() < Ecosystem.getCurrentEcosystem().getNbAntMax())
        {
            createAnt();
        }
    }

    public void createAnt()
    {
        Ecosystem.getCurrentEcosystem().addAnt(new Ant(this.getX(), this.getY(), 12, 12, this));
    }

    public void removeAnt(Ant ant)
    {
        Ecosystem.getCurrentEcosystem().removeAnt(ant);
    }
}
