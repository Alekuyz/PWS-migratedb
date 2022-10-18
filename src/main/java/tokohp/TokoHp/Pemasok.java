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
import javax.persistence.JoinColumn;
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
@Table(name = "pemasok")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pemasok.findAll", query = "SELECT p FROM Pemasok p"),
    @NamedQuery(name = "Pemasok.findByIdpemasok", query = "SELECT p FROM Pemasok p WHERE p.idpemasok = :idpemasok"),
    @NamedQuery(name = "Pemasok.findByNamapemasok", query = "SELECT p FROM Pemasok p WHERE p.namapemasok = :namapemasok"),
    @NamedQuery(name = "Pemasok.findByAlamat", query = "SELECT p FROM Pemasok p WHERE p.alamat = :alamat"),
    @NamedQuery(name = "Pemasok.findByNohp", query = "SELECT p FROM Pemasok p WHERE p.nohp = :nohp")})
public class Pemasok implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id_pemasok")
    private String idpemasok;
    @Basic(optional = false)
    @Column(name = "Nama_pemasok")
    private String namapemasok;
    @Basic(optional = false)
    @Column(name = "Alamat")
    private String alamat;
    @Basic(optional = false)
    @Column(name = "No_hp")
    private String nohp;
    @JoinColumn(name = "Id_pemasok", referencedColumnName = "Id_barang", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Barang barang;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "pemasok")
    private Pembelian pembelian;

    public Pemasok() {
    }

    public Pemasok(String idpemasok) {
        this.idpemasok = idpemasok;
    }

    public Pemasok(String idpemasok, String namapemasok, String alamat, String nohp) {
        this.idpemasok = idpemasok;
        this.namapemasok = namapemasok;
        this.alamat = alamat;
        this.nohp = nohp;
    }

    public String getIdpemasok() {
        return idpemasok;
    }

    public void setIdpemasok(String idpemasok) {
        this.idpemasok = idpemasok;
    }

    public String getNamapemasok() {
        return namapemasok;
    }

    public void setNamapemasok(String namapemasok) {
        this.namapemasok = namapemasok;
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

    public Barang getBarang() {
        return barang;
    }

    public void setBarang(Barang barang) {
        this.barang = barang;
    }

    public Pembelian getPembelian() {
        return pembelian;
    }

    public void setPembelian(Pembelian pembelian) {
        this.pembelian = pembelian;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idpemasok != null ? idpemasok.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pemasok)) {
            return false;
        }
        Pemasok other = (Pemasok) object;
        if ((this.idpemasok == null && other.idpemasok != null) || (this.idpemasok != null && !this.idpemasok.equals(other.idpemasok))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tokohp.TokoHp.Pemasok[ idpemasok=" + idpemasok + " ]";
    }
    
}
