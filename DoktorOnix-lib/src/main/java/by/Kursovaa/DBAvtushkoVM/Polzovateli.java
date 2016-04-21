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
@Table(name = "polzovateli", catalog = "avtushko_v_m", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Polzovateli.findAll", query = "SELECT p FROM Polzovateli p"),
    @NamedQuery(name = "Polzovateli.findById", query = "SELECT p FROM Polzovateli p WHERE p.id = :id"),
    @NamedQuery(name = "Polzovateli.findByLogin", query = "SELECT p FROM Polzovateli p WHERE p.login = :login"),
    @NamedQuery(name = "Polzovateli.findByParol", query = "SELECT p FROM Polzovateli p WHERE p.parol = :parol"),
    @NamedQuery(name = "Polzovateli.findByFamilia", query = "SELECT p FROM Polzovateli p WHERE p.familia = :familia"),
    @NamedQuery(name = "Polzovateli.findByIma", query = "SELECT p FROM Polzovateli p WHERE p.ima = :ima"),
    @NamedQuery(name = "Polzovateli.findByAdmin", query = "SELECT p FROM Polzovateli p WHERE p.admin = :admin"),
    @NamedQuery(name = "Polzovateli.findByAktiven", query = "SELECT p FROM Polzovateli p WHERE p.aktiven = :aktiven"),
    @NamedQuery(name = "Polzovateli.findByEmail", query = "SELECT p FROM Polzovateli p WHERE p.email = :email"),
    @NamedQuery(name = "Polzovateli.findByTelefon", query = "SELECT p FROM Polzovateli p WHERE p.telefon = :telefon")})
public class Polzovateli implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "login")
    private String login;
    @Basic(optional = false)
    @Column(name = "parol")
    private String parol;
    @Column(name = "familia")
    private String familia;
    @Column(name = "ima")
    private String ima;
    @Basic(optional = false)
    @Column(name = "admin")
    private int admin;
    @Basic(optional = false)
    @Column(name = "aktiven")
    private int aktiven;
    @Column(name = "email")
    private String email;
    @Column(name = "telefon")
    private String telefon;
    @JoinColumn(name = "idEmailSluzebny", referencedColumnName = "idemail")
    @ManyToOne
    private Email idEmailSluzebny;

    public Polzovateli() {
    }

    public Polzovateli(Integer id) {
        this.id = id;
    }

    public Polzovateli(Integer id, String login, String parol, int admin, int aktiven) {
        this.id = id;
        this.login = login;
        this.parol = parol;
        this.admin = admin;
        this.aktiven = aktiven;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getParol() {
        return parol;
    }

    public void setParol(String parol) {
        this.parol = parol;
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

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public int getAktiven() {
        return aktiven;
    }

    public void setAktiven(int aktiven) {
        this.aktiven = aktiven;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public Email getIdEmailSluzebny() {
        return idEmailSluzebny;
    }

    public void setIdEmailSluzebny(Email idEmailSluzebny) {
        this.idEmailSluzebny = idEmailSluzebny;
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
        if (!(object instanceof Polzovateli)) {
            return false;
        }
        Polzovateli other = (Polzovateli) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "by.kursovaa.dbAvtushkoVM.Polzovateli[ id=" + id + " ]";
    }

}
