package ch.hearc.simulanthill.actors;
import ch.hearc.simulanthill.Ecosystem;

public class Anthill extends ElementActor{
    private int nbLimit = 1000;
    private int nbAnts;
    public Anthill(float x, float y, float width, float height) {
        super(x, y, Asset.anthill(), "anthill");

        setSize(width, height);
        this.sprite.setOrigin(width/2, height/2);
    }

    public Anthill(float x, float y) {
        this(x, y, 30, 30);
    }

    public Anthill() {
        this(0, 0);
    }

    public void act(float delta) {
        super.act(delta);
        if (nbAnts < nbLimit)
        {
            int nbCreated = 1;
            for (int i = 0; i < nbCreated; i++) {
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
