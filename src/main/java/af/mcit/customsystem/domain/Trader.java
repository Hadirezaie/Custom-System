package af.mcit.customsystem.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A Trader.
 */
@Entity
@Table(name = "trader")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Trader implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "nid_number")
    private String nidNumber;

    @Column(name = "t_in")
    private String tIN;

    @Column(name = "license_number")
    private String licenseNumber;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Trader id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Trader name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNidNumber() {
        return this.nidNumber;
    }

    public Trader nidNumber(String nidNumber) {
        this.setNidNumber(nidNumber);
        return this;
    }

    public void setNidNumber(String nidNumber) {
        this.nidNumber = nidNumber;
    }

    public String gettIN() {
        return this.tIN;
    }

    public Trader tIN(String tIN) {
        this.settIN(tIN);
        return this;
    }

    public void settIN(String tIN) {
        this.tIN = tIN;
    }

    public String getLicenseNumber() {
        return this.licenseNumber;
    }

    public Trader licenseNumber(String licenseNumber) {
        this.setLicenseNumber(licenseNumber);
        return this;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Trader)) {
            return false;
        }
        return id != null && id.equals(((Trader) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Trader{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", nidNumber='" + getNidNumber() + "'" +
            ", tIN='" + gettIN() + "'" +
            ", licenseNumber='" + getLicenseNumber() + "'" +
            "}";
    }
}
