package ch.hearc.simulanthill.ecosystem.actors;

/**
 * Enum that gives the type of ElementActor
 */
public enum ElementActorType 
{
    NONE(-1), OBSTACLE(0), RESOURCE(1), ANTHILL(2), HOME_PHEROMONE(3), FOOD_PHEROMONE(4), ANT(5);
    
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

    public static ElementActorType fromValue(int i)
    {
        switch (i) {
            case -1:
                return NONE;
            case 0:
                return OBSTACLE;
            case 1:
                return RESOURCE;
            case 2:
                return ANTHILL;
            case 3:
                return HOME_PHEROMONE;
            case 4:
                return FOOD_PHEROMONE;
            case 5:
                return ANT;
            default:
                return null;
        }
    }
}
