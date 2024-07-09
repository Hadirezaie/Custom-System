package af.mcit.customsystem.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Tarif.
 */
@Entity
@Table(name = "tarif")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Tarif implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "paid_date")
    private LocalDate paidDate;

    @Column(name = "paid")
    private Boolean paid;

    @Column(name = "number_of_device")
    private Long numberOfDevice;

    @OneToMany(mappedBy = "tarif")
    @JsonIgnoreProperties(value = { "trader", "tarif" }, allowSetters = true)
    private Set<Device> devices = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tarif id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAmount() {
        return this.amount;
    }

    public Tarif amount(Long amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public LocalDate getPaidDate() {
        return this.paidDate;
    }

    public Tarif paidDate(LocalDate paidDate) {
        this.setPaidDate(paidDate);
        return this;
    }

    public void setPaidDate(LocalDate paidDate) {
        this.paidDate = paidDate;
    }

    public Boolean getPaid() {
        return this.paid;
    }

    public Tarif paid(Boolean paid) {
        this.setPaid(paid);
        return this;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Long getNumberOfDevice() {
        return this.numberOfDevice;
    }

    public Tarif numberOfDevice(Long numberOfDevice) {
        this.setNumberOfDevice(numberOfDevice);
        return this;
    }

    public void setNumberOfDevice(Long numberOfDevice) {
        this.numberOfDevice = numberOfDevice;
    }

    public Set<Device> getDevices() {
        return this.devices;
    }

    public void setDevices(Set<Device> devices) {
        if (this.devices != null) {
            this.devices.forEach(i -> i.setTarif(null));
        }
        if (devices != null) {
            devices.forEach(i -> i.setTarif(this));
        }
        this.devices = devices;
    }

    public Tarif devices(Set<Device> devices) {
        this.setDevices(devices);
        return this;
    }

    public Tarif addDevice(Device device) {
        this.devices.add(device);
        device.setTarif(this);
        return this;
    }

    public Tarif removeDevice(Device device) {
        this.devices.remove(device);
        device.setTarif(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tarif)) {
            return false;
        }
        return id != null && id.equals(((Tarif) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tarif{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", paidDate='" + getPaidDate() + "'" +
            ", paid='" + getPaid() + "'" +
            ", numberOfDevice=" + getNumberOfDevice() +
            "}";
    }
}
