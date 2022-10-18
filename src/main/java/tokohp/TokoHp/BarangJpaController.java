/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tokohp.TokoHp;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import tokohp.TokoHp.exceptions.IllegalOrphanException;
import tokohp.TokoHp.exceptions.NonexistentEntityException;
import tokohp.TokoHp.exceptions.PreexistingEntityException;

/**
 *
 * @author AXEL
 */
public class BarangJpaController implements Serializable {

    public BarangJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Barang barang) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pemasok pemasok = barang.getPemasok();
            if (pemasok != null) {
                pemasok = em.getReference(pemasok.getClass(), pemasok.getIdpemasok());
                barang.setPemasok(pemasok);
            }
            em.persist(barang);
            if (pemasok != null) {
                Barang oldBarangOfPemasok = pemasok.getBarang();
                if (oldBarangOfPemasok != null) {
                    oldBarangOfPemasok.setPemasok(null);
                    oldBarangOfPemasok = em.merge(oldBarangOfPemasok);
                }
                pemasok.setBarang(barang);
                pemasok = em.merge(pemasok);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findBarang(barang.getIdbarang()) != null) {
                throw new PreexistingEntityException("Barang " + barang + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Barang barang) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Barang persistentBarang = em.find(Barang.class, barang.getIdbarang());
            Pemasok pemasokOld = persistentBarang.getPemasok();
            Pemasok pemasokNew = barang.getPemasok();
            List<String> illegalOrphanMessages = null;
            if (pemasokOld != null && !pemasokOld.equals(pemasokNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Pemasok " + pemasokOld + " since its barang field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (pemasokNew != null) {
                pemasokNew = em.getReference(pemasokNew.getClass(), pemasokNew.getIdpemasok());
                barang.setPemasok(pemasokNew);
            }
            barang = em.merge(barang);
            if (pemasokNew != null && !pemasokNew.equals(pemasokOld)) {
                Barang oldBarangOfPemasok = pemasokNew.getBarang();
                if (oldBarangOfPemasok != null) {
                    oldBarangOfPemasok.setPemasok(null);
                    oldBarangOfPemasok = em.merge(oldBarangOfPemasok);
                }
                pemasokNew.setBarang(barang);
                pemasokNew = em.merge(pemasokNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = barang.getIdbarang();
                if (findBarang(id) == null) {
                    throw new NonexistentEntityException("The barang with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Barang barang;
            try {
                barang = em.getReference(Barang.class, id);
                barang.getIdbarang();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The barang with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Pemasok pemasokOrphanCheck = barang.getPemasok();
            if (pemasokOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Barang (" + barang + ") cannot be destroyed since the Pemasok " + pemasokOrphanCheck + " in its pemasok field has a non-nullable barang field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(barang);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Barang> findBarangEntities() {
        return findBarangEntities(true, -1, -1);
    }

    public List<Barang> findBarangEntities(int maxResults, int firstResult) {
        return findBarangEntities(false, maxResults, firstResult);
    }

    private List<Barang> findBarangEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Barang.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Barang findBarang(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Barang.class, id);
        } finally {
            em.close();
        }
    }

    public int getBarangCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Barang> rt = cq.from(Barang.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
