package ch.hearc.simulanthill;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;


public class SimulanthillScreen implements Screen {
 
	private final Game game;
	private Stage stage;
	private Viewport viewport;
	private final AssetManager manager;
   
	public SimulanthillScreen(Game game) {
	  this.game = game;
	  manager = new AssetManager();
	}
  
	@Override
	public void show() {
	  viewport = new FitViewport(1080, 720);
	  stage = new Stage(viewport);
   
	  Gdx.input.setInputProcessor(stage);
   
	  loadAssets();

	  MyActor myActor1 = new MyActor(new Texture("ant.png") , "ant");
	  myActor1.spritePos(200, 200);
	  MyActor myActor2 = new MyActor(new Texture("badlogic.jpg") , "bad");
	  myActor2.spritePos(700, 600);
   
	  stage.addActor(myActor1);
	  stage.addActor(myActor2);
	}
   
	@Override
	public void render(float delta) {
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
   
	private void loadAssets() {
	  manager.load(new AssetDescriptor<Texture>("ant.png", Texture.class));
	  manager.load(new AssetDescriptor<Texture>("badlogic.jpg", Texture.class));
   
	  manager.finishLoading();
	}
  }