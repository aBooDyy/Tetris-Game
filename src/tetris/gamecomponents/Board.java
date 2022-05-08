package tetris.gamecomponents;

import tetris.gamecomponents.gifts.Gift;
import tetris.utilities.Direction;
import tetris.utilities.Point;
import tetris.utilities.Properties;
import tetris.utilities.Utils;

import java.util.*;

public class Board {

    private static final int SCORE_FOR_GIFT = 100;

    private final LinkedList<Piece> nextFallingPieces;
    private final List<Point> pointsToClear;
    private final List<Point> pointsToColor;
    private final List<Point> giftPoints;

    private Map<Integer, List<Point>> filledPoints;
    private List<Point> shadowPoints;

    private Piece fallingPiece;
    private Piece fallingPiece2;
    private Piece heldPiece;

    private Gift gift;

    private boolean isHoldingPossible;
    private boolean isGameOver;
    private boolean updateNextPiecesGrid;

    private int lastGiftedScore;
    private int score;
    private int clearedLines;
    private final int level;

    public Board(int level, boolean isTwoPlayers) {
        this.level = level;

        nextFallingPieces = new LinkedList<>();
        pointsToClear = new ArrayList<>();
        pointsToColor = new ArrayList<>();
        giftPoints = new ArrayList<>();
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
        if (clearOld) {
            pointsToClear.addAll(shadowPoints);
        }

        boolean noShadow = true;
        List<Point> shadowPoints = new ArrayList<>();
        for (Point point : fallingPiece.getPoints()) {
            shadowPoints.add(point.duplicate());
        }

        boolean flag = true;
        while (flag) {
            for (Point point : shadowPoints) {
                Point checkerPoint = point.duplicate();
                checkerPoint.addY(1);

                if (containsPoint(checkerPoint) ||
                        checkerPoint.getY() < 0 || checkerPoint.getY() >= Properties.getHeight()) {
                    flag = false;
                }
            }

            if (flag) {
                noShadow = false;
                for (Point point : shadowPoints) {
                    point.addY(1);
                }
            }
        }

        this.shadowPoints = noShadow ? new ArrayList<>() : shadowPoints;
    }

    public boolean moveFallingPiece(Direction direction) {
        if (isGameOver) {
            return false;
        }

        if (!canMove(direction)) {
            if (direction == Direction.DOWN || direction == Direction.DOWN_BY_PLAYER) {
                placeFallingPiece();
                return false;
            }

            if (direction != Direction.ROTATE_CLOCKWISE && direction != Direction.ROTATE_COUNTERCLOCKWISE) {
                return false;
            }

            boolean canMoveLeft = canMove(Direction.LEFT);
            boolean canMoveRight = canMove(Direction.RIGHT);
            if (!canMoveLeft && !canMoveRight) {
                return false;
            }

            boolean movementAccomplished = false;

            if (!canMoveLeft) {
                moveFallingPiece(Direction.RIGHT);
                if (!moveFallingPiece(direction)) {
                    moveFallingPiece(Direction.RIGHT);
                    movementAccomplished = moveFallingPiece(direction);
                }
            }

            if (!canMoveRight) {
                moveFallingPiece(Direction.LEFT);
                if (!moveFallingPiece(direction)) {
                    moveFallingPiece(Direction.LEFT);
                    movementAccomplished = moveFallingPiece(direction);
                }
            }

            return movementAccomplished;
        }

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

        if (score != lastGiftedScore && score / SCORE_FOR_GIFT > lastGiftedScore / SCORE_FOR_GIFT) {
            showGift();
            lastGiftedScore = score;
        }

        checkGifts();
        return true;
    }

    public void hardDrop() {
        if (isGameOver) {
            return;
        }

        if (shadowPoints.isEmpty()) {
            return;
        }

        int fallingPieceLowestPoint = Utils.getLowestPoint(fallingPiece.getPoints());
        int shadowPieceLowestPoint = Utils.getLowestPoint(shadowPoints);

        pointsToClear.addAll(fallingPiece.getPoints());
        fallingPiece.getPoints().clear();
        for (Point shadowPoint : shadowPoints) {
            Point newPiecePoint = shadowPoint.duplicate();
            newPiecePoint.setColor(fallingPiece.getColor());
            fallingPiece.getPoints().add(newPiecePoint);
        }

        checkGifts();

        pointsToColor.addAll(fallingPiece.getPoints());
        placeFallingPiece();

        score += (shadowPieceLowestPoint - fallingPieceLowestPoint) * 2;

        if (score != lastGiftedScore && score / SCORE_FOR_GIFT > lastGiftedScore / SCORE_FOR_GIFT) {
            showGift();
            lastGiftedScore = score;
        }
    }

    public void holdFallingPiece() {
        if (isGameOver) {
            return;
        }

        if (heldPiece == null) {
            pointsToClear.addAll(fallingPiece.getPoints());
            pointsToClear.addAll(shadowPoints);
            heldPiece = Piece.getPiece(fallingPiece.getID());
            isHoldingPossible = false;
            generateNextFallingPiece();
            return;
        }

        if (!isHoldingPossible) {
            return;
        }

        pointsToClear.addAll(fallingPiece.getPoints());
        pointsToClear.addAll(shadowPoints);

        Piece temp = fallingPiece;
        fallingPiece = heldPiece;
        heldPiece = Piece.getPiece(temp.getID());
        isHoldingPossible = false;
        positionFallingPiece();
    }

