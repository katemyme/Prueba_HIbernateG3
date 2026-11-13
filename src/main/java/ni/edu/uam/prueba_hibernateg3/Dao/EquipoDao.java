package ni.edu.uam.prueba_hibernateg3.Dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ni.edu.uam.prueba_hibernateg3.Database.JPAUtil;
import ni.edu.uam.prueba_hibernateg3.Model.Equipo;
import ni.edu.uam.prueba_hibernateg3.Model.TipoEquipo;

import java.util.List;

public class EquipoDao {

    private TipoEquipo obtenerTipoEquipo(EntityManager em, Equipo equipo)
    {
        if(equipo.getTipoEquipo()==null || equipo.getTipoEquipo().getId() == null)
        {
            throw new RuntimeException("Error al obtener el tipo de equipo");
        }
        Long tipoEquipoId = equipo.getTipoEquipo().getId();
        TipoEquipo tipoEquipo = em.find(TipoEquipo.class, tipoEquipoId);
        if(tipoEquipo == null)
        {
            throw new RuntimeException("Error al obtener el tipo de equipo");
        }
        return tipoEquipo;
    }


    public void guardar(Equipo equipo) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try{
            // Iniciar la transacción en PostgreSQL
            tx.begin();
            TipoEquipo tipoEquipo = obtenerTipoEquipo(em, equipo);
            equipo.setTipoEquipo(tipoEquipo);
            // Decirle a Hibernate que prepare la persistencia del objeto.
            em.persist(equipo);
            // Confirmar los cambios en la base de datos
            tx.commit();
        }catch(RuntimeException ex){
            if(tx.isActive()){
                tx.rollback();
            }
            throw ex;
        }finally {
            em.close();
        }
    }

    public void actualizar(Equipo equipo) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            // merge copia los datos del objeto sobre el registro existente con el mismo id
            TipoEquipo tipoEquipo = obtenerTipoEquipo(em, equipo);
            equipo.setTipoEquipo(tipoEquipo);
            em.merge(equipo);
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    // Devuelve true si el equipo existía y se eliminó, false si no se encontró
    public boolean eliminar(long id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Equipo equipo = em.find(Equipo.class, id);
            if (equipo == null) {
                tx.rollback();
                return false;
            }
            em.remove(equipo);
            tx.commit();
            return true;
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    public List<Equipo> listar() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT e FROM Equipo e JOIN FETCH e.tipoEquipo ORDER BY e.id", Equipo.class).getResultList();
        }
        finally {
            em.close();
        }
    }
}
