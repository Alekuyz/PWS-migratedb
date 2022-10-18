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
@Table(name = "barang")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Barang.findAll", query = "SELECT b FROM Barang b"),
    @NamedQuery(name = "Barang.findByIdbarang", query = "SELECT b FROM Barang b WHERE b.idbarang = :idbarang"),
    @NamedQuery(name = "Barang.findByNamabarang", query = "SELECT b FROM Barang b WHERE b.namabarang = :namabarang"),
    @NamedQuery(name = "Barang.findByHarga", query = "SELECT b FROM Barang b WHERE b.harga = :harga"),
    @NamedQuery(name = "Barang.findByIdpemasok", query = "SELECT b FROM Barang b WHERE b.idpemasok = :idpemasok")})
public class Barang implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id_barang")
    private String idbarang;
    @Basic(optional = false)
    @Column(name = "Nama_barang")
    private String namabarang;
    @Basic(optional = false)
    @Column(name = "Harga")
    private String harga;
    @Basic(optional = false)
    @Column(name = "Id_pemasok")
    private String idpemasok;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "barang")
    private Pemasok pemasok;

    public Barang() {
    }

    public Barang(String idbarang) {
        this.idbarang = idbarang;
    }

    public Barang(String idbarang, String namabarang, String harga, String idpemasok) {
        this.idbarang = idbarang;
        this.namabarang = namabarang;
        this.harga = harga;
        this.idpemasok = idpemasok;
    }

    public String getIdbarang() {
        return idbarang;
    }

    public void setIdbarang(String idbarang) {
        this.idbarang = idbarang;
    }

    public String getNamabarang() {
        return namabarang;
    }

    public void setNamabarang(String namabarang) {
        this.namabarang = namabarang;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getIdpemasok() {
        return idpemasok;
    }

    public void setIdpemasok(String idpemasok) {
        this.idpemasok = idpemasok;
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
        hash += (idbarang != null ? idbarang.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Barang)) {
            return false;
        }
        Barang other = (Barang) object;
        if ((this.idbarang == null && other.idbarang != null) || (this.idbarang != null && !this.idbarang.equals(other.idbarang))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tokohp.TokoHp.Barang[ idbarang=" + idbarang + " ]";
    }
    
}
