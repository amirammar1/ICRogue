package ch.epfl.cs107.play.game.icrogue.actor.projectiles;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Arrow extends Projectile{
    private final Sprite sprite;
    public Arrow(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position, 4,1);
        sprite=new Sprite("zelda/arrow", 1f, 1f, this, new RegionOfInterest(32*orientation.ordinal(), 0, 32, 32), new Vector(0, 0));
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }
}
