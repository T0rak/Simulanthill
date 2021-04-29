package ch.hearc.simulanthill.actors;

public class Anthill extends ElementActor{
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
}
