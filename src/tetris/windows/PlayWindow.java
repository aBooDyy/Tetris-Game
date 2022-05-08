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
import tetris.gamecomponents.pieces.IPiece;
import tetris.gamecomponents.pieces.OPiece;
import tetris.gamecomponents.Piece;
import tetris.utilities.Direction;
import tetris.utilities.Point;
import tetris.utilities.Properties;
import tetris.utilities.Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PlayWindow extends StackPane {

    private final Scene scene;

    private final HBox windowContent;
    private final GridPane gameGrid;
    private final GridPane nextPiecesGrid;
    private final GridPane heldPieceGrid;

    private final VBox pauseMenu;
    private final VBox gameOverMenu;

    private Board gameBoard;
    private SequentialTransition fallTransition;

    private final int level;
    private Label score;
    private Label lines;
    private final PlayerData playerData;
    private PlayerData player2Data;


    public PlayWindow(String playerName, int level) {
        this(playerName, null, level);
    }

    public PlayWindow(String playerName, String player2Name, int level) {
        scene = new Scene(this);
        scene.getStylesheets().add("style.css");

        windowContent = new HBox();
        gameBoard = new Board(1, player2Name != null);
        gameGrid = new GridPane();
        nextPiecesGrid = new GridPane();
        heldPieceGrid = new GridPane();

        pauseMenu = new VBox();
        gameOverMenu = new VBox();

        this.level = level;
        score = new Label("0");
        lines = new Label("0");
        playerData = Data.getInstance().getPlayerData(playerName);

        if (player2Name != null) {
            player2Data = Data.getInstance().getPlayerData(player2Name);
        }

        createWindow();
        setKeyPressActions();
        fallTransition();
        updateGameGrid();
        updateNextPiecesGrid();
        createPauseMenu();
        createGameOverMenu();
        playerData.increaseGamesPlayed(1);
    }

    private void updateGameGrid() {
        if (gameBoard.isGameOver()) {
            showGameOverMenu();
            return;
        }

        double squarePixel = Properties.PIXEL - 5;

        for (Point point : gameBoard.getPointsToClear()) {
            Rectangle square = new Rectangle(Properties.PIXEL, Properties.PIXEL);
            square.setFill(Color.web(Properties.getColorScheme().getGray()));

            gameGrid.add(square, point.getX(), point.getY());
        }
        gameBoard.getPointsToClear().clear();

        for (Point point : gameBoard.getShadowPoints()) {
            Rectangle square = new Rectangle(squarePixel, squarePixel);
            square.setFill(Color.web(Properties.getColorScheme().getGray()));

            Group cell = Utils.getShadedCell(squarePixel);
            cell.getChildren().add(0, square);

            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(cell);

            gameGrid.add(borderPane, point.getX(), point.getY());
        }

        List<Point> pointsToColor = new ArrayList<>(gameBoard.getPointsToColor());
        pointsToColor.addAll(gameBoard.getFallingPiece().getPoints());

        for (Point point : pointsToColor) {
            Rectangle square = new Rectangle(squarePixel, squarePixel);
            square.setFill(Color.web(point.getColor()));

            Group cell = Utils.getShadedCell(squarePixel);
            cell.getChildren().add(0, square);

            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(cell);

            gameGrid.add(borderPane, point.getX(), point.getY());
        }
        gameBoard.getPointsToColor().clear();

        //TODO Put this nicely
        for (Point point : gameBoard.getGiftPoints()) {
            Circle circle = new Circle(squarePixel/2);
            circle.setFill(Color.web(point.getColor()));

            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(circle);

            gameGrid.add(borderPane, point.getX(), point.getY());
        }

        savePlayerData(Integer.parseInt(score.getText()), Integer.parseInt(lines.getText()));
        score.setText(String.valueOf(gameBoard.getScore()));
        lines.setText(String.valueOf(gameBoard.getClearedLines()));

        if (gameBoard.isUpdateNextPiecesGrid()) {
            updateNextPiecesGrid();
            gameBoard.setUpdateNextPiecesGrid(false);
        }
    }

    private void updateNextPiecesGrid() {
        LinkedList<Piece> nextPieces = gameBoard.getNextFallingPieces();
        double squarePixel = Properties.PIXEL / 2d;

        for (int row = 0; row < 10; row++) {
            for (int column = 0; column < 6; column++) {
                Rectangle square = new Rectangle(squarePixel, squarePixel);
                square.setFill(Color.web(Properties.getColorScheme().getGray()));
                nextPiecesGrid.add(square, column, row);
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

                Group cell = Utils.getShadedCell(squarePixel);
                cell.getChildren().add(0, square);

                nextPiecesGrid.add(cell, point.getX()+addedX, point.getY() + addedY);
            }

        }
    }

    private void updateHeldPieceGrid() {
        Piece piece = gameBoard.getHeldPiece();
        double squarePixel = Properties.PIXEL / 2d;

        int addedX = piece instanceof OPiece ? 2 : 1;
        int addedY = piece instanceof IPiece ? 2 : 1;

        for (int row = 1; row < 3; row++) {
            for (int column = 1; column < 5; column++) {
                Rectangle square = new Rectangle(squarePixel, squarePixel);
                square.setFill(Color.web(Properties.getColorScheme().getGray()));
                heldPieceGrid.add(square, column, row);
            }
        }

        if (piece == null) {
            return;
        }

        for (Point point : piece.getPoints()) {
            Rectangle square = new Rectangle(squarePixel, squarePixel);
            square.setFill(Color.web(piece.getColor()));

            Group cell = Utils.getShadedCell(squarePixel);
            cell.getChildren().add(0, square);

            heldPieceGrid.add(cell, point.getX() + addedX, point.getY() + addedY);
        }

    }

    private void setKeyPressActions() {
        scene.setOnKeyPressed(keyEvent -> {

            if (this.getChildren().contains(pauseMenu) && keyEvent.getCode() == KeyCode.ESCAPE) {
                resumeGame();
                return;
            }

            if ((this.getChildren().contains(pauseMenu) || this.getChildren().contains(gameOverMenu))) {
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
                    if (this.getChildren().contains(pauseMenu)) {
                        resumeGame();
                    } else {
                        pauseGame();
                    }
                    return;
                default:
                    return;
            }

            updateGameGrid();
        });
    }

    private void fallTransition() {
        // 800ms = 0.8s
        double speed = 900d - level * 50;

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

    private void createWindow() {

        setupGrid(gameGrid, Properties.getWidth(), Properties.getHeight(), Properties.PIXEL, false);
        setupGrid(nextPiecesGrid, 6, 10, Properties.PIXEL/2d, true);
        setupGrid(heldPieceGrid, 6, 4, Properties.PIXEL/2d, true);

        gameGrid.getStyleClass().add("game-grid");

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
        nextPiecesBoxPane.setCenter(nextPiecesGrid);
        nextPiecesBoxPane.setRight(leftArrow);
        BorderPane.setAlignment(leftArrow, Pos.BOTTOM_RIGHT);

        HBox pauseButton = new HBox();
        pauseButton.setMinSize(50, 50);
        pauseButton.setMaxSize(50, 50);
        pauseButton.getStyleClass().add("pause-button");

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

        pauseButton.setOnMouseClicked(mouseEvent -> {
            pauseGame();
        });

        rightSidePane.setTop(nextPiecesBoxPane);
        rightSidePane.setBottom(pauseButton);
        BorderPane.setAlignment(pauseButton, Pos.CENTER_RIGHT);

        BorderPane heldPieceBoxPane = new BorderPane();

        Label heldBoxLabel = new Label("HOLD");
        heldBoxLabel.getStyleClass().add("label");
        heldBoxLabel.setPadding(new Insets(0, 0, 10, 0));

        heldPieceBoxPane.setTop(heldBoxLabel);
        heldPieceBoxPane.setCenter(heldPieceGrid);
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
        gameGridBox.getChildren().add(gameGrid);

        windowContent.setAlignment(Pos.CENTER);
        windowContent.setPadding(new Insets(2));
        //space between the Game Grid and the text
        windowContent.setSpacing(25d);
        windowContent.getChildren().addAll(leftSidePane, gameGridBox, rightSidePane);
        windowContent.getStyleClass().add("background-color");

        this.getChildren().add(windowContent);
    }

    private void terminateWindow() {
        fallTransition = null;
        Tetris.switchScene(MainWindow.getInstance().getMainWindowScene());
    }

    private void startNewGame() {
        gameBoard = new Board(1, player2Data != null);
        for (int row = 0; row < Properties.getHeight(); row++) {
            for (int column = 0; column < Properties.getWidth(); column++) {
                Rectangle square = new Rectangle(Properties.PIXEL, Properties.PIXEL);
                square.setFill(Color.web(Properties.getColorScheme().getGray()));

                gameGrid.add(square, column, row);
            }
        }

        score.setText("0");
        lines.setText("0");

        updateNextPiecesGrid();
        updateHeldPieceGrid();
    }

    private void pauseGame() {
        fallTransition.pause();
        windowContent.setEffect(new GaussianBlur());

        this.getChildren().add(pauseMenu);
    }

    private void resumeGame() {
        fallTransition.play();
        windowContent.setEffect(null);

        this.getChildren().remove(pauseMenu);
    }

    private void createPauseMenu() {
        pauseMenu.setSpacing(25d);
        pauseMenu.setMinSize(200, 400);
        pauseMenu.setMaxSize(200, 400);
        // TODO: Style this
        pauseMenu.getStyleClass().add("pause-button");
        pauseMenu.setAlignment(Pos.CENTER);

        Label paused = new Label("PAUSED");
        Button resume = new Button("Resume");
        Button restart = new Button("Restart");
        Button quit = new Button("Quit");

        resume.setOnAction(actionEvent -> resumeGame());
        restart.setOnAction(actionEvent -> {
            startNewGame();
            resumeGame();
        });
        quit.setOnAction(actionEvent -> terminateWindow());

        pauseMenu.getChildren().addAll(paused, resume, restart, quit);
    }

    private void showGameOverMenu() {
        fallTransition.stop();
        windowContent.setEffect(new GaussianBlur());

        this.getChildren().add(gameOverMenu);
    }

    private void hideGameOverMenu() {
        fallTransition.play();
        windowContent.setEffect(null);

        this.getChildren().remove(gameOverMenu);
    }

    private void createGameOverMenu() {
        gameOverMenu.setSpacing(25d);
        gameOverMenu.setMinSize(200, 400);
        gameOverMenu.setMaxSize(200, 400);
        // TODO: Style this
        gameOverMenu.getStyleClass().add("pause-button");
        gameOverMenu.setAlignment(Pos.CENTER);

        Label gameOver = new Label("GAME OVER");
        Button tryAgain = new Button("Try again");
        Button quit = new Button("Quit");

        tryAgain.setOnAction(actionEvent -> {
            startNewGame();
            hideGameOverMenu();
        });
        quit.setOnAction(actionEvent -> terminateWindow());

        gameOverMenu.getChildren().addAll(gameOver, tryAgain, quit);
    }

    private void savePlayerData(int oldScore, int oldLines) {
        playerData.increaseLines(gameBoard.getClearedLines() - oldLines);
        playerData.increaseTotalScore(gameBoard.getScore() - oldScore);
        playerData.setHighestScore(gameBoard.getScore());
    }

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
                // TODO: use my own color object/value
                square.setFill(Color.web(Properties.getColorScheme().getGray()));
                grid.add(square, column, row);
            }
        }
    }

    public Scene getPlayWindowScene() {
        return scene;
    }
}
