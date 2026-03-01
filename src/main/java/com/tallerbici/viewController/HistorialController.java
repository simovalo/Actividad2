package com.tallerbici.viewController;

import com.tallerbici.model.OrdenServicio;
import com.tallerbici.model.singleton.GestorTaller;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controlador del módulo de Historial.
 * Permite buscar órdenes por serial de bicicleta o por fecha.
 *
 * @author Equipo TallerBici - Programación II
 */
public class HistorialController implements Initializable {

    @FXML private TextField               txtSerialBuscar;
    @FXML private DatePicker              dateFechaBuscar;
    @FXML private Label                   lblResultado;
    @FXML private ListView<OrdenServicio> listResultados;
    @FXML private TextArea                txtDetalleHistorial;

    private GestorTaller gestor = GestorTaller.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Mostrar todas las órdenes al abrir el módulo
        listResultados.setItems(gestor.getListaOrdenes());
    }

    @FXML
    private void buscarPorSerial() {
        String serial = txtSerialBuscar.getText().trim();
        if (serial.isEmpty()) {
            lblResultado.setText("❌ Escribe el número serial de la bicicleta.");
            lblResultado.setStyle("-fx-text-fill: #C62828;");
            return;
        }

        ArrayList<OrdenServicio> historial = gestor.getHistorialBicicleta(serial);

        if (historial.isEmpty()) {
            lblResultado.setText("No se encontraron órdenes para el serial: " + serial);
            lblResultado.setStyle("-fx-text-fill: #888888;");
        } else {
            lblResultado.setText("Se encontraron " + historial.size() + " orden(es) para: " + serial);
            lblResultado.setStyle("-fx-text-fill: #388E3C;");
        }

        listResultados.setItems(FXCollections.observableArrayList(historial));
        txtDetalleHistorial.clear();
    }

    @FXML
    private void buscarPorFecha() {
        LocalDate fecha = dateFechaBuscar.getValue();
        if (fecha == null) {
            lblResultado.setText("❌ Selecciona una fecha.");
            lblResultado.setStyle("-fx-text-fill: #C62828;");
            return;
        }

        ArrayList<OrdenServicio> ordenes = gestor.getOrdenesPorFecha(fecha);

        if (ordenes.isEmpty()) {
            lblResultado.setText("No hay órdenes registradas para: " + fecha);
            lblResultado.setStyle("-fx-text-fill: #888888;");
        } else {
            lblResultado.setText("Se encontraron " + ordenes.size() + " orden(es) para: " + fecha);
            lblResultado.setStyle("-fx-text-fill: #388E3C;");
        }

        listResultados.setItems(FXCollections.observableArrayList(ordenes));
        txtDetalleHistorial.clear();
    }

    @FXML
    private void mostrarTodasLasOrdenes() {
        listResultados.setItems(gestor.getListaOrdenes());
        lblResultado.setText("Mostrando todas las órdenes.");
        lblResultado.setStyle("-fx-text-fill: #888888;");
        txtSerialBuscar.clear();
        dateFechaBuscar.setValue(null);
        txtDetalleHistorial.clear();
    }

    @FXML
    private void mostrarDetalleHistorial(MouseEvent event) {
        OrdenServicio sel = listResultados.getSelectionModel().getSelectedItem();
        if (sel != null) {
            txtDetalleHistorial.setText(sel.getDetalles());
        }
    }
}