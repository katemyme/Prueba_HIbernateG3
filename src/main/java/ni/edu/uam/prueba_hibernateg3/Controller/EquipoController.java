package ni.edu.uam.prueba_hibernateg3.Controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import ni.edu.uam.prueba_hibernateg3.Dao.EquipoDao;
import ni.edu.uam.prueba_hibernateg3.Dao.TipoEquipoDao;
import ni.edu.uam.prueba_hibernateg3.Model.Equipo;
import ni.edu.uam.prueba_hibernateg3.Model.TipoEquipo;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;
import java.util.Optional;

public class EquipoController {

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtCodigo;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtMarca;
    @FXML
    private TextField txtCantidad;
    @FXML
    private ComboBox<TipoEquipo> cmbTipo;
    @FXML
    private ComboBox<String> cmbEstado;

    @FXML
    private TextField txtBuscarNombre;
    @FXML
    private ComboBox<TipoEquipo> cmbFiltroTipo;

    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnActualizar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Button btnBuscar;
    @FXML
    private Button btnQuitarFiltros;

    @FXML
    private TableView<Equipo> tblEquipos;

    @FXML
    private TableColumn<Equipo, String> colCodigo;
    @FXML
    private TableColumn<Equipo, String> colNombre;
    @FXML
    private TableColumn<Equipo, String> colMarca;
    @FXML
    private TableColumn<Equipo, String> colEstado;
    @FXML
    private TableColumn<Equipo, Integer> colCantidad;
    @FXML
    private TableColumn<Equipo, TipoEquipo> colTipo;
    private final EquipoDao equipoDao = new EquipoDao();
    private final TipoEquipoDao tipoEquipoDao = new TipoEquipoDao();

    private Long idSeleccionado = null;

