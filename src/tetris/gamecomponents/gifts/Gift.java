package tetris.gamecomponents.gifts;

import tetris.gamecomponents.Board;
import tetris.gamecomponents.Point;
import tetris.utilities.Properties;

import java.util.Random;

public abstract class Gift {

    protected final Board board;
    private final String color;

    private final Point point;

    public Gift(Board board, String color) {
        this.board = board;
        point = new Point(0, 0, color);
        this.color = color;
    }

    public Point getPoint() {
        return point;
    }

    public String getColor() {
        return color;
    }

    public void showGift() {
        Random random = new Random();
        // a variable of the starting position of the falling piece (the middle of the grid) to
        // avoid generating the gift point in that place.
        int randomX;
        int randomY;
        // generate random x and y values in the range of our board and set them to be the gift's point's values
        do {
            randomX = random.nextInt(Properties.getWidth());
            randomY = random.nextInt(Properties.getHeight() - 2) + 2;
            point.setX(randomX);
            point.setY(randomY);

            // we keep getting random numbers (new point position) until we find an empty place in the board that does not
            // have a point.
        } while (board.containsPoint(point));

        // after getting the point, we set it in the appropriate variable in the board object using the setter method
        board.setGiftPoint(point);
    }

    public abstract void giveReward();

    public static Gift getRandomGift(Board board) {
        Random random = new Random();
        int id = random.nextInt(2);

        switch (id) {
            case 1:
                return new ScorePoint(board);
            case 0:
            default:
                return new MagicPiece(board);
        }
    }
}
