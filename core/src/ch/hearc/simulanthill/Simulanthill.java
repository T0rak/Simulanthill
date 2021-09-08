package ch.hearc.simulanthill;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.kotcrab.vis.ui.VisUI;

import ch.hearc.simulanthill.screen.AboutScreen;
import ch.hearc.simulanthill.screen.MapDimensionsScreen;
import ch.hearc.simulanthill.screen.MapSelectionScreen;
import ch.hearc.simulanthill.tools.Asset;
import ch.hearc.simulanthill.screen.MainScreen;

/**
 * Is the class that handle the whole simulation.
 */
public class Simulanthill extends Game
{
    private Screen aboutScreen;
    private Screen mainScreen;
    private Screen mapDimensionsScreen;
    private Screen mapSelectionScreen;
    /**
     * Launches the creation process. Loads the main screen.
     */
    @Override
    public void create() 
    {
        Asset.loadAssets();
        VisUI.load();

        aboutScreen = new AboutScreen(this);
        mainScreen = new MainScreen(this);
        mapDimensionsScreen = new MapDimensionsScreen(this);
        mapSelectionScreen = new MapSelectionScreen(this);

        displayAboutScreen();
    }

    public void displayAboutScreen() {
        setScreen(aboutScreen);
    }

    public void displayMainScreen() {
        setScreen(mainScreen);
    }

    public void displayMapDimensionsScreen() {
        setScreen(mapDimensionsScreen);
    }
    public void displayMapSelectionScreen() {
        setScreen(mapSelectionScreen);
    }
}
