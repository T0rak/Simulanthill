package ch.hearc.simulanthill.screen.gui;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ch.hearc.simulanthill.ecosystem.Ecosystem;
import ch.hearc.simulanthill.ecosystem.actors.AntState;
import ch.hearc.simulanthill.ecosystem.actors.Anthill;
import ch.hearc.simulanthill.ecosystem.actors.ElementActorType;
import ch.hearc.simulanthill.ecosystem.actors.PheromoneType;
import ch.hearc.simulanthill.ecosystem.actors.Pheromone;

public class EcosystemGUI extends Stage
{
    private Ecosystem ecosystem;
    private ElementActorType typeOfAdd;
    private Group anthillDetails;
    private List<SelectionAnthillListener> selectionAnthillListeners;
    private Anthill selectedAnthill;
    private Actor touchActor;

    public EcosystemGUI(Ecosystem _ecosystem)
    {
        super(_ecosystem.getViewport());
        selectionAnthillListeners = new LinkedList<SelectionAnthillListener>();
        anthillDetails = new Group();
        ecosystem = _ecosystem;
        typeOfAdd = ElementActorType.NONE;
        initAnthillDetails();

        ecosystem.addMapListener(new MapListener(){

            @Override
            public void change() {
                anthillDetails.clear();
                clear();
                initAnthillDetails();
            }
            
        });
    }

    private void initAnthillDetails()
    {
        addTouchActor();
        addAnthill(ecosystem.getAnthills());
    }

    private void addAnthill(List<Anthill> _anthills)
	{
        selectedAnthill = null;
        signalSelectionAnthillListener();
		for (Anthill anthill : _anthills) {
            AnthillDetails a = new AnthillDetails(anthill);
            anthillDetails.addActor(a);
            a.addCListener(new ClickListener()
            {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                
                AnthillDetails a = (AnthillDetails)(event.getTarget().getParent());
                if (a.getSelected() == false)
                {
                    for (Actor other : anthillDetails.getChildren()) {
                        ((AnthillDetails)other).setSelected(false);
                    }
                    a.setSelected(true);
                    changeSelectedAnthill(a.getAnthill());
                }
                else
                {
                    a.setSelected(false);
                    changeSelectedAnthill(null);

                }
                
                super.clicked(event, x, y);
            }

            public void changeSelectedAnthill(Anthill anthill) {
                selectedAnthill = anthill;
                if (anthill != null)
                {
                    signalSelectionAnthillListener(anthill);
                } else
                {
                    signalSelectionAnthillListener();
                }
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                AnthillDetails a = (AnthillDetails)(event.getTarget().getParent());
                a.setHoverVisible(true);
                if (!a.getSelected())
                {
                    a.setClickVisible(true);
                }
                super.enter(event, x, y, pointer, fromActor);

            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                AnthillDetails a = (AnthillDetails)(event.getTarget().getParent());
                a.setHoverVisible(false);
                if (!a.getSelected())
                {
                    a.setClickVisible(false);
                }
                super.exit(event, x, y, pointer, fromActor);
            }
        });
		}
        addActor(anthillDetails);
    }

    private void addTouchActor()
    {
        touchActor = new Actor();
        touchActor.setBounds(0, 0, getWidth(), getHeight());
        touchActor.addListener(new ClickListener(){
            int count = 0;
            boolean dragging = false;
            @Override
            public boolean touchDown(InputEvent _event, float _x, float _y, int _pointer, int _button) {
                setButton(_button);
                count = 0;
                if (_button == 0) 
                {
                    addElement(_x, _y);
                }
                dragging = true;
                return super.touchDown(_event, _x, _y, _pointer, _button);
            }
            @Override
            public void touchDragged(InputEvent _event, float _x, float _y, int _pointer) 
            {
                count ++;
                if (getPressedButton() == 0)
                {
                    addElement(_x, _y);
                } else if (getPressedButton() == 1)
                {
                    removeElement(_x, _y);
                }
                super.touchDragged(_event, _x, _y, _pointer);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                dragging = false;
                super.touchUp(event, x, y, pointer, button);
            }

            private void addElement(float _x, float _y) {
                if (_x > 0 && _y > 0 && _x < ecosystem.getWorldMapWidth() && _y < ecosystem.getWorldMapHeight())
                {
                    if (typeOfAdd == ElementActorType.OBSTACLE || typeOfAdd == ElementActorType.RESOURCE) 
                    {
                        ecosystem.addMapTiles(ecosystem.floatToGridCoordinate(_x), ecosystem.floatToGridCoordinate(_y), typeOfAdd);
                    } else if (typeOfAdd == ElementActorType.ANTHILL && dragging == false)
                    {
                        ecosystem.addMapTiles(ecosystem.floatToGridCoordinate(_x), ecosystem.floatToGridCoordinate(_y), typeOfAdd);
                    }
                     else if (selectedAnthill != null)
                    {
                        if (typeOfAdd == ElementActorType.FOOD_PHEROMONE || typeOfAdd == ElementActorType.HOME_PHEROMONE)
                        {
                            PheromoneType pheromoneType = (typeOfAdd == ElementActorType.FOOD_PHEROMONE ? PheromoneType.RESSOURCE : PheromoneType.HOME);
                            ecosystem.addPheromone(_x, _y, pheromoneType, selectedAnthill, -count);
                        } else if (typeOfAdd == ElementActorType.ANT)
                        {
                            selectedAnthill.createAntAt(_x, _y, 100000, AntState.LOST);
                        }
                    }
                }
            }

            private void removeElement(float _x, float _y) {
                if (_x > 0 && _y > 0 && _x < ecosystem.getWorldMapWidth() && _y < ecosystem.getWorldMapHeight())
                {
                    int xCase = ecosystem.floatToGridCoordinate(_x);
                    int yCase = ecosystem.floatToGridCoordinate(_y);
                    if (typeOfAdd == ElementActorType.OBSTACLE && ecosystem.isObstacle(xCase, yCase)
                    || typeOfAdd == ElementActorType.RESOURCE && ecosystem.isResource(xCase, yCase)
                    || typeOfAdd == ElementActorType.ANTHILL && ecosystem.isAnthill(xCase, yCase))
                    {
                        ecosystem.removeMapTile(xCase, yCase);

                    } else if (typeOfAdd == ElementActorType.FOOD_PHEROMONE)
                    {
                        Pheromone p = ecosystem.isPheromoneFrom(_x, _y, selectedAnthill, PheromoneType.RESSOURCE);
                        if (p != null) 
                        {
                            ecosystem.removePheromone(p);
                            p.remove();
                        }
                    } else if (typeOfAdd == ElementActorType.HOME_PHEROMONE)
                    {
                        Pheromone p = ecosystem.isPheromoneFrom(_x, _y, selectedAnthill, PheromoneType.HOME);
                        if (p != null) 
                        {
                            ecosystem.removePheromone(p);
                            p.remove();
                        }
                    }
                }
            }

		});

        addActor(touchActor);
    }

    public void changeTypeOfAdd(ElementActorType _type)
    {
        typeOfAdd = _type;
    }

    public void addListener(SelectionAnthillListener _listener)
    {
       selectionAnthillListeners.add(_listener);
    }

    private void signalSelectionAnthillListener(Anthill anthill)
    {
        for (SelectionAnthillListener selectionAnthillListener : selectionAnthillListeners) {
            selectionAnthillListener.selected(anthill);
        }
    }

    private void signalSelectionAnthillListener()
    {
        for (SelectionAnthillListener selectionAnthillListener : selectionAnthillListeners) {
            selectionAnthillListener.unselected();
        }
    }
    
}
