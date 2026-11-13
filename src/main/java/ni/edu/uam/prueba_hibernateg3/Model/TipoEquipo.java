package ni.edu.uam.prueba_hibernateg3.Model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tipo_equipo", schema = "catalogos")
public class TipoEquipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo")
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(name = "descripcion", nullable = true, length = 255)
    private String descripcion;

    // Lado inverso de la relación: mappedBy es el nombre del ATRIBUTO en Equipo ("tipoEquipo").
    // Sin cascade: eliminar un tipo NO elimina sus equipos (la FK fk_equipo_tipo tiene ON DELETE RESTRICT).
    @OneToMany(mappedBy = "tipoEquipo", fetch = FetchType.LAZY)
    private List<Equipo> equipos = new ArrayList<>();

    public TipoEquipo() {
    }

    public TipoEquipo(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Equipo> getEquipos() {
        return equipos;
    }

    public void setEquipos(List<Equipo> equipos) {
        this.equipos = equipos;
    }

    // Los ComboBox<TipoEquipo> usan toString() para mostrar el nombre y no "TipoEquipo@1a2b3c"
    @Override
    public String toString() {
        return nombre;
    }
}
