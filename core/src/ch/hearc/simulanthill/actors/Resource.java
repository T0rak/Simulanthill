package ch.hearc.simulanthill.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class Resource extends ElementActor 
{
    int capacity;

    public Resource(float _x, float _y, float _width, float _height, int _capacity) {
        super(_x, _y, Asset.resource(), "food");
        setSize(_width, _height);
        this.capacity = _capacity;
        setTouchable(Touchable.enabled);
    }

    public int reduce(int _reduction) {
        int ret = 0;
        if (this.capacity < _reduction) {
            ret = this.capacity;
            this.remove();

        } else {
            this.capacity -= _reduction;
            ret = _reduction;
            
        }
        return ret;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
