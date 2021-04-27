package ch.hearc.simulanthill.drawable;

import com.badlogic.gdx.math.Rectangle;

public interface Drawable {
    public void draw();
    public int getX();
    public int getY();
    public void collidingItems();
}
