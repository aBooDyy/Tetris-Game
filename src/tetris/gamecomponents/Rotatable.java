package tetris.gamecomponents;

public interface Rotatable {

    /**
     * An abstract method in the interface Rotatable which is implemented in the Board class to rotate
     * pieces 90 degrees about their origin point either clockwise or counter-clockwise according to the given
     * parameter.
     *
     * @param isClockwise if true, the rotation will be clockwise, if false, the rotation will be counterclockwise
     */
    void rotate(boolean isClockwise);

}
