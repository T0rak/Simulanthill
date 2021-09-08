package ch.hearc.simulanthill.ecosystem.actors;
import com.badlogic.gdx.math.MathUtils;
import ch.hearc.simulanthill.ecosystem.Ecosystem;
import ch.hearc.simulanthill.tools.Asset;

import static ch.hearc.simulanthill.ecosystem.actors.AntState.*;

/**
 * The ant actor that contains it's behaviour
 */
public class Ant extends ElementActor
{

    private static final int MAX_CAPACITY = 1;
    
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
    private AntState state;
    
    private int avoidObstacleMemory;

	/**
	 * Constructor
	 * @param  _x the initial x position of the ant 
     * @param  _y the initial y position of the ant
     * @param  _width the width of the ant
     * @param  _height the height of the ant
     * @param  _anthill the anthill to which one an ant belongs
	 */
    public Ant(float _x, float _y, float _width, float _height, Anthill _anthill, int _stepFrom, AntState _state)   
    {
        super(_x, _y, _width, _height, Asset.ant(_anthill.getAntColor()), Ecosystem.getCurrentEcosystem());
        state = _state;
        capacity = 0;
        viewSpanAngle = 15;
        
        setDirection(MathUtils.random(360f));
        
        anthill = _anthill;

        pheromoneCountdown = anthill.getAntPheromoneReleaseFrequency();
        stepFrom = _stepFrom;
        lastStepFrom = Integer.MAX_VALUE;
        countLastPhero = 0;
        
        blocked = false;
        checkCountdown = anthill.getAntIndependance();
    }

    public void setDirection(float _direction)
    {
        float newDirection = _direction % 360f;
        if (newDirection < 0)
        {
            newDirection =  newDirection + 360;            
        }

        float deltaDirection = newDirection - this.direction;

        sprite.rotate(deltaDirection);

        direction = newDirection;
    }

    /**
	 * Behaviour of an ant
	 * @param _delta  the time passed since the last act call
	 */
    @Override
    public void act(float _delta)
    {
        super.act(_delta);
        
        if (state == SEARCHING_RESOURCE || state == LOST)
        {
            tryCollectFood();
        }
        else
        {     
            tryDepositFood();   
        }

        if (state == SEARCHING_RESOURCE)
        {   
            releasePheromone(PheromoneType.HOME);   
        }
        else if (state == SEARCHING_ANTHILL)
        {
            releasePheromone(PheromoneType.RESSOURCE);
        }

        if (checkCountdown < 0)
        {
            ElementActor newGoal = null;
            
            if (state == SEARCHING_RESOURCE || state == LOST)
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
                checkCountdown = anthill.getAntIndependance();
            }
            else
            {
                explore();
            }
        }

        move();
        
