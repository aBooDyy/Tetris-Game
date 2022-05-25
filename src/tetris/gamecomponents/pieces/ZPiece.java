package tetris.gamecomponents.pieces;

import tetris.gamecomponents.Piece;
import tetris.gamecomponents.Rotatable;
import tetris.gamecomponents.Point;
import tetris.utilities.Properties;

public class ZPiece extends Piece implements Rotatable {

    public ZPiece() {
        super(Properties.getColorScheme().getRed(), new Point(1, 1),
                new Point(0, 0),
                new Point(1, 0),
                new Point(2, 1)
        );
    }

    @Override
    public int getID() {
        return 5;
    }

    /**
     * Rotates each point of the piece 90 degrees about the origin either clockwise or counter-clockwise
     * according to the given parameter
     *
     * @param isClockwise if true, the rotation will be clockwise, if false, the rotation will be counterclockwise
     */
    @Override
    public void rotate(boolean isClockwise) {
        for (Point point : this.getPoints()) {
            if (point.equals(this.getOrigin())) {
                continue;
            }

            point.rotate(this.getOrigin(), isClockwise);
        }
    }

}
