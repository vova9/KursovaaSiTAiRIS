/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.dbAvtushkoVM;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "email", catalog = "avtushko_v_m", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Email.findAll", query = "SELECT e FROM Email e"),
    @NamedQuery(name = "Email.findByIdemail", query = "SELECT e FROM Email e WHERE e.idemail = :idemail"),
    @NamedQuery(name = "Email.findByLogin", query = "SELECT e FROM Email e WHERE e.login = :login"),
    @NamedQuery(name = "Email.findByPassword", query = "SELECT e FROM Email e WHERE e.password = :password"),
    @NamedQuery(name = "Email.findBySmtp", query = "SELECT e FROM Email e WHERE e.smtp = :smtp"),
    @NamedQuery(name = "Email.findByImap", query = "SELECT e FROM Email e WHERE e.imap = :imap")})
public class Email implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idemail")
    private Integer idemail;
    @Basic(optional = false)
    @Column(name = "login")
    private String login;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @Column(name = "smtp")
    private String smtp;
    @Basic(optional = false)
    @Column(name = "imap")
    private String imap;
    @OneToMany(mappedBy = "idEmailSluzebny", fetch = FetchType.EAGER)
    private List<Polzovateli> polzovateliList;

    public Email() {
    }

    public Email(Integer idemail) {
        this.idemail = idemail;
    }

    public Email(Integer idemail, String login, String password, String smtp, String imap) {
        this.idemail = idemail;
        this.login = login;
        this.password = password;
        this.smtp = smtp;
        this.imap = imap;
    }

    public Integer getIdemail() {
        return idemail;
    }

    public void setIdemail(Integer idemail) {
        this.idemail = idemail;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSmtp() {
        return smtp;
    }

    public void setSmtp(String smtp) {
        this.smtp = smtp;
    }

    public String getImap() {
        return imap;
    }

    public void setImap(String imap) {
        this.imap = imap;
    }

    @XmlTransient
    public List<Polzovateli> getPolzovateliList() {
        return polzovateliList;
    }

    public void setPolzovateliList(List<Polzovateli> polzovateliList) {
        this.polzovateliList = polzovateliList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idemail != null ? idemail.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Email)) {
            return false;
        }
        Email other = (Email) object;
        if ((this.idemail == null && other.idemail != null) || (this.idemail != null && !this.idemail.equals(other.idemail))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "by.kursovaa.dbAvtushkoVM.Email[ idemail=" + idemail + " ]";
    }
}
