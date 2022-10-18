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
@Table(name = "penjualan")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Penjualan.findAll", query = "SELECT p FROM Penjualan p"),
    @NamedQuery(name = "Penjualan.findByIdnota", query = "SELECT p FROM Penjualan p WHERE p.idnota = :idnota"),
    @NamedQuery(name = "Penjualan.findByIdPembeli", query = "SELECT p FROM Penjualan p WHERE p.idPembeli = :idPembeli")})
public class Penjualan implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id_nota")
    private String idnota;
    @Basic(optional = false)
    @Column(name = "id_pembeli")
    private String idPembeli;
    @JoinColumn(name = "Id_nota", referencedColumnName = "Id_pembeli", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Pembeli pembeli;

    public Penjualan() {
    }

    public Penjualan(String idnota) {
        this.idnota = idnota;
    }

    public Penjualan(String idnota, String idPembeli) {
        this.idnota = idnota;
        this.idPembeli = idPembeli;
    }

    public String getIdnota() {
        return idnota;
    }

    public void setIdnota(String idnota) {
        this.idnota = idnota;
    }

    public String getIdPembeli() {
        return idPembeli;
    }

    public void setIdPembeli(String idPembeli) {
        this.idPembeli = idPembeli;
    }

    public Pembeli getPembeli() {
        return pembeli;
    }

    public void setPembeli(Pembeli pembeli) {
        this.pembeli = pembeli;
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
        if (!(object instanceof Penjualan)) {
            return false;
        }
        Penjualan other = (Penjualan) object;
        if ((this.idnota == null && other.idnota != null) || (this.idnota != null && !this.idnota.equals(other.idnota))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tokohp.TokoHp.Penjualan[ idnota=" + idnota + " ]";
    }
    
}
