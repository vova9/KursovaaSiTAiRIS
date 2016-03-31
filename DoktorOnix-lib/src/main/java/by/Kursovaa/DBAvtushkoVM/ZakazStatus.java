/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.dbAvtushkoVM;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Vladimir
 */
@Entity
@Table(name = "zakaz_status", catalog = "avtushko_v_m", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZakazStatus.findAll", query = "SELECT z FROM ZakazStatus z"),
    @NamedQuery(name = "ZakazStatus.findByIdzakazStatus", query = "SELECT z FROM ZakazStatus z WHERE z.idzakazStatus = :idzakazStatus"),
    @NamedQuery(name = "ZakazStatus.findByStatus", query = "SELECT z FROM ZakazStatus z WHERE z.status = :status"),
    @NamedQuery(name = "ZakazStatus.findByData", query = "SELECT z FROM ZakazStatus z WHERE z.data = :data")})
public class ZakazStatus implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idzakaz_status")
    private Integer idzakazStatus;
    @Column(name = "status")
    private String status;
    @Column(name = "data")
    @Temporal(TemporalType.DATE)
    private Date data;
    @JoinColumn(name = "id_zakaz", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Zakazy idZakaz;

    public ZakazStatus() {
    }

    public ZakazStatus(Integer idzakazStatus) {
        this.idzakazStatus = idzakazStatus;
    }

    public Integer getIdzakazStatus() {
        return idzakazStatus;
    }

    public void setIdzakazStatus(Integer idzakazStatus) {
        this.idzakazStatus = idzakazStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Zakazy getIdZakaz() {
        return idZakaz;
    }

    public void setIdZakaz(Zakazy idZakaz) {
        this.idZakaz = idZakaz;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idzakazStatus != null ? idzakazStatus.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ZakazStatus)) {
            return false;
        }
        ZakazStatus other = (ZakazStatus) object;
        if ((this.idzakazStatus == null && other.idzakazStatus != null) || (this.idzakazStatus != null && !this.idzakazStatus.equals(other.idzakazStatus))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "by.kursovaa.dbAvtushkoVM.ZakazStatus[ idzakazStatus=" + idzakazStatus + " ]";
    }
    
}
