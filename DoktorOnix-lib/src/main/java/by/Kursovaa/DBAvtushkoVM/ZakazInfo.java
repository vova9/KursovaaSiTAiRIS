/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.dbAvtushkoVM;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Vladimir
 */
@Entity
@Table(name = "zakaz_info", catalog = "avtushko_v_m", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZakazInfo.findAll", query = "SELECT z FROM ZakazInfo z"),
    @NamedQuery(name = "ZakazInfo.findByIdzakazInfo", query = "SELECT z FROM ZakazInfo z WHERE z.idzakazInfo = :idzakazInfo"),
    @NamedQuery(name = "ZakazInfo.findByZena", query = "SELECT z FROM ZakazInfo z WHERE z.zena = :zena"),
    @NamedQuery(name = "ZakazInfo.findBySebestoimost", query = "SELECT z FROM ZakazInfo z WHERE z.sebestoimost = :sebestoimost"),
    @NamedQuery(name = "ZakazInfo.findByKolichestovo", query = "SELECT z FROM ZakazInfo z WHERE z.kolichestovo = :kolichestovo")})
public class ZakazInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idzakaz_info")
    private Integer idzakazInfo;
    @Basic(optional = false)
    @Column(name = "zena")
    private double zena;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "sebestoimost")
    private Double sebestoimost;
    @Column(name = "kolichestovo")
    private Integer kolichestovo;
    @JoinColumn(name = "tovary", referencedColumnName = "artikul")
    @ManyToOne(optional = false)
    private Tovary tovary;
    @JoinColumn(name = "id_zakat", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Zakazy idZakat;

    public ZakazInfo() {
    }

    public ZakazInfo(Integer idzakazInfo) {
        this.idzakazInfo = idzakazInfo;
    }

    public ZakazInfo(Integer idzakazInfo, double zena) {
        this.idzakazInfo = idzakazInfo;
        this.zena = zena;
    }

    public Integer getIdzakazInfo() {
        return idzakazInfo;
    }

    public void setIdzakazInfo(Integer idzakazInfo) {
        this.idzakazInfo = idzakazInfo;
    }

    public double getZena() {
        return zena;
    }

    public void setZena(double zena) {
        this.zena = zena;
    }

    public Double getSebestoimost() {
        return sebestoimost;
    }

    public void setSebestoimost(Double sebestoimost) {
        this.sebestoimost = sebestoimost;
    }

    public Integer getKolichestovo() {
        return kolichestovo;
    }

    public void setKolichestovo(Integer kolichestovo) {
        this.kolichestovo = kolichestovo;
    }

    public Tovary getTovary() {
        return tovary;
    }

    public void setTovary(Tovary tovary) {
        this.tovary = tovary;
    }

    public Zakazy getIdZakat() {
        return idZakat;
    }

    public void setIdZakat(Zakazy idZakat) {
        this.idZakat = idZakat;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idzakazInfo != null ? idzakazInfo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ZakazInfo)) {
            return false;
        }
        ZakazInfo other = (ZakazInfo) object;
        if ((this.idzakazInfo == null && other.idzakazInfo != null) || (this.idzakazInfo != null && !this.idzakazInfo.equals(other.idzakazInfo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "by.kursovaa.dbAvtushkoVM.ZakazInfo[ idzakazInfo=" + idzakazInfo + " ]";
    }
}
