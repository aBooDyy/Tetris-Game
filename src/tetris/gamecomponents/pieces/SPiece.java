package tetris.gamecomponents.pieces;

import tetris.gamecomponents.Piece;
import tetris.gamecomponents.Rotatable;
import tetris.utilities.Point;
import tetris.utilities.Properties;

public class SPiece extends Piece implements Rotatable {

    public SPiece() {
        super(Properties.getColorScheme().getGreen(), new Point(1, 1),
                new Point(1, 0),
                new Point(2, 0),
                new Point(0, 1)
        );
    }

    @Override
    public int getID() {
        return 4;
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
