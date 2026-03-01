package com.tallerbici.viewController;

import com.tallerbici.model.Cliente;
import com.tallerbici.model.singleton.GestorTaller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * controlador de cliente.
 * Soporta registrar y editar clientes.
 *
 * MODO NORMAL:  botón → "✅ Registrar Cliente"
 * MODO EDICIÓN: botón → "💾 Guardar Cambios"
 *
 * @author Equipo TallerBici - Programación II
 */
public class ClienteController implements Initializable {

    @FXML private TextField txtCedula;
    @FXML private TextField txtNombre;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtDireccion;
    @FXML private Button    btnAccion;
    @FXML private Label     lblMensaje;
    @FXML private Label     lblModoEdicion;
    @FXML private ListView<Cliente> listClientes;
    @FXML private TextField txtBuscarCliente;
    @FXML private TextArea  txtDetalleCliente;

    private GestorTaller gestor = GestorTaller.getInstance();
    private boolean modoEdicion = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Conectar el ListView con la lista del GestorTaller
        // Cuando se agregue o edite un cliente, el ListView se actualiza solo
        listClientes.setItems(gestor.getListaClientes());
    }

    // =================== ACCIÓN PRINCIPAL ===================

    @FXML
    private void accionPrincipal() {
        if (modoEdicion) {
            actualizarCliente();
        } else {
            registrarCliente();
        }
    }

    private void registrarCliente() {
        try {
            Cliente nuevo = new Cliente.Builder()
                    .numeroCedula(txtCedula.getText().trim())
                    .nombreCompleto(txtNombre.getText().trim())
                    .telefono(txtTelefono.getText().trim())
                    .direccion(txtDireccion.getText().trim())
                    .build();

            gestor.registrarCliente(nuevo);
            mostrarExito("✅ Cliente registrado: " + nuevo.getNombreCompleto());
            limpiarFormulario();

        } catch (IllegalStateException | IllegalArgumentException e) {
            mostrarError("❌ " + e.getMessage());
        }
    }

    private void actualizarCliente() {
        try {
            Cliente actualizado = new Cliente.Builder()
                    .numeroCedula(txtCedula.getText().trim())
                    .nombreCompleto(txtNombre.getText().trim())
                    .telefono(txtTelefono.getText().trim())
                    .direccion(txtDireccion.getText().trim())
                    .build();

            gestor.actualizarCliente(actualizado);
            mostrarExito("💾 Cliente actualizado: " + actualizado.getNombreCompleto());
            limpiarFormulario();

        } catch (IllegalStateException | IllegalArgumentException e) {
            mostrarError("❌ " + e.getMessage());
        }
    }

    // =================== EDITAR SELECCIONADO ===================

    @FXML
    private void editarSeleccionado() {
        Cliente seleccionado = listClientes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarError("❌ Selecciona un cliente de la lista para editar.");
            return;
        }
        // Cargar datos en el formulario
        txtCedula.setText(seleccionado.getNumeroCedula());
        txtCedula.setDisable(true); // La cédula no se puede cambiar
        txtNombre.setText(seleccionado.getNombreCompleto());
        txtTelefono.setText(seleccionado.getTelefono());
        txtDireccion.setText(seleccionado.getDireccion());

        // Activar modo edición
        modoEdicion = true;
        btnAccion.setText("💾 Guardar Cambios");
        btnAccion.setStyle("-fx-background-color: #F57C00; -fx-text-fill: white; -fx-font-weight: bold;");
        lblModoEdicion.setText("✏ Editando: " + seleccionado.getNombreCompleto());
        lblModoEdicion.setStyle("-fx-text-fill: #E65100; -fx-font-weight: bold;");
    }

    // =================== BÚSQUEDA ===================

    @FXML
    private void filtrarClientes() {
        String criterio = txtBuscarCliente.getText().trim().toLowerCase();

        if (criterio.isEmpty()) {
            // Sin texto: mostrar todos
            listClientes.setItems(gestor.getListaClientes());
        } else {
            // Con texto: recorrer la lista y agregar los que coincidan
            ArrayList<Cliente> filtrados = new ArrayList<>();
            for (Cliente c : gestor.getListaClientes()) {
                if (c.coincideCon(criterio)) {
                    filtrados.add(c);
                }
            }
            listClientes.setItems(FXCollections.observableArrayList(filtrados));
        }
    }

    // =================== DETALLE ===================

    @FXML
    private void mostrarDetalleCliente(MouseEvent event) {
        Cliente seleccionado = listClientes.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            txtDetalleCliente.setText(seleccionado.getDetalles());
        }
    }

    // =================== LIMPIAR ===================

    @FXML
    public void limpiarFormulario() {
        txtCedula.clear();
        txtCedula.setDisable(false);
        txtNombre.clear();
        txtTelefono.clear();
        txtDireccion.clear();
        lblMensaje.setText("");
        lblModoEdicion.setText("");
        txtDetalleCliente.clear();
        txtBuscarCliente.clear();
        modoEdicion = false;
        btnAccion.setText("✅ Registrar Cliente");
        btnAccion.setStyle("-fx-background-color: #1A237E; -fx-text-fill: white; -fx-font-weight: bold;");
        listClientes.setItems(gestor.getListaClientes());
        listClientes.getSelectionModel().clearSelection();
    }

    private void mostrarExito(String msg) {
        lblMensaje.setStyle("-fx-text-fill: #388E3C; -fx-font-size: 12px;");
        lblMensaje.setText(msg);
    }

    private void mostrarError(String msg) {
        lblMensaje.setStyle("-fx-text-fill: #C62828; -fx-font-size: 12px;");
        lblMensaje.setText(msg);
    }
}