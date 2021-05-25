package ch.hearc.simulanthill.actors;

import ch.hearc.simulanthill.Ecosystem;

public class Pheromone extends ElementActor {
    
    private static int INIT_LIFE_TIME = 500;
	private int lifeTime;
	private PheromoneType pheromoneType;
	private int power;
	private int maxLifeTime;

	public Pheromone(float _posX, float _posY, PheromoneType _type)
	{
		super(_posX, _posY, (_type == PheromoneType.HOME ? Asset.homePheromone() : Asset.foodPheromone()), "pheromone");

		this.lifeTime = INIT_LIFE_TIME;
		this.maxLifeTime = INIT_LIFE_TIME;
		this.pheromoneType = _type;
		this.power = 1;
		setSize(4, 4);
	}

	public void setLifetimeInit(int _lifeTimeInit)
	{
		INIT_LIFE_TIME = _lifeTimeInit;
	}

	public void decreaseLifeTime()
	{
		this.lifeTime--;
		if (lifeTime <= 0) {
			remove();
		}
		sprite.setAlpha((float)lifeTime / maxLifeTime);
	}

	public PheromoneType getType() {
		return pheromoneType;
	}

	//We don't need any id in Obstacles
	public int getId()
	{
		return -1;
	}

	public int getPower() {
		return power;
	}

	public void act(float _delta) {
		super.act(_delta);
		decreaseLifeTime();
	}

	public void reinforce() {
		lifeTime += 100;
		maxLifeTime = lifeTime;
		power *= 2;
	}

	@Override
	public boolean remove() {
		Ecosystem.getCurrentEcosystem().removePheromone(getX(), getY(), pheromoneType);
		return super.remove();
	}

}