    @FXML
    public void initialize() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        // La columna muestra el toString() de TipoEquipo, que devuelve su nombre
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoEquipo"));
        // Mismos valores que la restricción chk_equipo_estado de la tabla
        cmbEstado.setItems(FXCollections.observableArrayList(Equipo.ESTADOS));
        tblEquipos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                seleccionarEquipo(newValue);
            }
        });

        actualizarBotones();

        try {
            cargarTipos();
            cargarEquipos();
        } catch (Exception ex) {
            ex.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "No se pudieron cargar los tipos de equipo: " + ex.getMessage());
        }
    }

    // Llena el ComboBox del formulario y el del filtro con los tipos de la base de datos
    private void cargarTipos() {
        List<TipoEquipo> tipos = tipoEquipoDao.listar();
        cmbTipo.setItems(FXCollections.observableList(tipos));
        cmbFiltroTipo.setItems(FXCollections.observableList(tipos));
    }

    // Recarga la tabla respetando la búsqueda por nombre y el filtro por tipo que estén puestos
    private void cargarEquipos() {
        try {
            List<Equipo> equipos = equipoDao.buscar(txtBuscarNombre.getText(), cmbFiltroTipo.getValue());
            limpiarCampos();
            tblEquipos.setItems(FXCollections.observableList(equipos));
        } catch (Exception ex) {
            ex.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "No se pudieron cargar los equipos: " + ex.getMessage());
        }
    }

    @FXML
    private void guardarEquipo() {
        try
            {
            Equipo equipo = obtenerFormulario();
            if (equipo == null) {
                return;
            }
            equipoDao.guardar(equipo);
            limpiarCampos();
            cargarEquipos();
            }catch(Exception ex){
            ex.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "No se pudo guardar el equipo: " + mensajeDeError(ex));
        }
    }

    @FXML
    private void actualizarEquipo() {
        Equipo equipo = obtenerFormulario();
        if (equipo == null) {
            return;
        }
        equipo.setId(idSeleccionado);
        try {
            equipoDao.actualizar(equipo);
            limpiarCampos();
            cargarEquipos();
        } catch (RuntimeException ex) {
            mostrarAlerta(Alert.AlertType.ERROR, "No se pudo actualizar el equipo: " + mensajeDeError(ex));
        }
    }

    @FXML
    private void eliminarEquipo() {
        try{
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmacion de eliminacion de datos");
            confirmacion.setHeaderText("Eliminar equipo");
            confirmacion.setContentText("¿Estas seguro que desea eliminar el registro seleccionado ?");

            Optional<ButtonType> resultado =confirmacion.showAndWait();
            if(resultado.isEmpty() || resultado.get() != ButtonType.OK) {
                return;
            }
            boolean eliminado = equipoDao.eliminar(idSeleccionado);
            if (!eliminado){
                mostrarAlerta(Alert.AlertType.WARNING, "El equipo ya no existe en la base de datos.");
                cargarEquipos();
                return;
            }
            limpiarCampos();
            cargarEquipos();

        }catch (Exception ex){
            ex.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "No se pudo eliminar el equipo: " + ex.getMessage());
        }
    }

    // Búsqueda por parte del nombre y/o filtro por tipo (se pueden combinar)
    @FXML
    private void aplicarFiltros() {
        cargarEquipos();
    }

    // Borra la búsqueda y el filtro y vuelve a mostrar todos los equipos
    @FXML
    private void quitarFiltros() {
        txtBuscarNombre.clear();
        cmbFiltroTipo.setValue(null);
        cargarEquipos();
    }

    @FXML
    private void limpiarCampos() {
        txtId.clear();
        txtCodigo.clear();
        txtNombre.clear();
        txtMarca.clear();
        txtCantidad.clear();
        cmbTipo.setValue(null);
        cmbEstado.setValue(null);
        idSeleccionado = null;
        tblEquipos.getSelectionModel().clearSelection();
        actualizarBotones();
    }

    private void seleccionarEquipo(Equipo equipo) {
        idSeleccionado = equipo.getId();
        txtId.setText(String.valueOf(equipo.getId()));
        txtCodigo.setText(equipo.getCodigo());
        txtNombre.setText(equipo.getNombre());
        // La marca es opcional, puede venir null desde la base de datos
        txtMarca.setText(equipo.getMarca() == null ? "" : equipo.getMarca());
        txtCantidad.setText(String.valueOf(equipo.getCantidad()));
        cmbTipo.setValue(buscarTipo(equipo.getTipoEquipo()));
        cmbEstado.setValue(equipo.getEstado());
        actualizarBotones();
    }

    // Busca en el ComboBox el tipo con el mismo id; se compara por id porque
    // el tipo del equipo y los del ComboBox son objetos de consultas distintas
    private TipoEquipo buscarTipo(TipoEquipo tipoEquipo) {
        if (tipoEquipo == null) {
            return null;
        }
        for (TipoEquipo t : cmbTipo.getItems()) {
            if (t.getId().equals(tipoEquipo.getId())) {
                return t;
            }
        }
        return null;
    }

    private void actualizarBotones() {
        boolean sinSeleccion = idSeleccionado == null;
        // Con un equipo seleccionado no se puede "Guardar" (insertaría de nuevo el mismo código y nombre,
        // que son UNIQUE); para agregar uno nuevo primero se presiona "Limpiar"
        btnGuardar.setDisable(!sinSeleccion);
        btnActualizar.setDisable(sinSeleccion);
        btnEliminar.setDisable(sinSeleccion);
    }

    // Lee y valida los campos; devuelve null (y muestra un aviso) si algún dato es inválido
    private Equipo obtenerFormulario() {
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String marca = txtMarca.getText().trim();
        // Las columnas codigo y nombre son NOT NULL
        if (codigo.isEmpty() || nombre.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "El código y el nombre son obligatorios.");
            return null;
        }
        // Mismo largo que las columnas VARCHAR(100) de la tabla
        if (codigo.length() > 100 || nombre.length() > 100 || marca.length() > 100) {
            mostrarAlerta(Alert.AlertType.WARNING, "El código, el nombre y la marca admiten como máximo 100 caracteres.");
            return null;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText().trim());
        } catch (NumberFormatException ex) {
            mostrarAlerta(Alert.AlertType.WARNING, "La cantidad debe ser un número entero.");
            return null;
        }
        // Mismo límite que la restricción chk_equipo_cantidad de la tabla
        if (cantidad < 0) {
            mostrarAlerta(Alert.AlertType.WARNING, "La cantidad no puede ser negativa.");
            return null;
        }

        // La columna id_tipo es NOT NULL, así que el tipo es obligatorio
        TipoEquipo tipoEquipo = cmbTipo.getValue();
        if (tipoEquipo == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Debe seleccionar un tipo de equipo.");
            return null;
        }

        // La columna estado es NOT NULL y solo acepta los valores de chk_equipo_estado
        String estado = cmbEstado.getValue();
        if (estado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Debe seleccionar un estado.");
            return null;
        }

        Equipo equipo = new Equipo();
        equipo.setCodigo(codigo);
        equipo.setNombre(nombre);
        // La marca es opcional: si está vacía se guarda NULL
        equipo.setMarca(marca.isEmpty() ? null : marca);
        equipo.setCantidad(cantidad);
        equipo.setEstado(estado);
        equipo.setTipoEquipo(tipoEquipo);
        return equipo;
    }

    // Si PostgreSQL rechazó el código o el nombre por estar repetidos (restricciones UNIQUE
    // equipo_codigo_key y equipo_nombre_key), devuelve un mensaje claro; si es otro error, su mensaje normal
    private String mensajeDeError(Exception ex) {
        // La ConstraintViolationException de Hibernate puede venir directa o envuelta en otra excepción
        for (Throwable causa = ex; causa != null; causa = causa.getCause()) {
            if (causa instanceof ConstraintViolationException violacion && violacion.getConstraintName() != null) {
                String restriccion = violacion.getConstraintName().toLowerCase();
                if (restriccion.contains("equipo_codigo_key")) {
                    return "Ya existe un equipo con el código \"" + txtCodigo.getText().trim() + "\".";
                }
                if (restriccion.contains("equipo_nombre_key")) {
                    return "Ya existe un equipo con el nombre \"" + txtNombre.getText().trim() + "\".";
                }
            }
        }
        return ex.getMessage();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String mensaje) {
        Alert alerta = new Alert(tipo, mensaje);
        alerta.setHeaderText(null);
        alerta.showAndWait();
    }
}
