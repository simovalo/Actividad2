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
 * <p>Gestiona la barra lateral de navegación y el área central de contenido.
 * Muestra el nombre, dirección y teléfono del taller en el encabezado del
 * sidebar y carga dinámicamente las vistas al hacer clic en cada opción.</p>
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

    /**
     * Muestra los datos del taller en el sidebar y carga la vista inicial.
     * Llamado automáticamente por el FXMLLoader.
     */
    @FXML
    public void initialize() {
        lblNombreTaller.setText(taller.getNombre());
        lblDireccionTaller.setText(taller.getDireccion());
        lblTelefonoTaller.setText("📞 " + taller.getTelefono());
        cargarVista("clientes.fxml");
    }

    // ── Navegación ─────────────────────────────────────────────────────────────

    /** Carga la vista de gestión de clientes. */
    @FXML private void showClientes()  { cargarVista("clientes.fxml");  }

    /** Carga la vista de gestión de bicicletas. */
    @FXML private void showBicicletas(){ cargarVista("bicicletas.fxml"); }

    /** Carga la vista de gestión de mecánicos. */
    @FXML private void showMecanicos() { cargarVista("mecanicos.fxml"); }

    /** Carga la vista de órdenes de servicio. */
    @FXML private void showOrdenes()   { cargarVista("ordenes.fxml");   }

    /** Carga la vista de consultas y reportes. */
    @FXML private void showConsultas() { cargarVista("consultas.fxml"); }

    // ── Utilidad ───────────────────────────────────────────────────────────────

    /**
     * Carga un FXML en el área central de contenido.
     *
     * @param fxml Nombre del archivo FXML en el directorio de recursos.
     */
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
