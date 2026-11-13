module ni.edu.uam.Prueba_HibernateG {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;


    opens ni.edu.uam.prueba_hibernateg3 to javafx.fxml;
    // Hibernate necesita acceso por reflexión a las entidades (campos privados y proxies LAZY)
    // y javafx.base lo necesita para que PropertyValueFactory lea los getters en la tabla
    opens ni.edu.uam.prueba_hibernateg3.Model to org.hibernate.orm.core, javafx.base;
    exports ni.edu.uam.prueba_hibernateg3;
    exports ni.edu.uam.prueba_hibernateg3.Controller;
    opens ni.edu.uam.prueba_hibernateg3.Controller to javafx.fxml;
}