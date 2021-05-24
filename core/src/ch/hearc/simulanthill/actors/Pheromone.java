package ch.hearc.simulanthill.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.bullet.softbody.btSoftBody.Element;
import com.badlogic.gdx.utils.Disposable;

import ch.hearc.simulanthill.Ecosystem;

public class Pheromone extends ElementActor{
    
    private static int lifeTimeInit = 300;
	private int lifeTime;
	private PheromoneType pheromoneType;
	private double range;

	public Pheromone(float _posX, float _posY, PheromoneType _type)
	{
		super(_posX, _posY, Asset.resource(), "pheromone");
		this.lifeTime = lifeTimeInit;
		this.pheromoneType = _type;
		this.range = 20;
		setSize(8, 8);

	}

	public void setLifetimeInit(int _lifeTimeInit)
	{
		lifeTimeInit = _lifeTimeInit;
	}

	public void decreaseLifeTime()
	{
		this.lifeTime--;
		if (lifeTime <= 0) {
			remove();
		}

		sprite.setAlpha((float)lifeTime / lifeTimeInit);
	}

	public PheromoneType getType() {
		return pheromoneType;
	}

	//We don't need any id in Obstacles
	public int getId()
	{
		return -1;
	}

	public void act(float _delta) {
		super.act(_delta);
		decreaseLifeTime();
	}

	@Override
	public boolean remove() {
		Ecosystem.getCurrentEcosystem().removePheromone(this);
		return super.remove();
	}

}
