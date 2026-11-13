module ni.edu.uam.Prueba_HibernateG {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;


    opens ni.edu.uam.prueba_hibernateg3 to javafx.fxml;
    // Hibernate necesita acceso por reflexión a las entidades (campos privados y proxies LAZY)
    opens ni.edu.uam.prueba_hibernateg3.Model to org.hibernate.orm.core;
    exports ni.edu.uam.prueba_hibernateg3;
    exports ni.edu.uam.prueba_hibernateg3.Controller;
    opens ni.edu.uam.prueba_hibernateg3.Controller to javafx.fxml;
}