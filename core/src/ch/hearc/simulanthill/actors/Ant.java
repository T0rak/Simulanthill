package ch.hearc.simulanthill.actors;
import com.badlogic.gdx.math.MathUtils;

import ch.hearc.simulanthill.Ecosystem;

public class Ant extends ElementActor
{
    private static final int PHEROMONE_RELEASE_COUNTDOWN = 5;
    private static final int MAX_CAPACITY = 1;
    private static final int FIELD_OF_VIEW = 3;

	private static double speed = 1;
    
    private float direction;
    private int viewSpanAngle;
	private int capacity;
    private Anthill anthill;
    private int pheromoneCountdown;
    private int stepFrom;
    private ElementActor goal;
    private boolean goalIsPassed;
    private int lastStepFrom;
    private int countLastPhero;
    private boolean blocked;

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
        this.goalIsPassed = true;
        this.lastStepFrom = Integer.MAX_VALUE;
        countLastPhero = 0;
        blocked = false;
    }

    @Override
    public void act(float _delta)
    {
        super.act(_delta);

        if (capacity < MAX_CAPACITY)
        {
            
            tryCollectFood();
            
        }
        else{
            
            tryDepositFood();
            
        }

        ElementActor newGoal = null;
        
        if (capacity < MAX_CAPACITY)
        {
            releasePheromone(PheromoneType.HOME);
            newGoal = searchFood();
            
            if (newGoal == null)
            {
                newGoal = searchPheromone(PheromoneType.RESSOURCE);
            }
        }
        else
        {
            releasePheromone(PheromoneType.RESSOURCE);
            newGoal = searchAnthill();
            if (newGoal == null) {
                //newGoal = this.anthill;
                newGoal = searchPheromone(PheromoneType.HOME);
            }
        }

        if (newGoal != null)
        {
           goal = newGoal;
        }
        
        
        
        if (goal == null)
        {
            explore();
        }
        else{
            followGoal();
        }
        move();
        
        pheromoneCountdown--;
        stepFrom++;
    }

    public void tryCollectFood()
    {
        Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();
        if (ecosystem.isElement(getX(), getY(), ElementActorType.RESSOURCE) != null) {
            capacity += ecosystem.takeResource(getX(), getY(), MAX_CAPACITY - capacity);
            if (capacity >= MAX_CAPACITY)
            {
                stepFrom = 0;
                lastStepFrom = Integer.MAX_VALUE;
            }
            goal = null;
            
        }
    }

    public void tryDepositFood()
    {
        Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();
        if (ecosystem.isElement(getX(), getY(), ElementActorType.ANTHILL) != null) {
            capacity = 0;
            stepFrom = 0;
            goal = null;
            lastStepFrom = Integer.MAX_VALUE;
        }
    }

    public ElementActor searchAnthill()
    {
        Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();
        return ecosystem.checkRadial(getX(), getY(), FIELD_OF_VIEW, ElementActorType.ANTHILL);
    }

    public ElementActor searchFood()
    {
        Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();
        return ecosystem.checkRadial(getX(), getY(), FIELD_OF_VIEW, ElementActorType.RESSOURCE);
    }

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

    public void followGoal()
    {
        if (goal != null && blocked == false)
        {
            float newDirection = MathUtils.radiansToDegrees * MathUtils.atan2(goal.getY() - getY(), goal.getX() - getX());
            //System.out.println(newDirection);
            float deltaDirection = (float)(newDirection - this.direction) % 360f;

            direction = newDirection;
            sprite.rotate(deltaDirection);
        }
    }

    public void checkOnGoal()
    {
        Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();
        if (goal != null && goalIsPassed == false)
        {
            goalIsPassed = ecosystem.isOnElement(getX(),getY(), goal);
            System.out.println("("+ecosystem.castInCase(getX())+";"+ecosystem.castInCase(goal.getX())+")");
            System.out.println("("+ecosystem.castInCase(getY())+";"+ecosystem.castInCase(goal.getY())+")");
        }
    }

    public void changeGoal(ElementActor _newGoal)
    {
        goal = _newGoal;
        goalIsPassed = false;
    }

    public void explore()
    {
        float deltaDirection = MathUtils.random(this.viewSpanAngle) - this.viewSpanAngle / 2f;
        direction = (float)(this.direction + deltaDirection) % 360f;
        sprite.rotate(deltaDirection);
    }

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
        
        if (ecosystem.isElement(nextPosX, nextPosY, ElementActorType.OBSTACLE) != null)
        {
            blocked = true;

        }
        else {
            blocked = false;
            setPosition(nextPosX, nextPosY);

        }
    }
    
    public void releasePheromone(PheromoneType _type) {
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

    @Override
    public String toString()
    {
        return "Fourmi !";
    }
}
