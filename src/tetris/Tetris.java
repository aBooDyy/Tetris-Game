package tetris;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import tetris.datahandler.Data;
import tetris.windows.MainWindow;

public class Tetris extends Application {

    private static Stage stage;

    private static Gson gson;

    private static HostServices hostServices;

    /**
     * Overridden method from JavaFX library to start a JavaFX application.
     *
     * @param primaryStage the stage of our JavaFX application
     */
    @Override
    public void start(Stage primaryStage) {
        // Creates Data singleton class instance and reads the data from the data file
        Data.getInstance();
        stage = primaryStage;
        hostServices = getHostServices();

        // Sets the MainWindow scene to our JavaFX window stage
        switchScene(MainWindow.getInstance().getMainWindowScene());
        // Position the MainWindow in the middle of the screen when the window is shown
        primaryStage.sceneProperty().addListener((observable, oldScene, newScene) -> {
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            primaryStage.setX((screenBounds.getWidth() - primaryStage.getWidth()) / 2);
            primaryStage.setY((screenBounds.getHeight() - primaryStage.getHeight()) / 2);
        });
        // Sets the window title
        primaryStage.setTitle("Tetris");
        primaryStage.show();
        primaryStage.getIcons().add(new Image(Tetris.class.getResource("images/logo.png").toExternalForm()));
    }

    /**
     * Overridden method from JavaFX library to terminate a JavaFX application.
     */
    @Override
    public void stop() {
        // Saves the collected data to the data file
        Data.getInstance().save();
    }

    /**
     * the entry point method of a Java application launching the JavaFX application and setting up the
     * gson library used to handle the storage of data.
     *
     * @param args given arguments.
     */
    public static void main(String[] args) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        gson = gsonBuilder.create();

        launch();
    }

    /**
     * Alters the content of the stage to set the scene for different menus, eg: from the main
     * menu to the play window.
     *
     * @param scene the scene we want to alters to
     */
    public static void switchScene(Scene scene) {
        stage.setScene(scene);
    }

    public static Gson getGson() {
        return gson;
    }

    public static HostServices getHostService() {
        return hostServices;
    }
}
