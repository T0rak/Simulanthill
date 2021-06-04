package ch.hearc.simulanthill.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import ch.hearc.simulanthill.Ecosystem;

public class Ant extends ElementActor
{
    private static final int PHEROMONE_RELEASE_COUNTDOWN = 5;
    private static final int PHEROMONE_CHECK_COUNTDOWN = 0;
    private static final int NEXT_CHECK_COUNTDOWN = -1;
    private static final int MAX_CAPACITY = 1;
    private static final int FIELD_OF_VIEW = 3;
    private static final int PHEROMONE_RELEASE_TIME = 1000;

	private static double speed = 1;
    
    private float direction;
    private int viewSpanAngle;
	private int capacity;
    private Anthill anthill;
    private int nextCheck;
    private int pheromoneCountdown;
    private int pheromoneCheckCountdown;
    private Pheromone followedPheromone;
    private int pheromoneTime;
    private int stepFrom;
    private ElementActor goal;
    private boolean goalIsPassed;
    private int lastStepFrom;
    private int countLastPhero;
    private boolean blocked;
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
        //this.direction = 90;
        this.direction = MathUtils.random(360f);
        this.sprite.rotate(this.direction);
        this.anthill = anthill;
        this.followedPheromone = null;
        this.pheromoneCountdown = PHEROMONE_RELEASE_COUNTDOWN;
        this.pheromoneCheckCountdown = PHEROMONE_CHECK_COUNTDOWN;
        this.nextCheck = NEXT_CHECK_COUNTDOWN;
        this.pheromoneTime = PHEROMONE_RELEASE_TIME;
        this.stepFrom = 0;
        this.goalIsPassed = true;
        this.lastStepFrom = Integer.MAX_VALUE;
        countLastPhero = 0;
        blocked = false;
    }

    @Override
    public void act(float delta)
    {
        super.act(delta);

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
           //Gdx.app.log("("+goal.getX() +";"+ goal.getY()+")", goal.getClass().getName());
           if (goal.getClass() == Pheromone.class)
           {
            //Gdx.app.log("Type : ", ((Pheromone)goal).getStepFrom()+";"+((Pheromone)goal).getType());
           }
           
        }
        
        
        
        if (goal == null)
        {
            explore();
        }
        else{
            followGoal();
        }
     

        /*if (capacity >= MAX_CAPACITY)
        {
            
            homeResearch();
            releasePheromone(PheromoneType.RESSOURCE, stepFrom);
        }
        else
        {
            foodResearch();
            releasePheromone(PheromoneType.HOME, stepFrom);
            
        }*/
        //Ecosystem.getCurrentEcosystem().checkRadial(getX(), getY(), 5, ElementActorType.RESSOURCE);
        //explore();
        
        
        
        


        move();
        
        
        nextCheck--;
        pheromoneCheckCountdown--;
        pheromoneCountdown--;
        stepFrom++;
        //Gdx.app.log("nbSteps", "" + stepFrom);
    }

    public void tryCollectFood()
    {
        Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();
        if (ecosystem.isElement(getX(), getY(), ElementActorType.RESSOURCE) != null) {
            capacity += ecosystem.takeResource(getX(), getY(), MAX_CAPACITY - capacity);
            followedPheromone = null;
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

    public ElementActor searchPheromone(PheromoneType type)
    {
        Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();

        Pheromone res = ecosystem.checkRadialPheromone(getX(), getY(), FIELD_OF_VIEW, type);


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

    public void changeGoal(ElementActor newGoal)
    {
        goal = newGoal;
        goalIsPassed = false;
    }

   /* public void foodResearch()
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
            if (nextCheck < 0) {
                followPheromone(ElementActorType.FOOD_PHEROMONE);
                nextCheck = NEXT_CHECK_COUNTDOWN;
            }
            else
            {
                explore();
            }
            
            
        }
        tryCollectFood();
    }

    public void homeResearch()
    {
        Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();

            Vector2 res = ecosystem.checkRadial(getX(), getY(), FIELD_OF_VIEW, ElementActorType.ANTHILL);
            
            if (res != null) 
            {
                float newDirection = MathUtils.radiansToDegrees * MathUtils.atan2(res.y - getY(), res.x - getX());
                float deltaDirection = (float)(newDirection - this.direction) % 360f;

                direction = newDirection;
                sprite.rotate(deltaDirection);
            }
            else 
            {
                

                if (nextCheck < 0) {
                    followPheromone(ElementActorType.HOME_PHEROMONE);
                    nextCheck = NEXT_CHECK_COUNTDOWN;
                }
                else
                {
                    explore();
                }
            }
        tryDepositFood();
    }
    */
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
        //setPosition(getX(), getY());
    }
    
    public void releasePheromone(PheromoneType type) {
        Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();
        if (ecosystem.isElement(getX(), getY(), ElementActorType.OBSTACLE) == null)
        {
            if (pheromoneCountdown < 0)
            {
                ecosystem.addPheromone(getX(), getY(), type, this, stepFrom);
                pheromoneCountdown = PHEROMONE_RELEASE_COUNTDOWN;
            }
        }
        
    }
/*
    public void followPheromone(ElementActorType type) {
        Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();
        
        if (followedPheromone != null)
        {
            Pheromone res = ecosystem.checkRadialPheromone(getX(), getY(), FIELD_OF_VIEW, type, followedPheromone);
            //Gdx.app.log("test","dedans");
            if (res != null) 
            {
                followedPheromone = res;                                       
                float newDirection = MathUtils.radiansToDegrees * MathUtils.atan2(res.getY() - getY(), res.getX() - getX());
                float deltaDirection = (float)(newDirection - this.direction);

                direction = newDirection;
                sprite.rotate(deltaDirection);
                
            }
            else 
            {
                //if (fol)
                //followedPheromone = null;
                explore();
            }
        }
        else
        {
            followedPheromone = ecosystem.checkRadialPheromone(getX(), getY(), FIELD_OF_VIEW, type);
            if (followedPheromone == null)
              {
              explore();
              }
        }
        
    }
    */

    @Override
    public String toString()
    {
        return "Fourmi !";
        //return getX().toString();
    }
}
