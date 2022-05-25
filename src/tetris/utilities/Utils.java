package tetris.utilities;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import tetris.gamecomponents.Point;

import java.util.List;

public class Utils {

    /**
     * Gives the value of the point with the smallest y value from the given points list.
     *
     * @param points the list of points to get the highest point from
     * @return the Y value of the highest point from the given list
     */
    public static int getHighestPoint(List<Point> points) {
        // a variable of the highest point, by default it is the highest point in the board which is the first row
        int highestPoint = Properties.getHeight()-1;
        // loop over the points in the given list
        for (Point point : points) {
            // we check assign the highest point variable to the smallest number of the current point's y value,
            // or the previous value of highestPoint variable
            highestPoint = Math.min(point.getY(), highestPoint);
        }

        return highestPoint;
    }

    /**
     * Gives the value of the point with the largest y value from the given points list.
     *
     * @param points the list of points to get the lowest point from
     * @return the Y value of the lowest point from the given list
     */
    public static int getLowestPoint(List<Point> points) {
        // a variable of the lowest point, by default it is the lowest point in the board which is the last row
        int lowestPoint = 0;

        // loop over the points in the given list
        for (Point point : points) {
            // we check assign the lowest point variable to the greatest number of the current point's y value,
            // or the previous value of lowestPoint variable
            lowestPoint = Math.max(point.getY(), lowestPoint);
        }

        return lowestPoint;
    }

    /**
     * Combines two given lists of points into the former by duplicating each point
     * object in the latter.
     *
     * @param list the list of points to add to.
     * @param listToAdd the list of points to be added
     */
    public static void addDuplicatedPoints(List<Point> list, List<Point> listToAdd) {
        // loop over the list of points to be added
        for (Point point : listToAdd) {
            // add the duplicated point to the list we want to add to (first parameter)
            list.add(point.duplicate());
        }
    }

    /**
     * Converts the milliseconds to years, months, days, hours, minutes, and
     * seconds as needed.
     *
     * @param milliseconds the milliseconds value to format
     * @return the formatted string (X years, X months, X days, X hours, X minutes, X seconds)
     */
    public static String getFormattedMilliseconds(long milliseconds) {
        String result = "";

        int years = (int) (milliseconds / 31104000000L);
        int months = (int) ((milliseconds % 31104000000L) / 2592000000L);
        int days = (int) ((milliseconds % 2592000000L) / 86400000);
        int hours = (int) ((milliseconds % 86400000) / 3600000);
        int minutes = (int) ((milliseconds % 3600000) / 60000);
        int seconds = (int) ((milliseconds % 60000) / 1000);

        if (years != 0) {
            result += years + " year" + (years > 1 ? "s, " : ", ");
        }
        if (months != 0) {
            result += months + " month" + (months > 1 ? "s, " : ", ");
        }
        if (days != 0) {
            result += days + " day" + (days > 1 ? "s, " : ", ");
        }
        if (hours != 0) {
            result += hours + " hour" + (hours > 1 ? "s, " : ", ");
        }
        if (minutes != 0) {
            result += minutes + " minute" + (minutes > 1 ? "s, " : ", ");
        }
        if (seconds != 0) {
            result += seconds + " second" + (seconds > 1 ? "s" : "");
        }

        return result;
    }

    /**
     * Gives a JavaFX group element which will contain the shade components.
     *
     * @param squarePixel the size of the square in the game matrix.
     * @param isThick if true, it will give a thick shaded cell, if false, the shade will be thin
     * @return the shaded group component.
     */
    public static Group getShadedCell(double squarePixel, boolean isThick) {
        double size = isThick ? 5d : 2.5d;
        Group cell = new Group();

        Polygon topShade = new Polygon();
        topShade.setOpacity(0.5);
        topShade.setFill(Color.WHITE);
        topShade.getPoints().addAll(
                0d, 0d,
                squarePixel, 0d,
                squarePixel - size, size,
                size, size,
                size, squarePixel - size,
                0d, squarePixel
        );

        Polygon bottomShade = new Polygon();
        bottomShade.setOpacity(0.5);
        bottomShade.setFill(Color.BLACK);
        bottomShade.getPoints().addAll(
                squarePixel, squarePixel,
                squarePixel, 0d,
                squarePixel - size, size,
                squarePixel-size, squarePixel-size,
                size, squarePixel -size,
                0d, squarePixel
        );

        cell.getChildren().addAll(topShade, bottomShade);
        return cell;
    }

    /**
     * Gives a JavaFX group element which will contain the score point shape.
     *
     * @param squarePixel the size of the square in the game matrix.
     * @param score the score that the coin contains.
     * @return the group component containing the coin shapes.
     */
    public static Group getCoinShape(double squarePixel, int score) {
        Group cell = new Group();
        StackPane stackPane = new StackPane();

        Circle circle = new Circle(squarePixel/2);
        circle.setFill(Color.web("#CFB63C"));

        Circle circle1 = new Circle(squarePixel/2 - 2.5);
        circle1.setFill(Color.web("#FCD411"));

        Label scoreLabel = new Label(score + "");

        stackPane.getChildren().addAll(circle, circle1, scoreLabel);
        cell.getChildren().addAll(stackPane);
        return cell;
    }

}
