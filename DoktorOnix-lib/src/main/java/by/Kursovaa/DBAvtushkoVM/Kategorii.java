/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.dbAvtushkoVM;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Vladimir
 */
@Entity
@Table(name = "kategorii", catalog = "avtushko_v_m", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Kategorii.findAll", query = "SELECT k FROM Kategorii k"),
    @NamedQuery(name = "Kategorii.findById", query = "SELECT k FROM Kategorii k WHERE k.id = :id"),
    @NamedQuery(name = "Kategorii.findByName", query = "SELECT k FROM Kategorii k WHERE k.name = :name"),
    @NamedQuery(name = "Kategorii.findByRoditel", query = "SELECT k FROM Kategorii k WHERE k.roditel = :roditel")})
public class Kategorii implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "roditel")
    private int roditel;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "kategoria")
    private List<Tovary> tovaryList;

    public Kategorii() {
    }

    public Kategorii(Integer id) {
        this.id = id;
    }

    public Kategorii(Integer id, String name, int roditel) {
        this.id = id;
        this.name = name;
        this.roditel = roditel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRoditel() {
        return roditel;
    }

    public void setRoditel(int roditel) {
        this.roditel = roditel;
    }

    @XmlTransient
    public List<Tovary> getTovaryList() {
        return tovaryList;
    }

    public void setTovaryList(List<Tovary> tovaryList) {
        this.tovaryList = tovaryList;
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
        if (!(object instanceof Kategorii)) {
            return false;
        }
        Kategorii other = (Kategorii) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "by.kursovaa.dbAvtushkoVM.Kategorii[ id=" + id + " ]";
    }
}
