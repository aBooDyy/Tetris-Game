package tetris.utilities.colorscheme;

public class Sunset extends Scheme {

    private static final Sunset instance = new Sunset();

    private Sunset() {
    }

    @Override
    public String getLightBlue() {
        return "#765285";
    }

    @Override
    public String getDarkBlue() {
        return "#351c4d";
    }

    @Override
    public String getOrange() {
        return "#ff7e5f";
    }

    @Override
    public String getYellow() {
        return "#ffa542";
    }

    @Override
    public String getGreen() {
        return "#feb47b";
    }

    @Override
    public String getRed() {
        return "#f34740";
    }

    @Override
    public String getMagenta() {
        return "#f5ab99";
    }

    public static Sunset getInstance() {
        return instance;
    }
}
