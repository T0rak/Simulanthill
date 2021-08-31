package ch.hearc.simulanthill.screen.gui;

import java.util.LinkedList;
import java.util.List;

import javax.lang.model.util.ElementScanner6;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ch.hearc.simulanthill.ecosystem.Ecosystem;
import ch.hearc.simulanthill.ecosystem.actors.Anthill;
import ch.hearc.simulanthill.ecosystem.actors.ElementActorType;


public class EcosystemGUI extends Stage
{
    private Ecosystem ecosystem;
    private ElementActorType typeOfAdd;
    private Group anthillDetails;
    private AnthillDetails selectedAnthill;
    private List<SelectionAnthillListener> selectionAnthillListeners;

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
                    signalSelectionAnthillListener(a.getAnthill());
                }
                else
                {
                    a.setSelected(false);
                    signalSelectionAnthillListener();

                }
                
                super.clicked(event, x, y);
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                AnthillDetails a = (AnthillDetails)(event.getTarget().getParent());
                if (!a.getSelected())
                {
                    a.setVisible(true);
                }
                super.enter(event, x, y, pointer, fromActor);

            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                AnthillDetails a = (AnthillDetails)(event.getTarget().getParent());
                if (!a.getSelected())
                {
                    a.setVisible(false);
                }
                super.exit(event, x, y, pointer, fromActor);
            }
        });
		}
        addActor(anthillDetails);
    }

    private void addTouchActor()
    {
        Actor actor = new Actor();
        actor.setBounds(0, 0, getWidth(), getHeight());
        actor.addListener(new ClickListener(){
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if(x > 0 && y > 0 && x < getWidth() && y < getHeight() )
                {
                    ecosystem.addMapTiles(ecosystem.mouseToGrid(x), ecosystem.mouseToGrid(y), typeOfAdd);
                }
                super.touchDragged(event, x, y, pointer);
            }
			

		});

        addActor(actor);
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
            selectionAnthillListener.unselected();;
        }
    }
    
}
