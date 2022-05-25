package tetris.utilities.colorscheme;

public class Forest extends Scheme {

    private static final Forest instance = new Forest();

    private Forest() {
    }

    @Override
    public String getLightBlue() {
        return "#87f950";
    }

    @Override
    public String getDarkBlue() {
        return "#33a320";
    }

    @Override
    public String getOrange() {
        return "#d4b851";
    }

    @Override
    public String getYellow() {
        return "#f6f03f";
    }

    @Override
    public String getGreen() {
        return "#2b6924";
    }

    @Override
    public String getRed() {
        return "#833f0a";
    }

    @Override
    public String getMagenta() {
        return "#bd8f21";
    }

    public static Forest getInstance() {
        return instance;
    }
}
