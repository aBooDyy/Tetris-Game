package tetris.gamecomponents.gifts;

import tetris.gamecomponents.Board;
import tetris.utilities.Point;
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
        int pieceStartingPosition = Properties.getWidth() / 2 - 2;
        int randomX;
        int randomY;

        do {
            randomX = random.nextInt(Properties.getWidth());
            randomY = random.nextInt(Properties.getHeight());
            point.setX(randomX);
            point.setY(randomY);
        } while (board.containsPoint(point) ||
                (randomY == 0 || randomY == 1 && randomX < pieceStartingPosition + 3 && randomX > pieceStartingPosition));

        board.getGiftPoints().add(point);
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
