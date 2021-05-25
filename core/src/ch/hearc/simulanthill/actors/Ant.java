package ch.hearc.simulanthill.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import ch.hearc.simulanthill.Ecosystem;

public class Ant extends ElementActor
{
    private static final int PHEROMONE_RELEASE_COUNTDOWN = 2;
    private static final int PHEROMONE_CHECK_COUNTDOWN = 0;
    private static final int NEXT_CHECK_COUNTDOWN = 1;
    private static final int MAX_CAPACITY = 1;
    private static final int FIELD_OF_VIEW = 5;

	private static double speed = 1;
    
    private float direction;
    private int viewSpanAngle;
	private int capacity;
    private Anthill anthill;
    private int nextCheck;
    private int pheromoneCountdown;
    private int pheromoneCheckCountdown;
    private Pheromone followedPheromone;

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
        this.viewSpanAngle = 15;
        this.state = 0;
        this.direction = MathUtils.random(360f);
        this.sprite.rotate(this.direction);
        this.anthill = anthill;
        this.followedPheromone = null;
        this.pheromoneCountdown = PHEROMONE_RELEASE_COUNTDOWN;
        this.pheromoneCheckCountdown = PHEROMONE_CHECK_COUNTDOWN;
        this.nextCheck = NEXT_CHECK_COUNTDOWN;
    }

    @Override
    public void act(float delta)
    {
        super.act(delta);
        
        /*if (capacity >= MAX_CAPACITY) {
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
        }*/

        if (capacity >= MAX_CAPACITY)
        {
            homeResearch();
            releasePheromone(PheromoneType.RESSOURCE);
        }
        else
        {
            foodResearch();
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
            Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();

            Vector2 res = ecosystem.checkRadial(getX(), getY(), FIELD_OF_VIEW, ElementActorType.RESSOURCE);
            
            if (res != null) 
            {
                float newDirection = MathUtils.radiansToDegrees * MathUtils.atan2(res.y - getY(), res.x - getX());
                float deltaDirection = (float)(newDirection - this.direction) % 360f;

                direction = newDirection;
                sprite.rotate(deltaDirection);
            }
            else 
            {
                followPheromone(ElementActorType.FOOD_PHEROMONE);
            }
        collectFood();
    }

    public void homeResearch()
    {
        if (nextCheck < 0)
        {
            float newDirection = MathUtils.radiansToDegrees * MathUtils.atan2(anthill.getY() - getY(), anthill.getX() - getX());
            
            float deltaDirection = (float)(newDirection - this.direction) % 360f;
            direction = newDirection;
            sprite.rotate(deltaDirection);
            nextCheck = NEXT_CHECK_COUNTDOWN;
        }
        else
        {
            explore();
        }
        depositFood();
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
        if (pheromoneCountdown < 0) {
            Ecosystem.getCurrentEcosystem().addPheromone(getX(), getY(), type, this);
            pheromoneCountdown = PHEROMONE_RELEASE_COUNTDOWN;
        }
    }

    public void followPheromone(ElementActorType type) {
        Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();
        
        if (followedPheromone != null)
        {
            Pheromone res = ecosystem.checkRadialPheromone(getX(), getY(), FIELD_OF_VIEW, type, followedPheromone.getAnt());

            if (res != null) 
            {
                if (res != followedPheromone && res.getLifeTime() < followedPheromone.getLifeTime())
                {
                    followedPheromone = res;
                    float newDirection = MathUtils.radiansToDegrees * MathUtils.atan2(res.getY() - getY(), res.getX() - getX());
                    float deltaDirection = (float)(newDirection - this.direction) % 360f;

                    direction = newDirection;
                    sprite.rotate(deltaDirection);
                }
                else
                {
                    explore();
                }
            }
            else 
            {
                followedPheromone = null;
            }
        }
        else
        {
            followedPheromone = ecosystem.checkRadialPheromone(getX(), getY(), 3, type);
            if (followedPheromone == null)
              {
              explore();
              }
        }
        /*if (pheromoneCheckCountdown < 0) {
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
        return false;*/
    }

    @Override
    public String toString()
    {
        return "Fourmi !";
        //return getX().toString();
    }
}
