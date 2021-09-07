package ch.hearc.simulanthill.ecosystem.actors;

import com.badlogic.gdx.graphics.Color;

import ch.hearc.simulanthill.ecosystem.Ecosystem;
import ch.hearc.simulanthill.tools.Asset;

/**
* Represents a resource actor that progressively disapears when ants take it
*/
public class Resource extends ElementActor 
{
    private static final int INIT_CAPACITY = 10;
    public static final Color color = new Color(131/255f, 2/255f, 2/255f, 1);
    int capacity;
    /**
    * Main constructor
     * @param  _x the initial x position of the ant 
     * @param  _y the initial y position of the ant
     * @param  _width the width of the ant
     * @param  _height the height of the ant
    */
    public Resource(float _x, float _y, float _width, float _height) 
    {
        super(_x, _y, _width, _height, Asset.pixel(color));
        this.capacity = INIT_CAPACITY;
    }

    /**
    * Decreases the resource capacity and returns the quantity
     * @param  _quantity the quantity to remove from the capacity
     * @return the quantity removed from the capacity
    */
    public int decrease(int _quantity) 
    {
        int ret = 0;
        if (this.capacity <= _quantity) 
        {
            ret = this.capacity;
            remove();
        } else {
            ret = _quantity;
        }

        this.capacity -= _quantity;
        return ret;
    }

    /**
	 * Behaviour of a resource
	 * @param _delta  the time passed since the last act call
	 */
    @Override
    public void act(float _delta) 
    {
        super.act(_delta);
        sprite.setAlpha((float)capacity / INIT_CAPACITY);
    }

    /**
	 * Defins how the resource needs to be deleted
	 */
    @Override
	public boolean remove() 
    {
		Ecosystem.getCurrentEcosystem().removeResource(getX()+ this.getWidth()/2, getY()+ this.getHeight()/2);
		return super.remove();
	}
}
