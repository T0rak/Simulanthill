package ch.hearc.simulanthill.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

import ch.hearc.simulanthill.Simulanthill;
import ch.hearc.simulanthill.tools.Asset;

/**
 * Gives information about the institution and students that worked on the simulator
 */
public class AboutScreen implements Screen
{
    private final Game game;
    private Stage stage;
    private VisTable lytMain;
    private VisImage logo;
    private VisImage icon;
    private VisLabel authors;
    private VisLabel allRightsReserved;
    private float time;

    /**
     * Constructor
     * @param _game handler of the content of AboutScreen
     */
	public AboutScreen(Game _game) 
	{
        this.game = _game;
	}

    /**
     * Will load the screen on the programm.
     */
    @Override
    public void show() 
    {		
        stage = new Stage(new FitViewport(1600, 900));
		Gdx.input.setInputProcessor(stage);

        logo = new VisImage(new Image(Asset.logo()).getDrawable());
        icon = new VisImage(new Image(Asset.icon()).getDrawable());
        authors = new VisLabel("Luca Campana, Thierry Flück et Tania Tripiciano");
        allRightsReserved = new VisLabel("Tout droits reservés");

        lytMain = new VisTable();

        lytMain.add(icon).pad(10f);
        lytMain.row();
        lytMain.add(logo).pad(10f);
        lytMain.row();
        lytMain.add(authors).pad(10f);
        lytMain.row();
        lytMain.add(allRightsReserved).pad(10f);

        //lytMain.setDebug(true);

        lytMain.setFillParent(true);

		stage.addActor(lytMain);
        time = 0;
        
    }

    /**
     * Function that is called each time that it needs to re redrawn.
     * @param _delta interval of each redrawn
     */
    @Override
    public void render(float _delta) 
    {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
        stage.getViewport().apply();
		stage.draw();

        time += _delta;
        if(time > 0)
        {
            time = 0;
            ((Simulanthill)game).displayMainScreen();
        }
    }

    /**
     * Resizes the window.
     * @param _width width wanted
     * @param _height width wanted. 
     */
    @Override
    public void resize(int _width, int _height) 
    {
        stage.getViewport().update(_width, _height);
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
        
    }

    /**
     * Release all source of the object
     */
    @Override
    public void dispose() 
    {
        stage.dispose();
    }

}
