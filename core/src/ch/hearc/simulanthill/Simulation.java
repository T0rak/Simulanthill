package ch.hearc.simulanthill;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;

import ch.hearc.simulanthill.model.ant.Ant;

//import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;


public class Simulation implements Screen {
 
	private final Game game;
	private Stage stage;
	private Viewport viewport;
	//private final AssetManager manager;
	private AntActor antActor;
   
	public Simulation(Game game) {
		this.game = game;
		//manager = new AssetManager();
	}
  
	@Override
	public void show() {
		VisUI.load();
		viewport = new FitViewport(1600, 900);
		
		//stage = new SimInterface(viewport);
		stage = new Stage(viewport);
   
		Gdx.input.setInputProcessor(stage);
   
		//loadAssets();
		Asset.loadAssets();
		//Ant ant = new Ant(200,0);
		//antActor = new AntActor();
		//ant.spritePos(0, 0);
		//ElementActor myActor2 = new ElementActor(new Texture("badlogic.jpg") , "bad");
		//myActor2.spritePos(0, 0);
		
		AntActor tab[] = new AntActor[20000];
		for (int i = 0; i < 20000; i++) {
			tab[i] = new AntActor();
		}


		for (int i = 0; i < 20000; i++) {
			stage.addActor(tab[i]);
		}
		//stage.addActor(antActor);
		//stage.addActor(myActor2);
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