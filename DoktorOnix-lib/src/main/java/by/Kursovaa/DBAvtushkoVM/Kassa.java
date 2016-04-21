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
@Table(name = "kassa", catalog = "avtushko_v_m", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Kassa.findAll", query = "SELECT k FROM Kassa k"),
    @NamedQuery(name = "Kassa.findById", query = "SELECT k FROM Kassa k WHERE k.id = :id"),
    @NamedQuery(name = "Kassa.findByTip", query = "SELECT k FROM Kassa k WHERE k.tip = :tip"),
    @NamedQuery(name = "Kassa.findBySumma", query = "SELECT k FROM Kassa k WHERE k.summa = :summa"),
    @NamedQuery(name = "Kassa.findByDetali", query = "SELECT k FROM Kassa k WHERE k.detali = :detali"),
    @NamedQuery(name = "Kassa.findByDate", query = "SELECT k FROM Kassa k WHERE k.date = :date")})
public class Kassa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "tip")
    private String tip;
    @Basic(optional = false)
    @Column(name = "summa")
    private double summa;
    @Column(name = "detali")
    private String detali;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;

    public Kassa() {
    }

    public Kassa(Integer id) {
        this.id = id;
    }

    public Kassa(Integer id, String tip, double summa, Date date) {
        this.id = id;
        this.tip = tip;
        this.summa = summa;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public double getSumma() {
        return summa;
    }

    public void setSumma(double summa) {
        this.summa = summa;
    }

    public String getDetali() {
        return detali;
    }

    public void setDetali(String detali) {
        this.detali = detali;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Kassa)) {
            return false;
        }
        Kassa other = (Kassa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "by.kursovaa.dbAvtushkoVM.Kassa[ id=" + id + " ]";
    }
}
