/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.Kursovaa.DBAvtushkoVM;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Vladimir
 */
@Entity
@Table(name = "klienty", catalog = "avtushko_v_m", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Klienty.findAll", query = "SELECT k FROM Klienty k"),
    @NamedQuery(name = "Klienty.findByTelefon", query = "SELECT k FROM Klienty k WHERE k.telefon = :telefon"),
    @NamedQuery(name = "Klienty.findByFamilia", query = "SELECT k FROM Klienty k WHERE k.familia = :familia"),
    @NamedQuery(name = "Klienty.findByIma", query = "SELECT k FROM Klienty k WHERE k.ima = :ima"),
    @NamedQuery(name = "Klienty.findByEmail", query = "SELECT k FROM Klienty k WHERE k.email = :email"),
    @NamedQuery(name = "Klienty.findByGorod", query = "SELECT k FROM Klienty k WHERE k.gorod = :gorod"),
    @NamedQuery(name = "Klienty.findByNomerSklada", query = "SELECT k FROM Klienty k WHERE k.nomerSklada = :nomerSklada"),
    @NamedQuery(name = "Klienty.findByZip", query = "SELECT k FROM Klienty k WHERE k.zip = :zip"),
    @NamedQuery(name = "Klienty.findByAdress", query = "SELECT k FROM Klienty k WHERE k.adress = :adress")})
public class Klienty implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "telefon")
    private String telefon;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "familia")
    private String familia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "ima")
    private String ima;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Недопустимый адрес электронной почты")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 50)
    @Column(name = "email")
    private String email;
    @Size(max = 100)
    @Column(name = "gorod")
    private String gorod;
    @Column(name = "nomer_sklada")
    private Integer nomerSklada;
    @Column(name = "zip")
    private Integer zip;
    @Size(max = 150)
    @Column(name = "adress")
    private String adress;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "telefon")
    private List<Zakazy> zakazyList;

    public Klienty() {
    }

    public Klienty(String telefon) {
        this.telefon = telefon;
    }

    public Klienty(String telefon, String familia, String ima) {
        this.telefon = telefon;
        this.familia = familia;
        this.ima = ima;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getFamilia() {
        return familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public String getIma() {
        return ima;
    }

    public void setIma(String ima) {
        this.ima = ima;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGorod() {
        return gorod;
    }

    public void setGorod(String gorod) {
        this.gorod = gorod;
    }

    public Integer getNomerSklada() {
        return nomerSklada;
    }

    public void setNomerSklada(Integer nomerSklada) {
        this.nomerSklada = nomerSklada;
    }

    public Integer getZip() {
        return zip;
    }

    public void setZip(Integer zip) {
        this.zip = zip;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    @XmlTransient
    public List<Zakazy> getZakazyList() {
        return zakazyList;
    }

    public void setZakazyList(List<Zakazy> zakazyList) {
        this.zakazyList = zakazyList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (telefon != null ? telefon.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Klienty)) {
            return false;
        }
        Klienty other = (Klienty) object;
        if ((this.telefon == null && other.telefon != null) || (this.telefon != null && !this.telefon.equals(other.telefon))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "by.Kursovaa.DBAvtushkoVM.Klienty[ telefon=" + telefon + " ]";
    }
    
}
