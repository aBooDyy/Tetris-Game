package tetris.windows;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import tetris.Tetris;
import tetris.datahandler.Data;
import tetris.datahandler.PlayerData;
import tetris.utilities.Properties;
import tetris.utilities.Utils;

import java.text.NumberFormat;
import java.util.Arrays;

public class MainWindow extends StackPane {

    private final static MainWindow MAIN_MENU = new MainWindow();

    private final Scene SCENE;

    private VBox statisticsMenu;

    private VBox settingsMenu;

    private ScrollPane howToPlayMenu;

    private TextField playerNameField;

    private int level;

    private boolean isMainMenuLocked;

    private MainWindow() {
        SCENE = new Scene(this, 900, 461);
        SCENE.getStylesheets().add("style.css");
        SCENE.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                if (!isMainMenuLocked) {
                    return;
                }

                if (this.getChildren().contains(statisticsMenu)) {
                    this.getChildren().remove(statisticsMenu);
                    isMainMenuLocked = false;
                }

                if (this.getChildren().contains(settingsMenu)) {
                    this.getChildren().remove(settingsMenu);
                    isMainMenuLocked = false;
                }

                if (this.getChildren().contains(howToPlayMenu)) {
                    this.getChildren().remove(howToPlayMenu);
                    isMainMenuLocked = false;
                }
            }
        });
        level = 1;
        createWindow();
        createSettingsMenu();
        createHowToPlayMenu();
    }

    /**
     * Handles the creation of the window which contains javafx elements and will eventually
     * represent the user interface of the main window.
     */
    private void createWindow() {
        Image background = new Image(Tetris.class.getResource("images/main-menu-background.jpg").toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(background, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(1.0, 1.0,
                true, true, false, false));
        this.setBackground(new Background(backgroundImage));

        Image image = new Image(Tetris.class.getResource("images/logo.png").toExternalForm());
        ImageView logo = new ImageView(image);

        VBox playerNamesBox = new VBox();
        playerNamesBox.setAlignment(Pos.CENTER);
        playerNamesBox.setSpacing(5);

        playerNameField = new TextField();
        playerNameField.setPromptText("Player Name");
        playerNameField.setMaxSize(100, 200);
        playerNameField.setMinSize(100, 200);
        playerNameField.getStyleClass().add("player-name-field");

        playerNamesBox.getChildren().add(playerNameField);

        Button playButton = new Button("PLAY");
        playButton.getStyleClass().add("play-button");
        playButton.setOnAction(actionEvent -> {
            if (playerNameField.getText() == null || playerNameField.getText().trim().equals("") || isMainMenuLocked) {
                return;
            }

            PlayWindow playWindow = new PlayWindow(playerNameField.getText(), level);
            Tetris.switchScene(playWindow.getPlayWindowScene());
        });

        HBox hButtons = new HBox();
        hButtons.setSpacing(20d);
        hButtons.setAlignment(Pos.CENTER);

        Button levelButton = new Button("Level: " + level);
        levelButton.getStyleClass().add("buttons");
        levelButton.setOnAction(actionEvent -> {
            level = level == 5 ? 1 : level + 1;
            levelButton.setText("Level: " + level);
        });


        Button statistics = new Button("Statistics");
        statistics.getStyleClass().add("buttons");
        statistics.setOnAction(actionEvent -> {
            if (isMainMenuLocked) {
                return;
            }

            showStatisticsMenu();
        });

        Button settings = new Button("Settings");
        settings.getStyleClass().add("buttons");
        settings.setOnAction(actionEvent -> {
            if (isMainMenuLocked) {
                return;
            }

            showSettingsMenu();
        });

        // TODO:
        Button howToPlayButton = new Button("?");
        howToPlayButton.getStyleClass().add("buttons");
        howToPlayButton.setOnAction(actionEvent -> {
            if (isMainMenuLocked) {
                return;
            }

            showHowToPlayMenu();
        });

        hButtons.getChildren().addAll(levelButton, statistics, settings, howToPlayButton);

        VBox vBox = new VBox();
        vBox.setSpacing(25d);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(logo, playerNamesBox, playButton, hButtons);

        this.getChildren().addAll(vBox);
    }

    /**
     * Handles the creation of the window which contains javafx elements and will
     * eventually represent the user interface of the settings menu.
     */
    private void createSettingsMenu() {
        settingsMenu = new VBox();
        settingsMenu.setSpacing(25d);
        settingsMenu.setPadding(new Insets(5, 5, 0, 0));
        settingsMenu.setMinSize(200, 400);
        settingsMenu.setMaxSize(200, 400);
        settingsMenu.setAlignment(Pos.TOP_CENTER);
        settingsMenu.getStyleClass().add("tab-style");

        BorderPane header = new BorderPane();

        Label settings = new Label("Settings");
        settings.setStyle("-fx-font-weight: bold;");

        Button close = new Button("X");
        close.getStyleClass().add("close-button");
        close.setOnAction(actionEvent -> {
            getChildren().remove(settingsMenu);
            isMainMenuLocked = false;
        });

        header.setCenter(settings);
        header.setTop(close);
        BorderPane.setAlignment(close, Pos.TOP_RIGHT);

        Label width = new Label("Width");
        Spinner<Integer> widthSelector = new Spinner<>(10,30, Properties.getWidth());
        widthSelector.valueProperty().addListener((obs, oldValue, newValue) -> Properties.setWidth(newValue));

        Label height = new Label("Height");
        Spinner<Integer> heightSelector = new Spinner<>(10,22, Properties.getHeight());
        heightSelector.valueProperty().addListener((obs, oldValue, newValue) -> Properties.setHeight(newValue));

        Label schemeLabel = new Label("Schemes");
        String[] schemes = Properties.getColorSchemeList();

        ComboBox<String> scheme = new ComboBox<>(FXCollections.observableList(Arrays.asList(schemes)));
        scheme.getSelectionModel().selectFirst();
        scheme.setOnAction(actionEvent -> Properties.setColorScheme(scheme.getValue()));

        settingsMenu.getChildren().addAll(header, width, widthSelector, height, heightSelector, schemeLabel, scheme);
    }

    /**
     * Displays the settings menu over the main game window with options to change
     * the width and height of the game as well as the color scheme.
     */
    private void showSettingsMenu() {
        isMainMenuLocked = true;
        this.getChildren().add(settingsMenu);
    }

    /**
     * Handles the creation of the window which contains javafx elements and will
     * eventually represent the user interface of the How To Play menu.
     */
    private void createHowToPlayMenu() {
        howToPlayMenu = new ScrollPane();
        howToPlayMenu.setMinSize(700, 400);
        howToPlayMenu.setMaxSize(700, 400);
        howToPlayMenu.setPadding(new Insets(5, 5, 0, 0));
        howToPlayMenu.getStyleClass().add("tab-style");

        VBox content = new VBox();
        content.setMinWidth(670d);
        content.setMaxWidth(670d);
        content.setSpacing(20d);

        BorderPane header = new BorderPane();

        Label howToPlayLabel = new Label("How To Play");
        howToPlayLabel.setStyle("-fx-font-weight: bold;");
        Button close = new Button("X");
        close.getStyleClass().add("close-button");
        close.setOnAction(actionEvent -> {
            getChildren().remove(howToPlayMenu);
            isMainMenuLocked = false;
        });

        header.setTop(close);
        header.setCenter(howToPlayLabel);
        BorderPane.setAlignment(close, Pos.TOP_RIGHT);

        VBox mainContent = new VBox();
        mainContent.setSpacing(20d);
        mainContent.setPadding(new Insets(0, 0, 0, 20));

        Text description = new Text("Stack the pieces on top of each other in order to create a full line\n" +
                "to clear it from the board and be able to stack more pieces.");
        description.setFill(Color.WHITE);
        description.getStyleClass().add("label");

        Label imageLabel = new Label("Use your keyboard to play:");
        Image image = new Image(Tetris.class.getResource("images/HowToPlay-keyboard.PNG").toExternalForm());
        ImageView keyboardImage = new ImageView(image);

        Label copyrightLabel = new Label("Copyright notices:");
        VBox copyrightList = new VBox();
        copyrightList.setSpacing(5d);
        copyrightList.setPadding(new Insets(0, 0, 25, 0));

        Hyperlink tetrisLink = new Hyperlink("Tetris.com");
        tetrisLink.setOnAction(actionEvent -> Tetris.getHostService().showDocument("https://tetris.com/"));
        TextFlow firstNotice = new TextFlow(new Label("- Tetris® | "), tetrisLink);

        Hyperlink tetrisLogoLink = new Hyperlink("https://seeklogo.com/vector-logo/387138/tetris");
        tetrisLogoLink.setOnAction(actionEvent ->
                Tetris.getHostService().showDocument("https://seeklogo.com/vector-logo/387138/tetris"));
        TextFlow secondNotice = new TextFlow(new Label("- Tetris Game Title Logo, "), tetrisLogoLink);

        Hyperlink tetrisBGLink = new Hyperlink("https://tetris.com/article/35/tetris-lingo-every-player-should-know");
        tetrisBGLink.setOnAction(actionEvent ->
                Tetris.getHostService().showDocument("https://tetris.com/article/35/tetris-lingo-every-player-should-know"));
        TextFlow thirdNotice = new TextFlow(new Label("- Tetris image, "), tetrisBGLink);

        copyrightList.getChildren().addAll(firstNotice, secondNotice, thirdNotice);

        mainContent.getChildren().addAll(description, imageLabel, keyboardImage, copyrightLabel, copyrightList);

        content.getChildren().addAll(header, mainContent);
        howToPlayMenu.setContent(content);
    }

    /**
     * Displays the How To Play menu over the main game window.
     */
    private void showHowToPlayMenu() {
        isMainMenuLocked = true;
        this.getChildren().add(howToPlayMenu);
    }

    /**
     * Creates the menu with up-to-date data and shows it.
     */
    private void showStatisticsMenu() {
        isMainMenuLocked = true;
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        statisticsMenu = new VBox();
        statisticsMenu.setSpacing(25d);
        statisticsMenu.setMinSize(700, 300);
        statisticsMenu.setMaxSize(700, 300);
        statisticsMenu.getStyleClass().add("tab-style");
        statisticsMenu.setAlignment(Pos.CENTER);

        BorderPane header = new BorderPane();
        header.setPadding(new Insets(5, 5, 0, 0));

        Label title = new Label("Statistics");
        title.setStyle("-fx-font-weight: bold;");

        Button close = new Button("X");
        close.getStyleClass().add("close-button");
        close.setOnAction(actionEvent -> {
            getChildren().remove(statisticsMenu);
            isMainMenuLocked = false;
        });

        header.setCenter(title);
        header.setTop(close);
        BorderPane.setAlignment(close, Pos.TOP_RIGHT);

        statisticsMenu.getChildren().add(header);

        TableView<PlayerData> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<PlayerData, String> playerNameColumn = new TableColumn<>("Player Name");
        playerNameColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));

        TableColumn<PlayerData, String> linesColumn = new TableColumn<>("Cleared Lines");
        linesColumn.setCellValueFactory(playerData ->
            new SimpleStringProperty(numberFormat.format(playerData.getValue().getLines()))
        );

        TableColumn<PlayerData, String> totalScoreColumn = new TableColumn<>("Total Score");
        totalScoreColumn.setCellValueFactory(playerData ->
                new SimpleStringProperty(numberFormat.format(playerData.getValue().getTotalScore()))
        );

        TableColumn<PlayerData, String> highestScoreColumn = new TableColumn<>("Highest Score");
        highestScoreColumn.setCellValueFactory(playerData ->
                new SimpleStringProperty(numberFormat.format(playerData.getValue().getHighestScore()))
        );

        TableColumn<PlayerData, String> gamesPlayedColumn = new TableColumn<>("Games Played");
        gamesPlayedColumn.setCellValueFactory(playerData ->
                new SimpleStringProperty(numberFormat.format(playerData.getValue().getGamesPlayed()))
        );

        TableColumn<PlayerData, String> playTimeColumn = new TableColumn<>("Playtime");
        playTimeColumn.setCellValueFactory(playerData ->
                new SimpleStringProperty(Utils.getFormattedMilliseconds(playerData.getValue().getPlayTime()))
        );

        table.getColumns().add(playerNameColumn);
        table.getColumns().add(linesColumn);
        table.getColumns().add(totalScoreColumn);
        table.getColumns().add(highestScoreColumn);
        table.getColumns().add(gamesPlayedColumn);
        table.getColumns().add(playTimeColumn);

        table.getItems().addAll(Data.getInstance().getAllPlayersData());

        statisticsMenu.getChildren().add(table);

        this.getChildren().add(statisticsMenu);
    }

    public Scene getMainWindowScene() {
        return SCENE;
    }

    public static MainWindow getInstance() {
        return MAIN_MENU;
    }

}
