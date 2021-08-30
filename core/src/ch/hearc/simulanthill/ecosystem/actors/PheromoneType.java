package ch.hearc.simulanthill.ecosystem.actors;

/**
 * Enum that helps determinating the pheromone type
 */
public enum PheromoneType 
{
	RESSOURCE(0),
	HOME(1);

	private final int value;

    /**
     * Defines the elementActor type
     * @param _value the value wanted to assign the type
     */
    private PheromoneType(int _value) 
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
