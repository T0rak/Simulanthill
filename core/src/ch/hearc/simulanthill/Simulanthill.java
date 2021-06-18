package ch.hearc.simulanthill;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.kotcrab.vis.ui.VisUI;

/**
 * Is the class that handle the whole simulation.
 */
public class Simulanthill extends Game
{

    /**
     * Launches the creation process. Loads the main screen.
     */
    @Override
    public void create() 
    {
        Asset.loadAssets();
        VisUI.load();
        //Screen screen = new SimulathillScreen(this);
        Screen screen = new AboutScreen(this);
        setScreen(screen);
        
    }
    
}
