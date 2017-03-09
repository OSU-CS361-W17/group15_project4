package edu.oregonstate.cs361.battleship;

/**
 * Created by bryceegley on 3/3/17.
 */
public class CivilianShip extends Ship{
    public CivilianShip(String n, int l, Coordinate s, Coordinate e) {
        super(n, l, s, e);
    }
    public boolean destroyed() {
        if (hitsCount >= 1) {
            return true;
        }
        return false;
    }
}
