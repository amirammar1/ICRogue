package ch.epfl.cs107.play.game.icrogue.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;
import java.util.List;

public class Connector extends AreaEntity implements Interactable {
    public Connector(Connector transCon) {
        super(transCon.getOwnerArea(),transCon.getOrientation(),transCon.coords);

    }

    public enum Status{OPEN, CLOSED, LOCKED, INVISIBLE}
    private Status status;
    private String AireDestination;
    private DiscreteCoordinates coords;
    int NO_KEY_ID;
    private Sprite sprite;

    public void setAireDestination(String AireDestination){
        this.AireDestination=AireDestination;
    }
    public Connector(Area area, DiscreteCoordinates position, Orientation orientation){
        super(area,orientation,position);
        coords=position;
        status=Status.INVISIBLE;
        sprite=new Sprite("icrogue/invisibleDoor_"+orientation.ordinal(), (orientation.ordinal()+1)%2+1, orientation.ordinal()%2+1, this);
    }

    protected Status getStatus() {
        return status;
    }

    @Override
    public void draw(Canvas canvas) {
        if(status!=Status.OPEN)sprite.draw(canvas);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        DiscreteCoordinates coord = getCurrentMainCellCoordinates();
        return List.of(coord, coord.jump(new Vector((getOrientation().ordinal()+1)%2, getOrientation().ordinal()%2)));
    }

    @Override
    public boolean takeCellSpace() {
        return this.status != Status.OPEN;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) v).interactWith(this, isCellInteraction);
    }
    public void id(int id){
        NO_KEY_ID=id;
    }
    public void change_status(Status status){
        this.status=status;
    }

    public void update(float deltaTime) {
        Sprite_update();
        super.update(deltaTime);
    }
    private void Sprite_update(){
        if(status==Status.CLOSED)sprite=new Sprite("icrogue/door_"+getOrientation().ordinal(), (getOrientation().ordinal()+1)%2+1, getOrientation().ordinal()%2+1, this);
        if(status==Status.LOCKED)sprite=new Sprite("icrogue/lockedDoor_"+getOrientation().ordinal(), (getOrientation().ordinal()+1)%2+1, getOrientation().ordinal()%2+1, this);
    }

}
