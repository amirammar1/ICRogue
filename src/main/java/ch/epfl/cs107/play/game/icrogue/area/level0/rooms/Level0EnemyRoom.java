package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.icrogue.actor.enemies.Ennemi;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.List;

public class Level0EnemyRoom extends Level0Room{
    List<Ennemi> list=new ArrayList<>();
    public Level0EnemyRoom(DiscreteCoordinates coordinates) {
        super(coordinates);
    }
    protected void addList(Ennemi ennemi){
        list.add(ennemi);
    }
    private void createArea(){
        for (Ennemi ennemi : list) {
            ennemi.enterArea(this, ennemi.getPosition_ennemi());
        }
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            createArea();
            return true;
        }
        return false;
    }
    private boolean roomDone;

    @Override
    public void update(float deltaTime) {
        for (int i = 0; i < list.size(); i++) {
            if(!list.get(i).isAlive()){
                unregisterActor(list.get(i));
                list.remove(list.get(i));
            }
        }
        if(list.size()==0)roomDone=true;
        super.update(deltaTime);
    }

    @Override
    public boolean isOn() {
        return roomDone;
    }

    @Override
    public boolean isOff() {
        return !roomDone;
    }
}
