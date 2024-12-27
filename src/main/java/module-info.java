module datastruct.le {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;

    opens datastruct.le to javafx.fxml;
    exports datastruct.le;
}
