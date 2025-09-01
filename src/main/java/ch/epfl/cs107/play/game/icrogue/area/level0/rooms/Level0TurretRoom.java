package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level0TurretRoom extends Level0EnemyRoom{
    protected Turret turret1=new Turret(Orientation.UP,new DiscreteCoordinates(1,8),this,new Orientation[]{Orientation.DOWN,Orientation.RIGHT});
    protected Turret turret2=new Turret(Orientation.UP,new DiscreteCoordinates(8,1),this,new Orientation[]{Orientation.UP,Orientation.LEFT});
    public Level0TurretRoom(DiscreteCoordinates coordinates) {
        super(coordinates);
        addList(turret1);
        addList(turret2);
    }



}
