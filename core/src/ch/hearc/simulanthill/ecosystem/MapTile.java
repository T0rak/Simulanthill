package ch.hearc.simulanthill.ecosystem;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import ch.hearc.simulanthill.ecosystem.actors.ElementActor;
import ch.hearc.simulanthill.tools.Asset;

public class MapTile extends ElementActor 
{
    protected float centeredXCase;
    protected float centeredYCase;
    
    public MapTile(int _xCase, int _yCase, Texture _texture, Ecosystem _ecosystem) 
    {
        super(_xCase * _ecosystem.getMapCaseSize(), _yCase * _ecosystem.getMapCaseSize(), _ecosystem.getMapCaseSize(), _ecosystem.getMapCaseSize(), _texture, _ecosystem);
        xCase = _xCase;
        yCase = _yCase;
        float centeredDelta = ecosystem.getMapCaseSize() / 2;
        centeredXCase = xCase * ecosystem.getMapCaseSize() + centeredDelta;
        centeredYCase = yCase * ecosystem.getMapCaseSize() + centeredDelta;
    }
    
    public MapTile(int _xCase, int _yCase, Color _color, Ecosystem _ecosystem) 
    {
        this(_xCase, _yCase, Asset.pixel(_color), _ecosystem);
        
    }

    public int getXCase() 
    {
        return xCase;
    }
    
    public int getYCase() 
    {
        return yCase;
    }
    
    public float getCenteredXCase() 
    {
        return centeredXCase;
    }
    public float getCenteredYCase() 
    {
        return centeredYCase;
    }
    
}
