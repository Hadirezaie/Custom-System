package af.mcit.customsystem.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link af.mcit.customsystem.domain.CustomCharges} entity. This class is used
 * in {@link af.mcit.customsystem.web.rest.CustomChargesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /custom-charges?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomChargesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter deviceModel;

    private LongFilter customFee;

    private Boolean distinct;

    public CustomChargesCriteria() {}

    public CustomChargesCriteria(CustomChargesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.deviceModel = other.deviceModel == null ? null : other.deviceModel.copy();
        this.customFee = other.customFee == null ? null : other.customFee.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CustomChargesCriteria copy() {
        return new CustomChargesCriteria(this);
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

    public StringFilter getDeviceModel() {
        return deviceModel;
    }

    public StringFilter deviceModel() {
        if (deviceModel == null) {
            deviceModel = new StringFilter();
        }
        return deviceModel;
    }

    public void setDeviceModel(StringFilter deviceModel) {
        this.deviceModel = deviceModel;
    }

    public LongFilter getCustomFee() {
        return customFee;
    }

    public LongFilter customFee() {
        if (customFee == null) {
            customFee = new LongFilter();
        }
        return customFee;
    }

    public void setCustomFee(LongFilter customFee) {
        this.customFee = customFee;
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
        final CustomChargesCriteria that = (CustomChargesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(deviceModel, that.deviceModel) &&
            Objects.equals(customFee, that.customFee) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, deviceModel, customFee, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomChargesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (deviceModel != null ? "deviceModel=" + deviceModel + ", " : "") +
            (customFee != null ? "customFee=" + customFee + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
