package tetris.gamecomponents;

import tetris.gamecomponents.pieces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Piece {

    private final String COLOR;
    private final Point ORIGIN;
    private final List<Point> POINTS;

    /**
     * Returns the number between 0 and 6 describing which type of piece it is, either I, J, L, O, S, Z, or T.
     *
     * @return the piece ID.
     */
    public abstract int getID();

    public Piece(String color, Point origin, Point... points) {
        this.COLOR = color;
        this.ORIGIN = origin;
        this.POINTS = new ArrayList<>();
        this.POINTS.add(origin);
        this.POINTS.addAll(List.of(points));

        for (Point point : this.POINTS) {
            point.setColor(color);
        }
    }

    public String getColor() {
        return COLOR;
    }

    /**
     * Returns the point from the piece which is assigned as the origin of the piece around which it
     * rotates.
     *
     * @return the piece's origin point
     */
    public Point getOrigin() {
        return ORIGIN;
    }

    /**
     * Returns a list of points from which the piece is made.
     *
     * @return the list of points
     */
    public List<Point> getPoints() {
        return POINTS;
    }

    /**
     * Increases all the points’ y coordinates by 1 to move it down.
     */
    public void moveDown() {
        for (Point point : POINTS) {
            point.addY(1);
        }
    }

    /**
     * Decreases all the points’ x coordinates by 1 to move it down.
     */
    public void moveLeft() {
        for (Point point : POINTS) {
            point.addX(-1);
        }
    }

    /**
     * Increases all the points’ x coordinates by 1 to move it down.
     */
    public void moveRight() {
        for (Point point : POINTS) {
            point.addX(1);
        }
    }

    /**
     * Generates a new random piece, either I, J, L, O, S, Z, or T.
     *
     * @return the generated piece.
     */
    public static Piece getPiece() {
        Random random = new Random();
        int randomID = random.nextInt(7);

        return getPiece(randomID);
    }

    /**
     * Generates a new piece according to the parameter.
     *
     * @param id the piece's ID to get.
     * @return the piece with the given ID
     */
    public static Piece getPiece(int id) {
//        id = 999;
        switch (id) {
            case 0:
                return new IPiece();
            case 1:
                return new JPiece();
            case 2:
                return new LPiece();
            case 3:
                return new OPiece();
            case 4:
                return new SPiece();
            case 5:
                return new ZPiece();
            case 6:
                return new TPiece();
            case 999:
                return new TestPiece();
        }

        return new IPiece();
    }
}
