package tetris.utilities;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.List;

public class Utils {

    public static int getHighestPoint(List<Point> points) {
        int highestPoint = Properties.getHeight()-1;
        for (Point point : points) {
            highestPoint = Math.min(point.getY(), highestPoint);
        }

        return highestPoint;
    }

    public static int getLowestPoint(List<Point> points) {
        int lowestPoint = 0;

        for (Point point : points) {
            lowestPoint = Math.max(point.getY(), lowestPoint);
        }

        return lowestPoint;
    }

    public static void addDuplicatedPoints(List<Point> list, List<Point> listToAdd) {
        for (Point point : listToAdd) {
            list.add(point.duplicate());
        }
    }

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

    public static Group getShadedCell(double squarePixel) {
        Group cell = new Group();

        Polygon topShade = new Polygon();
        topShade.setOpacity(0.5);
        topShade.setFill(Color.WHITE);
        topShade.getPoints().addAll(
                0d, 0d,
                squarePixel, 0d,
                squarePixel - 2.5d, 2.5d,
                2.5d, 2.5d,
                2.5d, squarePixel - 2.5d,
                0d, squarePixel
        );

        Polygon bottomShade = new Polygon();
        bottomShade.setOpacity(0.5);
        bottomShade.setFill(Color.BLACK);
        bottomShade.getPoints().addAll(
                squarePixel, squarePixel,
                squarePixel, 0d,
                squarePixel - 2.5d, 2.5d,
                squarePixel-2.5d, squarePixel-2.5d,
                2.5d, squarePixel -2.5d,
                0d, squarePixel
        );

        cell.getChildren().addAll(topShade, bottomShade);
        return cell;
    }

}
