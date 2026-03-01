package com.tallerbici.viewController;

import com.tallerbici.model.Repuesto;
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
 * Controlador del módulo de Repuestos.
 * Funcionalidad propia: gestión de stock con alertas automáticas.
 *
 * Principio S (SRP): solo maneja la pantalla de repuestos e inventario.
 *
 * @author Equipo TallerBici - Programación II
 */
public class RepuestosController implements Initializable {

    @FXML private TextField          txtCodigo;
    @FXML private TextField          txtNombre;
    @FXML private ComboBox<String>   cmbCategoria;
    @FXML private TextField          txtPrecio;
    @FXML private Spinner<Integer>   spinnerStock;
    @FXML private Spinner<Integer>   spinnerMinimo;
    @FXML private Button             btnAccion;
    @FXML private Label              lblMensaje;
    @FXML private Label              lblModoEdicion;
    @FXML private Label              lblAlertas;
    @FXML private ListView<Repuesto> listRepuestos;
    @FXML private TextArea           txtDetalleRepuesto;
    @FXML private TextField          txtCodigoAjuste;
    @FXML private Spinner<Integer>   spinnerAjuste;

    private GestorTaller gestor = GestorTaller.getInstance();
    private boolean modoEdicion = false;

    private static final String[] CATEGORIAS = {
            "Frenos", "Transmisión", "Rodado", "Suspensión",
            "Dirección", "Cuadro", "Eléctrico", "Lubricantes", "General"
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cmbCategoria.setItems(FXCollections.observableArrayList(CATEGORIAS));
        listRepuestos.setItems(gestor.getListaRepuestos());
        verificarAlertas();
    }

    // Actualiza el label de alertas con los repuestos de stock bajo
    private void verificarAlertas() {
        ArrayList<Repuesto> bajos = gestor.getRepuestosConStockBajo();
        if (bajos.isEmpty()) {
            lblAlertas.setText("");
        } else {
            String mensaje = "⚠ STOCK BAJO en " + bajos.size() + " repuesto(s): ";
            for (int i = 0; i < bajos.size(); i++) {
                mensaje = mensaje + "[" + bajos.get(i).getCodigo() + "] ";
            }
            lblAlertas.setText(mensaje);
        }
    }

    @FXML
    private void accionPrincipal() {
        if (modoEdicion) {
            actualizarRepuesto();
        } else {
            registrarRepuesto();
        }
    }

    private void registrarRepuesto() {
        try {
            double precio = Double.parseDouble(txtPrecio.getText().trim());

            Repuesto nuevo = new Repuesto.Builder()
                    .codigo(txtCodigo.getText().trim())
                    .nombre(txtNombre.getText().trim())
                    .categoria(obtenerCategoria())
                    .precioUnitario(precio)
                    .cantidadDisponible(spinnerStock.getValue())
                    .stockMinimo(spinnerMinimo.getValue())
                    .build();

            gestor.registrarRepuesto(nuevo);
            verificarAlertas();
            mostrarExito("✅ Repuesto registrado: " + nuevo.getNombre());
            limpiarFormulario();

        } catch (NumberFormatException e) {
            mostrarError("❌ El precio debe ser un número. Ejemplo: 45000");
        } catch (Exception e) {
            mostrarError("❌ " + e.getMessage());
        }
    }

    private void actualizarRepuesto() {
        try {
            double precio = Double.parseDouble(txtPrecio.getText().trim());

            // Buscar el repuesto actual para conservar el stock disponible
            // (el stock solo se cambia con los botones ➕ y ➖)
            Repuesto actual = gestor.buscarRepuesto(txtCodigo.getText().trim());
            if (actual == null) {
                mostrarError("❌ No se encontró el repuesto.");
                return;
            }

            Repuesto actualizado = new Repuesto.Builder()
                    .codigo(actual.getCodigo())
                    .nombre(txtNombre.getText().trim())
                    .categoria(obtenerCategoria())
                    .precioUnitario(precio)
                    .cantidadDisponible(actual.getCantidadDisponible()) // stock no cambia aquí
                    .stockMinimo(spinnerMinimo.getValue())
                    .build();

            gestor.actualizarRepuesto(actualizado);
            verificarAlertas();
            mostrarExito("💾 Repuesto actualizado: " + actualizado.getNombre());
            limpiarFormulario();

        } catch (NumberFormatException e) {
            mostrarError("❌ El precio debe ser un número. Ejemplo: 45000");
        } catch (Exception e) {
            mostrarError("❌ " + e.getMessage());
        }
    }

