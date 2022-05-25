package tetris.gamecomponents.pieces;

import tetris.gamecomponents.Piece;
import tetris.gamecomponents.Rotatable;
import tetris.gamecomponents.Point;
import tetris.utilities.Properties;

public class JPiece extends Piece implements Rotatable {

    public JPiece() {
        super(Properties.getColorScheme().getDarkBlue(), new Point(1, 1),
                new Point(0, 0),
                new Point(0, 1),
                new Point(2, 1)
        );
    }

    @Override
    public int getID() {
        return 1;
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
