/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.dbAvtushkoVM;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Vladimir
 */
@Entity
@Table(name = "zakazy", catalog = "avtushko_v_m", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Zakazy.findAll", query = "SELECT z FROM Zakazy z"),
    @NamedQuery(name = "Zakazy.findById", query = "SELECT z FROM Zakazy z WHERE z.id = :id"),
    @NamedQuery(name = "Zakazy.findByDataZakaza", query = "SELECT z FROM Zakazy z WHERE z.dataZakaza = :dataZakaza"),
    @NamedQuery(name = "Zakazy.findByStatus", query = "SELECT z FROM Zakazy z WHERE z.status = :status"),
    @NamedQuery(name = "Zakazy.findBySumma", query = "SELECT z FROM Zakazy z WHERE z.summa = :summa"),
    @NamedQuery(name = "Zakazy.findByPribyl", query = "SELECT z FROM Zakazy z WHERE z.pribyl = :pribyl"),
    @NamedQuery(name = "Zakazy.findByDataOtpravki", query = "SELECT z FROM Zakazy z WHERE z.dataOtpravki = :dataOtpravki"),
    @NamedQuery(name = "Zakazy.findByNakladnaa", query = "SELECT z FROM Zakazy z WHERE z.nakladnaa = :nakladnaa"),
    @NamedQuery(name = "Zakazy.findByIstochnik", query = "SELECT z FROM Zakazy z WHERE z.istochnik = :istochnik"),
    @NamedQuery(name = "Zakazy.findByOplata", query = "SELECT z FROM Zakazy z WHERE z.oplata = :oplata"),
    @NamedQuery(name = "Zakazy.findBySkidka", query = "SELECT z FROM Zakazy z WHERE z.skidka = :skidka")})
public class Zakazy implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "data_zakaza")
    @Temporal(TemporalType.DATE)
    private Date dataZakaza;
    @Column(name = "status")
    private String status;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "summa")
    private Double summa;
    @Column(name = "pribyl")
    private Double pribyl;
    @Column(name = "data_otpravki")
    @Temporal(TemporalType.DATE)
    private Date dataOtpravki;
    @Column(name = "nakladnaa")
    private String nakladnaa;
    @Lob
    @Column(name = "zamechanie")
    private String zamechanie;
    @Basic(optional = false)
    @Column(name = "istochnik")
    private String istochnik;
    @Column(name = "oplata")
    private Integer oplata;
    @Column(name = "skidka")
    private Double skidka;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idZakat", fetch = FetchType.EAGER)
    private List<ZakazInfo> zakazInfoList;
    @JoinColumn(name = "telefon", referencedColumnName = "telefon")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Klienty telefon;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idZakaz", fetch = FetchType.EAGER)
    private List<ZakazStatus> zakazStatusList;

    public Zakazy() {
    }

    public Zakazy(Integer id) {
        this.id = id;
    }

    public Zakazy(Integer id, String istochnik) {
        this.id = id;
        this.istochnik = istochnik;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataZakaza() {
        return dataZakaza;
    }

    public void setDataZakaza(Date dataZakaza) {
        this.dataZakaza = dataZakaza;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getSumma() {
        return summa;
    }

    public void setSumma(Double summa) {
        this.summa = summa;
    }

    public Double getPribyl() {
        return pribyl;
    }

    public void setPribyl(Double pribyl) {
        this.pribyl = pribyl;
    }

    public Date getDataOtpravki() {
        return dataOtpravki;
    }

    public void setDataOtpravki(Date dataOtpravki) {
        this.dataOtpravki = dataOtpravki;
    }

    public String getNakladnaa() {
        return nakladnaa;
    }

    public void setNakladnaa(String nakladnaa) {
        this.nakladnaa = nakladnaa;
    }

    public String getZamechanie() {
        return zamechanie;
    }

    public void setZamechanie(String zamechanie) {
        this.zamechanie = zamechanie;
    }

    public String getIstochnik() {
        return istochnik;
    }

    public void setIstochnik(String istochnik) {
        this.istochnik = istochnik;
    }

    public Integer getOplata() {
        return oplata;
    }

    public void setOplata(Integer oplata) {
        this.oplata = oplata;
    }

    public Double getSkidka() {
        return skidka;
    }

    public void setSkidka(Double skidka) {
        this.skidka = skidka;
    }

    @XmlTransient
    public List<ZakazInfo> getZakazInfoList() {
        return zakazInfoList;
    }

    public void setZakazInfoList(List<ZakazInfo> zakazInfoList) {
        this.zakazInfoList = zakazInfoList;
    }

    public Klienty getTelefon() {
        return telefon;
    }

    public void setTelefon(Klienty telefon) {
        this.telefon = telefon;
    }

    @XmlTransient
    public List<ZakazStatus> getZakazStatusList() {
        return zakazStatusList;
    }

    public void setZakazStatusList(List<ZakazStatus> zakazStatusList) {
        this.zakazStatusList = zakazStatusList;
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
        if (!(object instanceof Zakazy)) {
            return false;
        }
        Zakazy other = (Zakazy) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "by.kursovaa.dbAvtushkoVM.Zakazy[ id=" + id + " ]";
    }
}
