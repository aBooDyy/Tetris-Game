package tetris.gamecomponents;

import tetris.gamecomponents.gifts.Gift;
import tetris.utilities.Direction;
import tetris.utilities.Properties;
import tetris.utilities.Utils;

import java.util.*;

public class Board {

    // Score required to get a gift (every x score)
    private static final int SCORE_FOR_GIFT = 100;

    // A list that stores the next falling pieces
    private final LinkedList<Piece> nextFallingPieces;
    // A list that has all the points we want to clear in the user interface
    private final List<Point> pointsToClear;
    // A list that has all the points we want to color in the user interface
    private final List<Point> pointsToColor;
    // The point of the gift to be shown in the user interface
    private Point giftPoint;

    // A map of the filled points (points of the placed pieces) stored in a map int(row number) and the list of points in that row
    // A map is a kind of data structure that stores data in pairs of keys and values
    private Map<Integer, List<Point>> filledPoints;
    // A list that has all the shadow points
    private List<Point> shadowPoints;

    // The falling piece object
    private Piece fallingPiece;
    // The held piece object
    private Piece heldPiece;

    // The variable to store the gift when it is shown/given
    private Gift gift;

    // a boolean that gives the possibility of using the holding feature
    private boolean isHoldingPossible;

    // a boolean that is used to tell the user interface if the game is over to show game over menu
    private boolean isGameOver;
    // a boolean that set if we want/need to update the next pieces box in the interface or not
    // it is used by the PlayWindow class which where we update the next pieces grid
    private boolean updateNextPiecesGrid;

    // a variable to hold the value of the last score the player received a gift
    // used to determine if the player should get another gift in the next time
    private int lastGiftedScore;
    // a variable to store the player's score
    private int score;
    // a variable to store the amount of cleared lines
    private int clearedLines;
    // a variable that the chosen level to used it when giving point on line clearing
    private final int level;

    public Board(int level) {
        this.level = level;

        nextFallingPieces = new LinkedList<>();
        pointsToClear = new ArrayList<>();
        pointsToColor = new ArrayList<>();
        filledPoints = new HashMap<>();
        shadowPoints = new ArrayList<>();

        isHoldingPossible = true;
        isGameOver = false;
        updateNextPiecesGrid = false;

        score = 0;
        clearedLines = 0;

        generateNextFallingPiece();
    }

    private void updateShadowPoints(boolean clearOld) {
        // check if it is required to clear the old shadowPoints or not, based on the situation from the passed boolean value
        if (clearOld) {
            // adds all current shadow points to the pointsToClear list
            pointsToClear.addAll(shadowPoints);
        }

        // a boolean variable to indicate of there is a shadow for the point of not, initially it is true
        boolean noShadow = true;
        // a temporary list to store the current shadow points in a new/different point object (duplicated)
        List<Point> shadowPoints = new ArrayList<>();
        // a loop to loop through the fallingPiece's points
        for (Point point : fallingPiece.getPoints()) {
            // duplicates the point and adds it to the list
            shadowPoints.add(point.duplicate());
        }

        // a boolean variable to stop the while loop when it is not possible to move the object down further more
        boolean flag = true;
        while (flag) {
            // a loop that loops over the temporary shadowPoints list
            for (Point point : shadowPoints) {
                // a duplicated checker point of the selected point to move it down and check if the movement is possible
                Point checkerPoint = point.duplicate();
                checkerPoint.addY(1);

                // checks if the point is filled at the checker point place or if it is beyond the board border
                if (containsPoint(checkerPoint) ||
                        checkerPoint.getY() < 0 || checkerPoint.getY() >= Properties.getHeight()) {
                    // stop the wile loop since we are at the end (the projected placing position)
                    flag = false;
                }
            }

            // check if the loop is still running which means we can still check if we can move further more to move
            // the points in the temporary shadow points list
            if (flag) {
                // sets the value to false since we have a shadow :)
                noShadow = false;
                // a loop to loops over the shadowPoints temporary list and moves all of its points 1 square down
                // since we know it is possible from the loop before as the value of flag hasn't changed
                for (Point point : shadowPoints) {
                    point.addY(1);
                }
            }
        }

        // sets the shadowPoints instance variable to the shadowPoints temporary list if there is a shaodw, if there isn't then
        // we set it as an empty list
        this.shadowPoints = noShadow ? new ArrayList<>() : shadowPoints;
        // equivalent code:
//        if (noShadow) {
//            this.shadowPoints = new ArrayList<>();
//        } else {
//            this.shadowPoints = shadowPoints;
//        }
    }

