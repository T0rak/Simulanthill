package ch.hearc.simulanthill.actors;

import ch.hearc.simulanthill.Ecosystem;

/**
* Represents a resource actor that progressively disapears when ants take it
*/
public class Resource extends ElementActor 
{
    private static final int INIT_CAPACITY = 10;
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
        super(_x, _y, Asset.resource(), "food");
        setSize(_width, _height);
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
        if (this.capacity < _quantity) 
        {
            ret = this.capacity;
            remove();
        } else {
            this.capacity -= _quantity;
            ret = _quantity;
            
        }
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
		Ecosystem.getCurrentEcosystem().removeResource(getX(), getY());
		return super.remove();
	}
}
