package ch.hearc.simulanthill.tools;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;

/**
 * Holds all assets used in Simulanthill.
 * When a new asset has to be used, it has to be imported here.
 */
public class Asset 
{
    static public AssetManager manager = new AssetManager();
    static private AssetDescriptor<Texture> ant = new AssetDescriptor<Texture>("ant.png", Texture.class);
    static private AssetDescriptor<Texture> anthill = new AssetDescriptor<Texture>("anthill.png", Texture.class);
    static private AssetDescriptor<Texture> obstacle = new AssetDescriptor<Texture>("obstacle.png", Texture.class);
    static private AssetDescriptor<Texture> resource = new AssetDescriptor<Texture>("resource.png", Texture.class);
    static private AssetDescriptor<Texture> homePheromone = new AssetDescriptor<Texture>("homePheromone.png", Texture.class);
    static private AssetDescriptor<Texture> foodPheromone = new AssetDescriptor<Texture>("foodPheromone.png", Texture.class);
    static private AssetDescriptor<Texture> antLegend = new AssetDescriptor<Texture>("antLegend.png", Texture.class);
    static private AssetDescriptor<Texture> anthillLegend = new AssetDescriptor<Texture>("anthillLegend.png", Texture.class);
    static private AssetDescriptor<Texture> obstacleLegend = new AssetDescriptor<Texture>("obstacleLegend.png", Texture.class);
    static private AssetDescriptor<Texture> resourceLegend = new AssetDescriptor<Texture>("resourceLegend.png", Texture.class);
    static private AssetDescriptor<Texture> homePheromoneLegend = new AssetDescriptor<Texture>("homePheromoneLegend.png", Texture.class);
    static private AssetDescriptor<Texture> foodPheromoneLegend = new AssetDescriptor<Texture>("foodPheromoneLegend.png", Texture.class);
    static private AssetDescriptor<Texture> backgound = new AssetDescriptor<Texture>("background.png", Texture.class);
    static private AssetDescriptor<Texture> icon = new AssetDescriptor<Texture>("icon.png", Texture.class);
    static private AssetDescriptor<Texture> logo = new AssetDescriptor<Texture>("logo.png", Texture.class);
    static private AssetDescriptor<Texture> anthillSelection = new AssetDescriptor<Texture>("anthillSelection.png", Texture.class);

    static private Map<Color,Texture> antTextures = new HashMap<Color,Texture>();

    /**
     * Loads all the needed assets.
     */
    static public void loadAssets() 
    {
		manager.load(Asset.ant);
        manager.load(Asset.anthill);
        manager.load(Asset.obstacle);
        manager.load(Asset.resource);
        manager.load(Asset.homePheromone);
        manager.load(Asset.foodPheromone);
        manager.load(Asset.backgound);
        manager.load(Asset.antLegend);
        manager.load(Asset.anthillLegend);
        manager.load(Asset.obstacleLegend);
        manager.load(Asset.resourceLegend);
        manager.load(Asset.homePheromoneLegend);
        manager.load(Asset.foodPheromoneLegend);
        manager.load(Asset.icon);
        manager.load(Asset.logo);
        manager.load(Asset.anthillSelection);
		manager.finishLoading();
	}

    static public Texture ant()
    {
        return manager.get(ant);
    }

    static public Texture ant(Color color)
    {
        if(antTextures.containsKey(color))
        {
            return antTextures.get(color);
        }
        else
        {
            Texture txt = manager.get(ant);
            TextureData a = txt.getTextureData();
            a.prepare();
            Pixmap b = a.consumePixmap();
            for (int i = 0; i < b.getWidth(); i++) {
                for (int j = 0; j < b.getHeight(); j++) {
                    b.setColor(color);
                    if(b.getPixel(i, j) != 0)
                    {
                        b.drawPixel(i, j);
                    }
                }
            }
            Texture newTxt = new Texture(b);
            antTextures.put(color, newTxt);
            return newTxt;
        }
        

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

    static public Texture backgound()
    {
        return manager.get(backgound);
    }

    static public Texture antLegend()
    {
        return manager.get(antLegend);
    }

    static public Texture anthillLegend()
    {
        return manager.get(anthillLegend);
    }

    static public Texture obstacleLegend()
    {
        return manager.get(obstacleLegend);
    }

    static public Texture resourceLegend()
    {
        return manager.get(resourceLegend);
    }

    static public Texture homePheromoneLegend()
    {
        return manager.get(homePheromoneLegend);
    }

    static public Texture foodPheromoneLegend()
    {
        return manager.get(foodPheromoneLegend);
    }
    static public Texture icon()
    {
        return manager.get(icon);
    }
    static public Texture logo()
    {
        return manager.get(logo);
    }

    static public Texture anthillSelection()
    {
        return manager.get(anthillSelection);
    }
}
