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
    static private AssetDescriptor<Texture> icon = new AssetDescriptor<Texture>("icon.png", Texture.class);
    static private AssetDescriptor<Texture> logo = new AssetDescriptor<Texture>("logo.png", Texture.class);
    static private AssetDescriptor<Texture> anthillSelection = new AssetDescriptor<Texture>("anthillSelection.png", Texture.class);

    static private AssetDescriptor<Texture> play = new AssetDescriptor<Texture>("play.png", Texture.class);
    static private AssetDescriptor<Texture> pause = new AssetDescriptor<Texture>("pause.png", Texture.class);

    static private Map<Color,Texture> ants = new HashMap<Color,Texture>();
    static private Map<Color, Texture> anthillSelections = new HashMap<Color,Texture>();
    static private Map<Color, Texture> pixels = new HashMap<Color,Texture>();

    /**
     * Loads all the needed assets.
     */
    static public void loadAssets() 
    {
		manager.load(Asset.ant);
        manager.load(Asset.anthill);
        manager.load(Asset.icon);
        manager.load(Asset.logo);
        manager.load(Asset.anthillSelection);
        manager.load(Asset.play);
        manager.load(Asset.pause);
		manager.finishLoading();
	}

    static public Texture ant()
    {
        return manager.get(ant);
    }

    static public Texture ant(Color color)
    {
        if(ants.containsKey(color))
        {
            return ants.get(color);
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
            ants.put(color, newTxt);
            return newTxt;
        }
        

    }

    static public Texture anthill()
    {
        return manager.get(anthill);
    }

    static public Texture pixel(Color _color)
    {
        if(pixels.containsKey(_color))
        {
            return pixels.get(_color);
        }
        else
        {
            
            Pixmap bgPixmap = new Pixmap(100, 100, Pixmap.Format.RGBA8888);
            bgPixmap.setColor(_color);
            bgPixmap.fill();
            Texture newTxt = new Texture(bgPixmap);
            pixels.put(_color, newTxt);
            return newTxt;
        }
    }

    static public Texture icon()
    {
        return manager.get(icon);
    }
    static public Texture logo()
    {
        return manager.get(logo);
    }

    static public Texture play()
    {
        return manager.get(play);
    }

    static public Texture pause()
    {
        return manager.get(pause);
    }

    static public Texture anthillSelection(Color color)
    {
        if(anthillSelections.containsKey(color))
        {
            return anthillSelections.get(color);
        }
        else
        {
            Texture txt = manager.get(anthillSelection);
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
            anthillSelections.put(color, newTxt);
            return newTxt;
        }
    }
}
