package taller.viewcontroller;

import javafx.scene.Node;
import taller.app.MainApp;
import taller.model.TallerBicicletas;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;

/**
 * Controlador de la ventana principal.
 *
 * Gestiona la barra lateral de navegación y el área central de contenido. Muestra el nombre, dirección y teléfono del taller en el encabezado del sidebar y carga dinámicamente las vistas al hacer clic en cada opción.</p>
 *
 * @author Samuel Marin
 * @version 1.0
 */
public class MainController {

    private final TallerBicicletas taller = MainApp.getTaller();

    @FXML private StackPane contentArea;
    @FXML private Label     lblNombreTaller;
    @FXML private Label     lblDireccionTaller;
    @FXML private Label     lblTelefonoTaller;

    //Muestra los datos del taller y carga la primera vista
    @FXML
    public void initialize() {
        lblNombreTaller.setText(taller.getNombre());
        lblDireccionTaller.setText(taller.getDireccion());
        lblTelefonoTaller.setText("Telefono: " + taller.getTelefono());
        cargarVista("clientes.fxml");
    }

    //Muestra la vista de los clientes
    @FXML private void showClientes()  { cargarVista("clientes.fxml");  }

    //Muestra la vista de las bicicletas
    @FXML private void showBicicletas(){ cargarVista("bicicletas.fxml"); }

    //Muestra la vista de mecanicos
    @FXML private void showMecanicos() { cargarVista("mecanicos.fxml"); }

    //Muestra la vista de las ordenes
    @FXML private void showOrdenes()   { cargarVista("ordenes.fxml");   }

    //Muestra la vista de las consultas y reportes
    @FXML private void showConsultas() { cargarVista("consultas.fxml"); }

    //Carga una vista FXML en el area central
    private void cargarVista(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/" + fxml));
            contentArea.getChildren().setAll((Node) loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
