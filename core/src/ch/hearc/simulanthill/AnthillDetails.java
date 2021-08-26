package ch.hearc.simulanthill;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

import ch.hearc.simulanthill.actors.Anthill;
import ch.hearc.simulanthill.actors.ElementActor;

public class AnthillDetails extends Group {

    private VisTable info;
    private VisLabel lblAntNumberInf;
    private VisLabel lblAntNumber;
    private VisLabel lblRessourceNumberInf;
    private VisLabel lblRessourceNumber;

    private Anthill anthill;
    private ElementActor hoverAnthill;
    private ElementActor background;

    public AnthillDetails(Anthill _anthill)
    {
        super();
        anthill = _anthill;
        
        setPosition(anthill.getX(), anthill.getY());

        info = new VisTable();
        info. align(Align.bottomLeft);
        info.setPosition(anthill.getWidth()+10, 0);
        
        lblAntNumberInf = new VisLabel("Nombre de fourmis : ");
        lblAntNumberInf.setColor(Color.RED);
        lblAntNumberInf.setFontScale(1.2f);
        


        lblAntNumber = new VisLabel("0");
        lblAntNumber.setColor(Color.RED);
        lblAntNumber.setFontScale(1.2f);

        lblRessourceNumberInf = new VisLabel("Nombre de ressource : ");
        lblRessourceNumberInf.setColor(Color.RED);
        lblRessourceNumberInf.setFontScale(1.2f);

        lblRessourceNumber = new VisLabel("0");
        lblRessourceNumber.setColor(Color.RED);
        lblRessourceNumber.setFontScale(1.2f);

        info.add(lblAntNumberInf).fillX();
        info.add(lblAntNumber);
        info.row();
        info.add(lblRessourceNumberInf).fillX();
        info.add(lblRessourceNumber);

        info.setVisible(false);

        hoverAnthill = new ElementActor(0,0, Asset.anthill(), "hoverAnthill");
        hoverAnthill.setSize(anthill.getWidth(), anthill.getHeight());

        background = new ElementActor(anthill.getWidth()+10, 0, Asset.obstacle(), "hoverAnthill");
        background.setSize(250,75);
        background.setVisible(false);

        hoverAnthill.addListener(new ClickListener()
        {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                hoverAnthill.setSprite(Asset.anthillSelection(), anthill.getWidth(), anthill.getHeight());
                info.setVisible(true);
                super.enter(event, x, y, pointer, fromActor);
                background.setVisible(true);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                hoverAnthill.setSprite(Asset.anthill(), anthill.getWidth(), anthill.getHeight());
                info.setVisible(false);
                background.setVisible(false);
                super.enter(event, x, y, pointer, fromActor);
            }
        });
        addActor(hoverAnthill);
        addActor(background);
        addActor(info);
        
    }

    @Override
    public void act(float delta) {
        lblAntNumber.setText(anthill.getNbAnts());
        lblRessourceNumber.setText(anthill.getNbResource());
        super.act(delta);
    }

}
