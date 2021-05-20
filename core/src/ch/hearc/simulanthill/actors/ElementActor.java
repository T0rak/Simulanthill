package ch.hearc.simulanthill.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;

//import ch.hearc.simulanthill.model.Element;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.Gdx;

public class ElementActor extends Actor {
    Sprite sprite;


    public ElementActor(float x, float y, Texture texture, final String actorName/*, Element element*/) {
      sprite = new Sprite(texture);
      //setPos(x, y);
      
      setPosition(x, y);
      setTouchable(Touchable.enabled);
      
      addListener(new InputListener() {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
          Gdx.app.log("Object at position: ", ElementActor.this.toString());
          return true;
        }
      });
    }

    public ElementActor(Texture texture, final String actorName) {
      this(0, 0, texture, actorName);
    }
   
   @Override
   public void setPosition(float x, float y) {
    // TODO Auto-generated method stub
    sprite.setPosition(x, y);
    super.setPosition(x, y);
   }

    @Override
    public void setSize(float width, float height) {
      // TODO Auto-generated method stub
      sprite.setSize(width, height);
      super.setSize(width, height);
    }
   
    @Override
    public void act(float delta) {
      super.act(delta);
    }
   
    @Override
    public void draw(Batch batch, float parentAlpha) {
      sprite.draw(batch);
    }

    @Override
    public String toString() {
      return "";
    }
  }
