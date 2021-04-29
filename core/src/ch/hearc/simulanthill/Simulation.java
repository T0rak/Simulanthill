package ch.hearc.simulanthill;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;

import ch.hearc.simulanthill.actors.Ant;
import ch.hearc.simulanthill.actors.Anthill;
import ch.hearc.simulanthill.actors.Asset;
import ch.hearc.simulanthill.actors.ElementActor;
import ch.hearc.simulanthill.actors.Obstacle;
import ch.hearc.simulanthill.actors.Resource;
import ch.hearc.simulanthill.map.MapConvertor;

//import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;

public class Simulation implements Screen {
 
	private final Game game;
	private Stage stage;
	private Viewport viewport;
   
	public Simulation(Game game) {
		this.game = game;
		//manager = new AssetManager();
	}
  
	@Override
	public void show() {
		VisUI.load();
		viewport = new FitViewport(1600, 900);
	
		stage = new Stage(viewport);
   
		Gdx.input.setInputProcessor(stage);
   
		//loadAssets();
		Asset.loadAssets();

		int nbAnts = 5000;
		Ant tab[] = new Ant[nbAnts];
		
		for (int i = 0; i < nbAnts; i++) {
			tab[i] = new Ant(MathUtils.random((float)Gdx.graphics.getWidth()), MathUtils.random((float)Gdx.graphics.getHeight()), 12, 12);
		}

		for (int i = 0; i < nbAnts; i++) {
			stage.addActor(tab[i]);
		}

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
		MapConvertor map = new MapConvertor(13, "..\\..\\maps\\testmap1.txt");

		for (ElementActor element : map.getActorList()) {
			stage.addActor(element);
		}

	}
   
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(250, 250, 250, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();

	}
   
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
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
		stage.dispose();
	}
   
	/*private void loadAssets() {
		//manager.load(Asset.ant);
		manager.load(new AssetDescriptor<Texture>("badlogic.jpg", Texture.class));
   
		manager.finishLoading();
	}*/
  }