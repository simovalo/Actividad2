package com.tallerbici.viewController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador principal de la ventana.
 * Principio S (SRP): solo muestra el estado general de la aplicación.
 *
 * @author Equipo TallerBici - Programación II
 */
public class MainController implements Initializable {

    @FXML private TabPane tabPane;
    @FXML private Label   lblEstado;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblEstado.setText("✅ Sistema listo | TallerBici v1.0");
    }
}