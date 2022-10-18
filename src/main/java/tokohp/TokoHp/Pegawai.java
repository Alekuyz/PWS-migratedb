/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tokohp.TokoHp;

import java.io.Serializable;
import javax.persistence.Basic;
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
@Table(name = "pegawai")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pegawai.findAll", query = "SELECT p FROM Pegawai p"),
    @NamedQuery(name = "Pegawai.findByIdpegawai", query = "SELECT p FROM Pegawai p WHERE p.idpegawai = :idpegawai"),
    @NamedQuery(name = "Pegawai.findByNamapegawai", query = "SELECT p FROM Pegawai p WHERE p.namapegawai = :namapegawai"),
    @NamedQuery(name = "Pegawai.findByPassword", query = "SELECT p FROM Pegawai p WHERE p.password = :password")})
public class Pegawai implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id_pegawai")
    private String idpegawai;
    @Basic(optional = false)
    @Column(name = "Nama_pegawai")
    private String namapegawai;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @JoinColumn(name = "Id_pegawai", referencedColumnName = "Id_barang", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private KeteranganPembelian keteranganPembelian;

    public Pegawai() {
    }

    public Pegawai(String idpegawai) {
        this.idpegawai = idpegawai;
    }

    public Pegawai(String idpegawai, String namapegawai, String password) {
        this.idpegawai = idpegawai;
        this.namapegawai = namapegawai;
        this.password = password;
    }

    public String getIdpegawai() {
        return idpegawai;
    }

    public void setIdpegawai(String idpegawai) {
        this.idpegawai = idpegawai;
    }

    public String getNamapegawai() {
        return namapegawai;
    }

    public void setNamapegawai(String namapegawai) {
        this.namapegawai = namapegawai;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public KeteranganPembelian getKeteranganPembelian() {
        return keteranganPembelian;
    }

    public void setKeteranganPembelian(KeteranganPembelian keteranganPembelian) {
        this.keteranganPembelian = keteranganPembelian;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idpegawai != null ? idpegawai.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pegawai)) {
            return false;
        }
        Pegawai other = (Pegawai) object;
        if ((this.idpegawai == null && other.idpegawai != null) || (this.idpegawai != null && !this.idpegawai.equals(other.idpegawai))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tokohp.TokoHp.Pegawai[ idpegawai=" + idpegawai + " ]";
    }
    
}
