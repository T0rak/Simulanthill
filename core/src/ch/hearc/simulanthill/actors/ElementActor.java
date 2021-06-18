package ch.hearc.simulanthill.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Holds the element informations. It's a superclasse of all elements used in simulantHill (ants, obstacle, resources, ...)
 */
public class ElementActor extends Actor 
{
    Sprite sprite;

    /**
     * Main constructor of ElementActor
     * @param _x x coordinate of the position
     * @param _y y coordinate of the position
     * @param _texture texture of the element
     * @param _actorName name of the actor
     */
    public ElementActor(float _x, float _y, Texture _texture, final String _actorName) 
    {
      sprite = new Sprite(_texture);
      
      setPosition(_x, _y);
    }

    /**
     * Constructor of ElementActor with coordinates to 0x 0y
     * @param _actorName name of the actor
     * @param _texture texture of the element
     */
    public ElementActor(Texture _texture, final String _actorName) 
    {
      this(0, 0, _texture, _actorName);
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
  /*
    @Override
    public String toString() 
    {
      return "";
    }
    */
  }
