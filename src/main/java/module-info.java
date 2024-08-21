module kotlin.scrcpytools {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires org.apache.commons.io;
    requires org.controlsfx.controls;
    requires java.desktop;

    opens br.com.saggion.scrcpytools to javafx.fxml;
    opens br.com.saggion.scrcpytools.controller to javafx.fxml;
    exports br.com.saggion.scrcpytools;
}