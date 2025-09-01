package ch.epfl.cs107.play.game.icrogue.actor;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.game.icrogue.actor.items.Cherry;
import ch.epfl.cs107.play.game.icrogue.actor.items.Key;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Fire;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ICRoguePlayer extends ICRogueActor implements Interactor {
    private int hp;
    private boolean magicb;
    private final ICRoguePlayerInteractionHandler handler;
    private final Keyboard keyboard= getOwnerArea().getKeyboard();
    private Animation player_s;

    private final Sprite[][] sprites = RPGSprite.extractSprites("zelda/player", 4, 1, 2, this, 16, 32, new Orientation[] {Orientation.DOWN, Orientation.RIGHT, Orientation.UP, Orientation.LEFT}); // crée un tableau de 4 animation
    private final int ANIMATION_DURATION=3;
    private final Animation[] animations = Animation.createAnimations(ANIMATION_DURATION/2,sprites);
    private final static int MOVE_DURATION = 8;
    private TextGraphics message;

    public boolean getDead(){return hp==0;}

    /** Contructeur pour initialiser le joueur
     *
     * @param area l'aire auquel le joueur est inscrit
     * @param orientation son orientation initial
     * @param position sa position de depart
     */
    public ICRoguePlayer(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        handler= new ICRoguePlayerInteractionHandler();
        hp=3;
        player_s=animations[0];

        message = new TextGraphics(Integer.toString((int)hp), 0.4f, Color.ORANGE);
        message.setParent(this);
        message.setAnchor(new Vector(-0.3f, 0.1f));
    }

    /** Méthode qui permet au joueur d'entrer à la salle donné en paramètre
     *
     * @param area l'aire à entrer
     * @param position position de depart dans l'aire
     */
    public void enterArea(ICRogueRoom area, DiscreteCoordinates position){
        area.registerActor(this);
        area.entred_player();
        setOwnerArea(area);
        setCurrentPosition(position.toVector());
    }

    /** Met à jour le jours à chaque instant deltaTime
     * affiche le niveau d'energie
     * permet au joueur de se deplacer et collecter les objets
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    @Override
    public void update(float deltaTime) {
        message.setText(Integer.toString((int)hp));
        Button x = keyboard.get(keyboard.X);
        if(x.isPressed()&&magicb){
            Fire feu= new Fire(getOwnerArea(),getOrientation(),getCurrentMainCellCoordinates());
            feu.enterArea(getOwnerArea(),getCurrentMainCellCoordinates());
        }
        moveIfPressed(Orientation.LEFT, keyboard.get(Keyboard.LEFT),deltaTime);
        moveIfPressed(Orientation.UP, keyboard.get(Keyboard.UP),deltaTime);
        moveIfPressed(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT),deltaTime);
        moveIfPressed(Orientation.DOWN, keyboard.get(Keyboard.DOWN),deltaTime);
        ICRogueRoom test=(ICRogueRoom) getOwnerArea();
        if(test.isOn()){
            for(Connector connector : ((ICRogueRoom) getOwnerArea()).getConnectors()){
                if(connector.getStatus()== Connector.Status.CLOSED)connector.change_status(Connector.Status.OPEN);

            }
        }
        super.update(deltaTime);
    }

    /** Accepte les interaction avec les autres objets
     *
     * @param v (AreaInteractionVisitor) : the visitor
     * @param isCellInteraction interaction à distance
     */
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) v).interactWith(this, isCellInteraction);
    }

    /** Deplace le joueur quand un bouton est préssé
     *
     * @param orientation orientation du player
     * @param b bouton d'appuis
     * @param dt temps
     */
    private void moveIfPressed(Orientation orientation, Button b, float dt){
        if(b.isDown()) {
            if (!isDisplacementOccurs()) {
                if(orientation==Orientation.UP){
                    player_s=animations[0];
                }
                if(orientation==Orientation.RIGHT){
                    player_s=animations[1];
                }
                if(orientation==Orientation.DOWN){
                    player_s=animations[2];
                }
                if(orientation==Orientation.LEFT){
                    player_s=animations[3];
                }
                player_s.update(dt);
                orientate(orientation);
                move(MOVE_DURATION);
            }
        }
    }

    /** Dessine le joueur et le message associé
     *
     * @param canvas target, not null
     */
    @Override
    public void draw(Canvas canvas) {
        player_s.draw(canvas);
        message.draw(canvas);
    }


    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());

    }

    /** Cause une diminution du niveau d'energie
     *
     */
    public void damage(){
        hp--;
        if (hp>=1) System.out.println("Attention!! votre energie est desormes: "+ hp);
    }

    public List<DiscreteCoordinates> getFieldOfViewCells(){
        return Collections.singletonList (getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }
    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }
    public boolean wantsCellInteraction(){
        return true;
    }
    public boolean wantsViewInteraction(){
        return true;
    }

    /** Permet au joueur d'interagir en tant que interactor
     *
     * @param other (Interactable). Not null
     * @param isCellInteraction True if this is a cell interaction
     */
    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    private Connector transCon;

    public Connector getTransCon() {
        return new Connector(transCon);
    }

    protected boolean conn_inter;

    /** Retourne Vrai si le joueur et le conneteur realise une interaction à distance et vice versa
     * @return
     */
    public boolean isConn_inter() {
        return conn_inter;
    }

    /** Methode qui modifie une valeur booleenne qui est vrai quand le joueur et le conneteur realise une interaction à distance
     * @param conn_inter
     */
    public void setConn_inter(boolean conn_inter) {
        this.conn_inter = conn_inter;
    }

    ArrayList<Integer> ID= new ArrayList<Integer>(); //Cette liste contient les ID des clés que le joueur possède

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    /** La methode leave Area desinscrit le joueur de l'aire courante à laquel il est inscrrit
     * Sans paramètre
     */
    public void leaveArea(){
        getOwnerArea().unregisterActor(this);
    }

    /**
     * Classe imbriqué dont le bute est d'interagir le player en tant qu'interactor avec des diverses interactable
     */
    private class ICRoguePlayerInteractionHandler implements ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler{
        public void interactWith(Turret turret, boolean isCellInteraction) {
            if(isCellInteraction)turret.mourrir();
        }

        public void interactWith(Cherry cherry, boolean isCellInteraction) {
            if(isCellInteraction){
                cherry.consume();
                if(hp<3)hp++;
                ICRoguePlayer.this.getOwnerArea().unregisterActor(cherry);
            }
        }
        public void interactWith(Key key, boolean isCellInteraction) {
            if(isCellInteraction){
                key.consume();
                ID.add(key.getIdentificateur());
            }
        }
        public void interactWith(Connector connector,boolean isCellInteraction){
            Button w = keyboard.get(Keyboard.W) ;
            if(!isCellInteraction && w.isDown() && ((ICRogueRoom)getOwnerArea()).isOn()){
                if (connector.getStatus()==Connector.Status.LOCKED) {
                    for (Integer integer : ID) {
                        if ((int) integer == connector.NO_KEY_ID) connector.change_status(Connector.Status.OPEN);}
                }
            }

            if(isCellInteraction && !isDisplacementOccurs()){
                transCon=connector;
                conn_inter=true;
            }

        }


        public void interactWith(Staff staff, boolean isCellInteraction) {
            Button w = keyboard.get(keyboard.W);
            if(!isCellInteraction && w.isDown()){
                staff.consume();
                magicb=true;
            }
        }
    }
}
