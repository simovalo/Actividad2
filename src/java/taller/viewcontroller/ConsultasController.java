package taller.viewcontroller;

import taller.app.MainApp;
import taller.model.OrdenServicio;
import taller.model.TallerBicicletas;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador de la vista de consultas y reportes.
 *
 * Concentra las tres funcionalidades de consulta: Historial de servicios por serial de bicicleta, Órdenes ingresadas en una fecha específica y Costo total acumulado por cliente.
 * 
 * @author Samuel Marin
 * @version 1.0
 */
public class ConsultasController {

    private final TallerBicicletas taller = MainApp.getTaller();

    @FXML private TextField  txtSerialHistorial;
    @FXML private DatePicker dpFechaConsulta;
    @FXML private TextField  txtIdClienteReporte;
    @FXML private Label      lblResultadoReporte;
    @FXML private Label      lblTituloHistorial;

    @FXML private TableView<OrdenServicio>            tablaResultados;
    @FXML private TableColumn<OrdenServicio, Integer> colNumero;
    @FXML private TableColumn<OrdenServicio, String>  colFecha;
    @FXML private TableColumn<OrdenServicio, String>  colBicicleta;
    @FXML private TableColumn<OrdenServicio, String>  colMecanico;
    @FXML private TableColumn<OrdenServicio, String>  colMotivo;
    @FXML private TableColumn<OrdenServicio, Double>  colCosto;

    @FXML private Label lblMensaje;

    // Configura la fecha actual y las columnas de la tabla
    @FXML
    public void initialize() {
        dpFechaConsulta.setValue(LocalDate.now());
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numeroOrden"));
        colFecha.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getFechaIngreso().toString()));
        colBicicleta.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getBicicleta().toString()));
        colMecanico.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getMecanico().getNombre()));
        colMotivo.setCellValueFactory(new PropertyValueFactory<>("motivo"));
        colCosto.setCellValueFactory(new PropertyValueFactory<>("costoTotal"));
    }

    //Busca el historial de servicios por serial de bicicleta y muestra los resultados en la tabla
    @FXML
    private void buscarHistorial() {
        String serial = txtSerialHistorial.getText().trim();
        if (serial.isEmpty()) { mostrarError("Ingrese un número de serial."); return; }
        List<OrdenServicio> r = taller.historialBicicleta(serial);
        tablaResultados.setItems(FXCollections.observableArrayList(r));
        lblTituloHistorial.setText("Historial — bicicleta " + serial + "  (" + r.size() + " orden(es))");
        lblMensaje.setText("");
    }

    //Busca las ordenes registradas en la fecha seleccionada
    @FXML
    private void buscarPorFecha() {
        LocalDate fecha = dpFechaConsulta.getValue();
        if (fecha == null) { mostrarError("Seleccione una fecha."); return; }
        List<OrdenServicio> r = taller.ordenesPorFecha(fecha);
        tablaResultados.setItems(FXCollections.observableArrayList(r));
        lblTituloHistorial.setText("Órdenes del " + fecha + "  (" + r.size() + " encontrada(s))");
        lblMensaje.setText("");
    }

    //Calcula el total acumulado de un cliente y muestra el resultado o error
    @FXML
    private void calcularReporte() {
        String id = txtIdClienteReporte.getText().trim();
        if (id.isEmpty()) { mostrarError("Ingrese un ID de cliente."); return; }
        try {
            double total = taller.costosAcumuladosPorCliente(id);
            lblResultadoReporte.setStyle(
                    "-fx-text-fill: #2563eb; -fx-font-size: 14px; -fx-font-weight: bold;");
            lblResultadoReporte.setText(String.format("Total acumulado: $%,.2f", total));
            lblMensaje.setText("");
        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
            lblResultadoReporte.setText("");
        }
    }

    //Muestra un mensaje de error en la pantalla
    private void mostrarError(String msg) {
        lblMensaje.setStyle("-fx-text-fill: #e74c3c;"); lblMensaje.setText(msg);
    }
}
