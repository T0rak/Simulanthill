package ch.hearc.simulanthill.drawable;

import com.badlogic.gdx.graphics.Texture;

import ch.hearc.simulanthill.model.Element;

public abstract class RectImageDrawable implements Drawable {
    private Texture image;
    private Element element;

    @Override
    public void draw()
    {
        getX();
        getY();
    }

    @Override
    public int getX()
    {
        return element.getX();
    }

    @Override
    public int getY()
    {
        return element.getY();
    }
}
