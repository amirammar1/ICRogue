package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.items.Item;
import ch.epfl.cs107.play.game.icrogue.actor.items.Key;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class Level0KeyRoom extends Level0ItemRoom {
    protected Item item;
    protected Key key;
    public Level0KeyRoom(DiscreteCoordinates coordinates, int ID_Key){
        super(coordinates);
        key= new Key(this, Orientation.DOWN,new DiscreteCoordinates(5,5),true,ID_Key);

    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            addItem(key);
            key.enterArea(this,new DiscreteCoordinates(5,5));
            return true;
        }
        return false;

    }
}
