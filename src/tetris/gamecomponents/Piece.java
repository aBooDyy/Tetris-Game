package tetris.gamecomponents;

import tetris.gamecomponents.pieces.*;
import tetris.utilities.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Piece {

    private static final List<Class<? extends Piece>> piecesTypes = new ArrayList<>();

    private final String color;
    private final Point origin;
    private final List<Point> points;

    public abstract int getID();

    public Piece(String color, Point origin, Point... points) {
        this.color = color;
        this.origin = origin;
        this.points = new ArrayList<>();
        this.points.add(origin);
        this.points.addAll(List.of(points));

        for (Point point : this.points) {
            point.setColor(color);
        }

        Class<? extends Piece> type = this.getClass();
        if (!piecesTypes.contains(type)) {
            piecesTypes.add(type);
        }
    }

    public String getColor() {
        return color;
    }

    public Point getOrigin() {
        return origin;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void moveDown() {
        for (Point point : points) {
            point.addY(1);
        }
    }

    public void moveLeft() {
        for (Point point : points) {
            point.addX(-1);
        }
    }

    public void moveRight() {
        for (Point point : points) {
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
