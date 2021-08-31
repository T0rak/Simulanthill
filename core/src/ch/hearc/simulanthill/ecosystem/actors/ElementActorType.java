package ch.hearc.simulanthill.ecosystem.actors;

/**
 * Enum that gives the type of ElementActor
 */
public enum ElementActorType 
{
    NONE(0), OBSTACLE(1), RESOURCE(2), ANTHILL(3), HOME_PHEROMONE(4), FOOD_PHEROMONE(5), ANT(6);
    
    private final int value;

    /**
     * Defines the elementActor type
     * @param _value the value wanted to assign the type
     */
    private ElementActorType(int _value) 
    {
        this.value = _value;
    }

    /**
     * getter of the type
     * @return the value of the type
     */
    public int getValue() 
    {
        return value;
    }
}
