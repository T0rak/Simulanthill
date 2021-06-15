package ch.hearc.simulanthill.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ch.hearc.simulanthill.Simulanthill;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1600;
		config.height = 900;
		config.addIcon("icon32.png", Files.FileType.Internal);
		new LwjglApplication(new Simulanthill(), config);
	}
}
