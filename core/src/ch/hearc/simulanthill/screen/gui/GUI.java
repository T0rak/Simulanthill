package ch.hearc.simulanthill.screen.gui;

import ch.hearc.simulanthill.ecosystem.actors.ElementActorType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.spinner.FloatSpinnerModel;
import com.kotcrab.vis.ui.widget.spinner.IntSpinnerModel;
import com.kotcrab.vis.ui.widget.spinner.Spinner;

import ch.hearc.simulanthill.Simulanthill;
import ch.hearc.simulanthill.ecosystem.Ecosystem;
import ch.hearc.simulanthill.ecosystem.actors.Ant;
import ch.hearc.simulanthill.ecosystem.actors.Anthill;
import ch.hearc.simulanthill.ecosystem.actors.Pheromone;
import ch.hearc.simulanthill.tools.Asset;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

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

	private ButtonGroup<VisImageButton> selectButtonGroup;

	private List<SelectionTypeOfAddListener> selectionListeners;
    
	/**
	 * Constructor
	 * @param _vp location where the stage can be drawed
	 */
	public GUI (Viewport _vp, Game _game) 
	{
		super(_vp);
		game = _game;
		
		selectionListeners = new LinkedList<SelectionTypeOfAddListener>();

		create();
		add();

		selectButtonGroup = new ButtonGroup<VisImageButton>(btnObstacle, btnFood, btnAnthill, btnHomePheromone, btnFoodPheromone, btnAnt);
		selectButtonGroup.setMaxCheckCount(1);
		selectButtonGroup.setMinCheckCount(0);
		selectButtonGroup.setUncheckLast(true);

		for (VisImageButton btn : selectButtonGroup.getButtons()) {
			btn.getStyle().checked = btn.getStyle().down;
		}
		
		ClickListener listener = new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y) 
			{	
				int index = selectButtonGroup.getCheckedIndex();
				signalSelectionListener(ElementActorType.fromValue(index));
				super.clicked(event, x, y);
			}

		};

		btnAnthill.addListener(listener);
		btnObstacle.addListener(listener);
		btnFood.addListener(listener);
		btnFoodPheromone.addListener(listener);
		btnHomePheromone.addListener(listener);
		btnAnt.addListener(listener);

		lytMain.setFillParent(true);
		addActor(lytMain);

		// control
		btnLoadMap.addListener(
			new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y) 
				{
					((Simulanthill)game).displayMapSelectionScreen();
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

	private void create()
	{
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
		
		btnFood 			= creaImageButton(Asset.resource(), "Resource");
		btnObstacle			= creaImageButton(Asset.obstacle(), "Obstacle");
		btnAnt				= creaImageButton(Asset.ant(), "Ant");
		btnAnthill			= creaImageButton(Asset.anthill(), "Anthill");
		btnHomePheromone 	= creaImageButton(Asset.homePheromone(), "Pheromone Home");
		btnFoodPheromone 	= creaImageButton(Asset.foodPheromone(), "Pheromone Food");
		
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
	

		simulation = new Actor();
	}

	private void add()
	{
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
		int sizeBtnLegend = 100;
		lytInteractibles.add(btnFood).size(sizeBtnLegend,sizeBtnLegend);
		lytInteractibles.add(btnObstacle).size(sizeBtnLegend,sizeBtnLegend);
		lytInteractibles.add(btnAnt).size(sizeBtnLegend,sizeBtnLegend);
		lytInteractibles.row();
		lytInteractibles.add(btnAnthill).size(sizeBtnLegend,sizeBtnLegend);
		lytInteractibles.add(btnFoodPheromone).size(sizeBtnLegend,sizeBtnLegend);
		lytInteractibles.add(btnHomePheromone).size(sizeBtnLegend,sizeBtnLegend);
	}

	public void selectAnthill(Anthill _anthill) {
		System.out.println("Select " + _anthill);
		//TODO: Implement
	}

	public void unselectAnthill() {
		System.out.println("Unselect");
		//TODO: Implement
	}

	public void signalSelectionListener(ElementActorType _type)
	{
		for (SelectionTypeOfAddListener selectionListener : selectionListeners) {
			selectionListener.changed(_type);
		}
	}

	public boolean addListener(SelectionTypeOfAddListener _listener) {
		selectionListeners.add(_listener);
		return true;
	}

	public VisImageButton creaImageButton(Texture _texture, String text)
	{
		VisImageButton btn = new VisImageButton(new Image(_texture).getDrawable());
		btn.row();
		btn.add(new VisLabel(text));
		return btn;
	}
}
