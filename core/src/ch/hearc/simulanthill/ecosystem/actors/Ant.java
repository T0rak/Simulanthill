package ch.hearc.simulanthill.ecosystem.actors;
import com.badlogic.gdx.math.MathUtils;

import ch.hearc.simulanthill.ecosystem.Ecosystem;

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
        super(_x, _y, _width, _height, _anthill.getTexture());

        this.capacity = 0;
        this.viewSpanAngle = 15;

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
        if (ecosystem.isResource(ecosystem.getElementFrom(getX(), getY()))) 
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
        ElementActor actorFound = ecosystem.getElementFrom(getX(), getY());
        if (ecosystem.isAnthill(actorFound) && anthill == (Anthill)actorFound) 
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
        Anthill actor = (Anthill)checkRadial(Anthill.class);

        if(actor != null && actor != anthill)
        {
            return null;
        }
    
        return actor;
    }

    /**
	 * Returns the closest resource
     * @return the closest resource if there is one in the field of view else returns null
	 */
    public ElementActor searchFood()
    {
        return checkRadial(Resource.class);
    }

    /**
	 * Returns the closest pheromone
     * @param _type type of the pheromone
     * @return the closest pheromone if there is one in the field of view else returns null
	 */
    public ElementActor searchPheromone(PheromoneType _type)
    {

        Pheromone res = checkRadialPheromone(_type);

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

        if (!canMove(nextPosX, nextPosY))
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
        if (ecosystem.getElementFrom(getX(), getY()) == null)
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

    public Anthill getAnthill()
    {
        return anthill;
    }

    private boolean canMove(float _destinationX, float _destinationY) {

        Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();
        int destinationXCase = ecosystem.castInCase(_destinationX);
        int destinationYCase = ecosystem.castInCase(_destinationY);

        int initXCase = ecosystem.castInCase(getX());
        int initYCase = ecosystem.castInCase(getY());

        int dx = destinationXCase - initXCase;
        int dy = destinationYCase - initYCase;
        

        if (ecosystem.isObstacle(ecosystem.getElement(destinationXCase, destinationYCase))) 
        {
            return false;
        }
        
        if (dx != 0 && dy != 0) 
        {
            if (ecosystem.isObstacle(ecosystem.getElement(initXCase, initYCase + dy)) && ecosystem.isObstacle(ecosystem.getElement(initXCase + dx, initYCase)))
            {
                return false;
            }
            
        }
        
        return true;
    }

     /**
     * Checks around a position if there is an actor of a given type and returns it 
     * @param _x position x of point (scene coordinates)
     * @param _y position y of point (scene coordinates)
     * @param _radius the number of cases around the position
     * @param _type type of actor to find
     * @return the ElementActor if there is one else null
     */
    public ElementActor checkRadial(Class<?> _class)
    {
        Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();
        ElementActor res = null;
        float distance = 0;
        
        int xCase = ecosystem.castInCase(getX());
        int yCase = ecosystem.castInCase(getY());

        for (int i = -1; i <= 1; i++)
        {
            for (int j = -1; j <= 1; j++)
            {
                if (!(i==0 && j==0))
                {
                    ElementActor v = checkRadialLine(xCase, yCase, i, j, _class);
                    if (v != null) 
                    {
                        float d = (float)(Math.pow(getX() - v.getX(), 2) + Math.pow(getY() - v.getY(), 2));
                        if (res == null || (res != null && d < distance))
                        {
                            distance = d;
                            res = v;   
                        }
                    }
                }
            }
        }
        return res;
        
    }
    /**
     * Checks on a line if ElementActor of a given type is present
     * @param _x start y position of point (case coordinates)
     * @param _y start x position of point (case coordinates)
     * @param _dx end y position of point (case coordinates)
     * @param _dy end x position of point (case coordinates)
     * @param _type type of actor to find
     * @return the ElementActor if there is one else null
     */
    private ElementActor checkRadialLine(int _x, int _y, int _dx, int _dy, Class<?> _class)
    {
        Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();
        for (int i = 1; i <= FIELD_OF_VIEW; i++)
        {
            int xi = _x + i * _dx;
            int yi = _y + i * _dy;
            
            if (Math.abs(_dx) == Math.abs(_dy))
            {
                ElementActor actor2 = ecosystem.getElement(xi - _dx, yi);
                ElementActor actor3 = ecosystem.getElement(xi, yi - _dy);

                if (ecosystem.isInstanceOf(actor2, _class))
                {
                    return actor2;
                }

                
                if (ecosystem.isInstanceOf(actor3, _class))
                {
                    return actor3;
                }

                
                if (ecosystem.isObstacle(actor2))
                {
                    return null;
                }

                if (ecosystem.isObstacle(actor3))
                {
                    return null;
                }
            }

            ElementActor actor = ecosystem.getElement(xi, yi);

            if (ecosystem.isInstanceOf(actor, _class))
            {
                return actor;
            }
            else if (ecosystem.isObstacle(actor))
            {
                return null;
            }
        }
        return null;
    }


    /**
     * Checks around a position if a phereomone of a given type is present
     * @param _x start y position of point (scene coordinates)
     * @param _y start x position of point (scene coordinates)
     * @param _radius the number of cases around the position
     * @param _type type of pheromone to find
     * @return the Pheromone if there is one else null
     */
     
    public Pheromone checkRadialPheromone(PheromoneType _type)
    {
        Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();
        
        Pheromone res = null;
        int resStepFrom = Integer.MAX_VALUE;

        int xCase = ecosystem.castInCase(getX());
        int yCase = ecosystem.castInCase(getY());

        for (int i = -1; i <= 1; i++)
        {
            for (int j = -1; j <= 1; j++)
            {
                if (!(j == 0 && i == 0))
                {
                    Pheromone actor = checkRadialLinePheromone(xCase, yCase, i, j, _type);
                    if (actor != null)
                    {
                        if(actor.getStepFrom() < resStepFrom)
                        {
                            res = actor;
                            resStepFrom = actor.getStepFrom();
                        }
                        
                    }
                }
            }
        }
        return res;
        
    }    
    
    /**
     * Checks on a line if a pheromone of a given type is present
     * @param _x start y position of point (case coordinates)
     * @param _y start x position of point (case coordinates)
     * @param _dx end y position of point (case coordinates)
     * @param _dy end x position of point (case coordinates)
     * @param _type type of actor to find
     * @param _pheromone type of pheromone to find
     * @return the Pheromone if there is one else null
     */
    private Pheromone checkRadialLinePheromone(int _x, int _y, int _dx, int _dy, PheromoneType _type)
    {
        Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();
        Pheromone res = null;
        int resStepFrom = Integer.MAX_VALUE;

        for (int i = 1; i <= FIELD_OF_VIEW; i++)
        {
            int xi = _x + i * _dx;
            int yi = _y + i * _dy;

            if (Math.abs(_dx) == Math.abs(_dy))
            {
                Pheromone actor2 = (Pheromone)ecosystem.isPheromone(xi - _dx, yi, anthill, _type);
                
                if (actor2 != null)
                {
                    if (actor2.getStepFrom() < resStepFrom)
                    {
                        res = actor2;
                        resStepFrom = actor2.getStepFrom();
                    }
                    
                }
                Pheromone actor3 = (Pheromone)ecosystem.isPheromone(xi, yi - _dy, anthill, _type);

                if (actor3 != null)
                {
                    if (actor3.getStepFrom() < resStepFrom)
                    {
                        res = actor3;
                        resStepFrom = actor3.getStepFrom();
                    }
                }

                if (ecosystem.isObstacle(ecosystem.getElement(xi - _dx, yi)))
                {
                    return res;
                }
                if (ecosystem.isObstacle(ecosystem.getElement(xi, yi - _dy)))
                {
                    return res;
                }
            }
            Pheromone actor = ecosystem.isPheromone(xi, yi, anthill, _type);
            if (actor != null)
            {
                if (actor.getStepFrom() < resStepFrom)
                {
                    res = actor;
                    resStepFrom = actor.getStepFrom();
                }
                
            }
            else if (ecosystem.isObstacle(ecosystem.getElement(xi, yi)))
            {
                return res;
            }

        }
        return res;
    }

    
}
