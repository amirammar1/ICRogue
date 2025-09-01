package ch.epfl.cs107.play.game.icrogue;

import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.window.Window;

public class ICRogueBehavior extends AreaBehavior {
    public enum ICRogueCellType{
        NONE(0,false),  // Should never been used except in the toType method
        GROUND(-16777216, true), // traversable
        WALL(-14112955, false), // non traversable
        HOLE(-65536, true);
        final int type;
        final boolean isWalkable;
        ICRogueCellType(int type, boolean isWalkable){
            this.type = type;
            this.isWalkable = isWalkable;
        }

        public static ICRogueCellType toType(int type){
            for(ICRogueCellType ict : ICRogueCellType.values()){
                if(ict.type == type)
                    return ict;
            }
            // When you add a new color, you can print the int value here before assign it to a type
            System.out.println(type);
            return NONE;
        }
    }
    public ICRogueBehavior(Window window, String name){
        super(window, name);
        int height = getHeight();
        int width = getWidth();
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width ; x++) {
                ICRogueCellType color = ICRogueCellType.toType(getRGB(height-1-y, x));
                setCell(x,y, new ICRogueCell(x,y,color));
            }
        }
    }
    public class ICRogueCell extends Cell implements Interactable {
        private final ICRogueCellType type;
        public  ICRogueCell(int x, int y, ICRogueCellType type){
            super(x, y);
            this.type = type;
        }

        public ICRogueCellType getType() {
            return type;
        }

        public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
            ((ICRogueInteractionHandler) v).interactWith(this, isCellInteraction);
        }

        @Override
        protected boolean canLeave(Interactable entity) {
            return true;
        }

        /**
         * check si un element peu entrer à une cellule
         * @param entity (Interactable), not null
         * @return une valeur booleene
         */
        @Override
        protected boolean canEnter(Interactable entity) {
            ICRogueCell cell= (ICRogueCell) getCell(getCurrentCells().get(0).x,getCurrentCells().get(0).y);
            for (Interactable x : cell.entities) {
                if(x.takeCellSpace() && !entity.takeCellSpace())return false;
            }
            return type.isWalkable;
        }
        @Override
        public boolean isCellInteractable() {
            return false;
        }

        @Override
        public boolean isViewInteractable() {
            return true;
        }

    }



}
