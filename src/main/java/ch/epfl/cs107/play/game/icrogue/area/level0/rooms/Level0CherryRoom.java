package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.items.Cherry;
import ch.epfl.cs107.play.game.icrogue.actor.items.Item;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class Level0CherryRoom extends Level0ItemRoom{
    protected Item item;
    protected Cherry cherry;
    public Level0CherryRoom(DiscreteCoordinates coords){
        super(coords);
        cherry= new Cherry(this, Orientation.DOWN,new DiscreteCoordinates(5,5),true);

    }
    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            addItem(cherry);
            cherry.enterArea(this,new DiscreteCoordinates(5,5));
            return true;
        }
        return false;

    }
}
