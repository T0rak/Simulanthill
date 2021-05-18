package ch.hearc.simulanthill;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

import ch.hearc.simulanthill.actors.Ant;
import ch.hearc.simulanthill.actors.ElementActor;
import ch.hearc.simulanthill.map.MapConvertor;

public class Ecosystem extends Stage
{
    private static Ecosystem instance = null;

    private Ecosystem(Viewport viewport, String filepath)
    {
        super(viewport);
        int nbAnts = 5000;
		Ant tab[] = new Ant[nbAnts];
		
		for (int i = 0; i < nbAnts; i++) {
			tab[i] = new Ant(MathUtils.random((float)Gdx.graphics.getWidth()), MathUtils.random((float)Gdx.graphics.getHeight()), 12, 12);
		}

		for (int i = 0; i < nbAnts; i++) {
			addActor(tab[i]);
		}

        loadMap(filepath, 1600, 900);

    }

    public static Ecosystem getInstance(Viewport viewport, String filepath)
    {
        if(instance == null)
        {
            return new Ecosystem(viewport, filepath);
        }
        else
        {
            return instance;
        }
        
    }

    public void loadMap(String filepath, float width, float height)
    {
        MapConvertor map = new MapConvertor(filepath, width, height);
        
        map.convert(1);
        //float size = width / map.getWidth();
        //Gdx.app.log("Y TEST", String.valueOf(map.getWidth()));
        //map.convert(size);
        
		for (ElementActor element : map.getActorList()) {
            //element.setPos(element.getPosX()*size, element.getPosY()*size);
            //element.setPos(element.getPosX(), element.getPosY());
            //Gdx.app.log("X TEST", String.valueOf(element.getPosX()));
            //Gdx.app.log("Y TEST", String.valueOf(element.getPosX()));
            //element.setSize(size, size);
			addActor(element);
		}
    }
}