    // this method returns true if the piece has been moved, and false if it wasn't moved.
    public boolean moveFallingPiece(Direction direction) {
        // Here we first check if the piece cannot move to cover specific cases or stop the movement completely
        if (!canMove(direction)) {

            // Here we are checking if we want to move the piece down
            if (direction == Direction.DOWN || direction == Direction.DOWN_BY_PLAYER) {
                // in this case, we place the piece since the movement is down, and it is not possible to move down
                placeFallingPiece();
                // returns false since the object wasn't moved, and we don't need anything else from this method, so it stops here
                return false;
            }

            // Here we are checking if the movement is not a rotation, return false to stop the method because nothing to do
            // in this case
            if (direction != Direction.ROTATE_CLOCKWISE && direction != Direction.ROTATE_COUNTERCLOCKWISE) {
                return false;
            }

            // At this point, the movement is rotation, but it is not possible, so we try to make it possible by moving
            // the object left or right.

            // boolean variables to check the possibility of moving left or right
            boolean canMoveLeft = canMove(Direction.LEFT);
            boolean canMoveRight = canMove(Direction.RIGHT);
            // if we can't move left nor right then nothing we can do, so we stop the method by returning false.
            if (!canMoveLeft && !canMoveRight) {
                return false;
            }

            // a variable to hold the status of the original direction movement, whether the piece has been moved or not.
            boolean movementAccomplished = false;

            // In the following two if blocks, we are checking the possibility of moving left or right.
            // We move the piece in the available direction to allow it to rotate.

            if (!canMoveLeft) {
                boolean rightMovement = moveFallingPiece(Direction.RIGHT);
                // After moving right, we move the piece again and check if it has been moved or not
                if (rightMovement && !moveFallingPiece(direction)) {
                    // if the rotation movement wasn't accomplished, we try to move it right again.
                    moveFallingPiece(Direction.RIGHT);
                    // In here we move the piece again, hoping it works, and we assign the result of the movement to our variable
                    movementAccomplished = moveFallingPiece(direction);
                }
            }

            if (!canMoveRight) {
                boolean leftMovement = moveFallingPiece(Direction.LEFT);
                // After moving left, we move the piece again and check if it has been moved or not
                if (leftMovement && !moveFallingPiece(direction)) {
                    // if the rotation movement wasn't accomplished, we try to move it right again.
                    moveFallingPiece(Direction.LEFT);
                    // In here we move the piece again, hoping it works, and we assign the result of the movement to our variable
                    movementAccomplished = moveFallingPiece(direction);
                }
            }

            // now we return the result of our movement for recursion checkers and stop the method here.
            return movementAccomplished;
        }

        // If the method reached this point, then the movement in the requested direction is possible, so we accomplish it
        // executing the appropriate methods and statements that corresponds to the requested movement.
        switch (direction) {
            case DOWN_BY_PLAYER:
                score++;
            case DOWN:
                Utils.addDuplicatedPoints(pointsToClear, fallingPiece.getPoints());
                fallingPiece.moveDown();
                break;
            case LEFT:
                Utils.addDuplicatedPoints(pointsToClear, fallingPiece.getPoints());
                fallingPiece.moveLeft();
                updateShadowPoints(true);
                break;
            case RIGHT:
                Utils.addDuplicatedPoints(pointsToClear, fallingPiece.getPoints());
                fallingPiece.moveRight();
                updateShadowPoints(true);
                break;
            case ROTATE_CLOCKWISE:
                Utils.addDuplicatedPoints(pointsToClear, fallingPiece.getPoints());
                ((Rotatable) fallingPiece).rotate(true);
                updateShadowPoints(true);
                break;
            case ROTATE_COUNTERCLOCKWISE:
                Utils.addDuplicatedPoints(pointsToClear, fallingPiece.getPoints());
                ((Rotatable) fallingPiece).rotate(false);
                updateShadowPoints(true);
        }

        // In here, we are checking if the player should be given a gift based on his score and the required amount that
        // she/he should pass to get a gift
        if (score - lastGiftedScore >= SCORE_FOR_GIFT) {
            // give/show a gift only if it is possible (the is no gift waiting to be collected in the board)
            if (gift == null) {
                showGift();
                // recording the last gifted score after showing the gift to the player.
                lastGiftedScore = score;
            }
        }

        // After the movement is accomplished, we check if the player took the gift in this movement.
        checkGifts();
        return true;
    }

