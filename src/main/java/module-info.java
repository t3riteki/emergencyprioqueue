module datastruct.le {
    requires javafx.controls;
    requires javafx.fxml;

    opens datastruct.le to javafx.fxml;
    exports datastruct.le;
}
