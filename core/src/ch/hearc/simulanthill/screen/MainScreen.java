package ch.hearc.simulanthill.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.viewport.Viewport;

import ch.hearc.simulanthill.ecosystem.Ecosystem;
import ch.hearc.simulanthill.ecosystem.actors.Ant;
import ch.hearc.simulanthill.ecosystem.actors.Anthill;
import ch.hearc.simulanthill.ecosystem.actors.ElementActorType;
import ch.hearc.simulanthill.screen.gui.EcosystemGUI;
import ch.hearc.simulanthill.screen.gui.GUI;
import ch.hearc.simulanthill.screen.gui.SelectionAnthillListener;
import ch.hearc.simulanthill.screen.gui.SelectionTypeOfAddListener;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;

/**
 * Class of the screen that handles the simulator. 
 */
public class MainScreen implements Screen 
{
	private final Game game;
	private Ecosystem ecosystem;
	private EcosystemGUI guiEco;
	private Viewport ecosystemViewport;
	private Viewport GUIViewport;
	private InputMultiplexer multiPlexer;
	private GUI gui;
   
    /**
     * Constructor
     * @param _game handler of the content of AboutScreen
     */
	public MainScreen(Game _game) 
	{
		this.game = _game;
		//manager = new AssetManager();
		// multiple stage
		multiPlexer = new InputMultiplexer();

		GUIViewport = new FitViewport(1600,900);
		ecosystemViewport = new FitViewport(1600,900);

		ecosystem = Ecosystem.getInstance(ecosystemViewport);
		Ecosystem.getCurrentEcosystem().loadMap(124, 70);
		//ecosystem.loadMap(124, 70);

		gui = new GUI(GUIViewport, game); 

		guiEco = new EcosystemGUI(ecosystem);

		guiEco.addListener(new SelectionAnthillListener(){

			@Override
			public void selected(Anthill _anthill) {
				gui.selectAnthill(_anthill);
			}

			@Override
			public void unselected() {
				gui.unselectAnthill();
				
			}
			
		});

		gui.addListener(new SelectionTypeOfAddListener(){

			@Override
			public void changed(ElementActorType _type) {
				guiEco.changeTypeOfAdd(_type);
			}
			
		});

		

			
	}
  
    /**
     * Will load the screen on the programm.
     */
	@Override
	public void show()
	{
		multiPlexer.addProcessor(gui);
		multiPlexer.addProcessor(ecosystem);
		multiPlexer.addProcessor(guiEco);
	
		Gdx.input.setInputProcessor(multiPlexer);
	}
   
    /**
     * Function that is called each time that it needs to re redraw.
     * @param _delta interval of each redraw
     */
	@Override
	public void render(float _delta)
	{

		Gdx.gl.glClearColor(0.12f, 0.12f, 0.12f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		// for the first render
		if(ecosystemViewport.getScreenX() == 0)
		{
			Vector2 v = gui.simulation.localToStageCoordinates(new Vector2(0,0));
			Vector2 v2 = gui.stageToScreenCoordinates(v);

			ecosystemViewport.update((int)(1600/1.25), (int)(900/1.25));
			ecosystemViewport.setScreenPosition((int)(v2.x), 900-(int)(v2.y));
		}

		ecosystem.getViewport().apply();
		
		if (ecosystem.getIsPlaying())
		{
			ecosystem.act(/*Gdx.graphics.getDeltaTime()*/);
		}
		ecosystem.draw();
		guiEco.act();
		guiEco.draw();	

		gui.getViewport().apply();
		gui.act(Gdx.graphics.getDeltaTime());
		gui.draw();
	}
   
    /**
     * Resizes the window.
     * @param _width width wanted.
     * @param _height width wanted. 
     */
	@Override
	public void resize(int _width, int _height)
	{
		GUIViewport.update(_width, _height);

		Vector2 v = gui.simulation.localToStageCoordinates(new Vector2(0,0));
		Vector2 v2 = gui.stageToScreenCoordinates(v);

		ecosystemViewport.update((int)(_width/1.25), (int)(_height/1.25));
		ecosystemViewport.setScreenPosition((int)(v2.x), _height-(int)(v2.y));
	}
   
	@Override
	public void pause() 
	{
   
	}
   
	@Override
	public void resume()
	{

	}
   
	@Override
	public void hide()
	{
		//dispose();
	}
   
    /**
     * Release all source of the object
     */
	@Override
	public void dispose()
	{
		ecosystem.dispose();
		gui.dispose();
	}
}
