package af.mcit.customsystem.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link af.mcit.customsystem.domain.Tarif} entity. This class is used
 * in {@link af.mcit.customsystem.web.rest.TarifResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tarifs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TarifCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter amount;

    private LocalDateFilter paidDate;

    private BooleanFilter paid;

    private LongFilter numberOfDevice;

    private LongFilter deviceId;

    private Boolean distinct;

    public TarifCriteria() {}

    public TarifCriteria(TarifCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.paidDate = other.paidDate == null ? null : other.paidDate.copy();
        this.paid = other.paid == null ? null : other.paid.copy();
        this.numberOfDevice = other.numberOfDevice == null ? null : other.numberOfDevice.copy();
        this.deviceId = other.deviceId == null ? null : other.deviceId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TarifCriteria copy() {
        return new TarifCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getAmount() {
        return amount;
    }

    public LongFilter amount() {
        if (amount == null) {
            amount = new LongFilter();
        }
        return amount;
    }

    public void setAmount(LongFilter amount) {
        this.amount = amount;
    }

    public LocalDateFilter getPaidDate() {
        return paidDate;
    }

    public LocalDateFilter paidDate() {
        if (paidDate == null) {
            paidDate = new LocalDateFilter();
        }
        return paidDate;
    }

    public void setPaidDate(LocalDateFilter paidDate) {
        this.paidDate = paidDate;
    }

    public BooleanFilter getPaid() {
        return paid;
    }

    public BooleanFilter paid() {
        if (paid == null) {
            paid = new BooleanFilter();
        }
        return paid;
    }

    public void setPaid(BooleanFilter paid) {
        this.paid = paid;
    }

    public LongFilter getNumberOfDevice() {
        return numberOfDevice;
    }

    public LongFilter numberOfDevice() {
        if (numberOfDevice == null) {
            numberOfDevice = new LongFilter();
        }
        return numberOfDevice;
    }

    public void setNumberOfDevice(LongFilter numberOfDevice) {
        this.numberOfDevice = numberOfDevice;
    }

    public LongFilter getDeviceId() {
        return deviceId;
    }

    public LongFilter deviceId() {
        if (deviceId == null) {
            deviceId = new LongFilter();
        }
        return deviceId;
    }

    public void setDeviceId(LongFilter deviceId) {
        this.deviceId = deviceId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TarifCriteria that = (TarifCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(paidDate, that.paidDate) &&
            Objects.equals(paid, that.paid) &&
            Objects.equals(numberOfDevice, that.numberOfDevice) &&
            Objects.equals(deviceId, that.deviceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, paidDate, paid, numberOfDevice, deviceId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TarifCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (amount != null ? "amount=" + amount + ", " : "") +
            (paidDate != null ? "paidDate=" + paidDate + ", " : "") +
            (paid != null ? "paid=" + paid + ", " : "") +
            (numberOfDevice != null ? "numberOfDevice=" + numberOfDevice + ", " : "") +
            (deviceId != null ? "deviceId=" + deviceId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
