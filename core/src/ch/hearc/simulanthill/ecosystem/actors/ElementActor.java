package ch.hearc.simulanthill.ecosystem.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import ch.hearc.simulanthill.ecosystem.Ecosystem;
import ch.hearc.simulanthill.tools.Asset;
import ch.hearc.simulanthill.tools.SpriteActor;

/**
 * Holds the element informations. It's a superclasse of all elements used in simulantHill (ants, obstacle, resources, ...)
 */
public class ElementActor extends SpriteActor 
{
    protected int xCase;
    protected int yCase;
    
    protected Ecosystem ecosystem;
    /**
        * Main constructor of ElementActor
        * @param _x x coordinate of the position
        * @param _y y coordinate of the position
        * @param _texture texture of the element
        * @param _actorName name of the actor
        */
    public ElementActor(float _x, float _y, float _width, float _height, Texture _texture, Ecosystem _ecosystem) 
    {
        super(_x, _y, _width, _height, _texture);
        ecosystem = Ecosystem.getCurrentEcosystem();
        sprite.setOrigin(_width / 2, _height / 2);
    }

    public ElementActor(float _x, float _y, float _width, float _height, Color _color, Ecosystem _ecosystem) 
    {
        this(_x, _y, _width, _height, Asset.pixel(_color), _ecosystem);
    }

    @Override
    public void setPosition(float _x, float _y) {
        setX(_x);
        setY(_y);
        super.setPosition(_x, _y);
    }

    @Override
    public void setX(float _x) {
        xCase = Ecosystem.getCurrentEcosystem().floatToGridCoordinate(_x);
        super.setX(_x);
    }

    @Override
    public void setY(float _y) {
        yCase = Ecosystem.getCurrentEcosystem().floatToGridCoordinate(_y);
        super.setY(_y);
    }

    @Override
    public void setBounds(float _x, float _y, float _width, float _height) {
        setX(_x);
        setY(_y);
        super.setBounds(_x, _y, _width, _height);
    }
    
    public int getXCase() 
    {
        return xCase;
    }
    
    public int getYCase() 
    {
        return yCase;
    }
}
