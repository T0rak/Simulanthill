package ch.hearc.simulanthill;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.layout.HorizontalFlowGroup;
import com.kotcrab.vis.ui.layout.VerticalFlowGroup;
import com.kotcrab.vis.ui.widget.Separator;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.spinner.FloatSpinnerModel;
import com.kotcrab.vis.ui.widget.spinner.IntSpinnerModel;
import com.kotcrab.vis.ui.widget.spinner.Spinner;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class Simulanthill extends ApplicationAdapter 
{

	private Stage stage;

	private VisTable lytMain;
	private VisTable lytSimulation;
	private VisTable lytSpeedParameters;
	private VisTable lytParameters;
	private VisTable lytMapButtons;
	private VisTable lytPheromonesParameters;
	private VisTable lytAntParameters;
	private VisTable lytInteractibles;

	private Texture simulationText;
	
	@Override
	public void create () {
		VisUI.load();

		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		// Create All layout
		lytMain = new VisTable();
		lytSimulation = new VisTable();
		lytSpeedParameters = new VisTable();
		lytParameters = new VisTable();
		lytMapButtons = new VisTable();
		lytPheromonesParameters = new VisTable();
		lytAntParameters = new VisTable();
		lytInteractibles = new VisTable();

		// Fill main layout
		lytMain.add(lytSimulation);
		lytMain.add(lytParameters);

		// Fill simulation layout
		simulationText = new Texture("badlogic.jpg");
		Image simulationImage = new Image(simulationText);
		lytSimulation.add(simulationImage).colspan(3);
		lytSimulation.row();
		VisTextButton btnReset = new VisTextButton("Reset");
		VisTextButton btnPlay = new VisTextButton("Play");
		VisSlider sliSpeed = new VisSlider(0.1f, 10f, 0.1f, false);
		lytSimulation.add(btnReset);
		lytSimulation.add(btnPlay);
		lytSimulation.add(sliSpeed);
		
		//Fill parameters layout

		lytParameters.add(lytMapButtons);
		lytParameters.row();
		lytParameters.add(lytPheromonesParameters);
		lytParameters.row();
		lytParameters.add(lytAntParameters);
		lytParameters.row();
		VisTextButton btnResetParameters = new VisTextButton("Reset Parameters");
		lytParameters.add(btnResetParameters);
		lytParameters.row();
		lytParameters.add(lytInteractibles);
		
		//Fill buttons layout
		VisTextButton btnLoadMap = new VisTextButton("Load Map");
		lytMapButtons.add(btnLoadMap);
		VisTextButton btnGenerateMap = new VisTextButton("Generate Map");
		lytMapButtons.add(btnGenerateMap);

		//Fill Pheromone layout
		lytPheromonesParameters.defaults().left().width(200);
		VisLabel lblPheromone = new VisLabel("Phéromones");
		
		Spinner spinLifeTime = new Spinner("Temps de vie", new IntSpinnerModel(5, 0, 5));
		Spinner spinRadius = new Spinner("Rayon", new FloatSpinnerModel("1", "0", "10", "0.5", 2));
		Spinner spinFrequence = new Spinner("Fréquence", new FloatSpinnerModel("1", "0", "10", "0.5", 2));
		lytPheromonesParameters.add(lblPheromone);
		lytPheromonesParameters.row();
		lytPheromonesParameters.add(spinLifeTime);
		lytPheromonesParameters.row();
		lytPheromonesParameters.add(spinRadius);
		lytPheromonesParameters.row();
		lytPheromonesParameters.add(spinFrequence);

		//Fill ant layout
		lytParameters.defaults().left();
		VisLabel lblAnt = new VisLabel("Fourmis");
		
		Spinner spinAntNumber = new Spinner("Nombre", new IntSpinnerModel(5, 0, 5));
		Spinner spinAntIndependence = new Spinner("Indépendance", new FloatSpinnerModel("1", "0", "10", "0.5", 2));
		Spinner spinAntSpeed = new Spinner("Vitesse", new FloatSpinnerModel("1", "0", "10", "0.5", 2));
		lytAntParameters.add(lblAnt);
		lytAntParameters.row();
		lytAntParameters.add(spinAntNumber);
		lytAntParameters.row();
		lytAntParameters.add(spinAntIndependence);
		lytAntParameters.row();
		lytAntParameters.add(spinAntSpeed);
		
		// Fill interactible layout

		Drawable imageUp = simulationImage.getDrawable();
		VisImageButton btnFood = new VisImageButton(imageUp);
		lytInteractibles.add(btnFood).size(100,100);
		VisImageButton btnObstacle = new VisImageButton(imageUp);
		lytInteractibles.add(btnObstacle).size(100,100);
		VisImageButton btnAnt = new VisImageButton(imageUp);
		lytInteractibles.add(btnAnt).size(100,100);
		lytInteractibles.row();
		VisImageButton btnAnthill = new VisImageButton(imageUp);
		lytInteractibles.add(btnAnthill).size(100,100);
		

		lytMain.setFillParent(true);

		stage.addActor(lytMain);


		lytMain.setDebug(true); // This is optional, but enables debug lines for tables.

		

		/*Skin skin = new Skin();

		// Generate a 1x1 white texture and store it in the skin named "white".
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		skin.add("white", new Texture(pixmap));

		// Store the default libgdx font under the name "default".
		skin.add("default", new BitmapFont());

		// Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
		textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
		textButtonStyle.font = skin.getFont("default");
		skin.add("default", textButtonStyle);*/

		

		

		

		
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}
	
	@Override
	public void dispose () {
		stage.dispose();
		VisUI.dispose();
	}
}
