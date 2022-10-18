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
@Table(name = "keterangan pembelian")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KeteranganPembelian.findAll", query = "SELECT k FROM KeteranganPembelian k"),
    @NamedQuery(name = "KeteranganPembelian.findByIdbarang", query = "SELECT k FROM KeteranganPembelian k WHERE k.idbarang = :idbarang"),
    @NamedQuery(name = "KeteranganPembelian.findByJumlahbarang", query = "SELECT k FROM KeteranganPembelian k WHERE k.jumlahbarang = :jumlahbarang"),
    @NamedQuery(name = "KeteranganPembelian.findByHarga", query = "SELECT k FROM KeteranganPembelian k WHERE k.harga = :harga"),
    @NamedQuery(name = "KeteranganPembelian.findByIdnota", query = "SELECT k FROM KeteranganPembelian k WHERE k.idnota = :idnota"),
    @NamedQuery(name = "KeteranganPembelian.findByIdPegawai", query = "SELECT k FROM KeteranganPembelian k WHERE k.idPegawai = :idPegawai")})
public class KeteranganPembelian implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id_barang")
    private String idbarang;
    @Basic(optional = false)
    @Column(name = "Jumlah_barang")
    private String jumlahbarang;
    @Basic(optional = false)
    @Column(name = "Harga")
    private String harga;
    @Basic(optional = false)
    @Column(name = "Id_nota")
    private String idnota;
    @Basic(optional = false)
    @Column(name = "id_pegawai")
    private String idPegawai;
    @JoinColumn(name = "Id_barang", referencedColumnName = "Id_nota", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Pembelian pembelian;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "keteranganPembelian")
    private Pegawai pegawai;

    public KeteranganPembelian() {
    }

    public KeteranganPembelian(String idbarang) {
        this.idbarang = idbarang;
    }

    public KeteranganPembelian(String idbarang, String jumlahbarang, String harga, String idnota, String idPegawai) {
        this.idbarang = idbarang;
        this.jumlahbarang = jumlahbarang;
        this.harga = harga;
        this.idnota = idnota;
        this.idPegawai = idPegawai;
    }

    public String getIdbarang() {
        return idbarang;
    }

    public void setIdbarang(String idbarang) {
        this.idbarang = idbarang;
    }

    public String getJumlahbarang() {
        return jumlahbarang;
    }

    public void setJumlahbarang(String jumlahbarang) {
        this.jumlahbarang = jumlahbarang;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getIdnota() {
        return idnota;
    }

    public void setIdnota(String idnota) {
        this.idnota = idnota;
    }

    public String getIdPegawai() {
        return idPegawai;
    }

    public void setIdPegawai(String idPegawai) {
        this.idPegawai = idPegawai;
    }

    public Pembelian getPembelian() {
        return pembelian;
    }

    public void setPembelian(Pembelian pembelian) {
        this.pembelian = pembelian;
    }

    public Pegawai getPegawai() {
        return pegawai;
    }

    public void setPegawai(Pegawai pegawai) {
        this.pegawai = pegawai;
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
        if (!(object instanceof KeteranganPembelian)) {
            return false;
        }
        KeteranganPembelian other = (KeteranganPembelian) object;
        if ((this.idbarang == null && other.idbarang != null) || (this.idbarang != null && !this.idbarang.equals(other.idbarang))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tokohp.TokoHp.KeteranganPembelian[ idbarang=" + idbarang + " ]";
    }
    
}
