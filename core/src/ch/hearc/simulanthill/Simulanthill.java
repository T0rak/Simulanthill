package ch.hearc.simulanthill;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.kotcrab.vis.ui.VisUI;

public class Simulanthill extends ApplicationAdapter {

	private SimInterface stage;
	private Texture antImage;
	private OrthographicCamera camera;
   	private SpriteBatch batch;
	private Array<Rectangle> raindrops;
	private Rectangle bucket;

	private float windowWidth;
	private float windowHeight;

	private long lastDropTime;
	
	@Override
	public void create() {

		VisUI.load();
		stage = new SimInterface();
		Gdx.input.setInputProcessor(stage);

		windowWidth = 800;
		windowHeight = 480;	

		antImage = new Texture("ant.png");

		camera = new OrthographicCamera();
		camera.setToOrtho(false, windowWidth, windowHeight);

		batch = new SpriteBatch();

		bucket = new Rectangle(windowWidth/2-50,0,100,100);

		raindrops = new Array<Rectangle>();
   		spawnRaindrop();

	}

	@Override
	public void render() {
		ScreenUtils.clear(250, 250, 250, 1);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(antImage, bucket.x, bucket.y, bucket.width, bucket.height);
		for (Rectangle drop : raindrops) {
			batch.draw(antImage, drop.x, drop.y, drop.width, drop.height);
		}
		batch.end();

		if(Gdx.input.isTouched())
		{
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucket.x = touchPos.x - 64 / 2;
		}

		if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
		{
			bucket.x -= 400 * Gdx.graphics.getDeltaTime();
		}

		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
		{
			bucket.x += 400 * Gdx.graphics.getDeltaTime();
		}

		if(bucket.x < 0)
		{
			bucket.x = 0;
		}

		if(bucket.x > windowWidth - bucket.width)
		{
			bucket.x = windowWidth - bucket.width;
		}

		for (Iterator<Rectangle> iter = raindrops.iterator(); iter.hasNext(); ) 
		{
			Rectangle drop = iter.next();
			drop.y -= 100 * Gdx.graphics.getDeltaTime();
			if(drop.y < - drop.height/2 || (bucket.overlaps(drop) && drop.y < bucket.y + bucket.height * 2/3))
			{
				iter.remove();
			}
		}

		if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();


		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		

	}

	private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, 800-64);
		raindrop.y = 480;
		raindrop.width = 64;
		raindrop.height = 64;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	 }
	
	@Override
	public void dispose() {
		antImage.dispose();
		antImage.dispose();
		batch.dispose();
		stage.dispose();
		VisUI.dispose();
	}
}
