package ch.hearc.simulanthill;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import ch.hearc.simulanthill.model.ant.Ant;

public class AntActor extends ElementActor {
    private float direction;
    private int viewSpanAngle;
    private int id;
	private int capacity;
	private static double speed = 1;
    private Vector2 position;

    public AntActor() {
        super(Asset.ant(), "ant");
        this.position = new Vector2(MathUtils.random(1600), MathUtils.random(900));
        setSize(15, 15);
        this.id = 1;
        this.capacity = 0;
        this.direction = MathUtils.random(720) - 360;
        this.sprite.rotate(this.direction);
        this.viewSpanAngle = 15;
        this.sprite.setOrigin(getWidth()/2, getHeight()/2);
    }

    @Override
    public void act(float delta) {
        float deltaDirection = MathUtils.random(this.viewSpanAngle) - this.viewSpanAngle / 2f;
        if (this.position.x > 1600 || this.position.x < 0 || this.position.y > 900 || this.position.y < 0) {

            deltaDirection -= 180;
        }
        this.direction = (float)(this.direction + deltaDirection) % 360f;
        
        //Gdx.app.log("TEST", String.valueOf(this.position));

        super.act(delta);
        this.sprite.rotate(deltaDirection);
        
        float directionRad = (float) Math.toRadians(this.direction);
        this.position.x += MathUtils.cos(directionRad) * AntActor.speed;
        this.position.y += MathUtils.sin(directionRad) * AntActor.speed;

        //this.sprite.rotate(this.direction);
        setPos(this.position.x, this.position.y);
    }
    
}
