package ch.hearc.simulanthill.actors;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import ch.hearc.simulanthill.Ecosystem;

public class Ant extends ElementActor
{
    private static int PHEROMONE_COUNTDOWN = 20;
    private float direction;
    private int viewSpanAngle;
	private int capacity;
	private static double speed = 1;
    private Anthill anthill;
    private int nextCheck;
    private int pheromoneCountdown;
    
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
        pheromoneCountdown = PHEROMONE_COUNTDOWN;
        nextCheck = 150;
    }

    @Override
    public void act(float delta)
    {
        super.act(delta);
        if (nextCheck == 0) {
            nextCheck = 100;
            foodResearch();
        } else {
            explore();
        }
        move();
        nextCheck--;
        
        pheromoneCountdown--;
        if (pheromoneCountdown < 0) {
            releasePheromone(PheromoneType.HOME);
            pheromoneCountdown = PHEROMONE_COUNTDOWN;
        }

    }

    public void collectFood()
    {
        
    }

    public void foodResearch()
    {
        //trouver nourriture
        Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();
        Vector2 res =  ecosystem.checkRessourceAround(getX(), getY(), 5);
        
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
        //trouver phéromone nourriture
        
        //rien

    }

    public void homeResearch()
    {
        explore();
    }

    public void explore()
    {
        float deltaDirection = MathUtils.random(this.viewSpanAngle) - this.viewSpanAngle / 2f;
        this.direction = (float)(this.direction + deltaDirection) % 360f;
        this.sprite.rotate(deltaDirection);
    }

    public void move()
    {
    
        float directionRad = (float) Math.toRadians(this.direction);
        float nextPosX = (float) (getX() + MathUtils.cos(directionRad) * Ant.speed);
        float nextPosY = (float) (getY() + MathUtils.sin(directionRad) * Ant.speed);

        Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();
        
        if (ecosystem.isObstacle(nextPosX, nextPosY))
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
    
    public void releasePheromone(PheromoneType type) {
        Ecosystem.getCurrentEcosystem().addPheromone(getX(), getY(), type);
    }

    @Override
    public String toString()
    {
        return "Fourmi !";
        //return getX().toString();
    }
}
