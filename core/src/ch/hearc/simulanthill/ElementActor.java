package ch.hearc.simulanthill;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import ch.hearc.simulanthill.model.Element;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.Gdx;

public class ElementActor extends Actor
{
    Sprite sprite;
   
    public ElementActor(Texture texture, final String actorName, Element element) 
    {
      sprite = new Sprite(texture);
   
      setPos(element.getX(), element.getY());
      
      setTouchable(Touchable.enabled);
   
      addListener(new InputListener() 
      {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) 
        {
          Gdx.app.log("Touch down asset with name ", actorName);
          return true;
        }
      });
    }
   
    public void setPos(float x, float y){
      sprite.setPosition(x, y);
      boundFromSprite();
    }

    public void setSize(float w, float h)
    {
      sprite.setSize(w, h);
      boundFromSprite();
    }

    private void boundFromSprite()
    {
      setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }
   
    @Override
    public void act(float delta) {
      super.act(delta);
    }
   
    @Override
    public void draw(Batch batch, float parentAlpha) {
      sprite.draw(batch);
    }
  }
