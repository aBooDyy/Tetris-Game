package tetris.windows;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import tetris.Tetris;
import tetris.datahandler.Data;
import tetris.datahandler.PlayerData;
import tetris.utilities.Properties;
import tetris.utilities.Utils;

import java.text.NumberFormat;
import java.util.Arrays;

public class MainWindow extends StackPane {

    private final static MainWindow mainMenu = new MainWindow();

    private final Scene scene;

    private VBox statisticsMenu;

    private VBox settingsMenu;

    private TextField playerNameField;
    private TextField player2NameField;

    private int level;

    private boolean isMainMenuLocked;

    private MainWindow() {
        scene = new Scene(this, 900, 461);
        //TODO handwritten path
        scene.getStylesheets().add("style.css");
        level = 1;
        createWindow();
        createSettingsMenu();
    }

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

        player2NameField = new TextField();
        player2NameField.setPromptText("Player 2 Name (Optional)");
        player2NameField.getStyleClass().add("player-name-field");

        playerNamesBox.getChildren().addAll(playerNameField);

        Button playButton = new Button("PLAY");
        playButton.getStyleClass().add("play-button");
        playButton.setOnAction(actionEvent -> {
            if (playerNameField.getText() == null || playerNameField.getText().trim().equals("") || isMainMenuLocked) {
                return;
            }

            if (player2NameField.getText() != null && !player2NameField.getText().trim().equals("")) {
                PlayWindow playWindow = new PlayWindow(playerNameField.getText(), player2NameField.getText(), level);
                Tetris.switchScene(playWindow.getPlayWindowScene());
                return;
            }

            PlayWindow playWindow = new PlayWindow(playerNameField.getText(), level);
            Tetris.switchScene(playWindow.getPlayWindowScene());
        });

        HBox hButtons = new HBox();
        hButtons.setSpacing(20d);
        hButtons.setAlignment(Pos.CENTER);

        Button levelButton = new Button("Level: " + level);
        levelButton.setOnAction(actionEvent -> {
            level = level == 1 ? 5 : level == 25 ? 1 : level + 5;
            levelButton.setText("Level: " + level);
        });


        Button statistics = new Button("Statistics");
        statistics.setOnAction(actionEvent -> {
            if (isMainMenuLocked) {
                return;
            }

            showStatisticsMenu();
        });

        Button settings = new Button("Settings");
        settings.setOnAction(actionEvent -> {
            if (isMainMenuLocked) {
                return;
            }

            showSettingsMenu();
        });

        hButtons.getChildren().addAll(levelButton, statistics, settings);

        VBox vBox = new VBox();
        vBox.setSpacing(25d);
//        vBox.setMinSize(100, 200);
//        vBox.setMaxSize(100, 200);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(logo, playerNamesBox, playButton, hButtons);

        this.getChildren().addAll(vBox);
    }

    private void createSettingsMenu() {
        settingsMenu = new VBox();
        settingsMenu.setSpacing(25d);
        settingsMenu.setMinSize(200, 400);
        settingsMenu.setMaxSize(200, 400);
        settingsMenu.setAlignment(Pos.CENTER);
        settingsMenu.getStyleClass().add("pause-button");

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER);

        Label settings = new Label("Settings");

        Button close = new Button("Close");
        close.setOnAction(actionEvent -> {
            getChildren().remove(settingsMenu);
            isMainMenuLocked = false;
        });

        header.getChildren().addAll(settings, close);

        Label width = new Label("Width");
        Spinner<Integer> widthSelector = new Spinner<>(10,30, Properties.getWidth());
        widthSelector.valueProperty().addListener((obs, oldValue, newValue) -> {
            Properties.setWidth(newValue);
        });

        Label height = new Label("Height");
        Spinner<Integer> heightSelector = new Spinner<>(10,22, Properties.getHeight());
        heightSelector.valueProperty().addListener((obs, oldValue, newValue) -> {
            Properties.setHeight(newValue);
        });
        String[] schemes = {"Classic", "Ocean"};

        ComboBox<String> scheme = new ComboBox<>(FXCollections.observableList(Arrays.asList(schemes)));
        scheme.getSelectionModel().selectFirst();
        scheme.setOnAction(actionEvent -> {
            Properties.setColorScheme(scheme.getValue());
        });

        settingsMenu.getChildren().addAll(header, width, widthSelector, height, heightSelector, scheme);
    }

    private void showSettingsMenu() {
        isMainMenuLocked = true;
        this.getChildren().add(settingsMenu);
    }

    private void showStatisticsMenu() {
        isMainMenuLocked = true;
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        statisticsMenu = new VBox();
        statisticsMenu.setSpacing(25d);
        statisticsMenu.setMinSize(700, 300);
        statisticsMenu.setMaxSize(700, 300);
        // TODO: Style this
        statisticsMenu.getStyleClass().add("pause-button");
        statisticsMenu.setAlignment(Pos.CENTER);

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER);

        Label title = new Label("Statistics");

        Button close = new Button("Close");
        close.setOnAction(actionEvent -> {
            getChildren().remove(statisticsMenu);
            isMainMenuLocked = false;
        });

        header.getChildren().addAll(title, close);

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
        highestScoreColumn.setCellValueFactory(playerData ->
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
        return scene;
    }

    public static MainWindow getInstance() {
        return mainMenu;
    }

}
