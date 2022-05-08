package tetris;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tetris.datahandler.Data;
import tetris.windows.MainWindow;

public class Tetris extends Application {

    private static Stage stage;

    private static Gson gson;

    // One board class.
    // Moveable interface have old points
    // A Shape abstract class
    // A falling shape class
    // An interface for different shape types
    // a class for all shapes

    // FIX Magic Piece making a mess IS IT FIXED NOW?? NEED TESTING A LOT

    // Settings menu
    // two players and two falling pieces at the same time
    //
    //
    // KINDA DONE IN TERMS OF STRUCTURE AND IMPLEMENTATION
    // points to collect while falling
    // any kind of features to give while falling down and collect them
    // a feature of collecting a square while falling down to fill the lowest empty spaces in the grid
    //


    @Override
    public void start(Stage primaryStage) throws Exception {
        Data.getInstance();
        stage = primaryStage;

        switchScene(MainWindow.getInstance().getMainWindowScene());
        primaryStage.addEventHandler(WindowEvent.WINDOW_SHOWN, event -> {
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            primaryStage.setX((screenBounds.getWidth() - primaryStage.getWidth()) / 2);
            primaryStage.setY((screenBounds.getHeight() - primaryStage.getHeight()) / 2);
        });
        primaryStage.sceneProperty().addListener((observable, oldScene, newScene) -> {
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            primaryStage.setX((screenBounds.getWidth() - primaryStage.getWidth()) / 2);
            primaryStage.setY((screenBounds.getHeight() - primaryStage.getHeight()) / 2);
        });
        primaryStage.setTitle("Tetris");
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        Data.getInstance().save();
    }

    public static void main(String[] args) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        gson = gsonBuilder.create();

        launch();
    }

    public static void switchScene(Scene scene) {
        stage.setScene(scene);
    }

    public static Gson getGson() {
        return gson;
    }
}
