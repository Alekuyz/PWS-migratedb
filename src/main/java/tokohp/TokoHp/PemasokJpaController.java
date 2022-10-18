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
public class PemasokJpaController implements Serializable {

    public PemasokJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pemasok pemasok) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Barang barangOrphanCheck = pemasok.getBarang();
        if (barangOrphanCheck != null) {
            Pemasok oldPemasokOfBarang = barangOrphanCheck.getPemasok();
            if (oldPemasokOfBarang != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Barang " + barangOrphanCheck + " already has an item of type Pemasok whose barang column cannot be null. Please make another selection for the barang field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Barang barang = pemasok.getBarang();
            if (barang != null) {
                barang = em.getReference(barang.getClass(), barang.getIdbarang());
                pemasok.setBarang(barang);
            }
            Pembelian pembelian = pemasok.getPembelian();
            if (pembelian != null) {
                pembelian = em.getReference(pembelian.getClass(), pembelian.getIdnota());
                pemasok.setPembelian(pembelian);
            }
            em.persist(pemasok);
            if (barang != null) {
                barang.setPemasok(pemasok);
                barang = em.merge(barang);
            }
            if (pembelian != null) {
                Pemasok oldPemasokOfPembelian = pembelian.getPemasok();
                if (oldPemasokOfPembelian != null) {
                    oldPemasokOfPembelian.setPembelian(null);
                    oldPemasokOfPembelian = em.merge(oldPemasokOfPembelian);
                }
                pembelian.setPemasok(pemasok);
                pembelian = em.merge(pembelian);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPemasok(pemasok.getIdpemasok()) != null) {
                throw new PreexistingEntityException("Pemasok " + pemasok + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pemasok pemasok) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pemasok persistentPemasok = em.find(Pemasok.class, pemasok.getIdpemasok());
            Barang barangOld = persistentPemasok.getBarang();
            Barang barangNew = pemasok.getBarang();
            Pembelian pembelianOld = persistentPemasok.getPembelian();
            Pembelian pembelianNew = pemasok.getPembelian();
            List<String> illegalOrphanMessages = null;
            if (barangNew != null && !barangNew.equals(barangOld)) {
                Pemasok oldPemasokOfBarang = barangNew.getPemasok();
                if (oldPemasokOfBarang != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Barang " + barangNew + " already has an item of type Pemasok whose barang column cannot be null. Please make another selection for the barang field.");
                }
            }
            if (pembelianOld != null && !pembelianOld.equals(pembelianNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Pembelian " + pembelianOld + " since its pemasok field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (barangNew != null) {
                barangNew = em.getReference(barangNew.getClass(), barangNew.getIdbarang());
                pemasok.setBarang(barangNew);
            }
            if (pembelianNew != null) {
                pembelianNew = em.getReference(pembelianNew.getClass(), pembelianNew.getIdnota());
                pemasok.setPembelian(pembelianNew);
            }
            pemasok = em.merge(pemasok);
            if (barangOld != null && !barangOld.equals(barangNew)) {
                barangOld.setPemasok(null);
                barangOld = em.merge(barangOld);
            }
            if (barangNew != null && !barangNew.equals(barangOld)) {
                barangNew.setPemasok(pemasok);
                barangNew = em.merge(barangNew);
            }
            if (pembelianNew != null && !pembelianNew.equals(pembelianOld)) {
                Pemasok oldPemasokOfPembelian = pembelianNew.getPemasok();
                if (oldPemasokOfPembelian != null) {
                    oldPemasokOfPembelian.setPembelian(null);
                    oldPemasokOfPembelian = em.merge(oldPemasokOfPembelian);
                }
                pembelianNew.setPemasok(pemasok);
                pembelianNew = em.merge(pembelianNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = pemasok.getIdpemasok();
                if (findPemasok(id) == null) {
                    throw new NonexistentEntityException("The pemasok with id " + id + " no longer exists.");
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
            Pemasok pemasok;
            try {
                pemasok = em.getReference(Pemasok.class, id);
                pemasok.getIdpemasok();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pemasok with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Pembelian pembelianOrphanCheck = pemasok.getPembelian();
            if (pembelianOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pemasok (" + pemasok + ") cannot be destroyed since the Pembelian " + pembelianOrphanCheck + " in its pembelian field has a non-nullable pemasok field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Barang barang = pemasok.getBarang();
            if (barang != null) {
                barang.setPemasok(null);
                barang = em.merge(barang);
            }
            em.remove(pemasok);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pemasok> findPemasokEntities() {
        return findPemasokEntities(true, -1, -1);
    }

    public List<Pemasok> findPemasokEntities(int maxResults, int firstResult) {
        return findPemasokEntities(false, maxResults, firstResult);
    }

    private List<Pemasok> findPemasokEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pemasok.class));
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

    public Pemasok findPemasok(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pemasok.class, id);
        } finally {
            em.close();
        }
    }

    public int getPemasokCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pemasok> rt = cq.from(Pemasok.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
