package tetris.utilities.colorscheme;

public class Ocean extends Scheme {

    private static Ocean instance = new Ocean();

    private Ocean() {
    }

    @Override
    public String getLightBlue() {
        return "#78d5ff";
    }

    @Override
    public String getDarkBlue() {
        return "#1010b0";
    }

    @Override
    public String getOrange() {
        return "#eda013";
    }

    @Override
    public String getYellow() {
        return "#ffeb29";
    }

    @Override
    public String getGreen() {
        return "#1af01a";
    }

    @Override
    public String getRed() {
        return "#ed1a1a";
    }

    @Override
    public String getMagenta() {
        return "de0bde";
    }

    public static Ocean getInstance() {
        return instance;
    }
}
