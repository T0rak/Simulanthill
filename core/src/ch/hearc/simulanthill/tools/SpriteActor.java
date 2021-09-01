package ch.hearc.simulanthill.tools;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class SpriteActor extends Actor
{
    private static Map<Color, Texture> ColorTexture = new HashMap<Color, Texture>();
    protected Sprite sprite;
    

    public SpriteActor(float _x, float _y, Texture _texture) 
    {
        super();
        sprite = new Sprite(_texture);
        setPosition(_x, _y);
    }

    public SpriteActor(float _x, float _y, float _width, float _height, Color _color) {
    this(_x, _y, _width, _height, createColorImage(_color));
    
    }

    public static Texture createColorImage(Color _color)
    {
        if (ColorTexture.containsKey(_color))
        {
            return ColorTexture.get(_color);
        }
        
        Pixmap bgPixmap = new Pixmap(1, 1, Pixmap.Format.RGB565);
        bgPixmap.setColor(_color);
        bgPixmap.fill();
        Texture texture = new Texture(bgPixmap);
        ColorTexture.put(_color, texture);
        return texture;
    }

        public SpriteActor(float _x, float _y, float _width, float _height, Texture _texture) 
        {
            this(_x,_y, _texture);
            setSize(_width, _height);
        }
    
        /**
         * sets the element's position on the scene
         * @param _x x coordinate of the position
         * @param _y y coordinate of the position
         */
        @Override
        public void setPosition(float _x, float _y) 
        {
        sprite.setPosition(_x, _y);
        super.setPosition(_x, _y);
        }

        /**
         * Sets the element's size on the scene
         * @param _width the width wanted
         * @param _height the width height
         */
        @Override
        public void setSize(float _width, float _height) 
        {
        sprite.setSize(_width, _height);
        super.setSize(_width, _height);
        } 

        @Override
        public void setBounds(float _x, float _y, float _width, float _height) {
            setPosition(_x, _y);
            setSize(_width, _height);
        }
    
        /**
         * gives an act to some elements 
         * @param _delta the time elapsed since the last act.
         */
        @Override
        public void act(float _delta) 
        {
        super.act(_delta);
        }

        /**
         * Draws the elementActor on the scene. 
         */
        @Override
        public void draw(Batch _batch, float _parentAlpha) 
        {
        sprite.draw(_batch);
        }

        public void setSprite(Texture _texture)
        {
        sprite = new Sprite(_texture);
        sprite.setSize(getWidth(), getHeight());
        }
    /*
        @Override
        public String toString() 
        {
        return "";
    }
    */
  }
