package ch.hearc.simulanthill.actors;

import ch.hearc.simulanthill.Ecosystem;

public class Pheromone extends ElementActor {
    
    private static int INIT_LIFE_TIME = 500;
	private int lifeTime;
	private PheromoneType pheromoneType;
	private int power;
	private int maxLifeTime;
	private Ant ant;

	public Pheromone(float _posX, float _posY, PheromoneType _type, Ant _ant)
	{
		super(_posX, _posY, (_type == PheromoneType.HOME ? Asset.homePheromone() : Asset.foodPheromone()), "pheromone");
		ant = _ant;
		lifeTime = INIT_LIFE_TIME;
		maxLifeTime = INIT_LIFE_TIME;
		pheromoneType = _type;
		power = 1;
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
		sprite.setAlpha(0/*(float)lifeTime / maxLifeTime*/);
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
		lifeTime += 5;
		maxLifeTime = lifeTime;
		power *= 2;
	}
	
	public Ant getAnt() {
		return ant;
	}
	
	public int getLifeTime() {
		return lifeTime;
	}

	@Override
	public boolean remove() {
		Ecosystem.getCurrentEcosystem().removePheromone(getX(), getY(), pheromoneType);
		return super.remove();
	}

}
