package af.mcit.customsystem.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link af.mcit.customsystem.domain.Imei} entity. This class is used
 * in {@link af.mcit.customsystem.web.rest.ImeiResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /imeis?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImeiCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter imeiNumber;

    private LongFilter deviceId;

    private Boolean distinct;

    public ImeiCriteria() {}

    public ImeiCriteria(ImeiCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.imeiNumber = other.imeiNumber == null ? null : other.imeiNumber.copy();
        this.deviceId = other.deviceId == null ? null : other.deviceId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ImeiCriteria copy() {
        return new ImeiCriteria(this);
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

    public LongFilter getImeiNumber() {
        return imeiNumber;
    }

    public LongFilter imeiNumber() {
        if (imeiNumber == null) {
            imeiNumber = new LongFilter();
        }
        return imeiNumber;
    }

    public void setImeiNumber(LongFilter imeiNumber) {
        this.imeiNumber = imeiNumber;
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
        final ImeiCriteria that = (ImeiCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(imeiNumber, that.imeiNumber) &&
            Objects.equals(deviceId, that.deviceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, imeiNumber, deviceId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImeiCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (imeiNumber != null ? "imeiNumber=" + imeiNumber + ", " : "") +
            (deviceId != null ? "deviceId=" + deviceId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
