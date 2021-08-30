package ch.hearc.simulanthill.screen.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

import ch.hearc.simulanthill.ecosystem.actors.Anthill;
import ch.hearc.simulanthill.tools.Asset;
import ch.hearc.simulanthill.tools.SpriteActor;

public class AnthillDetails extends Group {

    private VisTable info;
    private VisLabel lblAntNumberInf;
    private VisLabel lblAntNumber;
    private VisLabel lblRessourceNumberInf;
    private VisLabel lblRessourceNumber;

    private Anthill anthill;
    private SpriteActor hoverAnthill;
    private SpriteActor background;

    public AnthillDetails(Anthill _anthill)
    {
        super();
        anthill = _anthill;
        
        setPosition(anthill.getX(), anthill.getY());

        info = new VisTable();
        info.align(Align.bottomLeft);
        info.setPosition(anthill.getWidth()+10, 0);
        
        lblAntNumberInf = new VisLabel("Nombre de fourmis : ");
        lblAntNumber = new VisLabel("0");

        lblRessourceNumberInf = new VisLabel("Nombre de ressource : ");
        lblRessourceNumber = new VisLabel("0");

        info.add(lblAntNumberInf).fillX();
        info.add(lblAntNumber).fillX();
        info.row();
        info.add(lblRessourceNumberInf).fillX();
        info.add(lblRessourceNumber).fillX();

        for (Actor element : info.getChildren()) {
            element.setColor(Color.WHITE);
            if(element.getClass() == VisLabel.class)
            {
                ((VisLabel)element).setFontScale(1.5f);
            }
        }

        hoverAnthill = new SpriteActor(0,0,anthill.getWidth(), anthill.getHeight(), Asset.anthill());
        background = new SpriteActor(anthill.getWidth()+10, 0, info.getMinWidth(), info.getMinHeight(), Color.BLACK);
        
        setVisible(false);

        hoverAnthill.addListener(new ClickListener()
        {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                
                setVisible(true);
                super.enter(event, x, y, pointer, fromActor);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                setVisible(false);
                super.enter(event, x, y, pointer, fromActor);
            }
        });
        addActor(hoverAnthill);
        addActor(background);
        addActor(info);
        
    }

    @Override
    public void setVisible(boolean _visible) {
        if (!_visible)
        {
            hoverAnthill.setSprite(Asset.anthill());
        }
        else
        {
            hoverAnthill.setSprite(Asset.anthillSelection());
        }
        
        info.setVisible(_visible);
        background.setVisible(_visible);
    }

    @Override
    public void act(float delta) {
        lblAntNumber.setText(anthill.getNbAnts());
        lblRessourceNumber.setText(anthill.getNbResource());
        background.setSize(info.getMinWidth(), info.getMinHeight());
        super.act(delta);
    }

}
