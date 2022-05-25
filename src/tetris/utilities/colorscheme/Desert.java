package tetris.utilities.colorscheme;

public class Desert extends Scheme {

    private static final Desert instance = new Desert();

    private Desert() {
    }

    @Override
    public String getLightBlue() {
        return "#fdd68b";
    }

    @Override
    public String getDarkBlue() {
        return "#d27208";
    }

    @Override
    public String getOrange() {
        return "#ff8001";
    }

    @Override
    public String getYellow() {
        return "#f9f104";
    }

    @Override
    public String getGreen() {
        return "#fbcb56";
    }

    @Override
    public String getRed() {
        return "#980505";
    }

    @Override
    public String getMagenta() {
        return "#e51a06";
    }

    public static Desert getInstance() {
        return instance;
    }
}
