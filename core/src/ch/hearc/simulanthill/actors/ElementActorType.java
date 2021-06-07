package ch.hearc.simulanthill.actors;

public enum ElementActorType 
{
    OBSTACLE(0), RESSOURCE(1), ANTHILL(2), HOME_PHEROMONE(3), FOOD_PHEROMONE(4), DANGER_PHEROMONE(5) ;
    
    private final int value;

    private ElementActorType(int _value) 
    {
        this.value = _value;
    }

    public int getValue() 
    {
        return value;
    }
}
