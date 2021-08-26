package ch.hearc.simulanthill;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.spinner.FloatSpinnerModel;
import com.kotcrab.vis.ui.widget.spinner.IntSpinnerModel;
import com.kotcrab.vis.ui.widget.spinner.Spinner;

import ch.hearc.simulanthill.actors.Ant;
import ch.hearc.simulanthill.actors.Anthill;
import ch.hearc.simulanthill.actors.Pheromone;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.File;
import java.math.BigDecimal;


import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * The stage that contains input
 */
public class GUI extends Stage
{
	private final Game game;

	private VisTable lytMain;
	private VisTable lytSimulation;
	private VisTable lytParameters;
	private VisTable lytMapButtons;
	private VisTable lytPheromonesParameters;
	private VisTable lytAntParameters;
	private VisTable lytInteractibles;
	
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

	private static final int DEFAULT_RADIUS 		= 3;
	private static final int DEFAULT_LIFETIME 		= 150;
	private static final int DEFAULT_FREQUENCE 		= 0;
	private static final float DEFAULT_SPEED 		= 1;
	private static final int DEFAULT_INDEPENDENCE 	= 10;
	private static final int DEFAULT_ANT_NUMBER 	= 500;
    
	/**
	 * Constructor
	 * @param _vp location where the stage can be drawed
	 */
	public GUI (Viewport _vp, Game _game) 
	{
		super(_vp);
		game = _game;
		
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

		sliSpeed = new VisSlider(0f, 1f, 0.1f, false);

	
		lblSpeed = 		new VisLabel("Pheromone opacity");
		lblPheromone = 	new VisLabel("Phéromones");
		lblAnt = 		new VisLabel("Fourmis");

	
		spinRadius =			new Spinner("Rayon", new IntSpinnerModel(DEFAULT_RADIUS, 0, 5));
		spinFrequence =			new Spinner("Période entre 2", new IntSpinnerModel(DEFAULT_FREQUENCE, 0, 20));
		spinAntNumber =			new Spinner("Nombre", new IntSpinnerModel(DEFAULT_ANT_NUMBER, 1, 10000));
		spinAntIndependence =	new Spinner("Indépendance", new IntSpinnerModel(DEFAULT_INDEPENDENCE,0, 100));
		spinLifeTime =			new Spinner("Temps de vie", new IntSpinnerModel(DEFAULT_LIFETIME, 0, 3000));
		spinAntSpeed =			new Spinner("Vitesse", new FloatSpinnerModel(Float.toString(DEFAULT_SPEED), "0", "9", "0.5", 1));
	
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
		lytAntParameters.row();
		lytAntParameters.add(spinRadius);
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
		
		
		lytMain.setFillParent(true);
		addActor(lytMain);

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
							chooser.showOpenDialog(f);
							f.dispose();

							Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();
							if (ecosystem != null)
							{
								ecosystem.loadMap(chooser.getSelectedFile().getAbsolutePath());
								Ant.setSpeedFactor(((FloatSpinnerModel)spinAntSpeed.getModel()).getValue().floatValue());
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
						((Simulanthill) game).displayMapDimensionsScreen();
						//ecosystem.loadMap();
						//Ant.setSpeedFactor(((FloatSpinnerModel)spinAntSpeed.getModel()).getValue().floatValue());
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

		spinRadius.addListener(new ChangeListener()
		{

			@Override
			public void changed(ChangeEvent event, Actor actor) 
			{
				Ant.setFielOfView(((IntSpinnerModel)spinRadius.getModel()).getValue());
				
			}
			
		});

		spinFrequence.addListener(new ChangeListener(){

			@Override
			public void changed(ChangeEvent event, Actor actor) 
			{
				Ant.setReleasePheromoneTime(((IntSpinnerModel)spinFrequence.getModel()).getValue());
				
			}
			
		});

		spinLifeTime.addListener(new ChangeListener()
		{

			@Override
			public void changed(ChangeEvent event, Actor actor) 
			{
				Pheromone.setLifetimeInit(((IntSpinnerModel)spinLifeTime.getModel()).getValue());
				
			}
			
		});

		spinAntNumber.addListener(new ChangeListener()
		{

			@Override
			public void changed(ChangeEvent event, Actor actor) 
			{
				Ecosystem.getCurrentEcosystem().setNbAntMax(((IntSpinnerModel)spinAntNumber.getModel()).getValue());
				
			}
			
		});

		spinAntSpeed.addListener(new ChangeListener()
		{

			@Override
			public void changed(ChangeEvent event, Actor actor) 
			{
				Ant.setSpeedFactor(((FloatSpinnerModel)spinAntSpeed.getModel()).getValue().floatValue());
				
			}
			
		});

		spinAntIndependence.addListener(new ChangeListener()
		{

			@Override
			public void changed(ChangeEvent event, Actor actor) 
			{
				Ant.setAutonomy(((IntSpinnerModel)spinAntIndependence.getModel()).getValue());
				
			}
			
		});

		sliSpeed.addListener(new ChangeListener()
		{

			@Override
			public void changed(ChangeEvent event, Actor actor) 
			{
				//System.out.println(sliSpeed.getValue());
				Pheromone.setOpacityFactor(sliSpeed.getValue());
			}
			
		});

		btnResetParameters.addListener(
			new ClickListener()
		{
			@Override
				public void clicked(InputEvent event, float x, float y)
				{

					((IntSpinnerModel)(spinRadius.getModel())).setValue(DEFAULT_RADIUS);
 
					((IntSpinnerModel)(spinLifeTime.getModel())).setValue(DEFAULT_LIFETIME); 
					((IntSpinnerModel)(spinAntNumber.getModel())).setValue(DEFAULT_ANT_NUMBER);
					((IntSpinnerModel)(spinFrequence.getModel())).setValue(DEFAULT_FREQUENCE); 	
					((IntSpinnerModel)(spinAntIndependence.getModel())).setValue(DEFAULT_INDEPENDENCE);
					((FloatSpinnerModel)(spinAntSpeed.getModel())).setValue(new BigDecimal(DEFAULT_SPEED));
					
				}
			
		});

	}
}
