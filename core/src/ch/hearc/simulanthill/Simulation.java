package ch.hearc.simulanthill;

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
		viewport = new FitViewport(1600, 900);
		//viewport.setScreenPosition(500, 500);
	
		ecosystem = Ecosystem.getInstance(viewport);
		ecosystem.loadMap("..\\..\\maps\\testmap1.txt", 1600, 900);
		sm = new SimInterface(vp); 

		//ecosystem.getCamera().translate(-200, -200, 0);

		multiPlexer.addProcessor(sm);
		multiPlexer.addProcessor(ecosystem);
   
		//Gdx.input.setInputProcessor(ecosystem);
		Gdx.input.setInputProcessor(multiPlexer);
   
		//loadAssets();
		

		
		/*
		Anthill anthill = new Anthill(100,100);
		stage.addActor(anthill);

		Resource test = new Resource(200f, 500f, 70, 70, 100);
		stage.addActor(test);

		Obstacle testObst1 = new Obstacle(600f, 200f, 70, 70);
		stage.addActor(testObst1);

		Obstacle testObst2 = new Obstacle(890f, 121f, 70, 70);
		stage.addActor(testObst2);
		*/

		System.out.println(System.getProperty("user.dir"));

		// Pk le programme se lance dans ASSETS ???? 
		

	}
   
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(250, 250, 250, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//viewport.setScreenPosition(10, 10);
		//viewport.update(1600, 1900, true);
		Gdx.gl.glViewport( 250,240,Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2 );

		//ecosystem.getViewport().apply();
		ecosystem.act(Gdx.graphics.getDeltaTime());
		ecosystem.draw();

		//Gdx.gl.glViewport( 0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight() );
		sm.getViewport().apply();
		sm.act();
		sm.draw();

	}
   
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		vp.update(width, height);
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