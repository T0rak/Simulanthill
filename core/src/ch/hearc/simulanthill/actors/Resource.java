package ch.hearc.simulanthill.actors;

import ch.hearc.simulanthill.Ecosystem;

public class Resource extends ElementActor 
{
    private static final int INIT_CAPACITY = 10;
    int capacity;

    public Resource(float _x, float _y, float _width, float _height) {
        super(_x, _y, Asset.resource(), "food");
        setSize(_width, _height);
        this.capacity = INIT_CAPACITY;
    }

    public int decrease(int _quantity) {
        int ret = 0;
        if (this.capacity < _quantity) {
            ret = this.capacity;
            remove();
        } else {
            this.capacity -= _quantity;
            ret = _quantity;
            
        }
        return ret;
    }

    @Override
    public void act(float _delta) {
        super.act(_delta);
        sprite.setAlpha((float)capacity / INIT_CAPACITY);
    }

    
    @Override
	public boolean remove() {
		Ecosystem.getCurrentEcosystem().removeResource(getX(), getY());
		return super.remove();
	}
}
