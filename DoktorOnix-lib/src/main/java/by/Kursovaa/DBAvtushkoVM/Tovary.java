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
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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
@Table(name = "tovary", catalog = "avtushko_v_m", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tovary.findAll", query = "SELECT t FROM Tovary t"),
    @NamedQuery(name = "Tovary.findByArtikul", query = "SELECT t FROM Tovary t WHERE t.artikul = :artikul"),
    @NamedQuery(name = "Tovary.findByNaimenovanie", query = "SELECT t FROM Tovary t WHERE t.naimenovanie = :naimenovanie"),
    @NamedQuery(name = "Tovary.findByZena", query = "SELECT t FROM Tovary t WHERE t.zena = :zena")})
public class Tovary implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "artikul")
    private Integer artikul;
    @Basic(optional = false)
    @Column(name = "naimenovanie")
    private String naimenovanie;
    @Basic(optional = false)
    @Column(name = "zena")
    private float zena;
    @Lob
    @Column(name = "opisanie")
    private String opisanie;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tovary")
    private List<ZakazInfo> zakazInfoList;
    @JoinColumn(name = "Kategoria", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Kategorii kategoria;

    public Tovary() {
    }

    public Tovary(Integer artikul) {
        this.artikul = artikul;
    }

    public Tovary(Integer artikul, String naimenovanie, float zena) {
        this.artikul = artikul;
        this.naimenovanie = naimenovanie;
        this.zena = zena;
    }

    public Integer getArtikul() {
        return artikul;
    }

    public void setArtikul(Integer artikul) {
        this.artikul = artikul;
    }

    public String getNaimenovanie() {
        return naimenovanie;
    }

    public void setNaimenovanie(String naimenovanie) {
        this.naimenovanie = naimenovanie;
    }

    public float getZena() {
        return zena;
    }

    public void setZena(float zena) {
        this.zena = zena;
    }

    public String getOpisanie() {
        return opisanie;
    }

    public void setOpisanie(String opisanie) {
        this.opisanie = opisanie;
    }

    @XmlTransient
    public List<ZakazInfo> getZakazInfoList() {
        return zakazInfoList;
    }

    public void setZakazInfoList(List<ZakazInfo> zakazInfoList) {
        this.zakazInfoList = zakazInfoList;
    }

    public Kategorii getKategoria() {
        return kategoria;
    }

    public void setKategoria(Kategorii kategoria) {
        this.kategoria = kategoria;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (artikul != null ? artikul.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tovary)) {
            return false;
        }
        Tovary other = (Tovary) object;
        if ((this.artikul == null && other.artikul != null) || (this.artikul != null && !this.artikul.equals(other.artikul))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "by.kursovaa.dbAvtushkoVM.Tovary[ artikul=" + artikul + " ]";
    }
}
