module fr.com.gestionnairefilms {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires json.simple;
    requires org.json;

    opens fr.com.gestionnairefilms to javafx.fxml;
    exports fr.com.gestionnairefilms;
}