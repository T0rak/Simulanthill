package ch.hearc.simulanthill.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

import ch.hearc.simulanthill.Ecosystem;

public class Ant extends ElementActor {
    private float direction;
    private int viewSpanAngle;
	private int capacity;
	private static double speed = 1;
    private Anthill anthill;
    
    /**
     * 0 -> food exploration (look for food or food pheromones to follow)
     * 1 -> follow food pheromones 
     * 2 -> home exploration (explore to go back home or home pheromones to follow)
     * 3 -> follow home pheromones
     */
    private int state;

    public Ant(float x, float y, int width, int height, Anthill anthill) {
        super(x, y, Asset.ant(), "ant");

        sprite.setOrigin(width / 2, height / 2);
        setSize(width, height);
        this.capacity = 0;
        this.viewSpanAngle = 15;
        this.state = 0;
        this.direction = MathUtils.random(360f);
        this.sprite.rotate(this.direction);
        this.anthill = anthill;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        foodResearch();
        move();

    }

    public void foodResearch() {
        float deltaDirection = MathUtils.random(this.viewSpanAngle) - this.viewSpanAngle / 2f;
        this.direction = (float)(this.direction + deltaDirection) % 360f;
        this.sprite.rotate(deltaDirection);

    }

    public void move() {
    
        float directionRad = (float) Math.toRadians(this.direction);
        float nextPosX = (float) (getX() + MathUtils.cos(directionRad) * Ant.speed);
        float nextPosY = (float) (getY() + MathUtils.sin(directionRad) * Ant.speed);

        Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();
        
        if (ecosystem.isObstacle(MathUtils.round(nextPosX / ecosystem.getCaseSize()), MathUtils.round(nextPosY / ecosystem.getCaseSize())))
        {
            float deltaDirection = 12.5f;
            this.direction = (float)(direction + deltaDirection) % 360f;
            this.sprite.rotate(deltaDirection);
        }
        else {
            setPosition(nextPosX, nextPosY);
        }
        setPosition(getX(),getY());
    }
    
    
    @Override
    public String toString() {
        return "Fourmi !";
        //return getX().toString();
    }
}
