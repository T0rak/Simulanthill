package ch.hearc.simulanthill.screen.gui;

import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
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

    public EcosystemGUI(Ecosystem _ecosystem)
    {
        super(_ecosystem.getViewport());
        ecosystem = _ecosystem;
        typeOfAdd = ElementActorType.NONE;
        refreshAnthill(ecosystem.getAnthills());

        ecosystem.addMapListener(new MapListener(){

            @Override
            public void change() {
                refreshAnthill(ecosystem.getAnthills());
            }
            
        });
        
    }

    public void refreshAnthill(List<Anthill> _anthills)
	{
        this.clear();
        System.out.println(getWidth() + "/" + getHeight());
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
		for (Anthill anthill : _anthills) {
			addActor(new AnthillDetails(anthill));
		}
		
    }

    public void changeTypeOfAdd(ElementActorType _type)
    {
        typeOfAdd = _type;
    }

    
}
