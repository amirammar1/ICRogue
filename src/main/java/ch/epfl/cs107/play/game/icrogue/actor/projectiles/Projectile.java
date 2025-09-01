package ch.epfl.cs107.play.game.icrogue.actor.projectiles;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.Collections;
import java.util.List;

abstract public class Projectile extends ICRogueActor implements Consumable, Interactor, ICRogueInteractionHandler {

    private ICRogueProjectileInteractionHandler handler;
    protected int DEFAULT_DAMAGE=1;
    protected int DEFAULT_MOVE_DURATION=10;

    protected int frame;
    protected int dammagePoint;
    protected boolean isConsumed;

    public Projectile(Area area, Orientation orientation, DiscreteCoordinates position,int frame,int dammagePoint){
        super(area, orientation, position);
        this.frame=frame;
        this.dammagePoint=dammagePoint;
        handler=new ICRogueProjectileInteractionHandler();

    }
    public Projectile(Area area, Orientation orientation, DiscreteCoordinates position){
        super(area, orientation, position);
        dammagePoint=DEFAULT_DAMAGE;
        frame=DEFAULT_MOVE_DURATION;
    }

    public void consume(){
        isConsumed=true;
    }
    public boolean isConsumed(){
        return isConsumed;
    }


    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public void update(float deltaTime) {
        Keyboard keyboard= getOwnerArea().getKeyboard();
        if(isConsumed())getOwnerArea().unregisterActor(this);
        orientate(super.getOrientation());
        move(frame);
        super.update(deltaTime);
    }
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());

    }
    public List<DiscreteCoordinates> getFieldOfViewCells(){
        return Collections.singletonList (getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }
    public boolean wantsCellInteraction(){
        return true;
    }
    public boolean wantsViewInteraction(){
        return true;
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }
    private class ICRogueProjectileInteractionHandler implements ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler{
        public void interactWith(Turret turret,boolean isCellInteraction ){
            if(isCellInteraction && Projectile.this instanceof Fire){
                turret.mourrir();
                Projectile.this.consume();
            }
        }
        public void interactWith(Arrow arrow,boolean isCellInteraction){
            if(isCellInteraction){
                arrow.consume();
                Projectile.this.consume();
            }
        }
        public void interactWith(ICRoguePlayer player, boolean isCellInteraction ){
            if(isCellInteraction && Projectile.this instanceof Arrow){
                player.damage();
                Projectile.this.consume();
                Projectile.this.getOwnerArea().unregisterActor(Projectile.this);
            }
        }
        public void interactWith(ICRogueBehavior.ICRogueCell cell, boolean isCellInteraction) {
            if(!isCellInteraction &&(cell.getType()== ICRogueBehavior.ICRogueCellType.WALL||cell.getType()== ICRogueBehavior.ICRogueCellType.HOLE)){
                Projectile.this.consume();
            }
        }



    }


}
