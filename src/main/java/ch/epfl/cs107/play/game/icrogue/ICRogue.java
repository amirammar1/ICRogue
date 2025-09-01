package ch.epfl.cs107.play.game.icrogue;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.game.icrogue.area.Level;
import ch.epfl.cs107.play.game.icrogue.area.level0.Level0;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0Room;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;


public class ICRogue extends AreaGame{
    public final static float CAMERA_SCALE_FACTOR = 10.f;
    private ICRoguePlayer player;
    ICRogueRoom current_room;
    Level niveau;

    /**
     * Initialise le niveau du jeu
     */
    private void initlevel(){
        niveau=new Level0(false);
        for (ICRogueRoom[] rooms : niveau.getIcRogueRooms()) {
            for (ICRogueRoom room : rooms){
                if (room!=null)addArea(room);
            }
        }
        current_room = (Level0Room) setCurrentArea(niveau.getTitle(), true);
        player= new ICRoguePlayer(current_room,Orientation.UP,new DiscreteCoordinates(2,2));//revenir
        player.enterArea(current_room,new DiscreteCoordinates(2,2));
    }

    /**
     * change d'une aire à une autre
     * @param coords coordonnée de l'aire d'arrivé
     * @param room aire d'arrivé
     */
    private void switchArea(DiscreteCoordinates coords,String room) {
        player.leaveArea();
        current_room = (Level0Room) setCurrentArea(room, false);
        player.enterArea(current_room, coords);
    }
    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            initlevel();
            return true;
        }
        return false;
    }
    @Override
    public void update(float deltaTime) {
        Keyboard keyboard = getWindow().getKeyboard() ;
        Button x = keyboard.get(Keyboard.R) ;
        if(x.isDown())initlevel();
        String s =null;
        if(player.isConn_inter()){
            DiscreteCoordinates coord_room= new DiscreteCoordinates(0,0);
            DiscreteCoordinates coord_player=new DiscreteCoordinates(2,2);
            for (Level0Room.Level0Connectors con : Level0Room.Level0Connectors.values()) {
                if(player.getTransCon().getOrientation()==con.orientation.opposite()){
                     coord_player=con.getDestination();
                    if(con.orientation==Orientation.UP)coord_room=new DiscreteCoordinates(current_room.getRoomCoordinates().x ,current_room.getRoomCoordinates().y -1);
                    if(con.orientation==Orientation.DOWN)coord_room=new DiscreteCoordinates(current_room.getRoomCoordinates().x ,current_room.getRoomCoordinates().y +1);
                    if(con.orientation==Orientation.RIGHT)coord_room=new DiscreteCoordinates(current_room.getRoomCoordinates().x +1,current_room.getRoomCoordinates().y );
                    if(con.orientation==Orientation.LEFT)coord_room=new DiscreteCoordinates(current_room.getRoomCoordinates().x -1,current_room.getRoomCoordinates().y);
                    player.setConn_inter(false);
                    s= "icrogue/level0"+coord_room.x+(coord_room.y);
                }
            }
            switchArea(coord_player,s);
            player.setConn_inter(false);
        }
        if(niveau.isOn()){
            end();
        }
        if(player.getDead())end();
        super.update(deltaTime);
    }

    /**
     * Finalise le jeu en cas de sortie volentaire du gamer ou mort de joueyr ou en cas de victoire du level
     */
    @Override
    public void end() {
        if(niveau.isOn()) System.out.println("Bravooo! Tu as gagné");
        else System.out.println( player.getDead() ? "Malheureusement, Tu as perdus :(": "C'est dommage que tu quitte le jeu. N'hesite pas à rejouer!");
        System.exit(0);
    }

    public String getTitle() {
        return "ICRogue";
    }

}
