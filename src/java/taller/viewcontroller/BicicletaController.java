package taller.viewcontroller;

import taller.app.MainApp;
import taller.model.Bicicleta;
import taller.model.TallerBicicletas;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controlador de la vista de gestión de bicicletas.
 *
 * Permite registrar nuevas bicicletas asociándolas a un cliente existente mediante su ID, y visualizar todas las bicicletas en una tabla
 *
 * @author Juan Manuel Vera
 * @version 1.0
 */
public class BicicletaController {

    private final TallerBicicletas taller = MainApp.getTaller();

    @FXML private TextField              txtSerial;
    @FXML private TextField              txtMarca;
    @FXML private ComboBox<Bicicleta.Tipo> cmbTipo;
    @FXML private TextField              txtColor;
    @FXML private TextField              txtAnio;
    @FXML private TextField              txtIdCliente;
    @FXML private Label                  lblMensaje;

    @FXML private TableView<Bicicleta>            tablaBicicletas;
    @FXML private TableColumn<Bicicleta, String>  colSerial;
    @FXML private TableColumn<Bicicleta, String>  colMarca;
    @FXML private TableColumn<Bicicleta, String>  colTipo;
    @FXML private TableColumn<Bicicleta, String>  colColor;
    @FXML private TableColumn<Bicicleta, Integer> colAnio;
    @FXML private TableColumn<Bicicleta, String>  colPropietario;

    // Inicializa el combo, configura la tabla y carga los datos.
    @FXML
    public void initialize() {
        cmbTipo.setItems(FXCollections.observableArrayList(Bicicleta.Tipo.values()));
        cmbTipo.getSelectionModel().selectFirst();

        colSerial.setCellValueFactory(new PropertyValueFactory<>("serial"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));
        colPropietario.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getPropietario().getNombre()));
        refrescarTabla();
    }

    // Valida los campos y registra la nueva bicicleta.
    @FXML
    private void registrar() {
        String serial    = txtSerial.getText().trim();
        String marca     = txtMarca.getText().trim();
        String color     = txtColor.getText().trim();
        String anioStr   = txtAnio.getText().trim();
        String idCliente = txtIdCliente.getText().trim();

        if (serial.isEmpty() || marca.isEmpty() || color.isEmpty()
                || anioStr.isEmpty() || idCliente.isEmpty()) {
            mostrarError("Todos los campos son obligatorios."); return;
        }
        int anio;
        try { anio = Integer.parseInt(anioStr); }
        catch (NumberFormatException e) {
            mostrarError("El año debe ser un número entero (ej: 2022)."); return;
        }
        try {
            taller.registrarBicicleta(serial, marca, cmbTipo.getValue(), color, anio, idCliente);
            mostrarExito("Bicicleta registrada correctamente.");
            limpiarFormulario(); refrescarTabla();
        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        }
    }

    //Limpia el formulario y borra el mensajo
    @FXML
    private void limpiar() { limpiarFormulario(); lblMensaje.setText(""); }

    private void refrescarTabla() {
        tablaBicicletas.setItems(FXCollections.observableArrayList(taller.getBicicletas()));
    }

    //Limpia todos los campos del formulario
    private void limpiarFormulario() {
        txtSerial.clear(); txtMarca.clear(); txtColor.clear();
        txtAnio.clear(); txtIdCliente.clear();
        cmbTipo.getSelectionModel().selectFirst();
    }

    //Muestra un mensaje de exito
    private void mostrarExito(String msg) {
        lblMensaje.setStyle("-fx-text-fill: #27ae60;"); lblMensaje.setText(msg);
    }

    //Muestra un mensaje de error
    private void mostrarError(String msg) {
        lblMensaje.setStyle("-fx-text-fill: #e74c3c;"); lblMensaje.setText(msg);
    }
}
