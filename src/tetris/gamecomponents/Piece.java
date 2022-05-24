package tetris.gamecomponents;

import tetris.gamecomponents.pieces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Piece {

    private final String COLOR;
    private final Point ORIGIN;
    private final List<Point> POINTS;

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

    public Point getOrigin() {
        return ORIGIN;
    }

    public List<Point> getPoints() {
        return POINTS;
    }

    public void moveDown() {
        for (Point point : POINTS) {
            point.addY(1);
        }
    }

    public void moveLeft() {
        for (Point point : POINTS) {
            point.addX(-1);
        }
    }

    public void moveRight() {
        for (Point point : POINTS) {
            point.addX(1);
        }
    }

    public static Piece getPiece() {
        Random random = new Random();
        int randomID = random.nextInt(7);

        return getPiece(randomID);
    }

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
