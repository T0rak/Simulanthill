package ch.hearc.simulanthill;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.widget.VisImageButton;

import ch.hearc.simulanthill.actors.Anthill;

public class EcosystemGUI extends Stage
{
    private Ecosystem ecosystem;
    public EcosystemGUI(Ecosystem _ecosystem)
    {
        super(_ecosystem.getViewport());
        ecosystem = _ecosystem;

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
		for (Anthill anthill : _anthills) {
			addActor(new AnthillDetails(anthill));
		}
		
	}
}
