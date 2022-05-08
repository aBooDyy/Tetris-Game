package tetris.gamecomponents.pieces;

import tetris.gamecomponents.Piece;
import tetris.gamecomponents.Rotatable;
import tetris.utilities.Point;
import tetris.utilities.Properties;

public class IPiece extends Piece implements Rotatable {

    public IPiece() {
        super(Properties.getColorScheme().getLightBlue(), new Point(1, 0),
                new Point(0, 0),
                new Point(2, 0),
                new Point(3, 0)
        );
    }

    @Override
    public int getID() {
        return 0;
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
