package tetris.utilities;

import tetris.utilities.colorscheme.Scheme;

public class Properties {

    public final static int PIXEL = 30;
    private static int width = 10; // largest 30 smallest 10
    private static int height = 20; // largest 22 smallest 10

    private static Scheme colorScheme = Scheme.getInstance();

    public static Scheme getColorScheme() {
        return colorScheme;
    }

    public static String[] getColorSchemeList() {
        return new String[]{"Classic", "Coral", "Desert", "Forest", "Neon", "Ocean", "Pastel", "Sunset"};
    }

    public static void setColorScheme(String colorScheme) {
        Properties.colorScheme = Scheme.getScheme(colorScheme);
    }

    public static int getWidth() {
        return width;
    }

    public static void setWidth(int width) {
        Properties.width = width;
    }

    public  static int getHeight() {
        return height;
    }

    public static void setHeight(int height) {
        Properties.height = height;
    }

}
