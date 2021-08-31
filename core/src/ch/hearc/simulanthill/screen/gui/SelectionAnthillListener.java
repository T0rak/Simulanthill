package ch.hearc.simulanthill.screen.gui;

import com.badlogic.gdx.scenes.scene2d.EventListener;

import ch.hearc.simulanthill.ecosystem.actors.Anthill;

public interface SelectionAnthillListener{
    public void selected(Anthill anthill);
    public void unselected();
}
