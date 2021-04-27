package ch.hearc.simulanthill;

import com.badlogic.gdx.graphics.Texture;

import ch.hearc.simulanthill.model.ant.Ant;

public class AntActor extends ElementActor {

    public Ant ant;

    public AntActor(Ant ant) {
        super(Asset.ant(), "ant", ant);
        this.ant= ant;
        setSize(100, 100);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        ant.move();
        setPos(ant.getX(), ant.getY());
    }
    
}
