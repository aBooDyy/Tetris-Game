package tetris.windows;

import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import tetris.Tetris;
import tetris.datahandler.Data;
import tetris.datahandler.PlayerData;
import tetris.gamecomponents.Board;
import tetris.gamecomponents.gifts.ScorePoint;
import tetris.gamecomponents.pieces.IPiece;
import tetris.gamecomponents.pieces.OPiece;
import tetris.gamecomponents.Piece;
import tetris.utilities.Direction;
import tetris.gamecomponents.Point;
import tetris.utilities.Properties;
import tetris.utilities.Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PlayWindow extends StackPane {

    private final Scene SCENE;

    private final HBox WINDOW_CONTENT;
    private final GridPane GAME_GRID;
    private final GridPane NEXT_PIECES_GRID;
    private final GridPane HELD_PIECES_GIRD;

    private final VBox PAUSE_MENU;
    private final VBox GAME_OVER_MENU;

    private Board gameBoard;
    private SequentialTransition fallTransition;

    private final int level;
    private Label score;
    private Label lines;
    private final PlayerData playerData;


    public PlayWindow(String playerName, int level) {
        SCENE = new Scene(this);
        SCENE.getStylesheets().add("style.css");

        WINDOW_CONTENT = new HBox();
        gameBoard = new Board(1);
        GAME_GRID = new GridPane();
        NEXT_PIECES_GRID = new GridPane();
        HELD_PIECES_GIRD = new GridPane();

        PAUSE_MENU = new VBox();
        GAME_OVER_MENU = new VBox();

        this.level = level;
        score = new Label("0");
        lines = new Label("0");
        playerData = Data.getInstance().getPlayerData(playerName);

        createWindow();
        setKeyPressActions();
        fallTransition();
        updateGameGrid();
        updateNextPiecesGrid();
        createPauseMenu();
        createGameOverMenu();
        playerData.increaseGamesPlayed(1);
    }

    /**
     * Handles the update of the user interface visuals by accessing the board object and its
     * points lists (pointsToClear, pointsToColor, shadowPoints, and giftPoints) along with updating the
     * information in the score board.
     */
    private void updateGameGrid() {
        if (gameBoard.isGameOver()) {
            showGameOverMenu();
            return;
        }

        double squarePixel = Properties.PIXEL - 5;

        for (Point point : gameBoard.getPointsToClear()) {
            Rectangle square = new Rectangle(Properties.PIXEL, Properties.PIXEL);
            square.setFill(Color.web(Properties.getColorScheme().getGray()));

            GAME_GRID.add(square, point.getX(), point.getY());
        }
        gameBoard.getPointsToClear().clear();

        for (Point point : gameBoard.getShadowPoints()) {
            Rectangle square = new Rectangle(squarePixel, squarePixel);
            square.setFill(Color.web(Properties.getColorScheme().getGray()));

            Group cell = Utils.getShadedCell(squarePixel, false);
            cell.getChildren().add(0, square);

            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(cell);

            GAME_GRID.add(borderPane, point.getX(), point.getY());
        }

        List<Point> pointsToColor = new ArrayList<>(gameBoard.getPointsToColor());
        pointsToColor.addAll(gameBoard.getFallingPiece().getPoints());

        for (Point point : pointsToColor) {
            Rectangle square = new Rectangle(squarePixel, squarePixel);
            square.setFill(Color.web(point.getColor()));

            Group cell = Utils.getShadedCell(squarePixel, true);
            cell.getChildren().add(0, square);

            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(cell);

            GAME_GRID.add(borderPane, point.getX(), point.getY());
        }
        gameBoard.getPointsToColor().clear();

        Point point = gameBoard.getGiftPoint();
        if (point != null) {
            BorderPane borderPane = new BorderPane();

            if (gameBoard.getGift() instanceof ScorePoint) {
                borderPane.setCenter(Utils.getCoinShape(squarePixel, ((ScorePoint) gameBoard.getGift()).getRandomScore()));
            } else {
                Circle circle = new Circle(squarePixel/2);
                circle.setFill(Color.web(point.getColor()));
                borderPane.setCenter(circle);
            }

            GAME_GRID.add(borderPane, point.getX(), point.getY());
        }


        savePlayerData(Integer.parseInt(score.getText()), Integer.parseInt(lines.getText()));
        score.setText(String.valueOf(gameBoard.getScore()));
        lines.setText(String.valueOf(gameBoard.getClearedLines()));

        if (gameBoard.isUpdateNextPiecesGrid()) {
            updateNextPiecesGrid();
            gameBoard.setUpdateNextPiecesGrid(false);
        }
    }


    /**
     * Handles the update of the user interface visuals by accessing the next pieces grid
     * object and its nextFallingPieces points lists.
     */
    private void updateNextPiecesGrid() {
        LinkedList<Piece> nextPieces = gameBoard.getNextFallingPieces();
        double squarePixel = Properties.PIXEL / 2d;

        for (int row = 0; row < 10; row++) {
            for (int column = 0; column < 6; column++) {
                Rectangle square = new Rectangle(squarePixel, squarePixel);
                square.setFill(Color.web(Properties.getColorScheme().getGray()));
                NEXT_PIECES_GRID.add(square, column, row);
            }
        }

        for (int i = 0; i < nextPieces.size(); i++) {
            Piece piece = nextPieces.get(i);
            int addedX = piece instanceof OPiece ? 2 : 1;
            int addedY = 1 + (Math.abs(i-2) * 3);
            addedY = piece instanceof IPiece ? addedY + 1 : addedY;

            for (Point point : piece.getPoints()) {
                Rectangle square = new Rectangle(squarePixel, squarePixel);
                square.setFill(Color.web(piece.getColor()));

                Group cell = Utils.getShadedCell(squarePixel, false);
                cell.getChildren().add(0, square);

                NEXT_PIECES_GRID.add(cell, point.getX()+addedX, point.getY() + addedY);
            }

        }
    }


    /**
     * Handles the update of the user interface visuals by accessing the held pieces grid
     * object and its heldPiece points list.
     */
    private void updateHeldPieceGrid() {
        Piece piece = gameBoard.getHeldPiece();
        double squarePixel = Properties.PIXEL / 2d;

        int addedX = piece instanceof OPiece ? 2 : 1;
        int addedY = piece instanceof IPiece ? 2 : 1;

        for (int row = 1; row < 3; row++) {
            for (int column = 1; column < 5; column++) {
                Rectangle square = new Rectangle(squarePixel, squarePixel);
                square.setFill(Color.web(Properties.getColorScheme().getGray()));
                HELD_PIECES_GIRD.add(square, column, row);
            }
        }

        if (piece == null) {
            return;
        }

        for (Point point : piece.getPoints()) {
            Rectangle square = new Rectangle(squarePixel, squarePixel);
            square.setFill(Color.web(piece.getColor()));

            Group cell = Utils.getShadedCell(squarePixel, false);
            cell.getChildren().add(0, square);

            HELD_PIECES_GIRD.add(cell, point.getX() + addedX, point.getY() + addedY);
        }

    }


    /**
     * Assigns the keyboard inputs DOWN to DOWN_BY_PLAYER, LEFT arrow key to LEFT,
     * RIGHT arrow key to RIGHT, UP arrow key to ROTATE_CLOCKWISE, Z to ROTATE_COUNTERCLOCKWISE, C
     * to holding the piece, SPACE to hard drop the piece, and ESCAPE to pause or resume the game.
     */
    private void setKeyPressActions() {
        SCENE.setOnKeyPressed(keyEvent -> {

            if (this.getChildren().contains(PAUSE_MENU) && keyEvent.getCode() == KeyCode.ESCAPE) {
                resumeGame();
                return;
            }

            if ((this.getChildren().contains(PAUSE_MENU) || this.getChildren().contains(GAME_OVER_MENU))) {
                return;
            }

            switch (keyEvent.getCode()) {
                case DOWN:
                    gameBoard.moveFallingPiece(Direction.DOWN_BY_PLAYER);
                    updateNextPiecesGrid();
                    break;
                case LEFT:
                    gameBoard.moveFallingPiece(Direction.LEFT);
                    break;
                case RIGHT:
                    gameBoard.moveFallingPiece(Direction.RIGHT);
                    break;
                case UP:
                    gameBoard.moveFallingPiece(Direction.ROTATE_CLOCKWISE);
                    break;
                case Z:
                    gameBoard.moveFallingPiece(Direction.ROTATE_COUNTERCLOCKWISE);
                    break;
                case C:
                    gameBoard.holdFallingPiece();
                    updateHeldPieceGrid();
                    break;
                case SPACE:
                    gameBoard.hardDrop();
                    break;
                case ESCAPE:
                    pauseGame();
                    return;
                default:
                    return;
            }

            updateGameGrid();
        });
    }

    /**
     * Executes when an object is instantiated from the class and handles the functionality for
     * the automatic movement for the object and calculating the play time.
     */
    private void fallTransition() {
        // speed in milliseconds
        double speed = 900d - level * 150;

        fallTransition = new SequentialTransition();
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(speed));

        pauseTransition.setOnFinished(event -> {
            gameBoard.moveFallingPiece(Direction.DOWN);
            playerData.increasePlayTime((long) speed);
            updateGameGrid();
        });

        fallTransition.getChildren().add(pauseTransition);
        fallTransition.setCycleCount(Timeline.INDEFINITE);
        fallTransition.play();
    }

    /**
     * Handles the creation of the window which contains javafx elements and will eventually
     * represent the user interface of the game.
     */
    private void createWindow() {

        setupGrid(GAME_GRID, Properties.getWidth(), Properties.getHeight(), Properties.PIXEL, false);
        setupGrid(NEXT_PIECES_GRID, 6, 10, Properties.PIXEL/2d, true);
        setupGrid(HELD_PIECES_GIRD, 6, 4, Properties.PIXEL/2d, true);

        GAME_GRID.getStyleClass().add("game-grid");

        BorderPane rightSidePane = new BorderPane();
        rightSidePane.setPadding(new Insets(10));

        Label nextPiecesBoxLabel = new Label("NEXT");
        nextPiecesBoxLabel.setPadding(new Insets(0, 0, 10, 30));
        nextPiecesBoxLabel.getStyleClass().add("label");

        Label leftArrow = new Label("\u2B9C");
        leftArrow.setPadding(new Insets(0, 0, 20, 15));
        leftArrow.getStyleClass().add("label");

        BorderPane nextPiecesBoxPane = new BorderPane();
        nextPiecesBoxPane.setPadding(new Insets(25, 10, 25, 25));

        nextPiecesBoxPane.setTop(nextPiecesBoxLabel);
        nextPiecesBoxPane.setCenter(NEXT_PIECES_GRID);
        nextPiecesBoxPane.setRight(leftArrow);
        BorderPane.setAlignment(leftArrow, Pos.BOTTOM_RIGHT);

        HBox pauseButton = new HBox();
        pauseButton.setMinSize(50, 50);
        pauseButton.setMaxSize(50, 50);
        pauseButton.getStyleClass().add("tab-style");

        HBox pauseBars = new HBox();
        pauseBars.setPadding(new Insets(10));

        Pane bar1 = new Pane();
        bar1.setMinSize(8, 30);
        bar1.setMaxSize(8, 30);
        bar1.getStyleClass().add("pause-bar");

        Pane spaceBar = new Pane();
        spaceBar.setMaxSize(10, 30);
        spaceBar.setMinSize(10, 30);
        spaceBar.getStyleClass().add("pause-space-bar");

        Pane bar2 = new Pane();
        bar2.setMinSize(8, 30);
        bar2.setMaxSize(8, 30);
        bar2.getStyleClass().add("pause-bar");

        pauseBars.getChildren().addAll(bar1, spaceBar, bar2);

        pauseButton.setAlignment(Pos.CENTER);
        pauseButton.getChildren().add(pauseBars);

        pauseButton.setOnMouseClicked(mouseEvent -> pauseGame());

        rightSidePane.setTop(nextPiecesBoxPane);
        rightSidePane.setBottom(pauseButton);
        BorderPane.setAlignment(pauseButton, Pos.CENTER_RIGHT);

        BorderPane heldPieceBoxPane = new BorderPane();

        Label heldBoxLabel = new Label("HOLD");
        heldBoxLabel.getStyleClass().add("label");
        heldBoxLabel.setPadding(new Insets(0, 0, 10, 0));

        heldPieceBoxPane.setTop(heldBoxLabel);
        heldPieceBoxPane.setCenter(HELD_PIECES_GIRD);
        BorderPane.setAlignment(heldBoxLabel, Pos.CENTER);
        heldPieceBoxPane.setPadding(new Insets(25));


        VBox infoBox = new VBox();

        Label scoreText = new Label("Score");
        scoreText.getStyleClass().add("score-label");

        score = new Label("0");
        score.getStyleClass().add("score-label");


        Label levelText = new Label("Level");
        levelText.getStyleClass().add("score-label");
        levelText.setPadding(new Insets(5, 0,0,0));

        Label level = new Label(String.valueOf(this.level));
        level.getStyleClass().add("score-label");

        Label linesText = new Label("Lines");
        linesText.getStyleClass().add("score-label");
        linesText.setPadding(new Insets(5, 0,0,0));

        lines = new Label("0");
        lines.getStyleClass().add("score-label");


        infoBox.getChildren().addAll(scoreText, score, levelText, level, linesText, lines);
        infoBox.setPadding(new Insets(0, 25, 5, 5));

        BorderPane leftSidePane = new BorderPane();
        leftSidePane.setTop(heldPieceBoxPane);
        leftSidePane.setBottom(infoBox);

        VBox gameGridBox = new VBox();
        gameGridBox.setAlignment(Pos.CENTER);
        gameGridBox.getChildren().add(GAME_GRID);

        WINDOW_CONTENT.setAlignment(Pos.CENTER);
        WINDOW_CONTENT.setPadding(new Insets(2));
        //space between the Game Grid and the text
        WINDOW_CONTENT.setSpacing(25d);
        WINDOW_CONTENT.getChildren().addAll(leftSidePane, gameGridBox, rightSidePane);
        WINDOW_CONTENT.getStyleClass().add("background-color");

        this.getChildren().add(WINDOW_CONTENT);
    }

    /**
     * Handles the termination of the play window, used when the the user would like to
     * exit the game matrix and return to the main menu.
     */
    private void terminateWindow() {
        fallTransition = null;
        Tetris.switchScene(MainWindow.getInstance().getMainWindowScene());
    }

    /**
     * This method creates a new game board matrix when the user pauses and resumes the
     * game, and when the user fails the game and begins a new game.
     */
    private void startNewGame() {
        gameBoard = new Board(level);
        for (int row = 0; row < Properties.getHeight(); row++) {
            for (int column = 0; column < Properties.getWidth(); column++) {
                Rectangle square = new Rectangle(Properties.PIXEL, Properties.PIXEL);
                square.setFill(Color.web(Properties.getColorScheme().getGray()));

                GAME_GRID.add(square, column, row);
            }
        }

        score.setText("0");
        lines.setText("0");

        updateNextPiecesGrid();
        updateHeldPieceGrid();
    }

    /**
     * This method pauses the game play and displays the pause menu with options to resume,
     * restart, or close.
     */
    private void pauseGame() {
        fallTransition.pause();
        WINDOW_CONTENT.setEffect(new GaussianBlur());

        this.getChildren().add(PAUSE_MENU);
    }

    /**
     * This method hides the pause menu and resumes the game play.
     */
    private void resumeGame() {
        fallTransition.play();
        WINDOW_CONTENT.setEffect(null);

        this.getChildren().remove(PAUSE_MENU);
    }

    /**
     * Configures the pause menu options with javafx elements, including resume, restart,
     * and close.
     */
    private void createPauseMenu() {
        PAUSE_MENU.setSpacing(25d);
        PAUSE_MENU.setMinSize(200, 400);
        PAUSE_MENU.setMaxSize(200, 400);
        PAUSE_MENU.getStyleClass().add("tab-style");
        PAUSE_MENU.setAlignment(Pos.CENTER);

        Label paused = new Label("PAUSED");
        paused.setStyle("-fx-font-weight: bold;");
        Button resume = new Button("Resume");
        resume.getStyleClass().add("buttons");
        Button restart = new Button("Restart");
        restart.getStyleClass().add("buttons");
        Button quit = new Button("Quit");
        quit.getStyleClass().add("buttons");

        resume.setOnAction(actionEvent -> resumeGame());
        restart.setOnAction(actionEvent -> {
            startNewGame();
            resumeGame();
        });
        quit.setOnAction(actionEvent -> terminateWindow());

        PAUSE_MENU.getChildren().addAll(paused, resume, restart, quit);
    }

    /**
     * When the game is officially over (the center four points of the top row are filled),
     * this menu is displayed.
     */
    private void showGameOverMenu() {
        fallTransition.stop();
        WINDOW_CONTENT.setEffect(new GaussianBlur());

        this.getChildren().add(GAME_OVER_MENU);
    }

    /**
     * Hides the game over menu from view.
     */
    private void hideGameOverMenu() {
        fallTransition.play();
        WINDOW_CONTENT.setEffect(null);

        this.getChildren().remove(GAME_OVER_MENU);
    }

    /**
     *  configures the game over menu using JavaFX elements.
     */
    private void createGameOverMenu() {
        GAME_OVER_MENU.setSpacing(25d);
        GAME_OVER_MENU.setMinSize(200, 400);
        GAME_OVER_MENU.setMaxSize(200, 400);
        GAME_OVER_MENU.getStyleClass().add("tab-style");
        GAME_OVER_MENU.setAlignment(Pos.CENTER);

        Label gameOver = new Label("GAME OVER");
        Button tryAgain = new Button("Try again");
        Button quit = new Button("Quit");

        tryAgain.setOnAction(actionEvent -> {
            startNewGame();
            hideGameOverMenu();
        });
        quit.setOnAction(actionEvent -> terminateWindow());

        GAME_OVER_MENU.getChildren().addAll(gameOver, tryAgain, quit);
    }

    /**
     * Saves player data every time the piece is moved in the game which results in changing
     * the statistics of the player.
     *
     * @param oldScore the old score of the board to calculate the amount to increase the score
     * @param oldLines the old cleared lines of the board to calculate the amount to increase the cleared lines
     */
    private void savePlayerData(int oldScore, int oldLines) {
        playerData.increaseLines(gameBoard.getClearedLines() - oldLines);
        playerData.increaseTotalScore(gameBoard.getScore() - oldScore);
        playerData.setHighestScore(gameBoard.getScore());
    }

    /**
     * Sets up the JavaFX grid elements for a given grid pane
     *
     * @param grid the grid to set up
     * @param width the desired width of the grid
     * @param height the desired height of the grid
     * @param size the desired size of the square box
     * @param setStyle whether to apply css style to the grid
     */
    private static void setupGrid(GridPane grid, int width, int height, double size, boolean setStyle) {
        grid.setVgap(2);
        grid.setHgap(2);
        grid.setPadding(new Insets(2));

        if (setStyle) {
            grid.getStyleClass().addAll("grid", "grid-border");
        }

        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                Rectangle square = new Rectangle(size, size);
                square.setFill(Color.web(Properties.getColorScheme().getGray()));
                grid.add(square, column, row);
            }
        }
    }

    public Scene getPlayWindowScene() {
        return SCENE;
    }
}