    private void generateNextFallingPiece() {
        // Gameover
        for (int i = Properties.getWidth()/2 - 2; i < Properties.getWidth()/2; i++) {
            Point point = new Point(i, 0);

            if (containsPoint(point)) {
                isGameOver = true;
                return;
            }
        }

        if (nextFallingPieces.size() == 0) {
            fallingPiece = Piece.getPiece();
            positionFallingPiece();
            fillNextFallingPieces();
            return;
        }

        fallingPiece = nextFallingPieces.getFirst();
        nextFallingPieces.removeFirst();
        positionFallingPiece();
        fillNextFallingPieces();
    }

    private void fillNextFallingPieces() {
        for (int i = nextFallingPieces.size(); i < 3; i++) {
            nextFallingPieces.addLast(Piece.getPiece());
        }
    }

    private void positionFallingPiece() {
        int x = Properties.getWidth() % 2 == 0 ? Properties.getWidth() / 2 - 2 : Properties.getWidth() / 2 - 1;
        for (Point point : fallingPiece.getPoints()) {
            point.addX(x);
        }
        updateShadowPoints(false);
    }

    private boolean canMove(Direction direction) {
        for (Point piecePoint : fallingPiece.getPoints()) {
            Point point = piecePoint.duplicate();

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

            if (containsPoint(point) ||
                    point.getY() < 0 || point.getY() >= Properties.getHeight() ||
                    point.getX() < 0 || point.getX() >= Properties.getWidth()) {
                return false;
            }
        }

        return true;
    }

    private void checkForFullLines(int highestPoint, int lowestPoint) {
        int countOfLines = 0;
        for (int i = highestPoint; i <= lowestPoint; i++) {
            if (filledPoints.get(i) == null || filledPoints.get(i).size() != Properties.getWidth()) {
                continue;
            }

            pointsToClear.addAll(filledPoints.get(i));
            pointsToColor.removeAll(fallingPiece.getPoints());
            pointsToColor.removeAll(filledPoints.get(i));

            filledPoints.remove(i);
            countOfLines++;
            clearedLines++;

            Map<Integer, List<Point>> newFilledPoints = new HashMap<>();
            for (int row : filledPoints.keySet()) {
                if (row >= i) {
                    newFilledPoints.put(row, filledPoints.get(row));
                    continue;
                }

                List<Point> rowPoints = filledPoints.get(row);
                for (Point point : rowPoints) {
                    pointsToClear.add(point.duplicate());
                    point.addY(1);

                    if (gift != null && point.equals(gift.getPoint())) {
                        pointsToClear.add(gift.getPoint());
                        gift = null;
                        showGift();
                    }
                }

                newFilledPoints.put(row+1, rowPoints);
                pointsToColor.addAll(rowPoints);
            }
            filledPoints = newFilledPoints;
        }

        score += countOfLines == 0 ? 0 : (200*countOfLines - 100) * level;
    }

    private void placeFallingPiece() {
        for (Point point : fallingPiece.getPoints()) {
            if (filledPoints.get(point.getY()) == null) {
                filledPoints.put(point.getY(), new ArrayList<>());
            }

            filledPoints.get(point.getY()).add(point);
        }

        updateNextPiecesGrid = true;
        checkForFullLines(Utils.getHighestPoint(fallingPiece.getPoints()), Utils.getLowestPoint(fallingPiece.getPoints()));
        generateNextFallingPiece();
        isHoldingPossible = true;
    }

    private void showGift() {
        if (gift != null) {
            return;
        }

        gift = Gift.getRandomGift(this);
        gift.showGift();
    }

    private void checkGifts() {
        if (gift == null || !fallingPiece.getPoints().contains(gift.getPoint())) {
            return;
        }

        gift.giveReward();
        giftPoints.remove(gift.getPoint());
        gift = null;
        checkForFullLines(Properties.getHeight()-1, Properties.getHeight()-1);
        updateShadowPoints(true);
    }

    public void  fillPoint(Point point) {
        List<Point> rowPoints = filledPoints.get(point.getY()) == null ? new ArrayList<>() : filledPoints.get(point.getY());
        rowPoints.add(point);

        pointsToColor.add(point);
        filledPoints.put(point.getY(), rowPoints);
    }

    public boolean containsPoint(Point point) {
        List<Point> rowPoints = filledPoints.get(point.getY());

        return rowPoints != null && rowPoints.contains(point);
    }

    public LinkedList<Piece> getNextFallingPieces() {
        //TODO: Should i give copies of the pieces objects aswell?
        return new LinkedList<>(nextFallingPieces);
    }

    public List<Point> getPointsToClear() {
        return pointsToClear;
    }

    public List<Point> getPointsToColor() {
        return pointsToColor;
    }

    public List<Point> getGiftPoints() {
        return giftPoints;
    }

    public List<Point> getShadowPoints() {
        return shadowPoints;
    }

    public Piece getFallingPiece() {
        //TODO: Should i give a copy of this instrad?

        return fallingPiece;
    }

    public Piece getHeldPiece() {
        //TODO: Should i give a copy of this instrad?
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

    public int getLevel() {
        return level;
    }

    public void setUpdateNextPiecesGrid(boolean updateNextPiecesGrid) {
        this.updateNextPiecesGrid = updateNextPiecesGrid;
    }
}
