package ch.epfl.cs107.play.game.icrogue.area;

import ch.epfl.cs107.play.game.icrogue.RandomHelper;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;


abstract public class Level implements Logic {
    private int[] roomDistribution;

        // The room has been instantiated in public String toString() { return Integer.toString(ordinal()); } }
    protected Level(boolean randomMap, DiscreteCoordinates startPosition, int[] roomsDistribution, int width, int height){
        if(!randomMap){
            icRogueRooms=new ICRogueRoom[width][height];
            generateFixedMap();
            positionBoss=new DiscreteCoordinates(0,0);
            depart=startPosition;
        }
        else {
            this.roomDistribution=roomsDistribution;
            generateRandomMap();
        }
    }
    protected void setCoord_arrive(DiscreteCoordinates coord){
        coord_arrive=coord;
    }

    protected enum MapState { NULL,
        PLACED, // Empty space // The room has been placed but not yet explored by the room placement algorithm
        EXPLORED, // The room has been placed and explored by the algorithm
        BOSS_ROOM, // The room is a boss room
        CREATED; //the room map
        @Override
        public String toString() { return Integer.toString(ordinal()); }
    }
    abstract protected void roomPlace(DiscreteCoordinates coords,int type);
    abstract protected void BossroomPlace(DiscreteCoordinates coords,ICRogueRoom boss,MapState[][] roomsPlacement);
    abstract protected void setUpConnector(MapState[][] roomsPlacement, ICRogueRoom room);

    /**
     * genère une map aléatoire
     */
    protected void generateRandomMap(){
        int s=0;
        for (int j : roomDistribution) {
            s += j;
        }
        icRogueRooms=new ICRogueRoom[s][s];
        MapState[][] roomsPlacement= generateRandomRoomPlacement();
        int x;
        int y;
        for (int i = 0; i < roomDistribution.length; i++) {
            for (int j = 0; j < roomDistribution[i]; j++) {
                do{
                    x=RandomHelper.roomGenerator.nextInt(0, roomsPlacement.length);
                    y=RandomHelper.roomGenerator.nextInt(0, roomsPlacement[x].length);
                }while (!(roomsPlacement[x][y]==MapState.PLACED||roomsPlacement[x][y]==MapState.EXPLORED));
                roomPlace(new DiscreteCoordinates(y,x),i);
                roomsPlacement[x][y]=MapState.CREATED;
            }
        }
        for (ICRogueRoom[] icRogueRoom : icRogueRooms) {
            for (ICRogueRoom rogueRoom : icRogueRoom) {
                if(rogueRoom!=null){
                setUpConnector(roomsPlacement, rogueRoom);}
            }
        }
        for (int i = 0; i < roomsPlacement.length; i++) {
            for (int j = 0; j < roomsPlacement[i].length; j++) {
                if(roomsPlacement[i][j]==MapState.BOSS_ROOM){
                    positionBoss=new DiscreteCoordinates(j,i);
                    BossroomPlace(positionBoss,icRogueRooms[j][i],roomsPlacement);}
            }
        }

    }

