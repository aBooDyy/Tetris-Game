package tetris.utilities.colorscheme;

public class Neon extends Scheme {

    private static final Neon instance = new Neon();

    private Neon() {
    }

    @Override
    public String getLightBlue() {
        return "#15f4ee";
    }

    @Override
    public String getDarkBlue() {
        return "#4d4dff";
    }

    @Override
    public String getOrange() {
        return "#ff9f03";
    }

    @Override
    public String getYellow() {
        return "#ffff00";
    }

    @Override
    public String getGreen() {
        return "#39ff14";
    }

    @Override
    public String getRed() {
        return "#ff1b98";
    }

    @Override
    public String getMagenta() {
        return "#b026ff";
    }

    public static Neon getInstance() {
        return instance;
    }
}
