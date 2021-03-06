package com.bhp.opusb.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.UUIDFilter;

/**
 * Criteria class for the {@link com.bhp.opusb.domain.MProjectInformation} entity. This class is used
 * in {@link com.bhp.opusb.web.rest.MProjectInformationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /m-project-informations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MProjectInformationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter uid;

    private BooleanFilter active;

    private LongFilter biddingId;

    private LongFilter adOrganizationId;

    private LongFilter attachmentId;

    public MProjectInformationCriteria() {
    }

    public MProjectInformationCriteria(MProjectInformationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.uid = other.uid == null ? null : other.uid.copy();
        this.active = other.active == null ? null : other.active.copy();
        this.biddingId = other.biddingId == null ? null : other.biddingId.copy();
        this.adOrganizationId = other.adOrganizationId == null ? null : other.adOrganizationId.copy();
        this.attachmentId = other.attachmentId == null ? null : other.attachmentId.copy();
    }

    @Override
    public MProjectInformationCriteria copy() {
        return new MProjectInformationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public UUIDFilter getUid() {
        return uid;
    }

    public void setUid(UUIDFilter uid) {
        this.uid = uid;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
    }

    public LongFilter getBiddingId() {
        return biddingId;
    }

    public void setBiddingId(LongFilter biddingId) {
        this.biddingId = biddingId;
    }

    public LongFilter getAdOrganizationId() {
        return adOrganizationId;
    }

    public void setAdOrganizationId(LongFilter adOrganizationId) {
        this.adOrganizationId = adOrganizationId;
    }

    public LongFilter getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(LongFilter attachmentId) {
        this.attachmentId = attachmentId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MProjectInformationCriteria that = (MProjectInformationCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(uid, that.uid) &&
            Objects.equals(active, that.active) &&
            Objects.equals(biddingId, that.biddingId) &&
            Objects.equals(adOrganizationId, that.adOrganizationId) &&
            Objects.equals(attachmentId, that.attachmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        uid,
        active,
        biddingId,
        adOrganizationId,
        attachmentId
        );
    }

    @Override
    public String toString() {
        return "MProjectInformationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (uid != null ? "uid=" + uid + ", " : "") +
                (active != null ? "active=" + active + ", " : "") +
                (biddingId != null ? "biddingId=" + biddingId + ", " : "") +
                (adOrganizationId != null ? "adOrganizationId=" + adOrganizationId + ", " : "") +
                (attachmentId != null ? "attachmentId=" + attachmentId + ", " : "") +
            "}";
    }

}
