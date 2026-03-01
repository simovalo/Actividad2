package com.tallerbici.viewController;

import com.tallerbici.model.Bicicleta;
import com.tallerbici.model.Cliente;
import com.tallerbici.model.enums.TipoBicicleta;
import com.tallerbici.model.singleton.GestorTaller;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controlador del módulo de Bicicletas.
 * El ComboBox de clientes usa la ObservableList del GestorTaller,
 * así que cuando se registra un cliente nuevo aparece aquí automáticamente.
 *
 * @author Equipo TallerBici - Programación II
 */
public class BicicletaController implements Initializable {

    @FXML private TextField             txtSerial;
    @FXML private TextField             txtMarca;
    @FXML private ComboBox<TipoBicicleta> cmbTipo;
    @FXML private TextField             txtColor;
    @FXML private Spinner<Integer>      spinnerAnio;
    @FXML private ComboBox<Cliente>     cmbCliente;
    @FXML private Button                btnAccion;
    @FXML private Label                 lblMensaje;
    @FXML private Label                 lblModoEdicion;
    @FXML private ListView<Bicicleta>   listBicicletas;
    @FXML private TextField             txtBuscarBici;
    @FXML private TextArea              txtDetalleBici;

    private GestorTaller gestor = GestorTaller.getInstance();
    private boolean modoEdicion = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Cargar tipos de bicicleta en el combo
        cmbTipo.setItems(FXCollections.observableArrayList(TipoBicicleta.values()));
        cmbTipo.getSelectionModel().selectFirst();

        // El combo de clientes apunta a la lista del GestorTaller
        // Si se registra un cliente nuevo en otro tab, aquí aparece solo
        cmbCliente.setItems(gestor.getListaClientes());

        listBicicletas.setItems(gestor.getListaBicicletas());
    }

    @FXML
    private void accionPrincipal() {
        if (modoEdicion) {
            actualizarBicicleta();
        } else {
            registrarBicicleta();
        }
    }

    private void registrarBicicleta() {
        try {
            Cliente duenio = cmbCliente.getValue();
            TipoBicicleta tipo = cmbTipo.getValue();

            if (duenio == null) { mostrarError("❌ Selecciona el cliente dueño."); return; }
            if (tipo == null)   { mostrarError("❌ Selecciona el tipo de bicicleta."); return; }

            Bicicleta nueva = new Bicicleta.Builder()
                    .numeroSerial(txtSerial.getText().trim())
                    .marca(txtMarca.getText().trim())
                    .tipo(tipo)
                    .color(txtColor.getText().trim())
                    .anio(spinnerAnio.getValue())
                    .cedulaCliente(duenio.getNumeroCedula())
                    .build();

            gestor.registrarBicicleta(nueva);
            mostrarExito("✅ Bicicleta registrada: " + nueva.getResumen());
            limpiarFormulario();

        } catch (IllegalStateException | IllegalArgumentException e) {
            mostrarError("❌ " + e.getMessage());
        }
    }

    private void actualizarBicicleta() {
        try {
            Cliente duenio = cmbCliente.getValue();
            TipoBicicleta tipo = cmbTipo.getValue();

            if (duenio == null) { mostrarError("❌ Selecciona el cliente dueño."); return; }
            if (tipo == null)   { mostrarError("❌ Selecciona el tipo."); return; }

            Bicicleta actualizada = new Bicicleta.Builder()
                    .numeroSerial(txtSerial.getText().trim())
                    .marca(txtMarca.getText().trim())
                    .tipo(tipo)
                    .color(txtColor.getText().trim())
                    .anio(spinnerAnio.getValue())
                    .cedulaCliente(duenio.getNumeroCedula())
                    .build();

            gestor.actualizarBicicleta(actualizada);
            mostrarExito("💾 Bicicleta actualizada: " + actualizada.getResumen());
            limpiarFormulario();

        } catch (IllegalStateException | IllegalArgumentException e) {
            mostrarError("❌ " + e.getMessage());
        }
    }

    @FXML
    private void editarSeleccionado() {
        Bicicleta sel = listBicicletas.getSelectionModel().getSelectedItem();
        if (sel == null) { mostrarError("❌ Selecciona una bicicleta para editar."); return; }

        txtSerial.setText(sel.getNumeroSerial());
        txtSerial.setDisable(true); // El serial no se puede cambiar
        txtMarca.setText(sel.getMarca());
        cmbTipo.setValue(sel.getTipo());
        txtColor.setText(sel.getColor());
        spinnerAnio.getValueFactory().setValue(sel.getAnio());

        // Buscar y seleccionar el dueño en el combo
        Cliente duenio = gestor.buscarCliente(sel.getCedulaCliente());
        if (duenio != null) {
            cmbCliente.setValue(duenio);
        }

        modoEdicion = true;
        btnAccion.setText("💾 Guardar Cambios");
        btnAccion.setStyle("-fx-background-color: #F57C00; -fx-text-fill: white; -fx-font-weight: bold;");
        lblModoEdicion.setText("✏ Editando: " + sel.getResumen());
        lblModoEdicion.setStyle("-fx-text-fill: #E65100; -fx-font-weight: bold;");
    }

    @FXML
    private void filtrarBicicletas() {
        String criterio = txtBuscarBici.getText().trim().toLowerCase();

        if (criterio.isEmpty()) {
            listBicicletas.setItems(gestor.getListaBicicletas());
        } else {
            ArrayList<Bicicleta> filtradas = new ArrayList<>();
            for (Bicicleta b : gestor.getListaBicicletas()) {
                if (b.coincideCon(criterio)) {
                    filtradas.add(b);
                }
            }
            listBicicletas.setItems(FXCollections.observableArrayList(filtradas));
        }
    }

    @FXML
    private void mostrarDetalleBici(MouseEvent event) {
        Bicicleta sel = listBicicletas.getSelectionModel().getSelectedItem();
        if (sel != null) {
            String nombreDuenio = "Desconocido";
            Cliente duenio = gestor.buscarCliente(sel.getCedulaCliente());
            if (duenio != null) {
                nombreDuenio = duenio.getNombreCompleto();
            }
            txtDetalleBici.setText(sel.getDetalles() + "\nDueño: " + nombreDuenio);
        }
    }

    @FXML
    public void limpiarFormulario() {
        txtSerial.clear();
        txtSerial.setDisable(false);
        txtMarca.clear();
        txtColor.clear();
        cmbTipo.getSelectionModel().selectFirst();
        cmbCliente.getSelectionModel().clearSelection();
        lblMensaje.setText("");
        lblModoEdicion.setText("");
        txtDetalleBici.clear();
        txtBuscarBici.clear();
        modoEdicion = false;
        btnAccion.setText("✅ Registrar Bicicleta");
        btnAccion.setStyle("-fx-background-color: #1A237E; -fx-text-fill: white; -fx-font-weight: bold;");
        listBicicletas.setItems(gestor.getListaBicicletas());
        listBicicletas.getSelectionModel().clearSelection();
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