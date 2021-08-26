package ch.hearc.simulanthill.actors;

import com.badlogic.gdx.graphics.Texture;

/**
 * Holds the element informations. It's a superclasse of all elements used in simulantHill (ants, obstacle, resources, ...)
 */
public class ElementActor extends SpriteActor 
{

    /**
     * Main constructor of ElementActor
     * @param _x x coordinate of the position
     * @param _y y coordinate of the position
     * @param _texture texture of the element
     * @param _actorName name of the actor
     */
    public ElementActor(float _x, float _y, float _width, float _height, Texture _texture) 
    {
      super(_x, _y, _width, _height, _texture);
      sprite.setOrigin(_width / 2, _height / 2);
    }
  }
