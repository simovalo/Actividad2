package taller.viewcontroller;

import taller.app.MainApp;
import taller.model.EstadoOrden;
import taller.model.OrdenServicio;
import taller.model.TallerBicicletas;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Controlador de la vista de órdenes de servicio.
 *
 * Permite crear nuevas órdenes (estado inicial: PENDIENTE) y actualizar el estado de cualquier orden seleccionada en la tabla.
 *
 * @author Juan Manuel Vera
 * @version 1.0
 */
public class OrdenController {

    private final TallerBicicletas taller = MainApp.getTaller();

    @FXML private TextField  txtSerial;
    @FXML private TextField  txtCodigoMecanico;
    @FXML private DatePicker dpFecha;
    @FXML private TextField  txtMotivo;
    @FXML private TextArea   txaDiagnostico;
    @FXML private TextArea   txaTrabajos;
    @FXML private TextField  txtCosto;
    @FXML private Label      lblMensaje;

    @FXML private ComboBox<EstadoOrden> cmbEstado;
    @FXML private Label                 lblMensajeEstado;

    @FXML private TableView<OrdenServicio>            tablaOrdenes;
    @FXML private TableColumn<OrdenServicio, Integer> colNumero;
    @FXML private TableColumn<OrdenServicio, String>  colFecha;
    @FXML private TableColumn<OrdenServicio, String>  colBicicleta;
    @FXML private TableColumn<OrdenServicio, String>  colMecanico;
    @FXML private TableColumn<OrdenServicio, String>  colMotivo;
    @FXML private TableColumn<OrdenServicio, String>  colEstado;
    @FXML private TableColumn<OrdenServicio, Double>  colCosto;

    //Configura la fecha, el combo y la tabla
    @FXML
    public void initialize() {
        dpFecha.setValue(LocalDate.now());
        cmbEstado.setItems(FXCollections.observableArrayList(EstadoOrden.values()));
        cmbEstado.getSelectionModel().selectFirst();

        colNumero.setCellValueFactory(new PropertyValueFactory<>("numeroOrden"));
        colFecha.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getFechaIngreso().toString()));
        colBicicleta.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getBicicleta().toString()));
        colMecanico.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getMecanico().getNombre()));
        colMotivo.setCellValueFactory(new PropertyValueFactory<>("motivo"));
        colEstado.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getEstado().getEtiqueta()));
        colCosto.setCellValueFactory(new PropertyValueFactory<>("costoTotal"));

        tablaOrdenes.getSelectionModel().selectedItemProperty().addListener(
            (obs, old, sel) -> {
                if (sel != null) cmbEstado.setValue(sel.getEstado());
            });

        refrescarTabla();
    }

    //Crea una nueva orden de servicio
    @FXML
    private void crearOrden() {
        String serial   = txtSerial.getText().trim();
        String codMec   = txtCodigoMecanico.getText().trim();
        String motivo   = txtMotivo.getText().trim();
        String diag     = txaDiagnostico.getText().trim();
        String trabajos = txaTrabajos.getText().trim();
        String costoStr = txtCosto.getText().trim();
        LocalDate fecha = dpFecha.getValue();

        if (serial.isEmpty() || codMec.isEmpty() || motivo.isEmpty()
                || diag.isEmpty() || trabajos.isEmpty() || costoStr.isEmpty() || fecha == null) {
            mostrarError("Todos los campos son obligatorios."); return;
        }
        if (fecha.isAfter(LocalDate.now())) {
            mostrarError("La fecha de ingreso no puede ser futura."); return;
        }
        double costo;
        try { costo = Double.parseDouble(costoStr); }
        catch (NumberFormatException e) {
            mostrarError("El costo debe ser un número (ej: 85000)."); return;
        }
        if (costo < 0) { mostrarError("El costo no puede ser negativo."); return; }

        try {
            taller.crearOrden(serial, codMec, fecha, LocalTime.now(),
                    motivo, diag, trabajos, costo);
            mostrarExito("Orden creada como PENDIENTE.");
            limpiarFormulario(); refrescarTabla();
        } catch (IllegalArgumentException e) { mostrarError(e.getMessage()); }
    }

    //Cambia el estado de la orden seleccionada
    @FXML
    private void actualizarEstado() {
        OrdenServicio sel = tablaOrdenes.getSelectionModel().getSelectedItem();
        if (sel == null) {
            lblMensajeEstado.setStyle("-fx-text-fill: #e74c3c;");
            lblMensajeEstado.setText("Seleccione una orden de la tabla.");
            return;
        }
        sel.setEstado(cmbEstado.getValue());
        lblMensajeEstado.setStyle("-fx-text-fill: #27ae60;");
        lblMensajeEstado.setText("Estado actualizado a: " + cmbEstado.getValue().getEtiqueta());
        refrescarTabla();
        tablaOrdenes.getSelectionModel().clearSelection();
    }

    //Limpia el formulario y borra el mensaje
    @FXML private void limpiar() { limpiarFormulario(); lblMensaje.setText(""); }

    //Actualiza la tabla con las ordenes actuales
    private void refrescarTabla() {
        tablaOrdenes.setItems(FXCollections.observableArrayList(taller.getOrdenes()));
    }

    //Borra los campos del formulario
    private void limpiarFormulario() {
        txtSerial.clear(); txtCodigoMecanico.clear(); txtMotivo.clear();
        txaDiagnostico.clear(); txaTrabajos.clear(); txtCosto.clear();
        dpFecha.setValue(LocalDate.now());
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
