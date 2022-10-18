/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tokohp.TokoHp;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author AXEL
 */
@Entity
@Table(name = "pembelian")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pembelian.findAll", query = "SELECT p FROM Pembelian p"),
    @NamedQuery(name = "Pembelian.findByIdnota", query = "SELECT p FROM Pembelian p WHERE p.idnota = :idnota"),
    @NamedQuery(name = "Pembelian.findByIdpemasok", query = "SELECT p FROM Pembelian p WHERE p.idpemasok = :idpemasok"),
    @NamedQuery(name = "Pembelian.findByTanggal", query = "SELECT p FROM Pembelian p WHERE p.tanggal = :tanggal")})
public class Pembelian implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id_nota")
    private String idnota;
    @Basic(optional = false)
    @Column(name = "Id_pemasok")
    private String idpemasok;
    @Basic(optional = false)
    @Column(name = "tanggal")
    @Temporal(TemporalType.DATE)
    private Date tanggal;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "pembelian")
    private KeteranganPembelian keteranganPembelian;
    @JoinColumn(name = "Id_nota", referencedColumnName = "Id_pemasok", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Pemasok pemasok;

    public Pembelian() {
    }

    public Pembelian(String idnota) {
        this.idnota = idnota;
    }

    public Pembelian(String idnota, String idpemasok, Date tanggal) {
        this.idnota = idnota;
        this.idpemasok = idpemasok;
        this.tanggal = tanggal;
    }

    public String getIdnota() {
        return idnota;
    }

    public void setIdnota(String idnota) {
        this.idnota = idnota;
    }

    public String getIdpemasok() {
        return idpemasok;
    }

    public void setIdpemasok(String idpemasok) {
        this.idpemasok = idpemasok;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public KeteranganPembelian getKeteranganPembelian() {
        return keteranganPembelian;
    }

    public void setKeteranganPembelian(KeteranganPembelian keteranganPembelian) {
        this.keteranganPembelian = keteranganPembelian;
    }

    public Pemasok getPemasok() {
        return pemasok;
    }

    public void setPemasok(Pemasok pemasok) {
        this.pemasok = pemasok;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idnota != null ? idnota.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pembelian)) {
            return false;
        }
        Pembelian other = (Pembelian) object;
        if ((this.idnota == null && other.idnota != null) || (this.idnota != null && !this.idnota.equals(other.idnota))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tokohp.TokoHp.Pembelian[ idnota=" + idnota + " ]";
    }
    
}
