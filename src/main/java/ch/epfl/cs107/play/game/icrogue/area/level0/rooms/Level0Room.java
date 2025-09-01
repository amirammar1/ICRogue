package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.ICRogue;
import ch.epfl.cs107.play.game.icrogue.area.ConnectorInRoom;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.List;

public class Level0Room extends ICRogueRoom {
    public Level0Room(DiscreteCoordinates coordinates){
        super(Level0Connectors.getAllConnectorsPosition(),Level0Connectors.getAllConnectorsOrientation(),"icrogue/Level0Room",coordinates);
    }

    @Override
    public float getCameraScaleFactor() {
        return ICRogue.CAMERA_SCALE_FACTOR;
    }


    @Override
    public String getTitle() {
        return ("icrogue/level0"+Integer.toString(getRoomCoordinates().x)+Integer.toString(getRoomCoordinates().y));
    }



    public enum Level0Connectors implements ConnectorInRoom { // ordre des attributs: position, destination, orientation
         W(new DiscreteCoordinates(0, 4), new DiscreteCoordinates(8, 5), Orientation.LEFT),
         S(new DiscreteCoordinates(4, 0), new DiscreteCoordinates(5, 8), Orientation.DOWN),
         E(new DiscreteCoordinates(9, 4), new DiscreteCoordinates(1, 5), Orientation.RIGHT),
         N(new DiscreteCoordinates(4, 9), new DiscreteCoordinates(5, 1), Orientation.UP);

         public final DiscreteCoordinates position;
         public final DiscreteCoordinates destination;
         public final Orientation orientation;

        Level0Connectors(DiscreteCoordinates position,DiscreteCoordinates destination,Orientation orientation){
            this.position=position;
            this.destination=destination;
            this.orientation=orientation;
        }

        @Override
        public int getIndex() {
            return this.ordinal();
        }

        @Override
        public DiscreteCoordinates getDestination() {
            return destination;
        }


        public Orientation getOrientation() {
            return orientation;
        }

        public static List<Orientation> getAllConnectorsOrientation() {
            List<Orientation> connector_or= new ArrayList<>();
            for (Level0Connectors ict : Level0Connectors.values()) {
                connector_or.add(ict.orientation);
            }
            return connector_or;
        }
        public static List<DiscreteCoordinates> getAllConnectorsPosition(){
            List<DiscreteCoordinates> coords= new ArrayList<>();
            for (Level0Connectors ict : Level0Connectors.values()) {
                coords.add(ict.position);
            }
            return coords;
        }
    }
}
