module ni.edu.uam.prueba_hibernateg3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens ni.edu.uam.prueba_hibernateg3 to javafx.fxml;
    exports ni.edu.uam.prueba_hibernateg3;
    exports ni.edu.uam.prueba_hibernateg3.Controller;
    opens ni.edu.uam.prueba_hibernateg3.Controller to javafx.fxml;
}