    public void hardDrop() {
        // if the there is no shadow, then we cannot hard drop.
        if (shadowPoints.isEmpty()) {
            return;
        }

        // we store the lowest point of the falling piece and the shadow points to calculate the score to give.
        int fallingPieceLowestPoint = Utils.getLowestPoint(fallingPiece.getPoints());
        int shadowPieceLowestPoint = Utils.getLowestPoint(shadowPoints);

        // adding falling piece points to points to clear list to be cleared in the user interface.
        pointsToClear.addAll(fallingPiece.getPoints());
        // clearing the points of the current falling piece to assign the shadow points to it (to move it to the end).
        fallingPiece.getPoints().clear();
        // for loop to loop over current shadow points
        for (Point shadowPoint : shadowPoints) {
            // creates new point which has the same values as the shadow point
            Point newPiecePoint = shadowPoint.duplicate();
            // set the color of the point to the color of the piece
            newPiecePoint.setColor(fallingPiece.getColor());
            // add the point to the falling piece points list.
            fallingPiece.getPoints().add(newPiecePoint);
        }

        // After the movement is accomplished, we check if the player took the gift in this movement.
        checkGifts();

        // adding the new falling piece points to points to color list to be colored in the user interface.
        pointsToColor.addAll(fallingPiece.getPoints());
        placeFallingPiece();

        // increase the score by the distance multiplied by 2
        score += (shadowPieceLowestPoint - fallingPieceLowestPoint) * 2;

        // In here, we are checking if the player should be given a gift based on his score and the required amount that
        // she/he should pass to get a gift
        if (score - lastGiftedScore >= SCORE_FOR_GIFT) {
            // give/show a gift only if it is possible (the is no gift waiting to be collected in the board)
            if (gift == null) {
                showGift();
                // recording the last gifted score after showing the gift to the player.
                lastGiftedScore = score;
            }
        }
    }

    public void holdFallingPiece() {
        // check if the held piece slot is null (empty)
        if (heldPiece == null) {
            pointsToClear.addAll(fallingPiece.getPoints());
            pointsToClear.addAll(shadowPoints);
            // assign the held piece variable to have the same piece type as the falling piece
            heldPiece = Piece.getPiece(fallingPiece.getID());
            // disable the holding functionality.
            isHoldingPossible = false;
            generateNextFallingPiece();
            // stop here as we are done with this method in this case
            return;
        }

        // if the held piece slot is not null (has a piece), then we check if holding is possible to either switch or do nothing
        if (!isHoldingPossible) {
            // stop the method here as the holding is not possible
            return;
        }

        // if the holding is possible, then we clear the current falling piece points
        pointsToClear.addAll(fallingPiece.getPoints());
        // as well as the shadow points.
        pointsToClear.addAll(shadowPoints);

        // we switch the piece in the held slot and the current falling piece.
        Piece temp = fallingPiece;
        fallingPiece = heldPiece;
        heldPiece = Piece.getPiece(temp.getID());
        // we disable the holding functionality
        isHoldingPossible = false;
        positionFallingPiece();
    }

    private void generateNextFallingPiece() {
        // A loop to run 3 times to check the 3 points at the top if they are filled to indicate of the game is over.
        for (int i = Properties.getWidth()/2 - 2; i < Properties.getWidth()/2 + 1; i++) {
            // The selected point of the 3 points to be checked in this loop
            Point point = new Point(i, 0);

            // checking of the selected point is filled by using the containsPoint method
            if (containsPoint(point)) {
                isGameOver = true;
                // return immediately which will stop the method at this point and will exit the method if we found at least
                // one filled point of the 3 points we want to check
                return;
            }
        }

        // checking if we have pieces in the nextFallingPieces list to either take from it or directly get a random one
        if (nextFallingPieces.size() == 0) {
            // gets a random piece and store it in the fallingPiece variable
            fallingPiece = Piece.getPiece();
            positionFallingPiece();
            fillNextFallingPieces();
            // stop here since we are done with this method in this case
            return;
        }

        // takes the first element in the nextFallingPieces list and assign it to be our fallingPiece
        fallingPiece = nextFallingPieces.getFirst();
        // removes the first element from the list since we took/used it
        nextFallingPieces.removeFirst();
        positionFallingPiece();
        fillNextFallingPieces();
    }

