package ch.hearc.simulanthill.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.Batch;

public class ElementActor extends Actor 
{
    Sprite sprite;

    public ElementActor(float _x, float _y, Texture _texture, final String _actorName) 
    {
      sprite = new Sprite(_texture);
      
      setPosition(_x, _y);
    }

    public ElementActor(Texture _texture, final String _actorName) 
    {
      this(0, 0, _texture, _actorName);
    }
   
   @Override
   public void setPosition(float _x, float _y) 
   {
    // TODO Auto-generated method stub
    sprite.setPosition(_x, _y);
    super.setPosition(_x, _y);
   }

    @Override
    public void setSize(float _width, float _height) 
    {
      // TODO Auto-generated method stub
      sprite.setSize(_width, _height);
      super.setSize(_width, _height);
    }
   
    @Override
    public void act(float _delta) 
    {
      super.act(_delta);
    }

       
    @Override
    public void draw(Batch _batch, float _parentAlpha) 
    {
      sprite.draw(_batch);
    }

    @Override
    public String toString() 
    {
      return "";
    }
  }
