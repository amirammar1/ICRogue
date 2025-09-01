package ch.epfl.cs107.play.game.icrogue.actor.items;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

abstract public class Item extends CollectableAreaEntity implements Interactable, ICRogueInteractionHandler {

    protected boolean isConsumed;
    public Item(Area area, Orientation orientation, DiscreteCoordinates position, boolean isCollected) {
        super(area, orientation, position, isCollected);
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }


    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    public void enterArea(Area area, DiscreteCoordinates position){
        area.registerActor(this);
        setOwnerArea(area);
        setCurrentPosition(position.toVector());
    }
    @Override
    public void update(float deltaTime) {
        if(isConsumed())getOwnerArea().unregisterActor(this);
        super.update(deltaTime);
    }

    public void consume(){
        isConsumed=true;
    }
    public boolean isConsumed(){
        return isConsumed;
    }
}
