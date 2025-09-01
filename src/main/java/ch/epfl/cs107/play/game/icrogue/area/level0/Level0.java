package ch.epfl.cs107.play.game.icrogue.area.level0;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.game.icrogue.area.Level;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.*;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level0 extends Level {
    public enum RoomType { TURRET_ROOM(3), // type and number of roon
        STAFF_ROOM(1),
        BOSS_KEY(1),
        SPAWN(1),
        NORMAL(1),
        CHERRY_ROOM(2);

        public final int k;

        RoomType(int k) {
            this.k = k;
        }
        static public int[] tab(){
            int[] number=new int[RoomType.values().length];
            int i=0;
            for (RoomType type : RoomType.values()) {
                number[i]= type.k;
                i++;
            }
            return number;
        }
    }
    final private int PART_1_KEY_ID=0;
    final private int BOSS_KEY_ID=0;

    public Level0(){
        this(true);

    }
    public Level0(boolean randomMap){
        super(randomMap,new DiscreteCoordinates(1,1),RoomType.tab(),4,2);
    }


    private void generateMap1() {
        DiscreteCoordinates room00 = new DiscreteCoordinates(0, 0);
        setRoom(room00, new Level0KeyRoom(room00, PART_1_KEY_ID));
        setRoomConnector(room00, "icrogue/level010", Level0Room.Level0Connectors.E);
        lockRoomConnector(room00, Level0Room.Level0Connectors.E,  PART_1_KEY_ID);

        DiscreteCoordinates room10 = new DiscreteCoordinates(1, 0);
        setRoom(room10, new Level0Room(room10));
        setRoomConnector(room10, "icrogue/level000", Level0Room.Level0Connectors.W);
    }

    private void generateMap2() {
        DiscreteCoordinates room00 = new DiscreteCoordinates(0, 0);
        setRoom(room00, new Level0TurretRoom(room00));
        setRoomConnector(room00, "icrogue/level010", Level0Room.Level0Connectors.E);

        DiscreteCoordinates room10 = new DiscreteCoordinates(1,0);
        setRoom(room10, new Level0Room(room10));
        setRoomConnector(room10, "icrogue/level011", Level0Room.Level0Connectors.S);
        setRoomConnector(room10, "icrogue/level020", Level0Room.Level0Connectors.E);
        lockRoomConnector(room10, Level0Room.Level0Connectors.W,  BOSS_KEY_ID);
        setRoomConnectorDestination(room10, "icrogue/level000", Level0Room.Level0Connectors.W);

        DiscreteCoordinates room20 = new DiscreteCoordinates(2,0);
        setRoom(room20,  new Level0StaffRoom(room20));
        setRoomConnector(room20, "icrogue/level010", Level0Room.Level0Connectors.W);
        setRoomConnector(room20, "icrogue/level030", Level0Room.Level0Connectors.E);

        DiscreteCoordinates room30 = new DiscreteCoordinates(3,0);
        setRoom(room30, new Level0KeyRoom(room30, BOSS_KEY_ID));
        setRoomConnector(room30, "icrogue/level020", Level0Room.Level0Connectors.W);
        setCoord_arrive(new DiscreteCoordinates(2,0));
        DiscreteCoordinates room11 = new DiscreteCoordinates(1, 1);
        depart=new DiscreteCoordinates(1,1);
        setRoom (room11, new Level0Room(room11));
        setRoomConnector(room11, "icrogue/level010", Level0Room.Level0Connectors.N);
    }

    @Override
    protected void roomPlace(DiscreteCoordinates coords, int type) {
        ICRogueRoom room;
        switch (type) {
            case 0 -> room = new Level0TurretRoom(coords);
            case 1 -> room = new Level0StaffRoom(coords);
            case 2 -> room = new Level0KeyRoom(coords, BOSS_KEY_ID);
            case 3 -> {
                room = new Level0Room(coords);
                depart = coords;
            }
            case 4 -> room = new Level0Room(coords);
            case 5-> room = new Level0CherryRoom(coords);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
        setRoom(coords,room);
    }

    @Override
    protected void BossroomPlace(DiscreteCoordinates coords,ICRogueRoom boss,MapState[][] roomsPlacement) {
        boss=new Level0TurretRoom(coords);
        setRoom(coords,boss);
        setCoord_arrive(coords);
        for (Level0Room.Level0Connectors con : Level0Room.Level0Connectors.values()) {
            DiscreteCoordinates destination = boss.getRoomCoordinates().jump((con.getOrientation().opposite()).toVector());

            if(con.orientation== Orientation.UP)destination=new DiscreteCoordinates(boss.getRoomCoordinates().x ,boss.getRoomCoordinates().y -1);
            if(con.orientation==Orientation.DOWN)destination=new DiscreteCoordinates(boss.getRoomCoordinates().x ,boss.getRoomCoordinates().y +1);
            if(con.orientation==Orientation.RIGHT)destination=new DiscreteCoordinates(boss.getRoomCoordinates().x +1,boss.getRoomCoordinates().y );
            if(con.orientation==Orientation.LEFT)destination=new DiscreteCoordinates(boss.getRoomCoordinates().x -1,boss.getRoomCoordinates().y);
            if (destination.x>=0&&destination.y>=0&&destination.x<roomsPlacement.length&&destination.y<roomsPlacement[0].length) {
                if (roomsPlacement[destination.y][destination.x] != MapState.NULL)
                    setRoomConnector(boss.getRoomCoordinates(), "icrogue/level0" + (destination.x) + (destination.y), con);
            }
        }

    }

    @Override
    protected void setUpConnector(MapState[][] roomsPlacement, ICRogueRoom room) {
        for (Level0Room.Level0Connectors con : Level0Room.Level0Connectors.values()) {
                DiscreteCoordinates destination = room.getRoomCoordinates().jump(con.getOrientation().toVector());

                if(con.orientation== Orientation.UP)destination=new DiscreteCoordinates(room.getRoomCoordinates().x ,room.getRoomCoordinates().y -1);
                if(con.orientation==Orientation.DOWN)destination=new DiscreteCoordinates(room.getRoomCoordinates().x ,room.getRoomCoordinates().y +1);
                if(con.orientation==Orientation.RIGHT)destination=new DiscreteCoordinates(room.getRoomCoordinates().x +1,room.getRoomCoordinates().y );
                if(con.orientation==Orientation.LEFT)destination=new DiscreteCoordinates(room.getRoomCoordinates().x -1,room.getRoomCoordinates().y);

                if (destination.x>=0&&destination.y>=0&&destination.x<roomsPlacement.length&&destination.y<roomsPlacement[0].length) {
                    if (roomsPlacement[destination.y][destination.x] == MapState.BOSS_ROOM)
                        lockRoomConnector(room.getRoomCoordinates(), con, BOSS_KEY_ID);
                    else if (roomsPlacement[destination.y][destination.x] != MapState.NULL){
                        setRoomConnector(room.getRoomCoordinates(), "icrogue/level0" + Integer.toString(destination.x) + Integer.toString(destination.y), con);
                    }
            }
        }
    }

    @Override
    protected void generateFixedMap() {
        //generateMap1();
        generateMap2();
    }
}
