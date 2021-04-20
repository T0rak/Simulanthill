package ch.hearc.simulanthill;

public class Pheromone extends Element {

	private static int lifeTimeInit;
	private int lifeTime;
	private PheromoneType pheromoneType;
	private double range;

	public Pheromone(int _posX, int _posY) 
	{
		//TODO : surround with synchonized in case of Threads ?
		//TODO : define values from panel
		this.lifeTime = lifeTimeInit;
		this.pheromoneType = PheromoneType.HOME;
		this.range = 20;
		this.posX = _posX;
		this.posY = _posY;
	}

	public void setLifetimeInit(int _lifeTimeInit)
	{
		lifeTimeInit = _lifeTimeInit;
	}

	public void decreaseLifeTime()
	{
		this.lifeTime--;
	}

	//We don't need any id in Obstacles
	public int getId()
	{
		return -1;
	};

}
