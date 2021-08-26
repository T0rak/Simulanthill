package ch.hearc.simulanthill.actors;
import com.badlogic.gdx.math.MathUtils;

import ch.hearc.simulanthill.Asset;
import ch.hearc.simulanthill.Ecosystem;

/**
 * The ant actor that contains it's behaviour
 */
public class Ant extends ElementActor
{
    private static int PHEROMONE_RELEASE_COUNTDOWN = 5;
    private static int NEXT_CHECK_COUNTDOWN = 10;
    private static final int MAX_CAPACITY = 1;
    private static int FIELD_OF_VIEW = 3;

	private static double speed = 0;
    private static float speedFactor = 1f;
    
    private float direction;
    private int viewSpanAngle;
	private int capacity;
    private Anthill anthill;
    private int pheromoneCountdown;
    private int checkCountdown;
    private int stepFrom;
    private ElementActor goal;
    private int lastStepFrom;
    private int countLastPhero;
    private boolean blocked;

    
    
	/**
	 * Constructor
	 * @param  _x the initial x position of the ant 
     * @param  _y the initial y position of the ant
     * @param  _width the width of the ant
     * @param  _height the height of the ant
     * @param  _anthill the anthill to which one an ant belongs
	 */
    public Ant(float _x, float _y, int _width, int _height, Anthill _anthill)
    {
        super(_x, _y, Asset.ant(), "ant");
        sprite.setOrigin(_width / 2, _height / 2);
        setSize(_width, _height);
        this.capacity = 0;
        this.viewSpanAngle = 15;
        //this.direction = 90;
        this.direction = MathUtils.random(360f);
        this.sprite.rotate(this.direction);
        this.anthill = _anthill;
        this.pheromoneCountdown = PHEROMONE_RELEASE_COUNTDOWN;
        this.stepFrom = 0;
        this.lastStepFrom = Integer.MAX_VALUE;
        countLastPhero = 0;
        blocked = false;
        checkCountdown = NEXT_CHECK_COUNTDOWN;
    }

    /**
	 * Behaviour of an ant
	 * @param _delta  the time passed since the last act call
	 */
    @Override
    public void act(float _delta)
    {
        super.act(_delta);
        if (capacity < MAX_CAPACITY)
        {
            tryCollectFood();
        }
        else
        {     
            tryDepositFood();   
        }

        if (capacity < MAX_CAPACITY)
        {   
            releasePheromone(PheromoneType.HOME);   
        }
        else
        {
            releasePheromone(PheromoneType.RESSOURCE);
        }

        if (checkCountdown < 0)
        {
            ElementActor newGoal = null;
            
            if (capacity < MAX_CAPACITY)
            {
                newGoal = searchFood();
                
                if (newGoal == null)
                {
                    newGoal = searchPheromone(PheromoneType.RESSOURCE);
                }
            }
            else
            {       
                newGoal = searchAnthill();
                if (newGoal == null) 
                {
                    newGoal = searchPheromone(PheromoneType.HOME);
                }
            }

            if (newGoal != null)
            {
            goal = newGoal;
            }
        }    
        
        if (goal == null)
        {
            explore();
        }
        else
        {
            if (checkCountdown < 0)
            {
                followGoal();
                checkCountdown = NEXT_CHECK_COUNTDOWN;
            }
            else
            {
                explore();
            }
        }
        move();
    
        checkCountdown --;
        pheromoneCountdown--;
        stepFrom++;
    }
    
    /**
	 * Pick some food if there is a resource on ant's position
	 */
    public void tryCollectFood()
    {
        Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();
        if (ecosystem.isElement(getX(), getY(), ElementActorType.RESSOURCE) != null) 
        {
            capacity += ecosystem.takeResource(getX(), getY(), MAX_CAPACITY - capacity);
            if (capacity >= MAX_CAPACITY)
            {
                stepFrom = 0;
                lastStepFrom = Integer.MAX_VALUE;
            }
            goal = null;
            
        }
    }
    /**
	 * Drop the in the anthill if it is on ant's position
	 */
    public void tryDepositFood()
    {
        Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();
        if (ecosystem.isElement(getX(), getY(), ElementActorType.ANTHILL) != null) 
        {
            anthill.addRessource(capacity);
            capacity = 0;
            stepFrom = 0;
            goal = null;
            lastStepFrom = Integer.MAX_VALUE;
            if (ecosystem.getNbAnt() > ecosystem.getNbAntMax())
            {
                anthill.removeAnt(this);
            }
        }
    }
    
    /**
	 * Returns the closest anthill
     * @return the closest anthill if there is one in the field of view else returns null
	 */
    public ElementActor searchAnthill()
    {
        Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();
        return ecosystem.checkRadial(getX(), getY(), FIELD_OF_VIEW, ElementActorType.ANTHILL);
    }

