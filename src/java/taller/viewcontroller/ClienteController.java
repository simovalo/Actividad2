package taller.viewcontroller;

import taller.app.MainApp;
import taller.model.Cliente;
import taller.model.TallerBicicletas;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controlador de la vista de gestión de clientes.
 *
 * Permite registrar nuevos clientes mediante un formulario con validación de campos y visualizar la lista en una tabla.
 *
 * @author Samuel Marin
 * @version 1.0
 */
public class ClienteController {

    private final TallerBicicletas taller = MainApp.getTaller();

    @FXML private TextField txtNombre;
    @FXML private TextField txtId;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtDireccion;
    @FXML private Label     lblMensaje;

    @FXML private TableView<Cliente>           tablaClientes;
    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TableColumn<Cliente, String> colId;
    @FXML private TableColumn<Cliente, String> colTelefono;
    @FXML private TableColumn<Cliente, String> colDireccion;

    //Configura las columnas de la tabla y muestra los clientes guardados
    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colId.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        refrescarTabla();
    }

    //Registra un nuevo cliente
    @FXML
    private void registrar() {
        String nombre    = txtNombre.getText().trim();
        String id        = txtId.getText().trim();
        String telefono  = txtTelefono.getText().trim();
        String direccion = txtDireccion.getText().trim();

        if (nombre.isEmpty() || id.isEmpty() || telefono.isEmpty() || direccion.isEmpty()) {
            mostrarError("Todos los campos son obligatorios."); return;
        }
        try {
            taller.registrarCliente(nombre, id, telefono, direccion);
            mostrarExito("Cliente registrado correctamente.");
            limpiarFormulario();
            refrescarTabla();
        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        }
    }

    //Limpia el formulario y el mensaje
    @FXML
    private void limpiar() { limpiarFormulario(); lblMensaje.setText(""); }

    //Actualiza la tabla con la lista actual de clientes
    private void refrescarTabla() {
        tablaClientes.setItems(FXCollections.observableArrayList(taller.getClientes()));
    }

    //Borra el contenido de los campos de texto
    private void limpiarFormulario() {
        txtNombre.clear(); txtId.clear(); txtTelefono.clear(); txtDireccion.clear();
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
