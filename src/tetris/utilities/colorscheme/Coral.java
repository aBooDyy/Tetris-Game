package tetris.utilities.colorscheme;

public class Coral extends Scheme {

    private static final Coral instance = new Coral();

    private Coral() {
    }

    @Override
    public String getLightBlue() {
        return "#92dcf2";
    }

    @Override
    public String getDarkBlue() {
        return "#d4128a";
    }

    @Override
    public String getOrange() {
        return "#be25ac";
    }

    @Override
    public String getYellow() {
        return "#fb88f3";
    }

    @Override
    public String getGreen() {
        return "#e3fbfd";
    }

    @Override
    public String getRed() {
        return "#be4ef4";
    }

    @Override
    public String getMagenta() {
        return "#8810ce";
    }

    public static Coral getInstance() {
        return instance;
    }
}
