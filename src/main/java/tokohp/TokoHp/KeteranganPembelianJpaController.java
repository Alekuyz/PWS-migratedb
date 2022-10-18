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
public class KeteranganPembelianJpaController implements Serializable {

    public KeteranganPembelianJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("tokohp_TokoHp_jar_0.0.1-SNAPSHOTPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(KeteranganPembelian keteranganPembelian) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Pembelian pembelianOrphanCheck = keteranganPembelian.getPembelian();
        if (pembelianOrphanCheck != null) {
            KeteranganPembelian oldKeteranganPembelianOfPembelian = pembelianOrphanCheck.getKeteranganPembelian();
            if (oldKeteranganPembelianOfPembelian != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Pembelian " + pembelianOrphanCheck + " already has an item of type KeteranganPembelian whose pembelian column cannot be null. Please make another selection for the pembelian field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pembelian pembelian = keteranganPembelian.getPembelian();
            if (pembelian != null) {
                pembelian = em.getReference(pembelian.getClass(), pembelian.getIdnota());
                keteranganPembelian.setPembelian(pembelian);
            }
            Pegawai pegawai = keteranganPembelian.getPegawai();
            if (pegawai != null) {
                pegawai = em.getReference(pegawai.getClass(), pegawai.getIdpegawai());
                keteranganPembelian.setPegawai(pegawai);
            }
            em.persist(keteranganPembelian);
            if (pembelian != null) {
                pembelian.setKeteranganPembelian(keteranganPembelian);
                pembelian = em.merge(pembelian);
            }
            if (pegawai != null) {
                KeteranganPembelian oldKeteranganPembelianOfPegawai = pegawai.getKeteranganPembelian();
                if (oldKeteranganPembelianOfPegawai != null) {
                    oldKeteranganPembelianOfPegawai.setPegawai(null);
                    oldKeteranganPembelianOfPegawai = em.merge(oldKeteranganPembelianOfPegawai);
                }
                pegawai.setKeteranganPembelian(keteranganPembelian);
                pegawai = em.merge(pegawai);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findKeteranganPembelian(keteranganPembelian.getIdbarang()) != null) {
                throw new PreexistingEntityException("KeteranganPembelian " + keteranganPembelian + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(KeteranganPembelian keteranganPembelian) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            KeteranganPembelian persistentKeteranganPembelian = em.find(KeteranganPembelian.class, keteranganPembelian.getIdbarang());
            Pembelian pembelianOld = persistentKeteranganPembelian.getPembelian();
            Pembelian pembelianNew = keteranganPembelian.getPembelian();
            Pegawai pegawaiOld = persistentKeteranganPembelian.getPegawai();
            Pegawai pegawaiNew = keteranganPembelian.getPegawai();
            List<String> illegalOrphanMessages = null;
            if (pembelianNew != null && !pembelianNew.equals(pembelianOld)) {
                KeteranganPembelian oldKeteranganPembelianOfPembelian = pembelianNew.getKeteranganPembelian();
                if (oldKeteranganPembelianOfPembelian != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Pembelian " + pembelianNew + " already has an item of type KeteranganPembelian whose pembelian column cannot be null. Please make another selection for the pembelian field.");
                }
            }
            if (pegawaiOld != null && !pegawaiOld.equals(pegawaiNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Pegawai " + pegawaiOld + " since its keteranganPembelian field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (pembelianNew != null) {
                pembelianNew = em.getReference(pembelianNew.getClass(), pembelianNew.getIdnota());
                keteranganPembelian.setPembelian(pembelianNew);
            }
            if (pegawaiNew != null) {
                pegawaiNew = em.getReference(pegawaiNew.getClass(), pegawaiNew.getIdpegawai());
                keteranganPembelian.setPegawai(pegawaiNew);
            }
            keteranganPembelian = em.merge(keteranganPembelian);
            if (pembelianOld != null && !pembelianOld.equals(pembelianNew)) {
                pembelianOld.setKeteranganPembelian(null);
                pembelianOld = em.merge(pembelianOld);
            }
            if (pembelianNew != null && !pembelianNew.equals(pembelianOld)) {
                pembelianNew.setKeteranganPembelian(keteranganPembelian);
                pembelianNew = em.merge(pembelianNew);
            }
            if (pegawaiNew != null && !pegawaiNew.equals(pegawaiOld)) {
                KeteranganPembelian oldKeteranganPembelianOfPegawai = pegawaiNew.getKeteranganPembelian();
                if (oldKeteranganPembelianOfPegawai != null) {
                    oldKeteranganPembelianOfPegawai.setPegawai(null);
                    oldKeteranganPembelianOfPegawai = em.merge(oldKeteranganPembelianOfPegawai);
                }
                pegawaiNew.setKeteranganPembelian(keteranganPembelian);
                pegawaiNew = em.merge(pegawaiNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = keteranganPembelian.getIdbarang();
                if (findKeteranganPembelian(id) == null) {
                    throw new NonexistentEntityException("The keteranganPembelian with id " + id + " no longer exists.");
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
            KeteranganPembelian keteranganPembelian;
            try {
                keteranganPembelian = em.getReference(KeteranganPembelian.class, id);
                keteranganPembelian.getIdbarang();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The keteranganPembelian with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Pegawai pegawaiOrphanCheck = keteranganPembelian.getPegawai();
            if (pegawaiOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This KeteranganPembelian (" + keteranganPembelian + ") cannot be destroyed since the Pegawai " + pegawaiOrphanCheck + " in its pegawai field has a non-nullable keteranganPembelian field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Pembelian pembelian = keteranganPembelian.getPembelian();
            if (pembelian != null) {
                pembelian.setKeteranganPembelian(null);
                pembelian = em.merge(pembelian);
            }
            em.remove(keteranganPembelian);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<KeteranganPembelian> findKeteranganPembelianEntities() {
        return findKeteranganPembelianEntities(true, -1, -1);
    }

    public List<KeteranganPembelian> findKeteranganPembelianEntities(int maxResults, int firstResult) {
        return findKeteranganPembelianEntities(false, maxResults, firstResult);
    }

    private List<KeteranganPembelian> findKeteranganPembelianEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(KeteranganPembelian.class));
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

    public KeteranganPembelian findKeteranganPembelian(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(KeteranganPembelian.class, id);
        } finally {
            em.close();
        }
    }

    public int getKeteranganPembelianCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<KeteranganPembelian> rt = cq.from(KeteranganPembelian.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
