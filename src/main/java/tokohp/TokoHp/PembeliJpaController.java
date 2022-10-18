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
import javax.persistence.Persistence;
import tokohp.TokoHp.exceptions.IllegalOrphanException;
import tokohp.TokoHp.exceptions.NonexistentEntityException;
import tokohp.TokoHp.exceptions.PreexistingEntityException;

/**
 *
 * @author AXEL
 */
public class PembeliJpaController implements Serializable {

    public PembeliJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("tokohp_TokoHp_jar_0.0.1-SNAPSHOTPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pembeli pembeli) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Penjualan penjualan = pembeli.getPenjualan();
            if (penjualan != null) {
                penjualan = em.getReference(penjualan.getClass(), penjualan.getIdnota());
                pembeli.setPenjualan(penjualan);
            }
            em.persist(pembeli);
            if (penjualan != null) {
                Pembeli oldPembeliOfPenjualan = penjualan.getPembeli();
                if (oldPembeliOfPenjualan != null) {
                    oldPembeliOfPenjualan.setPenjualan(null);
                    oldPembeliOfPenjualan = em.merge(oldPembeliOfPenjualan);
                }
                penjualan.setPembeli(pembeli);
                penjualan = em.merge(penjualan);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPembeli(pembeli.getIdpembeli()) != null) {
                throw new PreexistingEntityException("Pembeli " + pembeli + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pembeli pembeli) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pembeli persistentPembeli = em.find(Pembeli.class, pembeli.getIdpembeli());
            Penjualan penjualanOld = persistentPembeli.getPenjualan();
            Penjualan penjualanNew = pembeli.getPenjualan();
            List<String> illegalOrphanMessages = null;
            if (penjualanOld != null && !penjualanOld.equals(penjualanNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Penjualan " + penjualanOld + " since its pembeli field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (penjualanNew != null) {
                penjualanNew = em.getReference(penjualanNew.getClass(), penjualanNew.getIdnota());
                pembeli.setPenjualan(penjualanNew);
            }
            pembeli = em.merge(pembeli);
            if (penjualanNew != null && !penjualanNew.equals(penjualanOld)) {
                Pembeli oldPembeliOfPenjualan = penjualanNew.getPembeli();
                if (oldPembeliOfPenjualan != null) {
                    oldPembeliOfPenjualan.setPenjualan(null);
                    oldPembeliOfPenjualan = em.merge(oldPembeliOfPenjualan);
                }
                penjualanNew.setPembeli(pembeli);
                penjualanNew = em.merge(penjualanNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = pembeli.getIdpembeli();
                if (findPembeli(id) == null) {
                    throw new NonexistentEntityException("The pembeli with id " + id + " no longer exists.");
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
            Pembeli pembeli;
            try {
                pembeli = em.getReference(Pembeli.class, id);
                pembeli.getIdpembeli();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pembeli with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Penjualan penjualanOrphanCheck = pembeli.getPenjualan();
            if (penjualanOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pembeli (" + pembeli + ") cannot be destroyed since the Penjualan " + penjualanOrphanCheck + " in its penjualan field has a non-nullable pembeli field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(pembeli);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pembeli> findPembeliEntities() {
        return findPembeliEntities(true, -1, -1);
    }

    public List<Pembeli> findPembeliEntities(int maxResults, int firstResult) {
        return findPembeliEntities(false, maxResults, firstResult);
    }

    private List<Pembeli> findPembeliEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pembeli.class));
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

    public Pembeli findPembeli(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pembeli.class, id);
        } finally {
            em.close();
        }
    }

    public int getPembeliCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pembeli> rt = cq.from(Pembeli.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
