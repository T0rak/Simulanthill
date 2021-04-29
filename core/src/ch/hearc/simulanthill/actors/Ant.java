package ch.hearc.simulanthill.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

public class Ant extends ElementActor {
    private float direction;
    private int viewSpanAngle;
	private int capacity;
	private static double speed = 1;
    
    /**
     * 0 -> food exploration (look for food or food pheromones to follow)
     * 1 -> follow food pheromones 
     * 2 -> home exploration (explore to go back home or home pheromones to follow)
     * 3 -> follow home pheromones
     */
    private int state;

    public Ant(float x, float y, int width, int height) {
        super(x, y, Asset.ant(), "ant");

        setSize(width, height);
        this.sprite.setOrigin(width/2, height/2);
        
        this.capacity = 0;
        this.viewSpanAngle = 15;
        this.state = 0;
        this.direction = MathUtils.random(360f);
        this.sprite.rotate(this.direction);
    }

    public Ant() {
        this(0, 0, 15, 15);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        moveExploration();
    }

    public void moveExploration() {
        float deltaDirection = MathUtils.random(this.viewSpanAngle) - this.viewSpanAngle / 2f;
        if (this.position.x > Gdx.graphics.getWidth() || this.position.x < 0 || this.position.y > Gdx.graphics.getHeight() || this.position.y < 0) {
            deltaDirection -= 128.25;
        }
        this.direction = (float)(this.direction + deltaDirection) % 360f;

        this.sprite.rotate(deltaDirection);
        
        float directionRad = (float) Math.toRadians(this.direction);
        this.position.x += MathUtils.cos(directionRad) * Ant.speed;
        this.position.y += MathUtils.sin(directionRad) * Ant.speed;

        setPos(this.position.x, this.position.y);
    }
    
    @Override
    public String toString() {
        return this.position.toString();
    }
}
