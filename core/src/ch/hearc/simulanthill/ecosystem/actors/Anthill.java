package ch.hearc.simulanthill.ecosystem.actors;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.MathUtils;

import ch.hearc.simulanthill.ecosystem.Ecosystem;
import ch.hearc.simulanthill.ecosystem.MapTile;
import ch.hearc.simulanthill.tools.Asset;
import ch.hearc.simulanthill.tools.ColorManagement;
import com.badlogic.gdx.graphics.Color;
/**
 * The anthill actor that makes ants spawning
 */
public class Anthill extends MapTile
{ 
    public static final int PHEROMONE_LIVE_TIME_DEFAULT = 150;
    public static final int ANT_PHEROMONE_RELEASE_FREQUENCY_DEFAULT = 0;
    public static final float ANT_SPEED_FACTOR_DEFAULT = 1;
    public static final int ANT_FIELD_OF_VIEW_DEFAULT = 3;
    public static final int ANT_INDEPENDENCE_DEFAULT = 10;

    private static int idGenerator = 0;
    private static int AntCreationRate = 5;

    private final Color antColor = ColorManagement.nextColor();

    private int id;
    private int nbAnts;
    private int nbResources;
    private List<Ant> antList;
    
    private int pheromoneLiveTime;
    private int antPheromoneReleaseFrequency;

    private float antSpeedFactor;
    private int antFieldOfView;
    private int antIndependance;
    private int maxAnts;

    private float antSizeFactor;

    private int AntCreationCountDown;

    /**
	 * Main constructor
	 * @param  _x the initial x position of the ant 
     * @param  _y the initial y position of the ant
     * @param  _width the width of the ant
     * @param  _height the height of the ant
	 */
    public Anthill(int _caseX, int _caseY, int _pheromoneLiveTime, int _antPheromoneReleaseFrequency, float _antSpeedFactor, int _antFieldOfView, int _antIndependance, int _maxAnts, float _antSizeFactor) 
    {
        super(_caseX, _caseY, Asset.anthill(), Ecosystem.getCurrentEcosystem());

        pheromoneLiveTime = _pheromoneLiveTime;
        antPheromoneReleaseFrequency = _antPheromoneReleaseFrequency;

        antSpeedFactor = _antSpeedFactor;
        antFieldOfView = _antFieldOfView;
        antIndependance = _antIndependance;
        
        maxAnts = _maxAnts;
        antSizeFactor = _antSizeFactor;

        antList = new ArrayList<Ant>();
        id = idGenerator ++;
        AntCreationCountDown = MathUtils.random(1, AntCreationRate);
    }

    public Anthill(int _caseX, int _caseY) 
    {
        this(_caseX, _caseY, PHEROMONE_LIVE_TIME_DEFAULT, ANT_PHEROMONE_RELEASE_FREQUENCY_DEFAULT, ANT_SPEED_FACTOR_DEFAULT, ANT_FIELD_OF_VIEW_DEFAULT, ANT_INDEPENDENCE_DEFAULT, 500, 1);
    }

    /**
    * Spawns ants if there are not enough
	* @param _delta  the time passed since the last act call
    */
    public void act(float _delta)
    {
        super.act(_delta);

        AntCreationCountDown--;
        
        if (ecosystem.getNbAnt() < ecosystem.getNbAntMax() && AntCreationCountDown < 0)
        {
            createAnt();
            AntCreationCountDown = AntCreationRate;
            
        }
    }

    /**
     * Spawns an ant
     */
    public void createAnt()
    {
        createAntAt(getCenteredX(), getCenteredY(), 0, AntState.SEARCHING_RESOURCE);
    }

    public void createAntAt(float _x, float _y, int _stepFrom, AntState _state) 
    {
        if (!ecosystem.isObstacle(ecosystem.floatToGridCoordinate(_x), ecosystem.floatToGridCoordinate(_y))) 
        {
            nbAnts++;

            Ant newAnt = new Ant(_x, _y,  antSizeFactor * getWidth(), antSizeFactor * getHeight(), this, _stepFrom, _state);
            antList.add(newAnt);
            ecosystem.addAnt(newAnt);
        }
    }
    /**
     * Removes an ant
     * @param ant  the ant that will removed
     */
    public void removeAnt(Ant _ant)
    {
        nbAnts--;
        antList.remove(_ant);
        ecosystem.removeAnt(_ant);
    }
    
    public void addResource(int _nbResources)
    {
        nbResources += _nbResources;
    }

    public int getNbAnts()
    {
        return nbAnts;
    }
    public int getNbResource()
    {
        return nbResources;
    }

    public int getId()
    {
        return id;
    }

    public Color getAntColor() {
        return antColor;
    }

    public void removeAllAnts() {
        for (Ant ant : antList) {
            ant.remove();
        }
        nbAnts = 0;
    }
}
