package ch.hearc.simulanthill.screen.gui;

import ch.hearc.simulanthill.ecosystem.actors.ElementActorType;
import ch.hearc.simulanthill.ecosystem.actors.Obstacle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
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
import ch.hearc.simulanthill.ecosystem.actors.Resource;
import ch.hearc.simulanthill.tools.Asset;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.utils.Align;
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
	private VisTextButton btnResetAnts;

	private VisImageButton btnFood;
	private VisImageButton btnObstacle;
	private VisImageButton btnAnt;
	private VisImageButton btnAnthill;
	private VisImageButton btnFoodPheromone;
	private VisImageButton btnHomePheromone;
	 
	private VisLabel lblOpacityFactor;
	private VisLabel lblAnt;
	private VisLabel lblPheromone;
	
	private VisSlider sliOpacityFactor;

	private Spinner spinLifeTime;
	
	private Spinner spinRadius;
	private Spinner spinFrequence;
	private Spinner spinAntNumber;
	private Spinner spinAntIndependence;
	private Spinner spinAntSpeed;

	public Actor simulation;
	
	private Anthill selectedAnthill;

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
		addAllActors();

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

		btnResetAnts.addListener(
			new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y) 
				{
					Ecosystem ecosystem = Ecosystem.getCurrentEcosystem();
					if (ecosystem != null)
					{
						ecosystem.resetAnts();
						ecosystem.resetPheromones();
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
				selectedAnthill.setAntFieldOfView(((IntSpinnerModel)spinRadius.getModel()).getValue());
				
			}
			
		});

		spinFrequence.addListener(new ChangeListener(){

			@Override
			public void changed(ChangeEvent event, Actor actor) 
			{
				selectedAnthill.setAntPheromoneReleaseFrequency(((IntSpinnerModel)spinFrequence.getModel()).getValue());
				
			}
			
		});

		spinLifeTime.addListener(new ChangeListener()
		{

			@Override
			public void changed(ChangeEvent event, Actor actor) 
			{
				selectedAnthill.setPheromoneLifeTime(((IntSpinnerModel)spinLifeTime.getModel()).getValue());
				
			}
			
		});

		spinAntNumber.addListener(new ChangeListener()
		{

			@Override
			public void changed(ChangeEvent event, Actor actor) 
			{
				selectedAnthill.setNbAntMax(((IntSpinnerModel)spinAntNumber.getModel()).getValue());
				
			}
			
		});

		spinAntSpeed.addListener(new ChangeListener()
		{

			@Override
			public void changed(ChangeEvent event, Actor actor) 
			{
				selectedAnthill.setAntSpeedFactor(((FloatSpinnerModel)spinAntSpeed.getModel()).getValue().floatValue());
			}
			
		});

		spinAntIndependence.addListener(new ChangeListener()
		{

			@Override
			public void changed(ChangeEvent event, Actor actor) 
			{
				selectedAnthill.setAntIndependance(((IntSpinnerModel)spinAntIndependence.getModel()).getValue());
				
			}
			
		});

		sliOpacityFactor.addListener(new ChangeListener()
		{

			@Override
			public void changed(ChangeEvent event, Actor actor) 
			{
				selectedAnthill.setPheromoneOpacityFactor(sliOpacityFactor.getValue());
			}
			
		});

		btnResetParameters.addListener(
			new ClickListener()
		{
			@Override
				public void clicked(InputEvent event, float x, float y)
				{

					((IntSpinnerModel)(spinRadius.getModel())).setValue(Anthill.ANT_FIELD_OF_VIEW_DEFAULT);
					((IntSpinnerModel)(spinLifeTime.getModel())).setValue(Anthill.PHEROMONE_LIFE_TIME_DEFAULT); 
					((IntSpinnerModel)(spinAntNumber.getModel())).setValue(Anthill.MAX_ANT_DEFAULT);
					((IntSpinnerModel)(spinFrequence.getModel())).setValue(Anthill.ANT_PHEROMONE_RELEASE_FREQUENCY_DEFAULT); 	
					((IntSpinnerModel)(spinAntIndependence.getModel())).setValue(Anthill.ANT_INDEPENDENCE_DEFAULT);
					((FloatSpinnerModel)(spinAntSpeed.getModel())).setValue(new BigDecimal(Anthill.ANT_SPEED_FACTOR_DEFAULT));
					sliOpacityFactor.setValue(Anthill.PHEROMONE_OPACITY_FACTOR);
					
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
		btnFood 			= creaImageButton(Asset.pixel(Resource.color), "Resource");
		btnObstacle			= creaImageButton(Asset.pixel(Obstacle.color), "Obstacle");
		btnAnt				= creaImageButton(Asset.ant(), "Ant");
		btnAnthill			= creaImageButton(Asset.anthill(), "Anthill");
		btnHomePheromone 	= creaImageButton(Asset.pixel(Pheromone.color_home), "Pheromone\nHome");
		btnFoodPheromone 	= creaImageButton(Asset.pixel(Pheromone.color_food), "Pheromone\nFood");
		

		
		btnResetParameters = new VisTextButton("Reset Parameters");
		
		btnReset =	new VisTextButton("Full Reset");
		btnResetAnts =	new VisTextButton("Reset ants");
		btnPlay =	new VisTextButton("Play");

		sliOpacityFactor = new VisSlider(0f, 1f, 0.1f, false);
		sliOpacityFactor.setValue(Anthill.PHEROMONE_OPACITY_FACTOR);

	
		lblOpacityFactor = 		new VisLabel("Pheromone opacity");
		lblPheromone = 	new VisLabel("Phéromones");
		lblAnt = 		new VisLabel("Fourmis");
		lblPheromone.setAlignment(Align.center);
		lblAnt.setAlignment(Align.center);

	
		spinRadius =			new Spinner("Rayon", new IntSpinnerModel(Anthill.ANT_FIELD_OF_VIEW_DEFAULT, 0, 5));
		spinFrequence =			new Spinner("Période entre 2", new IntSpinnerModel(Anthill.ANT_PHEROMONE_RELEASE_FREQUENCY_DEFAULT, 0, 20));
		spinAntNumber =			new Spinner("Nombre", new IntSpinnerModel(Anthill.MAX_ANT_DEFAULT, 1, 10000));
		spinAntIndependence =	new Spinner("Indépendance", new IntSpinnerModel(Anthill.ANT_INDEPENDENCE_DEFAULT,0, 100));
		spinLifeTime =			new Spinner("Temps de vie", new IntSpinnerModel(Anthill.PHEROMONE_LIFE_TIME_DEFAULT, 0, 3000));
		spinAntSpeed =			new Spinner("Vitesse", new FloatSpinnerModel(Float.toString(Anthill.ANT_SPEED_FACTOR_DEFAULT), "0", "9", "0.5", 1));
	

		simulation = new Actor();
	}

	private void addAllActors()
	{
		//Fill main layout

		int padding = 5;
		lytMain.add(lytSimulation).pad(padding);
		lytMain.add(lytParameters).pad(padding);

		//Fill simulation layout
		//lytSimulation.add(simulationImage).colspan(4);
	
		lytSimulation.add(simulation).colspan(5).size((int)(1600/1.25), (int)(900/1.25)).pad(padding);
		lytSimulation.row();
		lytSimulation.add(btnReset).pad(padding);
		lytSimulation.add(btnResetAnts).pad(padding);
		lytSimulation.add(btnPlay).pad(padding);
		lytSimulation.add(lblOpacityFactor).pad(padding);
		lytSimulation.add(sliOpacityFactor).pad(padding);
		
		//Fill parameters layout
		lytParameters.add(lytMapButtons).pad(padding);
		lytParameters.row();
		lytParameters.add(lytInteractibles).pad(padding);
		lytParameters.row();
		lytParameters.add(lytPheromonesParameters).pad(padding);
		lytParameters.row();
		lytParameters.add(lytAntParameters).pad(padding);
		lytParameters.row();
		lytParameters.add(btnResetParameters).pad(padding);
		
		
		//Fill buttons layout
		lytMapButtons.add(btnLoadMap).pad(padding);
		lytMapButtons.add(btnGenerateMap).pad(padding);

		//Fill Pheromone layout
		lytPheromonesParameters.defaults().left().width(200).pad(padding);
		
		lytPheromonesParameters.add(lblPheromone).pad(padding);
		lytPheromonesParameters.row();
		lytPheromonesParameters.add(spinLifeTime).pad(padding);
		lytPheromonesParameters.row();
		lytPheromonesParameters.add(spinFrequence).pad(padding);

		//Fill ant layout
		lytAntParameters.defaults().left().width(200);
		
		lytAntParameters.add(lblAnt).pad(padding);
		lytAntParameters.row();
		lytAntParameters.add(spinRadius).pad(padding);
		lytAntParameters.row();
		lytAntParameters.add(spinAntNumber).pad(padding);
		lytAntParameters.row();
		lytAntParameters.add(spinAntIndependence).pad(padding);
		lytAntParameters.row();
		lytAntParameters.add(spinAntSpeed).pad(padding);
		
		// Fill interactible layout
		int sizeBtnLegend = 100;
		lytInteractibles.add(btnObstacle).size(sizeBtnLegend,sizeBtnLegend);
		lytInteractibles.add(btnFood).size(sizeBtnLegend,sizeBtnLegend);
		lytInteractibles.add(btnAnthill).size(sizeBtnLegend,sizeBtnLegend);
		lytInteractibles.row();
		lytInteractibles.add(btnFoodPheromone).size(sizeBtnLegend,sizeBtnLegend);
		lytInteractibles.add(btnHomePheromone).size(sizeBtnLegend,sizeBtnLegend);
		lytInteractibles.add(btnAnt).size(sizeBtnLegend,sizeBtnLegend);

		unselectAnthill();
	}


	public void selectAnthill(Anthill _anthill) {
		float value = sliOpacityFactor.getValue();
		if (selectedAnthill != null)
		{	
			sliOpacityFactor.setValue(Anthill.PHEROMONE_OPACITY_FACTOR);
		}
		
		selectedAnthill = _anthill;
		sliOpacityFactor.setValue(value);
		btnAnt.setVisible(true);
		btnFoodPheromone.setVisible(true);
		btnHomePheromone.setVisible(true);

		((IntSpinnerModel)(spinRadius.getModel())).setValue(selectedAnthill.getAntFieldOfView());
		((IntSpinnerModel)(spinLifeTime.getModel())).setValue(selectedAnthill.getPheromoneLifeTime()); 
		((IntSpinnerModel)(spinAntNumber.getModel())).setValue(selectedAnthill.getNbAntMax());
		((IntSpinnerModel)(spinFrequence.getModel())).setValue(selectedAnthill.getAntPheromoneReleaseFrequency()); 	
		((IntSpinnerModel)(spinAntIndependence.getModel())).setValue(selectedAnthill.getAntIndependance());
		((FloatSpinnerModel)(spinAntSpeed.getModel())).setValue(new BigDecimal(selectedAnthill.getAntSpeedFactor()));
		sliOpacityFactor.setValue(selectedAnthill.getPheromoneOpacityFactor());

		lblOpacityFactor.setVisible(true);
		lblPheromone.setVisible(true);
		lblAnt.setVisible(true);
		btnResetParameters.setVisible(true);
		spinRadius.setVisible(true);
		spinLifeTime.setVisible(true);
		spinAntNumber.setVisible(true);
		spinFrequence.setVisible(true);
		spinAntIndependence.setVisible(true);
		spinAntSpeed.setVisible(true);
		sliOpacityFactor.setVisible(true);
	}

	public void unselectAnthill() {
		if (selectedAnthill != null)
		{	
			sliOpacityFactor.setValue(Anthill.PHEROMONE_OPACITY_FACTOR);
		}
		selectedAnthill = null;
		btnResetParameters.setVisible(false);
		lblOpacityFactor.setVisible(false);
		lblPheromone.setVisible(false);
		lblAnt.setVisible(false);
		btnAnt.setVisible(false);
		btnFoodPheromone.setVisible(false);
		btnHomePheromone.setVisible(false);

		spinRadius.setVisible(false);
		spinLifeTime.setVisible(false);
		spinAntNumber.setVisible(false);
		spinFrequence.setVisible(false);
		spinAntIndependence.setVisible(false);
		spinAntSpeed.setVisible(false);
		sliOpacityFactor.setVisible(false);
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
		VisLabel lbl = new VisLabel(text);
		lbl.setAlignment(Align.center);
		btn.add(lbl);
		return btn;
	}
}
