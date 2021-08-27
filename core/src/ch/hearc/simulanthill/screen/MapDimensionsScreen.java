package ch.hearc.simulanthill.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.spinner.Spinner;

import ch.hearc.simulanthill.Ecosystem;
import ch.hearc.simulanthill.Simulanthill;
import ch.hearc.simulanthill.actors.Ant;

import com.kotcrab.vis.ui.widget.spinner.IntSpinnerModel;

/**
 * Gives information about the institution and students that worked on the simulator
 */
public class MapDimensionsScreen implements Screen
{
    private final Game game;
    private Stage stage;
    private VisTable lytMain;

    private VisLabel lblDimensions;
    
    private Spinner spinWidth;
    private Spinner spinHeight;

    private VisTextButton btnValidate;
	private VisTextButton btnCancel;

    private static final int DEFAULT_WIDTH = 70;
    private static final int DEFAULT_HEIGHT = 70;
    private static final int MIN_WIDTH = 15;
    private static final int MIN_HEIGHT = 15;
    private static final int MAX_WIDTH = 150;
    private static final int MAX_HEIGHT = 150;
    /**
     * Constructor
     * @param _game handler of the content of AboutScreen
     */
	public MapDimensionsScreen(Game _game) 
	{
        this.game = _game;

        stage = new Stage(new FitViewport(1600, 900));

        lytMain = new VisTable();

        lblDimensions = new VisLabel("Dimensions:");

        spinWidth = new Spinner("Width", new IntSpinnerModel(DEFAULT_WIDTH, MIN_WIDTH, MAX_WIDTH));
        spinHeight = new Spinner("Height", new IntSpinnerModel(DEFAULT_HEIGHT, MIN_HEIGHT, MAX_HEIGHT));

        btnValidate = new VisTextButton("Valider");
        btnCancel = new VisTextButton("Annuler");

        lytMain.add(lblDimensions);
        lytMain.row();
        lytMain.add(spinWidth);
        lytMain.row();
        lytMain.add(spinHeight);
        lytMain.row();
        lytMain.add(btnValidate);
        lytMain.add(btnCancel);

        btnCancel.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y) 
                {           
                    ((Simulanthill)MapDimensionsScreen.this.game).displayMainScreen();
                }
            }
        );

        btnValidate.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y) 
                {           
                    Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();
                    if (ecosystem != null)
                    {
                        int newWidth = ((IntSpinnerModel)spinWidth.getModel()).getValue();
                        int newHeight = ((IntSpinnerModel)spinHeight.getModel()).getValue();

                        ecosystem.loadMap(newWidth, newHeight);
                        //Ant.setSpeedFactor(((FloatSpinnerModel)spinAntSpeed.getModel()).getValue().floatValue());
                        Ant.updateSpeed();
                    }
                    ((Simulanthill)MapDimensionsScreen.this.game).displayMainScreen();
                            
                }
            }
        );

        lytMain.setFillParent(true);

		stage.addActor(lytMain);
	}

    /**
     * Will load the screen on the programm.
     */
    @Override
    public void show() 
    {
        Gdx.input.setInputProcessor(stage);
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
