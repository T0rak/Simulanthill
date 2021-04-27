package ch.hearc.simulanthill.model.ant;

import java.util.Random;

public class AntBehaviour
{
    private int view_span_angle;
    private State state;
    private int direction;
    private int speed;

    public AntBehaviour(int _speed)
    {
        this.view_span_angle = 15; //default value

        this.state = (State) new ExploreState();   //Was it this that was intended ? Each State inherits from State ? 

        Random rand = new Random(360);
        this.direction  = rand.nextInt();
        this.speed = _speed;
    }

    public void check()
    {
        /*switch(this.state)
        {
            
        }*/
    }




}