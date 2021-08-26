package ch.hearc.simulanthill.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class ColorActor extends SpriteActor{

    public ColorActor(float _x, float _y, float _width, float _height, Color _color) {
        super(_x, _y, _width, _height, createColorImage(_color));
        
    }

    public static Texture createColorImage(Color _color)
    {
        Pixmap bgPixmap = new Pixmap(1,1, Pixmap.Format.RGB565);
        bgPixmap.setColor(_color);
        bgPixmap.fill();
        return new Texture(bgPixmap);
    }

}
