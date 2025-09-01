package ch.epfl.cs107.play.game.icrogue.actor.enemies;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Arrow;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Turret extends Ennemi{
    Sprite sprite;
    Orientation[] fleches_dir;

    public Turret( Orientation orientation, DiscreteCoordinates position, Area area, Orientation[] fleches_dir) {
        super( orientation, position, area);
        sprite=new Sprite("icrogue/static_npc", 1.5f, 1.5f, this, null, new Vector(-0.25f, 0));
        this.fleches_dir=fleches_dir;
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }

    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) v).interactWith(this, isCellInteraction);
    }
    final float COOLDOWN=2.f;
    float contor=0.f;

    @Override
    public void update(float deltaTime) {
        contor+=deltaTime;
        if((int)(contor)==COOLDOWN) {
            for (Orientation orientation : fleches_dir) {
                Arrow arrow = new Arrow(getOwnerArea(), orientation, getCurrentMainCellCoordinates());
                arrow.enterArea(getOwnerArea(), getCurrentMainCellCoordinates());
            }
            contor = 0.f;
        }

        super.update(deltaTime);
    }
}
