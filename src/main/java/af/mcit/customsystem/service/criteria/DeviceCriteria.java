package af.mcit.customsystem.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link af.mcit.customsystem.domain.Device} entity. This class is used
 * in {@link af.mcit.customsystem.web.rest.DeviceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /devices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeviceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter serialNumber;

    private StringFilter modelNumber;

    private LongFilter traderId;

    private LongFilter tarifId;

    private Boolean distinct;

    public DeviceCriteria() {}

    public DeviceCriteria(DeviceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.serialNumber = other.serialNumber == null ? null : other.serialNumber.copy();
        this.modelNumber = other.modelNumber == null ? null : other.modelNumber.copy();
        this.traderId = other.traderId == null ? null : other.traderId.copy();
        this.tarifId = other.tarifId == null ? null : other.tarifId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DeviceCriteria copy() {
        return new DeviceCriteria(this);
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

    public StringFilter getSerialNumber() {
        return serialNumber;
    }

    public StringFilter serialNumber() {
        if (serialNumber == null) {
            serialNumber = new StringFilter();
        }
        return serialNumber;
    }

    public void setSerialNumber(StringFilter serialNumber) {
        this.serialNumber = serialNumber;
    }

    public StringFilter getModelNumber() {
        return modelNumber;
    }

    public StringFilter modelNumber() {
        if (modelNumber == null) {
            modelNumber = new StringFilter();
        }
        return modelNumber;
    }

    public void setModelNumber(StringFilter modelNumber) {
        this.modelNumber = modelNumber;
    }

    public LongFilter getTraderId() {
        return traderId;
    }

    public LongFilter traderId() {
        if (traderId == null) {
            traderId = new LongFilter();
        }
        return traderId;
    }

    public void setTraderId(LongFilter traderId) {
        this.traderId = traderId;
    }

    public LongFilter getTarifId() {
        return tarifId;
    }

    public LongFilter tarifId() {
        if (tarifId == null) {
            tarifId = new LongFilter();
        }
        return tarifId;
    }

    public void setTarifId(LongFilter tarifId) {
        this.tarifId = tarifId;
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
        final DeviceCriteria that = (DeviceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(serialNumber, that.serialNumber) &&
            Objects.equals(modelNumber, that.modelNumber) &&
            Objects.equals(traderId, that.traderId) &&
            Objects.equals(tarifId, that.tarifId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serialNumber, modelNumber, traderId, tarifId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeviceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (serialNumber != null ? "serialNumber=" + serialNumber + ", " : "") +
            (modelNumber != null ? "modelNumber=" + modelNumber + ", " : "") +
            (traderId != null ? "traderId=" + traderId + ", " : "") +
            (tarifId != null ? "tarifId=" + tarifId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
