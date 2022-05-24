package tetris.gamecomponents.pieces;

import tetris.gamecomponents.Piece;
import tetris.gamecomponents.Rotatable;
import tetris.gamecomponents.Point;
import tetris.utilities.Properties;

public class LPiece extends Piece implements Rotatable {

    public LPiece() {
        super(Properties.getColorScheme().getOrange(), new Point(1, 1),
                new Point(2, 0),
                new Point(0, 1),
                new Point(2, 1)
        );
    }

    @Override
    public int getID() {
        return 2;
    }

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
