package ch.hearc.simulanthill.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import ch.hearc.simulanthill.Ecosystem;

public class Ant extends ElementActor
{
    private static final int PHEROMONE_RELEASE_COUNTDOWN = 5;
    private static final int PHEROMONE_CHECK_COUNTDOWN = 3;
    private static final int NEXT_CHECK_COUNTDOWN = 100;
    private static final int MAX_CAPACITY = 1;
	private static double speed = 1;
    private float direction;
    private int viewSpanAngle;
	private int capacity;
    private Anthill anthill;
    private int nextCheck;
    private int pheromoneCountdown;
    private int pheromoneCheckCountdown;
    /**
     * 0 -> food exploration (look for food or food pheromones to follow)
     * 1 -> follow food pheromones 
     * 2 -> home exploration (explore to go back home or home pheromones to follow)
     * 3 -> follow home pheromones
     */
    private int state;

    public Ant(float x, float y, int width, int height, Anthill anthill)
    {
        super(x, y, Asset.ant(), "ant");

        sprite.setOrigin(width / 2, height / 2);
        setSize(width, height);
        this.capacity = 0;
        this.viewSpanAngle = 10;
        this.state = 0;
        this.direction = MathUtils.random(360f);
        this.sprite.rotate(this.direction);
        this.anthill = anthill;
        pheromoneCountdown = PHEROMONE_RELEASE_COUNTDOWN;
        pheromoneCheckCountdown = PHEROMONE_CHECK_COUNTDOWN;
        nextCheck = NEXT_CHECK_COUNTDOWN;
    }

    @Override
    public void act(float delta)
    {
        super.act(delta);
        
        if (capacity >= MAX_CAPACITY) {
            if (nextCheck < 0) {
                nextCheck = NEXT_CHECK_COUNTDOWN;
                homeResearch();

            } else {
                explore();
            }

            releasePheromone(PheromoneType.RESSOURCE);
            depositFood();

        } else {
            if (nextCheck < 0) {
                nextCheck = NEXT_CHECK_COUNTDOWN;
                foodResearch();

            } else if (pheromoneCheckCountdown < 0) {
                pheromoneCheckCountdown = PHEROMONE_CHECK_COUNTDOWN;
                followPheromone(PheromoneType.RESSOURCE);

            } else {
                explore();
            }
            //releasePheromone(PheromoneType.HOME);
            collectFood();
        }

        move();
        nextCheck--;
        pheromoneCheckCountdown--;
        pheromoneCountdown--;
        
    }

    public void collectFood()
    {
        Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();
        if (ecosystem.isResource(getX(), getY())) {
            capacity += ecosystem.takeResource(getX(), getY(), MAX_CAPACITY - capacity);
        }
    }

    public void depositFood()
    {
        Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();
        if (ecosystem.isAnthill(getX(), getY())) {
            capacity = 0;
        }
    }

    public void foodResearch()
    {
        //trouver nourriture
        Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();
        Vector2 res =  ecosystem.checkResourceAround(getX(), getY(), 3);
        
        if (res != null) 
        {
            float newDirection = MathUtils.radiansToDegrees * MathUtils.atan2(res.y - getY(), res.x - getX());
            float deltaDirection = (float)(newDirection - this.direction) % 360f;

            direction = newDirection;
            sprite.rotate(deltaDirection);
        }
        else 
        {
             explore();
        }

    }

    public void homeResearch()
    {
        float newDirection = MathUtils.radiansToDegrees * MathUtils.atan2(anthill.getY() - getY(), anthill.getX() - getX());
        
        float deltaDirection = (float)(newDirection - this.direction) % 360f;
        direction = newDirection;
        sprite.rotate(deltaDirection);
    }

    public void explore()
    {
        float deltaDirection = MathUtils.random(this.viewSpanAngle) - this.viewSpanAngle / 2f;
        direction = (float)(this.direction + deltaDirection) % 360f;
        sprite.rotate(deltaDirection);
    }

    public void move()
    {
        float directionRad = (float) Math.toRadians(this.direction);
        float nextPosX = (float) (getX() + MathUtils.cos(directionRad) * Ant.speed);
        float nextPosY = (float) (getY() + MathUtils.sin(directionRad) * Ant.speed);

        Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();
        
        if (ecosystem.isObstacle(nextPosX, nextPosY))
        {
            float deltaDirection = MathUtils.random(-180f, 180f);
            direction = (float)(direction + deltaDirection) % 360f;
            sprite.rotate(deltaDirection);

        }
        else {
            setPosition(nextPosX, nextPosY);

        }
        setPosition(getX(), getY());
    }
    
    public void releasePheromone(PheromoneType type) {
        //if (pheromoneCountdown < 0) {
            Ecosystem.getCurrentEcosystem().addPheromone(getX(), getY(), type);
            pheromoneCountdown = PHEROMONE_RELEASE_COUNTDOWN;
        //}
    }

    public boolean followPheromone(PheromoneType type) {
        if (pheromoneCheckCountdown < 0) {
            float testY = MathUtils.sin(direction);
            float testX = MathUtils.cos(direction);
            Vector2 res = Ecosystem.getCurrentEcosystem().checkStrongestPheromoneAround(getX() + 3*testX, getY() + 3*testY, 3, type);

            if (res != null) 
            {
                float newDirection = MathUtils.radiansToDegrees * MathUtils.atan2(res.y - getY(), res.x - getX());
                float deltaDirection = (float)(newDirection - this.direction) % 360f;
    
                direction = newDirection;
                sprite.rotate(deltaDirection);
                pheromoneCheckCountdown = PHEROMONE_CHECK_COUNTDOWN;
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString()
    {
        return "Fourmi !";
        //return getX().toString();
    }
}
