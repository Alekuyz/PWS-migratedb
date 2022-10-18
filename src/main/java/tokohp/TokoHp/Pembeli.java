/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tokohp.TokoHp;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author AXEL
 */
@Entity
@Table(name = "pembeli")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pembeli.findAll", query = "SELECT p FROM Pembeli p"),
    @NamedQuery(name = "Pembeli.findByIdpembeli", query = "SELECT p FROM Pembeli p WHERE p.idpembeli = :idpembeli"),
    @NamedQuery(name = "Pembeli.findByNamapembeli", query = "SELECT p FROM Pembeli p WHERE p.namapembeli = :namapembeli"),
    @NamedQuery(name = "Pembeli.findByAlamat", query = "SELECT p FROM Pembeli p WHERE p.alamat = :alamat"),
    @NamedQuery(name = "Pembeli.findByNohp", query = "SELECT p FROM Pembeli p WHERE p.nohp = :nohp")})
public class Pembeli implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id_pembeli")
    private String idpembeli;
    @Basic(optional = false)
    @Column(name = "Nama_pembeli")
    private String namapembeli;
    @Basic(optional = false)
    @Column(name = "Alamat")
    private String alamat;
    @Basic(optional = false)
    @Column(name = "No_hp")
    private String nohp;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "pembeli")
    private Penjualan penjualan;

    public Pembeli() {
    }

    public Pembeli(String idpembeli) {
        this.idpembeli = idpembeli;
    }

    public Pembeli(String idpembeli, String namapembeli, String alamat, String nohp) {
        this.idpembeli = idpembeli;
        this.namapembeli = namapembeli;
        this.alamat = alamat;
        this.nohp = nohp;
    }

    public String getIdpembeli() {
        return idpembeli;
    }

    public void setIdpembeli(String idpembeli) {
        this.idpembeli = idpembeli;
    }

    public String getNamapembeli() {
        return namapembeli;
    }

    public void setNamapembeli(String namapembeli) {
        this.namapembeli = namapembeli;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }

    public Penjualan getPenjualan() {
        return penjualan;
    }

    public void setPenjualan(Penjualan penjualan) {
        this.penjualan = penjualan;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idpembeli != null ? idpembeli.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pembeli)) {
            return false;
        }
        Pembeli other = (Pembeli) object;
        if ((this.idpembeli == null && other.idpembeli != null) || (this.idpembeli != null && !this.idpembeli.equals(other.idpembeli))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tokohp.TokoHp.Pembeli[ idpembeli=" + idpembeli + " ]";
    }
    
}
