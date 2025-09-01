package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.icrogue.actor.items.Item;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.List;

abstract public class Level0ItemRoom extends Level0Room{
    protected boolean roomDone;


    private List<Item> list= new ArrayList<>();

    public Level0ItemRoom(DiscreteCoordinates coordinates) {
        super(coordinates);
    }

    protected void addItem(Item item){
        list.add(item);

    }

    @Override
    public void update(float deltaTime) {
        int i=0;
        do{
            if(list.get(i).isConsumed())roomDone=true;
            i++;
        }while (!roomDone && i< list.size());
        super.update(deltaTime);
    }

    @Override
    public boolean isOn() {
        return (super.isOn() && roomDone);
    }

    @Override
    public boolean isOff() {
        return !isOn();
    }
}
