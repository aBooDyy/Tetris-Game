package tetris.utilities.colorscheme;

public class Ocean extends Scheme {

    private static final Ocean instance = new Ocean();

    private Ocean() {
    }

    @Override
    public String getLightBlue() {
        return "#8ad4f6";
    }

    @Override
    public String getDarkBlue() {
        return "#06568d";
    }

    @Override
    public String getOrange() {
        return "#95eec5";
    }

    @Override
    public String getYellow() {
        return "#eedb95";
    }

    @Override
    public String getGreen() {
        return "#2596be";
    }

    @Override
    public String getRed() {
        return "#fbecec";
    }

    @Override
    public String getMagenta() {
        return "#12d4a4";
    }

    public static Ocean getInstance() {
        return instance;
    }
}