    private void fillNextFallingPieces() {
        // a loop to run 3 or 2 or 1 time(s) to fill the nextFallingPieces list to have a maximum of 3 elements
        for (int i = nextFallingPieces.size(); i < 3; i++) {
            // adds a random piece to the end of the list
            nextFallingPieces.addLast(Piece.getPiece());
            // since we added a piece to the box, we want to update it
            updateNextPiecesGrid = true;
        }
    }

    private void positionFallingPiece() {
        // assigns the amount of points the piece should be moved from the origin (0,0) at the top left
        // to be in the middle of the board (right movement, which means increasing the x value)
        int x = Properties.getWidth() % 2 == 0 ? Properties.getWidth() / 2 - 2 : Properties.getWidth() / 2 - 1;
        // equivalent code:
//        int x;
//        if (Properties.getWidth() % 2 == 0) {
//            x = Properties.getWidth() / 2 - 2;
//        } else {
//            x = Properties.getWidth() / 2 - 1;
//        }
        // a loop to loop over the points of the fallingPiece points
        for (Point point : fallingPiece.getPoints()) {
            // adds the value of the variable x to the x value of the point
            point.addX(x);
        }
        updateShadowPoints(false);
    }

    private boolean canMove(Direction direction) {
        // we loop over the falling piece points and perform the requested movement on every point to check if it is possible
        // as in there is no piece at that point.
        for (Point piecePoint : fallingPiece.getPoints()) {
            // as we are just checking objects, we duplicate, so we do not change the original falling piece points.
            Point point = piecePoint.duplicate();

            // in this switch statement, we
            // execute the appropriate methods that corresponds to the requested movement.
            switch (direction) {
                case DOWN_BY_PLAYER:
                case DOWN:
                    point.addY(1);
                    break;
                case LEFT:
                    point.addX(-1);
                    break;
                case RIGHT:
                    point.addX(1);
                    break;
                case ROTATE_CLOCKWISE:
                    if (!(fallingPiece instanceof Rotatable)) {
                        return false;
                    }
                    point.rotate(fallingPiece.getOrigin(), true);
                    break;
                case ROTATE_COUNTERCLOCKWISE:
                    if (!(fallingPiece instanceof Rotatable)) {
                        return false;
                    }
                    point.rotate(fallingPiece.getOrigin(), false);
                    break;
            }

            // this if check is checking if there is a piece at the point we are moving to, or the point is outside the board.
            if (containsPoint(point) ||
                    point.getY() < 0 || point.getY() >= Properties.getHeight() ||
                    point.getX() < 0 || point.getX() >= Properties.getWidth()) {
                // immediately retun false if at least one of the points cannot be moved.
                return false;
            }
        }

        // if the method reached this point, the all points can be moved successfully, so we return true.
        return true;
    }

    // this method check if there are full lines in the region given, from the lowest point to the highest point.
    private void checkForFullLines(int highestPoint, int lowestPoint) {
        // a variable to count how many lines has been cleared.
        int countOfLines = 0;
        // we loop over the lines between and including the given highest and lowest points.
        for (int i = highestPoint; i <= lowestPoint; i++) {
            // we check if there is no list of points at i row or if the amount of points at i row are not equal
            // to the width (not a full line)
            if (filledPoints.get(i) == null || filledPoints.get(i).size() != Properties.getWidth()) {
                // we skip the row because it is not full to clear it.
                continue;
            }

            // row is full, so we add the points of the row to be cleared
            pointsToClear.addAll(filledPoints.get(i));
            // we avoid bugs of having unwanted colored points by removing the falling piece points and the cleared
            // row points from the points to color list
            pointsToColor.removeAll(fallingPiece.getPoints());
            pointsToColor.removeAll(filledPoints.get(i));

            // we remove the row from our filled points map
            filledPoints.remove(i);
            // we increase the cleared lines by one
            countOfLines++;
            clearedLines++;

            // create new map to be the new filledPoints map without the removed line.
            Map<Integer, List<Point>> newFilledPoints = new HashMap<>();
            // we loop over all the keys in the current filled points map (including the one we want to clear)
            for (int row : filledPoints.keySet()) {
                // if the row/key is greater than the line to be cleared (at a lower point that it)
                if (row >= i) {
                    // we add the row as it is in the new list.
                    newFilledPoints.put(row, filledPoints.get(row));
                    continue;
                }

                // the list of points of the row which is at a higher point that the line to be cleared.
                List<Point> rowPoints = filledPoints.get(row);
                // we loop over all the points of this ^ list to move the y value down by 1
                for (Point point : rowPoints) {
                    pointsToClear.add(point.duplicate());
                    point.addY(1);

                    // in this if check, we see if there is a gift point at the new position of the above lines
                    if (gift != null && point.equals(gift.getPoint())) {
                        // if so, we clear the gift point and generate a new one in a new place again.
                        pointsToClear.add(gift.getPoint());
                        gift = null;
                        showGift();
                    }
                }

                // we add the list of points that were moved down by one to the map and by moving the key down as well.
                newFilledPoints.put(row+1, rowPoints);
                // we add the moved row to the points to color list.
                pointsToColor.addAll(rowPoints);
            }
            // we assign the filledPoints variable to the newFilledPoints map which has the updated lists and keys
            // after clearing a line
            filledPoints = newFilledPoints;
        }

        // we increase the score.
        score += countOfLines == 0 ? 0 : (200*countOfLines - 100) * level;
    }

