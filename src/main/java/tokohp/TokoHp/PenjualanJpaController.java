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
public class PenjualanJpaController implements Serializable {

    public PenjualanJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Penjualan penjualan) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Pembeli pembeliOrphanCheck = penjualan.getPembeli();
        if (pembeliOrphanCheck != null) {
            Penjualan oldPenjualanOfPembeli = pembeliOrphanCheck.getPenjualan();
            if (oldPenjualanOfPembeli != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Pembeli " + pembeliOrphanCheck + " already has an item of type Penjualan whose pembeli column cannot be null. Please make another selection for the pembeli field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pembeli pembeli = penjualan.getPembeli();
            if (pembeli != null) {
                pembeli = em.getReference(pembeli.getClass(), pembeli.getIdpembeli());
                penjualan.setPembeli(pembeli);
            }
            em.persist(penjualan);
            if (pembeli != null) {
                pembeli.setPenjualan(penjualan);
                pembeli = em.merge(pembeli);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPenjualan(penjualan.getIdnota()) != null) {
                throw new PreexistingEntityException("Penjualan " + penjualan + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Penjualan penjualan) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Penjualan persistentPenjualan = em.find(Penjualan.class, penjualan.getIdnota());
            Pembeli pembeliOld = persistentPenjualan.getPembeli();
            Pembeli pembeliNew = penjualan.getPembeli();
            List<String> illegalOrphanMessages = null;
            if (pembeliNew != null && !pembeliNew.equals(pembeliOld)) {
                Penjualan oldPenjualanOfPembeli = pembeliNew.getPenjualan();
                if (oldPenjualanOfPembeli != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Pembeli " + pembeliNew + " already has an item of type Penjualan whose pembeli column cannot be null. Please make another selection for the pembeli field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (pembeliNew != null) {
                pembeliNew = em.getReference(pembeliNew.getClass(), pembeliNew.getIdpembeli());
                penjualan.setPembeli(pembeliNew);
            }
            penjualan = em.merge(penjualan);
            if (pembeliOld != null && !pembeliOld.equals(pembeliNew)) {
                pembeliOld.setPenjualan(null);
                pembeliOld = em.merge(pembeliOld);
            }
            if (pembeliNew != null && !pembeliNew.equals(pembeliOld)) {
                pembeliNew.setPenjualan(penjualan);
                pembeliNew = em.merge(pembeliNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = penjualan.getIdnota();
                if (findPenjualan(id) == null) {
                    throw new NonexistentEntityException("The penjualan with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Penjualan penjualan;
            try {
                penjualan = em.getReference(Penjualan.class, id);
                penjualan.getIdnota();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The penjualan with id " + id + " no longer exists.", enfe);
            }
            Pembeli pembeli = penjualan.getPembeli();
            if (pembeli != null) {
                pembeli.setPenjualan(null);
                pembeli = em.merge(pembeli);
            }
            em.remove(penjualan);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Penjualan> findPenjualanEntities() {
        return findPenjualanEntities(true, -1, -1);
    }

    public List<Penjualan> findPenjualanEntities(int maxResults, int firstResult) {
        return findPenjualanEntities(false, maxResults, firstResult);
    }

    private List<Penjualan> findPenjualanEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Penjualan.class));
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

    public Penjualan findPenjualan(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Penjualan.class, id);
        } finally {
            em.close();
        }
    }

    public int getPenjualanCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Penjualan> rt = cq.from(Penjualan.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
