package ch.hearc.simulanthill.actors;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Asset 
{
    static public AssetManager manager = new AssetManager();
    static private AssetDescriptor<Texture> ant = new AssetDescriptor<Texture>("ant.png", Texture.class);
    static private AssetDescriptor<Texture> anthill = new AssetDescriptor<Texture>("anthill.png", Texture.class);
    static private AssetDescriptor<Texture> obstacle = new AssetDescriptor<Texture>("obstacle.png", Texture.class);
    static private AssetDescriptor<Texture> resource = new AssetDescriptor<Texture>("resource.png", Texture.class);
    static private AssetDescriptor<Texture> homePheromone = new AssetDescriptor<Texture>("homePheromone.png", Texture.class);
    static private AssetDescriptor<Texture> foodPheromone = new AssetDescriptor<Texture>("foodPheromone.png", Texture.class);
    static private AssetDescriptor<Texture> marker = new AssetDescriptor<Texture>("marker.png", Texture.class);
    static public void loadAssets() 
    {
		manager.load(Asset.ant);
        manager.load(Asset.anthill);
        manager.load(Asset.obstacle);
        manager.load(Asset.resource);
        manager.load(Asset.homePheromone);
        manager.load(Asset.foodPheromone);
        manager.load(Asset.marker);
		manager.finishLoading();
	}

    static public Texture ant()
    {
        return manager.get(ant);
    }

    static public Texture anthill()
    {
        return manager.get(anthill);
    }

    static public Texture obstacle()
    {
        return manager.get(obstacle);
    }

    static public Texture resource()
    {
        return manager.get(resource);
    }

    static public Texture homePheromone()
    {
        return manager.get(homePheromone);
    }

    static public Texture foodPheromone()
    {
        return manager.get(foodPheromone);
    }
    static public Texture marker()
    {
        return manager.get(marker);
    }
}
