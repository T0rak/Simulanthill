package ch.hearc.simulanthill;

import java.util.Vector;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;

import ch.hearc.simulanthill.actors.Asset;
//import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;

public class Simulation implements Screen {
 
	private final Game game;
	private Ecosystem ecosystem;
	private Viewport viewport;
	private Viewport vp;
	SimInterface sm;
   
	public Simulation(Game game) {
		this.game = game;
		//manager = new AssetManager();
	}
  
	@Override
	public void show() {
		VisUI.load();
		Asset.loadAssets();
		
		InputMultiplexer multiPlexer = new InputMultiplexer();

		vp = new FitViewport(1600,900);
		viewport = new FitViewport(1600,900);

	
		ecosystem = Ecosystem.getInstance(viewport);
		ecosystem.loadMap("..\\..\\maps\\testmap1.txt", 1600, 900);
		sm = new SimInterface(vp); 


		multiPlexer.addProcessor(sm);
		multiPlexer.addProcessor(ecosystem);
   
		Gdx.input.setInputProcessor(multiPlexer);

		System.out.println(System.getProperty("user.dir"));
	}
   
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(100, 100, 100, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		sm.getViewport().apply();
		sm.act();
		sm.draw();

		if(viewport.getScreenX() == 0)
		{
			Vector2 v = sm.simulation.localToStageCoordinates(new Vector2(0,0));
			Vector2 v2 = sm.stageToScreenCoordinates(v);

			viewport.update(1600/2, 900/2);
			viewport.setScreenPosition((int)(v2.x), 900-(int)(v2.y));
		}

		ecosystem.getViewport().apply();
		ecosystem.act(Gdx.graphics.getDeltaTime());
		ecosystem.draw();

	
	}
   
	@Override
	public void resize(int width, int height) {

		vp.update(width, height);
		//sm.getViewport().apply();
		Vector2 v = sm.simulation.localToStageCoordinates(new Vector2(0,0));
		Vector2 v2 = sm.stageToScreenCoordinates(v);

		viewport.update(width/2, height/2);
		viewport.setScreenPosition((int)(v2.x), height-(int)(v2.y));

		
	}
   
	@Override
	public void pause() {
   
	}
   
	@Override
	public void resume() {
   
	}
   
	@Override
	public void hide() {
		dispose();
	}
   
	@Override
	public void dispose() {
		ecosystem.dispose();
	}
   
	/*private void loadAssets() {
		//manager.load(Asset.ant);
		manager.load(new AssetDescriptor<Texture>("badlogic.jpg", Texture.class));
   
		manager.finishLoading();
	}*/
  }