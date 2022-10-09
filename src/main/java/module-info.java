module com.example.spotifyclonejavafxapi {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires se.michaelthelin.spotify;
    requires org.apache.httpcomponents.core5.httpcore5;

    opens com.example.spotifyclonejavafxapi to javafx.fxml;
    exports com.example.spotifyclonejavafxapi;
}