module Tetris {
    requires javafx.controls;
    requires java.sql;
    requires gson;

    exports tetris.datahandler;

    opens tetris;
    opens tetris.datahandler;
}