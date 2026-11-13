package ni.edu.uam.prueba_hibernateg3.Dao;

import jakarta.persistence.EntityManager;
import ni.edu.uam.prueba_hibernateg3.Database.JPAUtil;
import ni.edu.uam.prueba_hibernateg3.Model.TipoEquipo;

import java.util.List;

public class TipoEquipoDao {
    public List<TipoEquipo> listar() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT t FROM TipoEquipo t ORDER BY t.nombre", TipoEquipo.class).getResultList();
        } finally {
            em.close();
        }
    }
}
