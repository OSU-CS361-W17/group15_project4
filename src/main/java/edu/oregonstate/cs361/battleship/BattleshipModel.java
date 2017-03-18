package edu.oregonstate.cs361.battleship;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

/**
 * Created by michaelhilton on 1/4/17.
 */
public class BattleshipModel {

    private Ship aircraftCarrier = new Ship("AircraftCarrier",5, new Coordinate(0,0),new Coordinate(0,0));
    private Ship battleship = new StealthShip("Battleship",4, new Coordinate(0,0),new Coordinate(0,0));
    private Ship submarine = new StealthShip("Submarine",2, new Coordinate(0,0),new Coordinate(0,0));
    private Ship clipper = new CivilianShip("Clipper",3, new Coordinate(0,0),new Coordinate(0,0));
    private Ship dinghy = new CivilianShip("Dinghy",1, new Coordinate(0,0),new Coordinate(0,0));

    private Ship computer_aircraftCarrier = new Ship("Computer_AircraftCarrier",5, new Coordinate(2,2),new Coordinate(2,7));
    private Ship computer_battleship = new StealthShip("Computer_Battleship",4, new Coordinate(2,8),new Coordinate(6,8));
    private Ship computer_submarine = new StealthShip("Computer_Submarine",2, new Coordinate(9,6),new Coordinate(9,8));
    private Ship computer_clipper = new CivilianShip("Computer_Clipper",3, new Coordinate(4,1),new Coordinate(4,4));
    private Ship computer_dinghy = new CivilianShip("Computer_Dinghy",1, new Coordinate(7,3),new Coordinate(7,4));

    ArrayList<Coordinate> playerHits;
    private ArrayList<Coordinate> playerMisses;
    ArrayList<Coordinate> computerHits;
    private ArrayList<Coordinate> computerMisses;
    private Stack<Coordinate> verticalStack;
    private Stack<Coordinate> horizontalStack;
    private Stack<Coordinate> shotListStack;

    boolean scanResult = false;
    boolean easyMode = true;
    //boolean easymode = gameMode();
    boolean computerIsBeingSmart = false;
    boolean computerIsShootingVertical = true;



    public BattleshipModel() {
        playerHits = new ArrayList<>();
        playerMisses= new ArrayList<>();
        computerHits = new ArrayList<>();
        computerMisses= new ArrayList<>();
        verticalStack = new Stack<>();
        horizontalStack = new Stack<>();
        shotListStack = new Stack<>();
        populateShotListStack();
    }


    public Ship getShip(String shipName) {
        if (shipName.equalsIgnoreCase("aircraftcarrier")) {
            return aircraftCarrier;
        } if(shipName.equalsIgnoreCase("battleship")) {
            return battleship;
        }if(shipName.equalsIgnoreCase("submarine")) {
            return submarine;
        }if(shipName.equalsIgnoreCase("Clipper")) {
            return clipper;
        }if(shipName.equalsIgnoreCase("Dinghy")) {
            return dinghy;
        } else {
            return null;
        }
    }

