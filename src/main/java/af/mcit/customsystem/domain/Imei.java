package af.mcit.customsystem.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A Imei.
 */
@Entity
@Table(name = "imei")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Imei implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "imei_number")
    private Long imeiNumber;

    @ManyToOne
    @JsonIgnoreProperties(value = { "trader" }, allowSetters = true)
    private Device device;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Imei id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getImeiNumber() {
        return this.imeiNumber;
    }

    public Imei imeiNumber(Long imeiNumber) {
        this.setImeiNumber(imeiNumber);
        return this;
    }

    public void setImeiNumber(Long imeiNumber) {
        this.imeiNumber = imeiNumber;
    }

    public Device getDevice() {
        return this.device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Imei device(Device device) {
        this.setDevice(device);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Imei)) {
            return false;
        }
        return id != null && id.equals(((Imei) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Imei{" +
            "id=" + getId() +
            ", imeiNumber=" + getImeiNumber() +
            "}";
    }
}
