package taller.app;

import taller.model.TallerBicicletas;
import taller.viewcontroller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * entrada a la aplicacion echa en fx.
 *
 * <p>se inicializa la instancia unica de con los datos
 * quemados.</p>
 *
 * @author Juan Manuel Vera
 * @version 1.0
 */
public class MainApp extends Application {

    /**
     * instancia unica del taller, compartida por todos los controladores
     * a través de {@link #getTaller()}.
     */
    private static final TallerBicicletas taller = new TallerBicicletas(
            "Ciclo Taller El Parche",
            "Cra. 15 # 82-34, Bogotá",
            "3201234567"
    );

    /**
     * Retorna la instancia compartida del taller.
     *
     * @return instancia de {@link TallerBicicletas}.
     */
    public static TallerBicicletas getTaller() { return taller; }

    /**
     * Inicializa y muestra la ventana principal.
     *
     * @param stage Ventana principal proporcionada por JavaFX.
     * @throws Exception si el FXML no se encuentra o falla al cargarse.
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/main.fxml"));
        Scene scene = new Scene(loader.load(), 960, 680);
        scene.getStylesheets().add(
                getClass().getResource("/fxml/styles.css").toExternalForm());

        stage.setTitle("🚲  " + taller.getNombre() + "  —  " + taller.getDireccion());
        stage.setScene(scene);
        stage.setMinWidth(820);
        stage.setMinHeight(600);
        stage.show();
    }

    /**
     * Método principal. Lanza la aplicación JavaFX.
     *
     * @param args Argumentos de línea de comandos (no se usan).
     */
    public static void main(String[] args) { launch(args); }
}