    @FXML
    private void editarSeleccionado() {
        Repuesto sel = listRepuestos.getSelectionModel().getSelectedItem();
        if (sel == null) { mostrarError("❌ Selecciona un repuesto para editar."); return; }

        txtCodigo.setText(sel.getCodigo());
        txtCodigo.setDisable(true); // El código es la clave, no se puede cambiar
        txtNombre.setText(sel.getNombre());
        cmbCategoria.getEditor().setText(sel.getCategoria());
        txtPrecio.setText(String.valueOf(sel.getPrecioUnitario()));
        spinnerStock.getValueFactory().setValue(sel.getCantidadDisponible());
        spinnerStock.setDisable(true); // El stock se ajusta con ➕ y ➖
        spinnerMinimo.getValueFactory().setValue(sel.getStockMinimo());

        modoEdicion = true;
        btnAccion.setText("💾 Guardar Cambios");
        btnAccion.setStyle("-fx-background-color: #F57C00; -fx-text-fill: white; -fx-font-weight: bold;");
        lblModoEdicion.setText("✏ Editando: [" + sel.getCodigo() + "] " + sel.getNombre());
        lblModoEdicion.setStyle("-fx-text-fill: #E65100; -fx-font-weight: bold;");
    }

    @FXML
    private void agregarStock() {
        ajustarStock(true);
    }

    @FXML
    private void usarStock() {
        ajustarStock(false);
    }

    private void ajustarStock(boolean esAgregar) {
        String codigo = txtCodigoAjuste.getText().trim();
        if (codigo.isEmpty()) {
            mostrarError("❌ Escribe el código del repuesto a ajustar.");
            return;
        }

        Repuesto rep = gestor.buscarRepuesto(codigo);
        if (rep == null) {
            mostrarError("❌ No existe un repuesto con código: " + codigo);
            return;
        }

        try {
            int cantidad = spinnerAjuste.getValue();
            if (esAgregar) {
                rep.agregarStock(cantidad);
                mostrarExito("✅ Se agregaron " + cantidad + " unidades a: " + rep.getNombre());
            } else {
                rep.usarStock(cantidad);
                mostrarExito("✅ Se usaron " + cantidad + " unidades de: " + rep.getNombre());
            }
            // Notificar a la ObservableList que este repuesto cambió
            gestor.actualizarRepuesto(rep);
            verificarAlertas();

        } catch (Exception e) {
            mostrarError("❌ " + e.getMessage());
        }
    }

    @FXML
    private void verAlertas() {
        ArrayList<Repuesto> alertas = gestor.getRepuestosConStockBajo();
        if (alertas.isEmpty()) {
            mostrarExito("✅ Todos los repuestos tienen stock suficiente.");
            listRepuestos.setItems(gestor.getListaRepuestos());
        } else {
            listRepuestos.setItems(FXCollections.observableArrayList(alertas));
            mostrarError("⚠ Mostrando " + alertas.size() + " repuesto(s) con stock bajo.");
        }
    }

    @FXML
    private void refrescarLista() {
        listRepuestos.setItems(gestor.getListaRepuestos());
        verificarAlertas();
        mostrarExito("🔄 Lista actualizada.");
    }

    @FXML
    private void mostrarDetalleRepuesto(MouseEvent event) {
        Repuesto sel = listRepuestos.getSelectionModel().getSelectedItem();
        if (sel != null) {
            txtDetalleRepuesto.setText(sel.getDetalles());
        }
    }

    @FXML
    public void limpiarFormulario() {
        txtCodigo.clear();
        txtCodigo.setDisable(false);
        txtNombre.clear();
        txtPrecio.clear();
        spinnerStock.setDisable(false);
        cmbCategoria.getSelectionModel().clearSelection();
        cmbCategoria.getEditor().clear();
        lblMensaje.setText("");
        lblModoEdicion.setText("");
        modoEdicion = false;
        btnAccion.setText("✅ Registrar Repuesto");
        btnAccion.setStyle("-fx-background-color: #1A237E; -fx-text-fill: white; -fx-font-weight: bold;");
        listRepuestos.setItems(gestor.getListaRepuestos());
        listRepuestos.getSelectionModel().clearSelection();
    }

    private String obtenerCategoria() {
        String escrita = cmbCategoria.getEditor().getText().trim();
        if (!escrita.isEmpty()) {
            return escrita;
        }
        String seleccionada = cmbCategoria.getValue();
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