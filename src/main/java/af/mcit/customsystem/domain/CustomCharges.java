package af.mcit.customsystem.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A CustomCharges.
 */
@Entity
@Table(name = "custom_charges")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomCharges implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "device_model")
    private String deviceModel;

    @Column(name = "custom_fee")
    private Long customFee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CustomCharges id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceModel() {
        return this.deviceModel;
    }

    public CustomCharges deviceModel(String deviceModel) {
        this.setDeviceModel(deviceModel);
        return this;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public Long getCustomFee() {
        return this.customFee;
    }

    public CustomCharges customFee(Long customFee) {
        this.setCustomFee(customFee);
        return this;
    }

    public void setCustomFee(Long customFee) {
        this.customFee = customFee;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomCharges)) {
            return false;
        }
        return id != null && id.equals(((CustomCharges) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomCharges{" +
            "id=" + getId() +
            ", deviceModel='" + getDeviceModel() + "'" +
            ", customFee=" + getCustomFee() +
            "}";
    }
}
