package ni.edu.uam.prueba_hibernateg3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ni.edu.uam.prueba_hibernateg3.Database.JPAUtil;

import java.io.IOException;

public class LaboratorioApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LaboratorioApplication.class.getResource("Laboratorio-view.fxml"));
        // Sin ancho/alto fijos: la ventana usa el tamaño del FXML (900 x 640)
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Control de Equipos de Laboratorio");
        stage.setScene(scene);
        stage.show();
    }

    // Al cerrar la ventana se cierra el EntityManagerFactory y sus conexiones a PostgreSQL
    @Override
    public void stop() {
        JPAUtil.cerrar();
    }
}
