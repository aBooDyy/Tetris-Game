package tetris.gamecomponents;

import tetris.utilities.Properties;

public class Point {

    private int x;
    private int y;
    private String color;

    public Point(int x, int y) {
        this(x, y, Properties.getColorScheme().getGray());
    }

    public Point(int x, int y, String color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    /**
     * turns a point 90 degrees over the given origin point either clockwise or counterclockwise.
     *
     * @param originPoint the origin point to rotate over it
     * @param isClockwise determines if the rotation is clockwise or not
     */
    public void rotate(Point originPoint, boolean isClockwise) {
        x = x - originPoint.x;
        y = y - originPoint.y;

        int temp = x;
        if (isClockwise) {
            x = y * -1;
            y = temp;
        } else  {
            x = y;
            y = temp * -1;
        }

        setX(x + originPoint.x);
        setY(y + originPoint.y);
    }

    public int getX() {
        return x;
    }

    /**
     * assigns the given parameter to the point’s x value.
     *
     * @param x the new x value
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     *  increases the x coordinate of the point by the given parameter.
     *
     * @param x the amount to increase x value
     */
    public void addX(int x) {
        this.x = this.x + x;
    }

    public int getY() {
        return y;
    }

    /**
     * assigns the given parameter to the point’s y value
     *
     * @param y the new y value
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     *  increases the y coordinate of the point by the given parameter.
     *
     * @param y the amount to increase y value
     */
    public void addY(int y) {
        this.y = this.y + y;
    }

    public String getColor() {
        return color;
    }

    /**
     * assigns the given parameter color as the point’s color attribute.
     *
     * @param color the new color value
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Creates a new point with the same x and y coordinates.
     *
     * @return the new point
     */
    public Point duplicate() {
        return new Point(this.x, this.y);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Point)) {
            return false;
        }

        Point point = (Point) obj;
        return this.x == point.x && this.y == point.y;
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
}
