package ch.hearc.simulanthill;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;

import ch.hearc.simulanthill.actors.Asset;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import java.awt.*;

public class SimulathillScreen implements Screen 
{
	private final Game game;
	private Ecosystem ecosystem;
	private Viewport ecosystemViewport;
	private Viewport GUIViewport;
	GUI gui;
   
	public SimulathillScreen(Game _game) 
	{
		this.game = _game;
		//manager = new AssetManager();
	}
  
	@Override
	public void show()
	{
		VisUI.load();
		Asset.loadAssets();
		
		InputMultiplexer multiPlexer = new InputMultiplexer();

		GUIViewport = new FitViewport(1600,900);
		ecosystemViewport = new FitViewport(1600,900);

		ecosystem = Ecosystem.getInstance(ecosystemViewport);

		//ecosystem.loadMap("..\\..\\maps\\testmap1.txt");
		ecosystem.loadMap();

		gui = new GUI(GUIViewport); 

		multiPlexer.addProcessor(gui);
		multiPlexer.addProcessor(ecosystem);
   
		Gdx.input.setInputProcessor(multiPlexer);
	}
   

	@Override
	public void render(float _delta)
	{

		Gdx.gl.glClearColor(0.12f, 0.12f, 0.12f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		gui.getViewport().apply();
		gui.act(Gdx.graphics.getDeltaTime());
		gui.draw();

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
	}
   
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
		dispose();
	}
   
	@Override
	public void dispose()
	{
		ecosystem.dispose();
		gui.dispose();
	}
}
