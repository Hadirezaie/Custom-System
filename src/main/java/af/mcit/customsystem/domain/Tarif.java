package af.mcit.customsystem.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
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

    @JsonIgnoreProperties(value = { "trader" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Device device;

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

    public Device getDevice() {
        return this.device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Tarif device(Device device) {
        this.setDevice(device);
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
            "}";
    }
}
