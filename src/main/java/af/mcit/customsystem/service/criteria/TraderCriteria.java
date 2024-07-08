package af.mcit.customsystem.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link af.mcit.customsystem.domain.Trader} entity. This class is used
 * in {@link af.mcit.customsystem.web.rest.TraderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /traders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TraderCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter nidNumber;

    private StringFilter tIN;

    private StringFilter licenseNumber;

    private Boolean distinct;

    public TraderCriteria() {}

    public TraderCriteria(TraderCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.nidNumber = other.nidNumber == null ? null : other.nidNumber.copy();
        this.tIN = other.tIN == null ? null : other.tIN.copy();
        this.licenseNumber = other.licenseNumber == null ? null : other.licenseNumber.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TraderCriteria copy() {
        return new TraderCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getNidNumber() {
        return nidNumber;
    }

    public StringFilter nidNumber() {
        if (nidNumber == null) {
            nidNumber = new StringFilter();
        }
        return nidNumber;
    }

    public void setNidNumber(StringFilter nidNumber) {
        this.nidNumber = nidNumber;
    }

    public StringFilter gettIN() {
        return tIN;
    }

    public StringFilter tIN() {
        if (tIN == null) {
            tIN = new StringFilter();
        }
        return tIN;
    }

    public void settIN(StringFilter tIN) {
        this.tIN = tIN;
    }

    public StringFilter getLicenseNumber() {
        return licenseNumber;
    }

    public StringFilter licenseNumber() {
        if (licenseNumber == null) {
            licenseNumber = new StringFilter();
        }
        return licenseNumber;
    }

    public void setLicenseNumber(StringFilter licenseNumber) {
        this.licenseNumber = licenseNumber;
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
        final TraderCriteria that = (TraderCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(nidNumber, that.nidNumber) &&
            Objects.equals(tIN, that.tIN) &&
            Objects.equals(licenseNumber, that.licenseNumber) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nidNumber, tIN, licenseNumber, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TraderCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (nidNumber != null ? "nidNumber=" + nidNumber + ", " : "") +
            (tIN != null ? "tIN=" + tIN + ", " : "") +
            (licenseNumber != null ? "licenseNumber=" + licenseNumber + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
