package ch.hearc.simulanthill;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.layout.GridGroup;
import com.kotcrab.vis.ui.layout.HorizontalFlowGroup;
import com.kotcrab.vis.ui.layout.VerticalFlowGroup;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class Simulanthill extends ApplicationAdapter {

	private Stage stage;

	private HorizontalFlowGroup lytMain;
	private VerticalFlowGroup lytSimulation;
	private GridGroup lytSimulationParameters;
	private HorizontalFlowGroup lytSpeedParameters;
	private VerticalFlowGroup lytParameters;
	private HorizontalFlowGroup lytMapButtons;
	private GridGroup lytPheromonesParameters;
	private GridGroup lytAntParameters;
	private GridGroup lytInteractibles;

	private Texture simulationText;
	
	@Override
	public void create () {
		VisUI.load();

		simulationText = new Texture("badlogic.jpg");
		Image simulationImage = new Image(simulationText);

		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		

		lytMain = new HorizontalFlowGroup();
		lytSimulation = new VerticalFlowGroup();
		lytSimulationParameters = new GridGroup();
		lytSpeedParameters = new HorizontalFlowGroup();
		lytParameters = new VerticalFlowGroup();
		lytMapButtons = new HorizontalFlowGroup();
		lytPheromonesParameters = new GridGroup();
		lytAntParameters = new GridGroup();
		lytInteractibles = new GridGroup();

		lytMain.addActor(lytSimulation);
		lytMain.addActor(lytParameters);

		lytSimulation.addActor(simulationImage);
		lytSimulation.addActor(lytSimulationParameters);
		
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

		

		

		VisTextButton b = new VisTextButton("Coucou je suis bouton");
		lytMapButtons.addActor(b);

		
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
