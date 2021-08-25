package ch.hearc.simulanthill;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.kotcrab.vis.ui.VisUI;

/**
 * Is the class that handle the whole simulation.
 */
public class Simulanthill extends Game
{
    private Screen aboutScreen;
    private Screen simulanthillScreen;
    private Screen mapDimensionsScreen;
 
    /**
     * Launches the creation process. Loads the main screen.
     */
    @Override
    public void create() 
    {
        Asset.loadAssets();
        VisUI.load();

        aboutScreen = new AboutScreen(this);
        simulanthillScreen = new SimulathillScreen(this);
        mapDimensionsScreen = new MapDimensionsScreen(this);

        displayAboutScreen();
    }

    public void displayAboutScreen() {
        setScreen(aboutScreen);
    }

    public void displaySimulanthillScreen() {
        setScreen(simulanthillScreen);
    }

    public void displayMapDimensionsScreen() {
        setScreen(mapDimensionsScreen);
    }
}