    public BattleshipModel placeShip(String shipName, String row, String col, String orientation) {
        int rowint = Integer.parseInt(row);
        int colInt = Integer.parseInt(col);
        if(orientation.equals("horizontal")){
            if (shipName.equalsIgnoreCase("aircraftcarrier")) {
                this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint,colInt+5));
            } if(shipName.equalsIgnoreCase("battleship")) {
                this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint,colInt+4));
            } if(shipName.equalsIgnoreCase("submarine")) {
                this.getShip(shipName).setLocation(new Coordinate(rowint, colInt), new Coordinate(rowint, colInt+2));
            } if(shipName.equalsIgnoreCase("clipper")) {
                this.getShip(shipName).setLocation(new Coordinate(rowint, colInt), new Coordinate(rowint, colInt+3));
            } if(shipName.equalsIgnoreCase("dinghy")) {
                this.getShip(shipName).setLocation(new Coordinate(rowint, colInt), new Coordinate(rowint, colInt+1));
            }
        }else{
            //vertical
                if (shipName.equalsIgnoreCase("aircraftcarrier")) {
                    this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint+5,colInt));
                } if(shipName.equalsIgnoreCase("battleship")) {
                    this.getShip(shipName).setLocation(new Coordinate(rowint,colInt),new Coordinate(rowint+4,colInt));
                } if(shipName.equalsIgnoreCase("submarine")) {
                    this.getShip(shipName).setLocation(new Coordinate(rowint, colInt), new Coordinate(rowint+2, colInt));
                } if(shipName.equalsIgnoreCase("clipper")) {
                    this.getShip(shipName).setLocation(new Coordinate(rowint, colInt), new Coordinate(rowint+3, colInt));
                } if(shipName.equalsIgnoreCase("dinghy")) {
                    this.getShip(shipName).setLocation(new Coordinate(rowint, colInt), new Coordinate(rowint+1, colInt));
                }
        }
        return this;
    }

    public void shootAtComputer(int row, int col) {
        Coordinate coor = new Coordinate(row,col);
        if(computer_aircraftCarrier.covers(coor)){
            computerHits.add(coor);
        }else if (computer_battleship.covers(coor)){
            computerHits.add(coor);
        }else if (computer_submarine.covers(coor)){
            computerHits.add(coor);
        }else if (computer_clipper.covers(coor)){

            computerHits.add(coor);
        }else if (computer_dinghy.covers(coor)){

            computerHits.add(coor);
        } else {
            computerMisses.add(coor);
        }
    }

    public void shootAtPlayer() {
     if (easyMode) {                                        //Comment out this
         Coordinate coor = shotListStack.pop();             //section for
         playerShot(coor);                                  //hard mode
     }                                                      //testing without
     else                                                   //front end option
        intelligentShooting();
    }

    private void populateShotListStack(){
        int numberAdded = 0;
        while(numberAdded < 100){
            int max = 10;
            int min = 1;
            Random random = new Random();
            int randRow = random.nextInt(max - min + 1) + min;
            int randCol = random.nextInt(max - min + 1) + min;
            Coordinate newCoordinate = new Coordinate(randRow, randCol);
            if(!shotListStack.contains(newCoordinate)){
                shotListStack.push(newCoordinate);
                numberAdded++;
            }
        }
    }

    private Coordinate getNextCoordinate(){
        Coordinate nextCoordinate;
        do {
            if (!verticalStack.isEmpty()) {
                nextCoordinate = verticalStack.pop();
                computerIsShootingVertical = true;
            } else if (!horizontalStack.isEmpty()) {
                nextCoordinate = horizontalStack.pop();
                computerIsShootingVertical = false;
            } else {
                nextCoordinate = shotListStack.pop();
                computerIsBeingSmart = false;
            }
        } while(playerHits.contains(nextCoordinate) || playerMisses.contains(nextCoordinate));
        return nextCoordinate;
    }

    private void pushVertical(Coordinate nextCoordinate){
        int tempDown = nextCoordinate.getDown();
        int tempAcross = nextCoordinate.getAcross();
        if(tempDown > 1)
            verticalStack.push(new Coordinate(tempAcross, tempDown-1));
        if(tempDown < 10)
            verticalStack.push(new Coordinate(tempAcross, tempDown+1));
    }
    private void pushHorizontal(Coordinate nextCoordinate){
        int tempDown = nextCoordinate.getDown();
        int tempAcross = nextCoordinate.getAcross();
        if(tempAcross > 1)
            horizontalStack.push(new Coordinate(tempAcross-1, tempDown));
        if(tempAcross < 10)
            horizontalStack.push(new Coordinate(tempAcross+1, tempDown));
    }

    private void pushAround(Coordinate nextCoordinate){
        pushVertical(nextCoordinate);
        pushHorizontal(nextCoordinate);
    }

    private void intelligentShooting(){
        int tempHits = playerHits.size();
        Coordinate nextCoordinate = getNextCoordinate();
        playerShot(nextCoordinate);
        if(playerHits.size() > tempHits) {
            if (!computerIsBeingSmart) {
                computerIsBeingSmart = true;
                pushAround(nextCoordinate);
            }
            else {
                pushAround(nextCoordinate);
            }
        }
    }

    void playerShot(Coordinate coor) {
        if(playerMisses.contains(coor)){
            System.out.println("Dupe");
        }

        if(aircraftCarrier.covers(coor)){
            playerHits.add(coor);
        }else if (battleship.covers(coor)){
            playerHits.add(coor);
        }else if (submarine.covers(coor)){
            playerHits.add(coor);
        }else if (clipper.covers(coor)){

            playerHits.add(coor);
        }else if (dinghy.covers(coor)){

            playerHits.add(coor);
        } else {
            playerMisses.add(coor);
        }
    }


    public void scan(int rowInt, int colInt) {
        Coordinate coor = new Coordinate(rowInt,colInt);
        scanResult = false;
        if(computer_aircraftCarrier.scan(coor)){
            scanResult = true;
        }
        else if (computer_battleship.scan(coor)){
            scanResult = true;
        }else if (computer_submarine.scan(coor)){
            scanResult = true;
        }else if (computer_clipper.scan(coor)){
            scanResult = true;
        }else if (computer_dinghy.scan(coor)){
            scanResult = true;
        } else {
            scanResult = false;
        }
    }

    public boolean getScanResult() {
        return scanResult;
    }
}