    /**
     * genère des placements aléatoires pour la salle
     * @return un tableau composé du type enumere MapState
     */
    protected MapState[][] generateRandomRoomPlacement(){
        int roomsToPlace= icRogueRooms.length -1;
        MapState[][] mapStates= new MapState[icRogueRooms.length][icRogueRooms[0].length];
        for (int i = 0; i < mapStates.length; i++) {
            for (int j = 0; j < mapStates[i].length; j++) {
                mapStates[i][j]=MapState.NULL;
            }
        }
        mapStates[((mapStates.length-1)/2)][(mapStates[0].length-1) /2]=MapState.PLACED;
        int freeSlots=0;
        int hasard;
        while (roomsToPlace>0){
            for (int i = 0; i <mapStates.length ; i++) {
                for (int j=0; j<mapStates[i].length; j++){
                    if(mapStates[i][j]==MapState.PLACED){
                        if((i+1)< mapStates.length && mapStates[i+1][j]==MapState.NULL){
                            freeSlots+=1;
                        }
                        if((i-1)>=0 && mapStates[(i-1)][j]==MapState.NULL){
                            freeSlots+=1;}
                        if((j+1)<mapStates[i].length && mapStates[i][j+1]==MapState.NULL){
                            freeSlots+=1;}
                        if((j-1)>=0 && mapStates[i][j-1]==MapState.NULL){
                            freeSlots+=1;}

                        if(roomsToPlace>0 && freeSlots>0){
                            hasard= RandomHelper.roomGenerator.nextInt(0, (Math.min(roomsToPlace,freeSlots)+1));
                            if(hasard>=1)mapStates[i][j]=MapState.EXPLORED;
                        }
                        else hasard=0;
                        freeSlots=0;
                        if(hasard>0 && (i+1)< mapStates.length && mapStates[i+1][j]==MapState.NULL){
                            mapStates[i+1][j]=MapState.PLACED;
                            hasard--;
                            roomsToPlace--;
                        }
                        if(hasard>0 && (i-1)>=0 &&  mapStates[(i-1)][j]==MapState.NULL){
                            mapStates[(i-1)][j]=MapState.PLACED;
                            hasard--;
                            roomsToPlace--;

                        }
                        if(hasard>0 &&(j+1)<mapStates[i].length&& mapStates[i][j+1]==MapState.NULL){
                            mapStates[i][j+1]=MapState.PLACED;
                            hasard--;
                            roomsToPlace--;}
                        if(hasard>0 &&(j-1)>=0&& mapStates[i][j-1]==MapState.NULL){
                            mapStates[i][j-1]=MapState.PLACED;
                            roomsToPlace--;
                        }

                    }
                }
            }
        }
        for (int i = 0; i <mapStates.length ; i++) {
            for (int j=0; j<mapStates[i].length; j++){
                if(mapStates[i][j]==MapState.PLACED){
                    if((i+1)< mapStates.length && mapStates[i+1][j]==MapState.NULL){
                        mapStates[i+1][j]=MapState.BOSS_ROOM;
                        i= mapStates.length-1;
                        j= mapStates[i].length -1;
                    }
                    else if((i-1)>=0 && mapStates[(i-1)][j]==MapState.NULL){
                        mapStates[(i-1)][j]=MapState.BOSS_ROOM;
                        i= mapStates.length-1;
                        j= mapStates[i].length -1;
                    }
                    else if((j+1)< mapStates[i].length && mapStates[i][j+1]==MapState.NULL){
                        mapStates[i][j+1]=MapState.BOSS_ROOM;
                        i= mapStates.length-1;
                        j= mapStates[i].length -1;
                    }
                    else if((j-1)>=0&& mapStates[i][j-1]==MapState.NULL){
                        mapStates[i][j-1]=MapState.BOSS_ROOM;
                        i= mapStates.length-1;
                        j= mapStates[i].length -1;
                    }
                }

            }
        }
        return mapStates;
    }

    /**
     * genère une map deja preparé par le programmeur
     */
    protected abstract void generateFixedMap();
    protected ICRogueRoom[][] icRogueRooms;
    protected DiscreteCoordinates coord_arrive;
    protected DiscreteCoordinates depart;
    protected DiscreteCoordinates positionBoss;
    protected String salleDepart;

    public ICRogueRoom[][] getIcRogueRooms() {
        return icRogueRooms;
    }

    protected void setRoom(DiscreteCoordinates coords, ICRogueRoom room){
        icRogueRooms[coords.x][coords.y]=room;
    }
    protected void setRoomConnectorDestination(DiscreteCoordinates coords, String destination, ConnectorInRoom connector){
        icRogueRooms[coords.x][coords.y].connectors.get(connector.getIndex()).setAireDestination(destination);
    }
    protected void setRoomConnector(DiscreteCoordinates coords, String destination, ConnectorInRoom connector){
        icRogueRooms[coords.x][coords.y].connectors.get(connector.getIndex()).setAireDestination(destination);
        icRogueRooms[coords.x][coords.y].connectors.get(connector.getIndex()).change_status(Connector.Status.CLOSED);
    }
    protected void lockRoomConnector(DiscreteCoordinates coords, ConnectorInRoom connector, int keyId){
        icRogueRooms[coords.x][coords.y].connectors.get(connector.getIndex()).id(keyId);
        icRogueRooms[coords.x][coords.y].connectors.get(connector.getIndex()).change_status(Connector.Status.LOCKED);
    }
    void setName(DiscreteCoordinates coord){
        salleDepart= ("icrogue/level0"+Integer.toString(coord.x)+Integer.toString(coord.y));
    }


    public String getTitle() {
        setName(depart);
        return salleDepart;
    }

    @Override
    public boolean isOn() {
        return icRogueRooms[positionBoss.x][positionBoss.y]==null||icRogueRooms[positionBoss.x][positionBoss.y].isOn();
    }

    @Override
    public boolean isOff() {
        return !isOff();
    }

    @Override
    public float getIntensity() {
        return 0;
    }
}
