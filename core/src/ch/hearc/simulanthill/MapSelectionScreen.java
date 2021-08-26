package ch.hearc.simulanthill;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisList;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import ch.hearc.simulanthill.actors.Ant;

import java.io.File;

/**
 * Gives information about the institution and students that worked on the simulator
 */
public class MapSelectionScreen implements Screen
{
    private final Game game;
    private Stage stage;
    private VisTable lytMain;

    private VisLabel lblMaps;
    private VisList<String> lstMapFiles;

    private VisTextButton btnValidate;
	private VisTextButton btnCancel;
    /**
     * Constructor
     * @param _game handler of the content of AboutScreen
     */
	public MapSelectionScreen(Game _game) 
	{
        this.game = _game;

        stage = new Stage(new FitViewport(1600, 900));

        lytMain = new VisTable();

        lblMaps = new VisLabel("Choix de carte:");

        lstMapFiles = new VisList<String>();
        
        Array<String> fileArray = new Array<String>(); 
        File[] files = new File("D:/thierry.fluck/Documents/He-Arc/HES_ETE/simulanthill/maps").listFiles();
        for (File file : files) {
            if (!file.isDirectory()) {
                fileArray.add(file.getName());
            }
        }

        lstMapFiles.setItems(fileArray);

        btnValidate = new VisTextButton("Valider");
        btnCancel = new VisTextButton("Annuler");

        lytMain.add(lblMaps);
        lytMain.row();
        lytMain.add(lstMapFiles);
        lytMain.row();
        lytMain.add(btnValidate);
        lytMain.add(btnCancel);

        btnCancel.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y) 
                {           
                    ((Simulanthill)MapSelectionScreen.this.game).displaySimulanthillScreen();
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
                        String selectedFileName = lstMapFiles.getSelected();
                        System.out.println(selectedFileName);
                        ecosystem.loadMap("D:/thierry.fluck/Documents/He-Arc/HES_ETE/simulanthill/maps/" + selectedFileName);
                        Ant.updateSpeed();
                    }
                    ((Simulanthill)MapSelectionScreen.this.game).displaySimulanthillScreen();
                            
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