    private void placeFallingPiece() {
        // loop over the falling piece points to place the piece
        for (Point point : fallingPiece.getPoints()) {
            fillPoint(point);
        }

        checkForFullLines(Utils.getHighestPoint(fallingPiece.getPoints()), Utils.getLowestPoint(fallingPiece.getPoints()));
        generateNextFallingPiece();
        // since we placed, we can now hold no matter what.
        isHoldingPossible = true;
    }

    private void showGift() {
        // if there is currently a gift, then we don't do anything until the current one is collected
        if (gift != null) {
            return;
        }

        // we get a random gift and show it using its method
        gift = Gift.getRandomGift(this);
        gift.showGift();
    }

    private void checkGifts() {
        // if there is no gift, or the player didn't go over the gift, then we don't do anything
        if (gift == null || !fallingPiece.getPoints().contains(gift.getPoint())) {
            return;
        }

        // we give the reward as it has been collected if the method reached here.
        gift.giveReward();
        // we assign the gift and gift point variables to null to empty the gift slot of future gifts.
        giftPoint = null;
        gift = null;
        checkForFullLines(Properties.getHeight()-1, Properties.getHeight()-1);
        updateShadowPoints(true);
    }

    public void  fillPoint(Point point) {
        // the current list of points at the given point's y value in the map, or if there is none, then we
        // create a new empty list
        List<Point> rowPoints = filledPoints.get(point.getY()) == null ? new ArrayList<>() : filledPoints.get(point.getY());
        // we add the given point to the list
        rowPoints.add(point);

        // we color the point in the user interface by adding it to the appropriate list
        pointsToColor.add(point);
        // we set/update the row list in our map
        filledPoints.put(point.getY(), rowPoints);
    }

    public boolean containsPoint(Point point) {
        // the list of points at the given point's y value in the map to check
        List<Point> rowPoints = filledPoints.get(point.getY());

        // if the list is not null (there are points at the given point's y value) and one of the points matches our point
        // then the method returns true, false otherwise.
        return rowPoints != null && rowPoints.contains(point);
    }

    public LinkedList<Piece> getNextFallingPieces() {
        return new LinkedList<>(nextFallingPieces);
    }

    public List<Point> getPointsToClear() {
        return pointsToClear;
    }

    public List<Point> getPointsToColor() {
        return pointsToColor;
    }

    public Point getGiftPoint() {
        return giftPoint;
    }

    public void
    setGiftPoint(Point giftPoint) {
        this.giftPoint = giftPoint;
    }

    public List<Point> getShadowPoints() {
        return shadowPoints;
    }

    public Piece getFallingPiece() {
        return fallingPiece;
    }

    public Piece getHeldPiece() {
        return heldPiece;
    }

    public Gift getGift() {
        return gift;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public boolean isUpdateNextPiecesGrid() {
        return updateNextPiecesGrid;
    }

    public int getScore() {
        return score;
    }

    public void increaseScore(int value) {
        score += value;
    }

    public int getClearedLines() {
        return clearedLines;
    }

    public void setUpdateNextPiecesGrid(boolean updateNextPiecesGrid) {
        this.updateNextPiecesGrid = updateNextPiecesGrid;
    }
}
