/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "login", nullable = false, length = 45)
    private String login;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "parol", nullable = false, length = 45)
    private String parol;

    @Size(max = 45)
    @Column(name = "familia", length = 45)
    private String familia;

    @Size(max = 45)
    @Column(name = "ima", length = 45)
    private String ima;

    @Basic(optional = false)
    @NotNull
    @Column(name = "admin", nullable = false)
    private int admin;

    @Basic(optional = false)
    @NotNull
    @Column(name = "aktiven", nullable = false)
    private int aktiven;

// @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Недопустимый адрес электронной почты")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 50)
    @Column(name = "email", length = 50)
    private String email;

    @Size(max = 12)
    @Column(name = "telefon", length = 12)
    private String telefon;

    @JoinColumn(name = "idEmailSluzebny", referencedColumnName = "idemail")
    @ManyToOne(fetch = FetchType.EAGER)
    private Email idEmailSluzebny;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userid", fetch = FetchType.EAGER)
    private List<UserRoles> userRolesList;

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

    @XmlTransient
    public List<UserRoles> getUserRolesList() {
        return userRolesList;
    }

    public void setUserRolesList(List<UserRoles> userRolesList) {
        this.userRolesList = userRolesList;
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
        return "by.kursovaa.entity.Polzovateli[ id=" + id + " ]";
    }

}
