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
public class PegawaiJpaController implements Serializable {

    public PegawaiJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pegawai pegawai) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        KeteranganPembelian keteranganPembelianOrphanCheck = pegawai.getKeteranganPembelian();
        if (keteranganPembelianOrphanCheck != null) {
            Pegawai oldPegawaiOfKeteranganPembelian = keteranganPembelianOrphanCheck.getPegawai();
            if (oldPegawaiOfKeteranganPembelian != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The KeteranganPembelian " + keteranganPembelianOrphanCheck + " already has an item of type Pegawai whose keteranganPembelian column cannot be null. Please make another selection for the keteranganPembelian field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            KeteranganPembelian keteranganPembelian = pegawai.getKeteranganPembelian();
            if (keteranganPembelian != null) {
                keteranganPembelian = em.getReference(keteranganPembelian.getClass(), keteranganPembelian.getIdbarang());
                pegawai.setKeteranganPembelian(keteranganPembelian);
            }
            em.persist(pegawai);
            if (keteranganPembelian != null) {
                keteranganPembelian.setPegawai(pegawai);
                keteranganPembelian = em.merge(keteranganPembelian);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPegawai(pegawai.getIdpegawai()) != null) {
                throw new PreexistingEntityException("Pegawai " + pegawai + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pegawai pegawai) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pegawai persistentPegawai = em.find(Pegawai.class, pegawai.getIdpegawai());
            KeteranganPembelian keteranganPembelianOld = persistentPegawai.getKeteranganPembelian();
            KeteranganPembelian keteranganPembelianNew = pegawai.getKeteranganPembelian();
            List<String> illegalOrphanMessages = null;
            if (keteranganPembelianNew != null && !keteranganPembelianNew.equals(keteranganPembelianOld)) {
                Pegawai oldPegawaiOfKeteranganPembelian = keteranganPembelianNew.getPegawai();
                if (oldPegawaiOfKeteranganPembelian != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The KeteranganPembelian " + keteranganPembelianNew + " already has an item of type Pegawai whose keteranganPembelian column cannot be null. Please make another selection for the keteranganPembelian field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (keteranganPembelianNew != null) {
                keteranganPembelianNew = em.getReference(keteranganPembelianNew.getClass(), keteranganPembelianNew.getIdbarang());
                pegawai.setKeteranganPembelian(keteranganPembelianNew);
            }
            pegawai = em.merge(pegawai);
            if (keteranganPembelianOld != null && !keteranganPembelianOld.equals(keteranganPembelianNew)) {
                keteranganPembelianOld.setPegawai(null);
                keteranganPembelianOld = em.merge(keteranganPembelianOld);
            }
            if (keteranganPembelianNew != null && !keteranganPembelianNew.equals(keteranganPembelianOld)) {
                keteranganPembelianNew.setPegawai(pegawai);
                keteranganPembelianNew = em.merge(keteranganPembelianNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = pegawai.getIdpegawai();
                if (findPegawai(id) == null) {
                    throw new NonexistentEntityException("The pegawai with id " + id + " no longer exists.");
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
            Pegawai pegawai;
            try {
                pegawai = em.getReference(Pegawai.class, id);
                pegawai.getIdpegawai();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pegawai with id " + id + " no longer exists.", enfe);
            }
            KeteranganPembelian keteranganPembelian = pegawai.getKeteranganPembelian();
            if (keteranganPembelian != null) {
                keteranganPembelian.setPegawai(null);
                keteranganPembelian = em.merge(keteranganPembelian);
            }
            em.remove(pegawai);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pegawai> findPegawaiEntities() {
        return findPegawaiEntities(true, -1, -1);
    }

    public List<Pegawai> findPegawaiEntities(int maxResults, int firstResult) {
        return findPegawaiEntities(false, maxResults, firstResult);
    }

    private List<Pegawai> findPegawaiEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pegawai.class));
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

    public Pegawai findPegawai(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pegawai.class, id);
        } finally {
            em.close();
        }
    }

    public int getPegawaiCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pegawai> rt = cq.from(Pegawai.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
