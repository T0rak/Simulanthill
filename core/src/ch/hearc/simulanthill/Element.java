package ch.hearc.simulanthill;

public abstract class Element {

	protected int posX;
	protected int posY;
	protected static int idGen;

	// pure virtual method
	public abstract int getId();

	public int getX() 
	{
		return posX;
	}

	public int getY() 
	{
		return posY;
	}

}
