package taller.viewcontroller;

import taller.app.MainApp;
import taller.model.Mecanico;
import taller.model.TallerBicicletas;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controlador de la vista de gestión de mecánicos.
 *
 * Permite registrar mecánicos con nombre, teléfono, especialidad y código único. Permite visualizar el equipo actual en una tabla.
 *
 * @author Simon Valencia
 * @version 1.0
 */
public class MecanicoController {

    private final TallerBicicletas taller = MainApp.getTaller();

    @FXML private TextField txtNombre;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtEspecialidad;
    @FXML private TextField txtCodigo;
    @FXML private Label     lblMensaje;

    @FXML private TableView<Mecanico>           tablaMecanicos;
    @FXML private TableColumn<Mecanico, String> colNombre;
    @FXML private TableColumn<Mecanico, String> colTelefono;
    @FXML private TableColumn<Mecanico, String> colEspecialidad;
    @FXML private TableColumn<Mecanico, String> colCodigo;

    //Configura las columnas y muestra los mecanicos registrados
    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colEspecialidad.setCellValueFactory(new PropertyValueFactory<>("especialidad"));
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        refrescarTabla();
    }

    //Registra un nuevo mecanico
    @FXML
    private void registrar() {
        String nombre       = txtNombre.getText().trim();
        String telefono     = txtTelefono.getText().trim();
        String especialidad = txtEspecialidad.getText().trim();
        String codigo       = txtCodigo.getText().trim();

        if (nombre.isEmpty() || telefono.isEmpty() || especialidad.isEmpty() || codigo.isEmpty()) {
            mostrarError("Todos los campos son obligatorios."); return;
        }
        try {
            taller.registrarMecanico(nombre, telefono, especialidad, codigo);
            mostrarExito("Mecánico registrado correctamente.");
            limpiarFormulario(); refrescarTabla();
        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        }
    }

    //Limpia los campos y borra el mensaje
    @FXML private void limpiar() { limpiarFormulario(); lblMensaje.setText(""); }

    //Actualiza la tabla con la lista actual
    private void refrescarTabla() {
        tablaMecanicos.setItems(FXCollections.observableArrayList(taller.getMecanicos()));
    }

    //Borra el contenido del formulario
    private void limpiarFormulario() {
        txtNombre.clear(); txtTelefono.clear(); txtEspecialidad.clear(); txtCodigo.clear();
    }
    
    //Muestra un mensaje de exito en la pantalla
    private void mostrarExito(String msg) {
        lblMensaje.setStyle("-fx-text-fill: #27ae60;"); lblMensaje.setText(msg);
    }

    //Muestra un mensaje de error en la pantalla
    private void mostrarError(String msg) {
        lblMensaje.setStyle("-fx-text-fill: #e74c3c;"); lblMensaje.setText(msg);
    }
}
