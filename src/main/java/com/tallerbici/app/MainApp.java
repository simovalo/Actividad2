package com.tallerbici.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

/**
 * principla de fx.
 * entrada al sistema, carga el fxml principal y muestra la ventana.
 *
 *
 * @author simon valencia - Programación II
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // carga del fxml principal
        URL fxmlUrl = getClass().getResource("/com/tallerbici/viewController/main.fxml");
        if (fxmlUrl == null) {
            throw new RuntimeException("No se encontró el archivo main." +
                    "Verifica que esté en src/main/resources/com/tallerbici/viewController/");
        }

        Parent root = FXMLLoader.load(fxmlUrl);
        Scene scene = new Scene(root, 1000, 700);

        primaryStage.setTitle("tallerBici - sistema para gestión de mantenimiento de bicicletas");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    /**
     * metodo que usamos de fallback
     */
    public static void main(String[] args) {
        launch(args);
    }
}