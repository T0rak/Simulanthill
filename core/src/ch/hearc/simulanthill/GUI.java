package ch.hearc.simulanthill;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.SingleFileChooserListener;
import com.kotcrab.vis.ui.widget.spinner.FloatSpinnerModel;
import com.kotcrab.vis.ui.widget.spinner.IntSpinnerModel;
import com.kotcrab.vis.ui.widget.spinner.Spinner;
import com.kotcrab.vis.ui.widget.spinner.Spinner.SpinnerStyle;

import ch.hearc.simulanthill.actors.Ant;
import ch.hearc.simulanthill.actors.Asset;
import ch.hearc.simulanthill.actors.Pheromone;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.awt.*;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GUI extends Stage
{

	private VisTable lytMain;
	private VisTable lytSimulation;
	private VisTable lytParameters;
	private VisTable lytMapButtons;
	private VisTable lytPheromonesParameters;
	private VisTable lytAntParameters;
	private VisTable lytInteractibles;

	private Texture simulationText;
	private Image simulationImage;
	private Drawable drwbInteractible;
	
	private VisTextButton btnReset;
	private VisTextButton btnPlay;
	private VisTextButton btnResetParameters;
	private VisTextButton btnLoadMap;
	private VisTextButton btnGenerateMap;
	
	private VisImageButton btnFood;
	private VisImageButton btnObstacle;
	private VisImageButton btnAnt;
	private VisImageButton btnAnthill;
	private VisImageButton btnFoodPheromone;
	private VisImageButton btnHomePheromone;
	 
	private VisLabel lblSpeed;
	private VisLabel lblAnt;
	private VisLabel lblPheromone;
	
	private VisSlider sliSpeed;

	private Spinner spinLifeTime;
	private Spinner spinRadius;
	private Spinner spinFrequence;
	private Spinner spinAntNumber;
	private Spinner spinAntIndependence;
	private Spinner spinAntSpeed;

	public Actor simulation;

    
	public GUI (Viewport _vp) 
	{
		super(_vp);
		
		
		simulation = new Actor();
		
		//Create All layout
		lytMain = 					new VisTable();
		lytSimulation = 			new VisTable();
		lytParameters = 			new VisTable();
		lytMapButtons = 			new VisTable();
		lytPheromonesParameters = 	new VisTable();
		lytAntParameters =			new VisTable();
		lytInteractibles = 			new VisTable();
		

		//Create Vis Attribute

		
		
		btnLoadMap = 		new VisTextButton("Load Map");
		btnGenerateMap = 	new VisTextButton("Generate Map");
		
		btnFood =		new VisImageButton(new Image(Asset.resourceLegend()).getDrawable());
		btnObstacle =	new VisImageButton(new Image(Asset.obstacleLegend()).getDrawable());
		btnAnt =		new VisImageButton(new Image(Asset.antLegend()).getDrawable());
		btnAnthill =	new VisImageButton(new Image(Asset.anthillLegend()).getDrawable());
		btnHomePheromone =	new VisImageButton(new Image(Asset.homePheromoneLegend()).getDrawable());
		btnFoodPheromone =	new VisImageButton(new Image(Asset.foodPheromoneLegend()).getDrawable());

		btnResetParameters = new VisTextButton("Reset Parameters");
		
		btnReset =	new VisTextButton("Reset");
		btnPlay =	new VisTextButton("Play");

		sliSpeed = new VisSlider(0.1f, 10f, 0.1f, false);

	
		lblSpeed = 		new VisLabel("Speed");
		lblPheromone = 	new VisLabel("Phéromones");
		lblAnt = 		new VisLabel("Fourmis");

	
		spinRadius =			new Spinner("Rayon", new IntSpinnerModel(3, 0, 5));
		spinFrequence =			new Spinner("Fréquence", new IntSpinnerModel(0, 0, 20));
		spinAntNumber =			new Spinner("Nombre", new IntSpinnerModel(500, 1, 10000));
		spinAntIndependence =	new Spinner("Indépendance", new IntSpinnerModel(0, 0, 10));
		spinLifeTime =			new Spinner("Temps de vie", new IntSpinnerModel(150, 0, 3000));
		spinAntSpeed =			new Spinner("Vitesse", new FloatSpinnerModel("1", "0", "10", "0.5", 2));
		

		

		//Fill main layout
		lytMain.add(lytSimulation);
		lytMain.add(lytParameters);

		//Fill simulation layout
		//lytSimulation.add(simulationImage).colspan(4);
	
		lytSimulation.add(simulation).colspan(4).size((int)(1600/1.25), (int)(900/1.25));
		lytSimulation.row();
		lytSimulation.add(btnReset);
		lytSimulation.add(btnPlay);
		lytSimulation.add(lblSpeed);
		lytSimulation.add(sliSpeed);
		
		//Fill parameters layout
		lytParameters.add(lytMapButtons);
		lytParameters.row();
		lytParameters.add(lytPheromonesParameters);
		lytParameters.row();
		lytParameters.add(lytAntParameters);
		lytParameters.row();
		lytParameters.add(btnResetParameters);
		lytParameters.row();
		lytParameters.add(lytInteractibles);
		
		//Fill buttons layout
		lytMapButtons.add(btnLoadMap);
		lytMapButtons.add(btnGenerateMap);

		//Fill Pheromone layout
		lytPheromonesParameters.defaults().left().width(200);
		
		lytPheromonesParameters.add(lblPheromone);
		lytPheromonesParameters.row();
		lytPheromonesParameters.add(spinLifeTime);
		lytPheromonesParameters.row();
		lytPheromonesParameters.add(spinFrequence);

		//Fill ant layout
		lytAntParameters.defaults().left().width(200);
		
		lytAntParameters.add(lblAnt);
		lytPheromonesParameters.row();
		lytPheromonesParameters.add(spinRadius);
		lytAntParameters.row();
		lytAntParameters.add(spinAntNumber);
		lytAntParameters.row();
		lytAntParameters.add(spinAntIndependence);
		lytAntParameters.row();
		lytAntParameters.add(spinAntSpeed);
		
		// Fill interactible layout
		lytInteractibles.add(btnFood).size(100,100);
		lytInteractibles.add(btnObstacle).size(100,100);
		lytInteractibles.add(btnAnt).size(100,100);
		lytInteractibles.row();
		lytInteractibles.add(btnAnthill).size(100,100);
		lytInteractibles.add(btnFoodPheromone).size(100,100);
		lytInteractibles.add(btnHomePheromone).size(100,100);
		
		//sim.setFillParent(true);
		lytMain.setFillParent(true);

		addActor(lytMain);

		//lytMain.setDebug(true); // This is optional, but enables debug lines for tables.


		// control
		btnLoadMap.addListener(
			new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y) 
				{
					new Thread(new Runnable() {             
						@Override
						public void run() {
							JFileChooser chooser = new JFileChooser();
							chooser.setCurrentDirectory(new File("..\\..\\maps\\testmap1.txt"));
        					chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
							chooser.addChoosableFileFilter(new FileNameExtensionFilter("TXT file", "txt"));
							chooser.setAcceptAllFileFilterUsed(true);
							JFrame f = new JFrame();
							f.setVisible(true);
							f.toFront();
							f.setVisible(false);
							int res = chooser.showOpenDialog(f);
							f.dispose();

							Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();
							if (ecosystem != null)
							{
								ecosystem.loadMap(chooser.getSelectedFile().getAbsolutePath());
							}
							
						}
					}).start();
				}
			}
		);

		btnGenerateMap.addListener(
			new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y) 
				{
					Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();
					if (ecosystem != null)
					{
						ecosystem.loadMap();
					}

				}
			}
		);

		btnReset.addListener(
			new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y) 
				{
					Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();
					if (ecosystem != null)
					{
						ecosystem.reset();
					}

				}
			}
		);


		btnPlay.addListener(
			new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y) 
				{
					Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();
					if (ecosystem != null)
					{
						ecosystem.swicthIsPlaying();

						if (ecosystem.getIsPlaying())
						{
							btnPlay.setText("Pause");
						}
						else
						{
							btnPlay.setText("Play");
						}

					}

				}
			}
		);

		spinRadius.addListener(new ChangeListener(){

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Ant.setFielOfView(((IntSpinnerModel)spinRadius.getModel()).getValue());
				
			}
			
		});

		spinFrequence.addListener(new ChangeListener(){

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Ant.setReleasePheromoneTime(((IntSpinnerModel)spinFrequence.getModel()).getValue());
				
			}
			
		});

		spinLifeTime.addListener(new ChangeListener(){

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Pheromone.setLifetimeInit(((IntSpinnerModel)spinLifeTime.getModel()).getValue());
				
			}
			
		});

		spinAntNumber.addListener(new ChangeListener(){

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Ecosystem.getCurrentEcosystem().setNbAntMax(((IntSpinnerModel)spinAntNumber.getModel()).getValue());
				
			}
			
		});
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
}