    /**
	 * Returns the closest resource
     * @return the closest resource if there is one in the field of view else returns null
	 */
    public ElementActor searchFood()
    {
        Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();
        return ecosystem.checkRadial(getX(), getY(), FIELD_OF_VIEW, ElementActorType.RESSOURCE);
    }

    /**
	 * Returns the closest pheromone
     * @param _type type of the pheromone
     * @return the closest pheromone if there is one in the field of view else returns null
	 */
    public ElementActor searchPheromone(PheromoneType _type)
    {
        Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();

        Pheromone res = ecosystem.checkRadialPheromone(getX(), getY(), FIELD_OF_VIEW, _type);

        if (res != null && (res.getStepFrom() < lastStepFrom || countLastPhero == 200))
        {
            lastStepFrom = res.getStepFrom();
            countLastPhero = 0;
            return res;
        }
        else
        {
            if (res != null)
            {
                countLastPhero ++;
            }
            goal = null;
        }        
        
        if (res == null)
        {
            lastStepFrom = Integer.MAX_VALUE;
            goal = null;
        }

        return null;
    }
    /**
	 * Turns the ant in the direction of it's goal
	 */
    public void followGoal()
    {
        if (goal != null && blocked == false)
        {
            float newDirection = MathUtils.radiansToDegrees * MathUtils.atan2(goal.getY() + Ecosystem.getCurrentEcosystem().getMapCaseSize()/2 - getY(), goal.getX() + Ecosystem.getCurrentEcosystem().getMapCaseSize()/2 - getX());
            float deltaDirection = (float)(newDirection - this.direction) % 360f;

            direction = newDirection;
            sprite.rotate(deltaDirection);
        }
    }


    /**
	 * Makes the ant turning "randomly"  on itself
	 */
    public void explore()
    {
        float deltaDirection = MathUtils.random(this.viewSpanAngle) - this.viewSpanAngle / 2f;
        direction = (float)(this.direction + deltaDirection) % 360f;
        sprite.rotate(deltaDirection);
    }

    /**
	 * Moves the ant in the direction it is looking to
	 */
    public void move()
    {
        if (blocked)
        {
            float deltaDirection = MathUtils.random(-40f, 40f);
            direction = (float)(direction + deltaDirection) % 360f;
            sprite.rotate(deltaDirection);
        }
        float directionRad = (float) Math.toRadians(this.direction);
        float nextPosX = (float) (getX() + MathUtils.cos(directionRad) * Ant.speed);
        float nextPosY = (float) (getY() + MathUtils.sin(directionRad) * Ant.speed);

        Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();

        if (!ecosystem.canMove(getX(), getY(), nextPosX, nextPosY))
        {
            blocked = true;
        }
        else
        {
            blocked = false;
            setPosition(nextPosX, nextPosY);

        }
    }
    
    /**
	 * Drops a pheromone on the ant position
     * @param _type type of the pheromone to release
	 */
    public void releasePheromone(PheromoneType _type) 
    {
        Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();
        if (ecosystem.isElement(getX(), getY(), ElementActorType.OBSTACLE) == null)
        {
            if (pheromoneCountdown < 0)
            {
                ecosystem.addPheromone(getX(), getY(), _type, this, stepFrom);
                pheromoneCountdown = PHEROMONE_RELEASE_COUNTDOWN;
            }
        }
        
    }

    /**
	 * Changes the field of view of the ant
     @param _fieldOfView new field of view
	 */    
    public static void setFielOfView(int _fieldOfView)
    {
        FIELD_OF_VIEW = _fieldOfView;
    }

    /**
	 * Changes the pheromone release frequency
     * @param  _releasePheromoneTime the number of cycles before the ant releases an new pheromone
	 */
    public static void setReleasePheromoneTime(int _releasePheromoneTime)
    {
        PHEROMONE_RELEASE_COUNTDOWN = _releasePheromoneTime;
    }

    /**
	 * Changes the movement speed of the ant
     * @param _speed new speed
	 */
    public static  void setSpeedFactor(float _speedFactor)
    {
        speedFactor = _speedFactor;
        Ant.updateSpeed();
    }
    /**
    * Changes the movement speed of the ant
    * @param _speed new speed
    */
    public static  void updateSpeed()
    {
        speed = speedFactor * Ecosystem.getCurrentEcosystem().getMapCaseSize() / 10;
    }

    /**
	 * Changes the autonomy of the ant (how often it follows pheromones, resources)
     * @param _autonomy the number of cycles before the ant checks for goals
	 */
    public static void setAutonomy(int _autonomy)
    {
        NEXT_CHECK_COUNTDOWN = _autonomy;
    }


    @Override
    public void setPosition(float _x, float _y) 
    {
        super.setPosition(_x, _y);
        Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();
        sprite.setPosition(_x - getWidth()/2 + ecosystem.getMapCaseSize()/2, _y - getHeight()/2 + ecosystem.getMapCaseSize()/2);
    }
}