        if (ecosystem.isObstacle(xCase, yCase))
        {
            ecosystem.moveAntOnGrid(xCase, yCase, anthill.getXCase(), anthill.getYCase(), anthill.getId());
            setPosition(anthill.getX(), anthill.getY());
        }
        checkCountdown--;
        pheromoneCountdown--;
        stepFrom++;
    }
    
    /**
	 * Pick some food if there is a resource on ant's position
	 */
    public void tryCollectFood()
    {
        
        if (ecosystem.isResource(xCase, yCase))
        {
            int res = ecosystem.takeResource(getX(), getY(), MAX_CAPACITY - capacity);
            
            capacity += res;
            if (res != 0)
            {
                goal = null;
                stepFrom = 0;
                lastStepFrom = Integer.MAX_VALUE;
                if (capacity >= MAX_CAPACITY)
                {
                    state = SEARCHING_ANTHILL;
                }
            }

        }
    }
    /**
	 * Drop the in the anthill if it is on ant's position
	 */
    public void tryDepositFood()
    {
        
        MapTile actorFound = ecosystem.getMapTileAt(xCase, yCase);
        
        if (ecosystem.isAnthill(actorFound) && anthill == (Anthill)actorFound) 
        {
            anthill.addResource(capacity);
            capacity = 0;
            stepFrom = 0;
            goal = null;
            lastStepFrom = Integer.MAX_VALUE;
            state = SEARCHING_RESOURCE;
        }
    }
    
    /**
	 * Returns the closest anthill
     * @return the closest anthill if there is one in the field of view else returns null
	 */
    public ElementActor searchAnthill()
    {
        Anthill actor = (Anthill)checkRadial(Anthill.class);

        if (actor != null && actor != anthill)
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
            float newDirection = MathUtils.radiansToDegrees * MathUtils.atan2(goal.getCenteredY() - getY(), goal.getCenteredX() - getX());
            setDirection(newDirection);
        }
    }


    /**
	 * Makes the ant turning "randomly"  on itself
	 */
    public void explore()
    {
        float deltaDirection = MathUtils.random(this.viewSpanAngle * anthill.getAntSpeedFactor()) - this.viewSpanAngle * anthill.getAntSpeedFactor() / 2f;
        setDirection(this.direction + deltaDirection);
    }

    /**
	 * Moves the ant in the direction it is looking to
	 */
    public void move()
    {
        float caseSize = ecosystem.getMapCaseSize();
        
        float directionRad = (float) Math.toRadians(direction);
        
        float nextCaseFloatX = (float) (getX() + MathUtils.cos(directionRad) * caseSize);
        float nextCaseFloatY = (float) (getY() + MathUtils.sin(directionRad) * caseSize);

        int nextCaseX = ecosystem.floatToGridCoordinate(nextCaseFloatX);
        int nextCaseY = ecosystem.floatToGridCoordinate(nextCaseFloatY);

        int dx = nextCaseX - xCase;
        int dy = nextCaseY - yCase;

        int referenceX = nextCaseX;
        int referenceY = nextCaseY;
        
        if (Math.abs(dx) == Math.abs(dy))
        {
            int rand0 = MathUtils.random(0,1);
            int rand1 = Math.abs(rand0 - 1);
            
            int x2 = xCase + rand0 * dx;
            int y2 = yCase + rand1 * dy;

            int x3 = xCase + rand1 * dx;
            int y3 = yCase + rand0 * dy;
            
            if (isCaseToAvoid(x2, y2))
            {
                referenceX = x2;
                referenceY = y2;
            }
            else if (isCaseToAvoid(x3, y3))
            {
                referenceX = x3;
                referenceY = y3;
            }
            
        }
        
        if (isCaseToAvoid(referenceX, referenceY))
        {
            avoidCase(referenceX, referenceY);
        } else
        {
            avoidObstacleMemory = 0;
        }
        
        float nextPosX = (float) (getX() + MathUtils.cos(directionRad) * anthill.getAntSpeedFactor()* ecosystem.getMapCaseSize()/10);
        float nextPosY = (float) (getY() + MathUtils.sin(directionRad) * anthill.getAntSpeedFactor() * ecosystem.getMapCaseSize()/10);

        if (!canMove(nextPosX, nextPosY))
        {
            blocked = true;
        } else 
        {
            blocked = false;
            ecosystem.moveAntOnGrid(xCase, yCase, ecosystem.floatToGridCoordinate(nextPosX), ecosystem.floatToGridCoordinate(nextPosY), anthill.getId());
            setPosition(nextPosX, nextPosY);

        }
    }

    public boolean isCaseToAvoid(int _x, int _y)
    {  
        int nbInCase = ecosystem.getOthersNbAntsAt(_x,_y, anthill.getId());
        return ecosystem.isObstacle(_x, _y) || nbInCase > ecosystem.getNbAntsAt(_x, _y, anthill.getId());
    }
    
    public void avoidCase(int _x, int _y) 
    {
        if (avoidObstacleMemory == 0)
        {
            float caseSize = ecosystem.getMapCaseSize();

            int dx = _x - xCase;
            int dy = _y - yCase;

            if (dx == 0 && dy != 0)
            {
                if (direction > 0 && direction < 90 || direction > 180 && direction < 270)
                {
                    avoidObstacleMemory = -1;
                }
                else if (direction > 90 && direction < 180 || direction > 270 && direction < 360)
                {
                    avoidObstacleMemory = 1;
                }
                else
                {
                    avoidObstacleMemory = MathUtils.random(0, 1);
                    if (avoidObstacleMemory == 0)
                    {
                        avoidObstacleMemory = -1;
                    }

                }
            }
            else if (dy == 0 && dx != 0)
            {
                if (direction > 0 && direction < 90 || direction > 180 && direction < 270)
                {
                    avoidObstacleMemory = 1;
                }
                else if (direction > 90 && direction < 180 || direction > 270 && direction < 360)
                {
                    avoidObstacleMemory = -1;
                }
                else
                {
                    avoidObstacleMemory = MathUtils.random(0, 1);
                    if (avoidObstacleMemory == 0)
                    {
                        avoidObstacleMemory = -1;
                    }
                }
            }
            else
            {
                float verticeX = (_x + (-(- dx - 1 ) / 2f) * caseSize);
                float verticeY = (_y + (-(- dy - 1) / 2f) * caseSize);

                float angle = MathUtils.atan2(getY() - verticeY ,getX() - verticeX);
                avoidObstacleMemory = (int)Math.signum(direction - angle);
            } 
        }
            
        float deltaDirection = 15 * avoidObstacleMemory * (float)Math.sqrt(Math.sqrt(anthill.getAntSpeedFactor()));
        
        setDirection(direction + deltaDirection);
    }
    
    /**
	 * Drops a pheromone on the ant position
     * @param _type type of the pheromone to release
	 */
    public void releasePheromone(PheromoneType _type) 
    {
        
        if (ecosystem.getMapTileAt(xCase, yCase) == null)
        {
            if (pheromoneCountdown < 0)
            {
                ecosystem.addPheromone(new Pheromone(getX(), getY(), _type, anthill, stepFrom));
                pheromoneCountdown = anthill.getAntPheromoneReleaseFrequency();
            }
        }
        
    }


    @Override
    public void setPosition(float _x, float _y) 
    {
        super.setPosition(_x, _y);
        
        sprite.setPosition(_x - getWidth()/2, _y - getHeight()/2);
    }

    public Anthill getAnthill()
    {
        return anthill;
    }

    private boolean canMove(float _destinationX, float _destinationY) {

        
        int destinationXCase = ecosystem.floatToGridCoordinate(_destinationX);
        int destinationYCase = ecosystem.floatToGridCoordinate(_destinationY);

        int dx = destinationXCase - xCase;
        int dy = destinationYCase - yCase;
        
        if (ecosystem.isObstacle(destinationXCase, destinationYCase)) 
        {
            return false;
        }
        
        if (dx != 0 && dy != 0) 
        {
            if (ecosystem.isObstacle(xCase, yCase + dy) && ecosystem.isObstacle(xCase + dx, yCase))
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
        
        ElementActor res = null;
        float distance = 0;

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
        
        for (int i = 1; i <= anthill.getAntFieldOfView(); i++)
        {
            int xi = _x + i * _dx;
            int yi = _y + i * _dy;
            
            if (Math.abs(_dx) == Math.abs(_dy))
            {
                ElementActor actor2 = ecosystem.getMapTileAt(xi - _dx, yi);
                ElementActor actor3 = ecosystem.getMapTileAt(xi, yi - _dy);

                if (ecosystem.isInstanceOf(actor2, _class))
                {
                    return actor2;
                }

                
                if (ecosystem.isInstanceOf(actor3, _class))
                {
                    return actor3;
                }

                
                if (isCaseToAvoid(xi - _dx, yi) || isCaseToAvoid(xi, yi - _dy))
                {
                    return null;
                }
            }

            ElementActor actor = ecosystem.getMapTileAt(xi, yi);

            if (ecosystem.isInstanceOf(actor, _class))
            {
                return actor;
            }
            else if (isCaseToAvoid(xi, yi))
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
        Pheromone res = null;
        int resStepFrom = Integer.MAX_VALUE;

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
        
        Pheromone res = null;
        int resStepFrom = Integer.MAX_VALUE;

        for (int i = 1; i <= anthill.getAntFieldOfView(); i++)
        {
            int xi = _x + i * _dx;
            int yi = _y + i * _dy;

            if (Math.abs(_dx) == Math.abs(_dy))
            {
                if (isCaseToAvoid(xi - _dx, yi) || isCaseToAvoid(xi, yi - _dy))
                {
                    return res;
                }

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
            }
            if (isCaseToAvoid(xi, yi))
            {
                return res;
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
            

        }
        return res;
    }
}
