package ch.hearc.simulanthill;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class Simulanthill extends Game
{

    @Override
    public void create() {
        Screen screen = new SimulanthillScreen(this);
        setScreen(screen);
        
    }
    
}
