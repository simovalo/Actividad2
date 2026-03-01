package com.tallerbici.viewController;

import com.tallerbici.model.Mecanico;
import com.tallerbici.model.singleton.GestorTaller;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador del módulo de Mecánicos.
 * Soporta registro y edición.
 *
 * @author Equipo TallerBici - Programación II
 */
public class MecanicoController implements Initializable {

    @FXML private TextField          txtCodigo;
    @FXML private TextField          txtNombre;
    @FXML private ComboBox<String>   cmbEspecialidad;
    @FXML private TextField          txtCertificacion;
    @FXML private Button             btnAccion;
    @FXML private Label              lblMensaje;
    @FXML private Label              lblModoEdicion;
    @FXML private ListView<Mecanico> listMecanicos;
    @FXML private TextArea           txtDetalleMecanico;

    private GestorTaller gestor = GestorTaller.getInstance();
    private boolean modoEdicion = false;

    private static final String[] ESPECIALIDADES = {
            "Frenos y Transmisión", "Suspensión", "Bicicletas Eléctricas",
            "Cuadros y Estructura", "Rodado y Llantas", "General"
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cmbEspecialidad.setItems(FXCollections.observableArrayList(ESPECIALIDADES));
        listMecanicos.setItems(gestor.getListaMecanicos());
    }

    @FXML
    private void accionPrincipal() {
        if (modoEdicion) {
            actualizarMecanico();
        } else {
            registrarMecanico();
        }
    }

    private void registrarMecanico() {
        try {
            String especialidad = obtenerEspecialidad();
            Mecanico nuevo = new Mecanico.Builder()
                    .codigoInterno(txtCodigo.getText().trim())
                    .nombreCompleto(txtNombre.getText().trim())
                    .especialidad(especialidad)
                    .numeroCertificacion(txtCertificacion.getText().trim())
                    .build();
            gestor.registrarMecanico(nuevo);
            mostrarExito("✅ Mecánico registrado: " + nuevo.getNombreCompleto());
            limpiarFormulario();
        } catch (IllegalStateException | IllegalArgumentException e) {
            mostrarError("❌ " + e.getMessage());
        }
    }

    private void actualizarMecanico() {
        try {
            String especialidad = obtenerEspecialidad();
            Mecanico actualizado = new Mecanico.Builder()
                    .codigoInterno(txtCodigo.getText().trim())
                    .nombreCompleto(txtNombre.getText().trim())
                    .especialidad(especialidad)
                    .numeroCertificacion(txtCertificacion.getText().trim())
                    .build();
            gestor.actualizarMecanico(actualizado);
            mostrarExito("💾 Mecánico actualizado: " + actualizado.getNombreCompleto());
            limpiarFormulario();
        } catch (IllegalStateException | IllegalArgumentException e) {
            mostrarError("❌ " + e.getMessage());
        }
    }

    @FXML
    private void editarSeleccionado() {
        Mecanico sel = listMecanicos.getSelectionModel().getSelectedItem();
        if (sel == null) { mostrarError("❌ Selecciona un mecánico para editar."); return; }

        txtCodigo.setText(sel.getCodigoInterno());
        txtCodigo.setDisable(true); // El código no se puede cambiar
        txtNombre.setText(sel.getNombreCompleto());
        cmbEspecialidad.getEditor().setText(sel.getEspecialidad());
        txtCertificacion.setText(sel.getNumeroCertificacion());

        modoEdicion = true;
        btnAccion.setText("💾 Guardar Cambios");
        btnAccion.setStyle("-fx-background-color: #F57C00; -fx-text-fill: white; -fx-font-weight: bold;");
        lblModoEdicion.setText("✏ Editando: " + sel.getNombreCompleto());
        lblModoEdicion.setStyle("-fx-text-fill: #E65100; -fx-font-weight: bold;");
    }

    @FXML
    private void mostrarDetalleMecanico(MouseEvent event) {
        Mecanico sel = listMecanicos.getSelectionModel().getSelectedItem();
        if (sel != null) {
            txtDetalleMecanico.setText(sel.getDetalles());
        }
    }

    @FXML
    public void limpiarFormulario() {
        txtCodigo.clear();
        txtCodigo.setDisable(false);
        txtNombre.clear();
        cmbEspecialidad.getSelectionModel().clearSelection();
        cmbEspecialidad.getEditor().clear();
        txtCertificacion.clear();
        lblMensaje.setText("");
        lblModoEdicion.setText("");
        txtDetalleMecanico.clear();
        modoEdicion = false;
        btnAccion.setText("✅ Registrar Mecánico");
        btnAccion.setStyle("-fx-background-color: #1A237E; -fx-text-fill: white; -fx-font-weight: bold;");
        listMecanicos.getSelectionModel().clearSelection();
    }

    private String obtenerEspecialidad() {
        // Si el usuario escribió algo directo en el combo (editable), usar eso
        String escrita = cmbEspecialidad.getEditor().getText().trim();
        if (!escrita.isEmpty()) {
            return escrita;
        }
        // Si seleccionó de la lista
        String seleccionada = cmbEspecialidad.getValue();
        if (seleccionada != null) {
            return seleccionada;
        }
        return "";
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