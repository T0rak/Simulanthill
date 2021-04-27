package ch.hearc.simulanthill;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Asset {
    static public AssetManager manager = new AssetManager();
    static private AssetDescriptor<Texture> ant = new AssetDescriptor<Texture>("ant.png", Texture.class);
    static public void loadAssets() {
		manager.load(Asset.ant);
		//manager.load(new AssetDescriptor<Texture>("badlogic.jpg", Texture.class));
		manager.finishLoading();
	}

    static public Texture ant()
    {
        return manager.get(ant);
    }

}
