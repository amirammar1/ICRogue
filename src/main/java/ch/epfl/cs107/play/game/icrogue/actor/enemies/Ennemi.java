package ch.epfl.cs107.play.game.icrogue.actor.enemies;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

abstract public class Ennemi extends ICRogueActor {
    protected boolean isAlive=true;

    public boolean isAlive() {
        return isAlive;
    }
    private final DiscreteCoordinates position;
    public Ennemi(Orientation orientation, DiscreteCoordinates position, Area area){
        super(area, orientation, position);
        this.position=position;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    public DiscreteCoordinates getPosition_ennemi() {
        return position;
    }

    public void mourrir(){isAlive=false;}

    @Override
    public void update(float deltaTime) {
        if(!isAlive)getOwnerArea().unregisterActor(this);
        super.update(deltaTime);
    }
}
