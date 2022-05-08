package tetris.utilities;

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

    public void setX(int x) {
        this.x = x;
    }

    public void addX(int x) {
        this.x = this.x + x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void addY(int y) {
        this.y = this.y + y;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

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
