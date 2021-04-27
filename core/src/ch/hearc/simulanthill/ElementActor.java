package ch.hearc.simulanthill;

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
    protected Vector2 position;

    public ElementActor(float x, float y, Texture texture, final String actorName/*, Element element*/) {
      sprite = new Sprite(texture);
      this.position = new Vector2(x, y);
      setPos(x, y);

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
   
    public void setPos(float x, float y){
      sprite.setPosition(x, y);
      boundFromSprite();
    }

    public void setSize(float w, float h) {
      sprite.setSize(w, h);
      boundFromSprite();
    }

    private void boundFromSprite() {
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

    @Override
    public String toString() {
      return "";
    }
  }
