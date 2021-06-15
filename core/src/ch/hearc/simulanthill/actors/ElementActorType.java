package ch.hearc.simulanthill.actors;

/**
 * Enum that gives the type of ElementActor
 */
public enum ElementActorType 
{
    OBSTACLE(0), RESSOURCE(1), ANTHILL(2), HOME_PHEROMONE(3), FOOD_PHEROMONE(4), DANGER_PHEROMONE(5) ;
    
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
