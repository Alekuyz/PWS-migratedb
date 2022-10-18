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
public class PembelianJpaController implements Serializable {

    public PembelianJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pembelian pembelian) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Pemasok pemasokOrphanCheck = pembelian.getPemasok();
        if (pemasokOrphanCheck != null) {
            Pembelian oldPembelianOfPemasok = pemasokOrphanCheck.getPembelian();
            if (oldPembelianOfPemasok != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Pemasok " + pemasokOrphanCheck + " already has an item of type Pembelian whose pemasok column cannot be null. Please make another selection for the pemasok field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            KeteranganPembelian keteranganPembelian = pembelian.getKeteranganPembelian();
            if (keteranganPembelian != null) {
                keteranganPembelian = em.getReference(keteranganPembelian.getClass(), keteranganPembelian.getIdbarang());
                pembelian.setKeteranganPembelian(keteranganPembelian);
            }
            Pemasok pemasok = pembelian.getPemasok();
            if (pemasok != null) {
                pemasok = em.getReference(pemasok.getClass(), pemasok.getIdpemasok());
                pembelian.setPemasok(pemasok);
            }
            em.persist(pembelian);
            if (keteranganPembelian != null) {
                Pembelian oldPembelianOfKeteranganPembelian = keteranganPembelian.getPembelian();
                if (oldPembelianOfKeteranganPembelian != null) {
                    oldPembelianOfKeteranganPembelian.setKeteranganPembelian(null);
                    oldPembelianOfKeteranganPembelian = em.merge(oldPembelianOfKeteranganPembelian);
                }
                keteranganPembelian.setPembelian(pembelian);
                keteranganPembelian = em.merge(keteranganPembelian);
            }
            if (pemasok != null) {
                pemasok.setPembelian(pembelian);
                pemasok = em.merge(pemasok);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPembelian(pembelian.getIdnota()) != null) {
                throw new PreexistingEntityException("Pembelian " + pembelian + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pembelian pembelian) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pembelian persistentPembelian = em.find(Pembelian.class, pembelian.getIdnota());
            KeteranganPembelian keteranganPembelianOld = persistentPembelian.getKeteranganPembelian();
            KeteranganPembelian keteranganPembelianNew = pembelian.getKeteranganPembelian();
            Pemasok pemasokOld = persistentPembelian.getPemasok();
            Pemasok pemasokNew = pembelian.getPemasok();
            List<String> illegalOrphanMessages = null;
            if (keteranganPembelianOld != null && !keteranganPembelianOld.equals(keteranganPembelianNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain KeteranganPembelian " + keteranganPembelianOld + " since its pembelian field is not nullable.");
            }
            if (pemasokNew != null && !pemasokNew.equals(pemasokOld)) {
                Pembelian oldPembelianOfPemasok = pemasokNew.getPembelian();
                if (oldPembelianOfPemasok != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Pemasok " + pemasokNew + " already has an item of type Pembelian whose pemasok column cannot be null. Please make another selection for the pemasok field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (keteranganPembelianNew != null) {
                keteranganPembelianNew = em.getReference(keteranganPembelianNew.getClass(), keteranganPembelianNew.getIdbarang());
                pembelian.setKeteranganPembelian(keteranganPembelianNew);
            }
            if (pemasokNew != null) {
                pemasokNew = em.getReference(pemasokNew.getClass(), pemasokNew.getIdpemasok());
                pembelian.setPemasok(pemasokNew);
            }
            pembelian = em.merge(pembelian);
            if (keteranganPembelianNew != null && !keteranganPembelianNew.equals(keteranganPembelianOld)) {
                Pembelian oldPembelianOfKeteranganPembelian = keteranganPembelianNew.getPembelian();
                if (oldPembelianOfKeteranganPembelian != null) {
                    oldPembelianOfKeteranganPembelian.setKeteranganPembelian(null);
                    oldPembelianOfKeteranganPembelian = em.merge(oldPembelianOfKeteranganPembelian);
                }
                keteranganPembelianNew.setPembelian(pembelian);
                keteranganPembelianNew = em.merge(keteranganPembelianNew);
            }
            if (pemasokOld != null && !pemasokOld.equals(pemasokNew)) {
                pemasokOld.setPembelian(null);
                pemasokOld = em.merge(pemasokOld);
            }
            if (pemasokNew != null && !pemasokNew.equals(pemasokOld)) {
                pemasokNew.setPembelian(pembelian);
                pemasokNew = em.merge(pemasokNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = pembelian.getIdnota();
                if (findPembelian(id) == null) {
                    throw new NonexistentEntityException("The pembelian with id " + id + " no longer exists.");
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
            Pembelian pembelian;
            try {
                pembelian = em.getReference(Pembelian.class, id);
                pembelian.getIdnota();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pembelian with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            KeteranganPembelian keteranganPembelianOrphanCheck = pembelian.getKeteranganPembelian();
            if (keteranganPembelianOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pembelian (" + pembelian + ") cannot be destroyed since the KeteranganPembelian " + keteranganPembelianOrphanCheck + " in its keteranganPembelian field has a non-nullable pembelian field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Pemasok pemasok = pembelian.getPemasok();
            if (pemasok != null) {
                pemasok.setPembelian(null);
                pemasok = em.merge(pemasok);
            }
            em.remove(pembelian);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pembelian> findPembelianEntities() {
        return findPembelianEntities(true, -1, -1);
    }

    public List<Pembelian> findPembelianEntities(int maxResults, int firstResult) {
        return findPembelianEntities(false, maxResults, firstResult);
    }

    private List<Pembelian> findPembelianEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pembelian.class));
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

    public Pembelian findPembelian(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pembelian.class, id);
        } finally {
            em.close();
        }
    }

    public int getPembelianCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pembelian> rt = cq.from(Pembelian.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
