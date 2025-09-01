package ch.epfl.cs107.play.game.icrogue.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.List;

abstract public class ICRogueRoom extends Area implements Logic {
    private boolean player_enter;
    public void entred_player(){player_enter=true;}
    private ICRogueBehavior behavior;

    /**
     * initialise le level
     */
    protected void initlevel() {
        registerActor(new Background(this,getBehaviorName()));
        for (Connector connector : connectors) {
            registerActor(connector);
        }
        registerActor(new Foreground(this,null,getBehaviorName()));
    }
    protected String behaviorName;
    public DiscreteCoordinates roomCoordinates;
    protected List<Connector> connectors=new ArrayList<>();

    public List<Connector> getConnectors() {
        return connectors;
    }

    /**
     *
     * @param connectorsCoordinates coordonnees des connecteurs
     * @param orientations leurs orientation
     * @param behaviorName nom du compartement
     * @param roomCoordinates coordonnee de l'aire
     */
    public ICRogueRoom(List<DiscreteCoordinates> connectorsCoordinates, List<Orientation> orientations, String behaviorName, DiscreteCoordinates roomCoordinates){
        this.behaviorName=behaviorName;
        this.roomCoordinates=roomCoordinates;
        for (int i = 0; i < orientations.size(); i++) {
            connectors.add(new Connector(this,connectorsCoordinates.get(i),orientations.get(i).opposite()));
        }
    }

    protected String getBehaviorName(){
        return behaviorName;
    }
    public DiscreteCoordinates getRoomCoordinates(){
        return new DiscreteCoordinates(roomCoordinates.x, roomCoordinates.y);
    }


    public String getTitle() {
        return "ICRogueRoom";
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            behavior = new ICRogueBehavior(window, behaviorName);
            setBehavior(behavior);
            initlevel();
            return true;
        }
        return false;
    }

    @Override
    public void end() {
    }

    @Override
    public boolean isOn() {
        return player_enter;
    }

    @Override
    public boolean isOff() {
        return !player_enter;
    }

    @Override
    public float getIntensity() {
        return 0;
    }
    /// EnigmeArea extends Area

}
