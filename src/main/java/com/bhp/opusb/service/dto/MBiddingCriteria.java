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
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.LocalDateFilter;
import io.github.jhipster.service.filter.UUIDFilter;

/**
 * Criteria class for the {@link com.bhp.opusb.domain.MBidding} entity. This class is used
 * in {@link com.bhp.opusb.web.rest.MBiddingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /m-biddings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MBiddingCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter biddingNo;

    private StringFilter name;

    private StringFilter vendorSelection;

    private BigDecimalFilter ceilingPrice;

    private BigDecimalFilter estimatedPrice;

    private StringFilter documentAction;

    private StringFilter documentStatus;

    private BooleanFilter approved;

    private BooleanFilter processed;

    private LocalDateFilter dateReject;

    private LocalDateFilter dateApprove;

    private UUIDFilter uid;

    private BooleanFilter active;

    private LongFilter adOrganizationId;

    private LongFilter costCenterId;

    private LongFilter requisitionId;

    private LongFilter biddingTypeId;

    private LongFilter eventTypeId;

    private LongFilter adUserId;

    public MBiddingCriteria() {
    }

    public MBiddingCriteria(MBiddingCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.biddingNo = other.biddingNo == null ? null : other.biddingNo.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.vendorSelection = other.vendorSelection == null ? null : other.vendorSelection.copy();
        this.ceilingPrice = other.ceilingPrice == null ? null : other.ceilingPrice.copy();
        this.estimatedPrice = other.estimatedPrice == null ? null : other.estimatedPrice.copy();
        this.documentAction = other.documentAction == null ? null : other.documentAction.copy();
        this.documentStatus = other.documentStatus == null ? null : other.documentStatus.copy();
        this.approved = other.approved == null ? null : other.approved.copy();
        this.processed = other.processed == null ? null : other.processed.copy();
        this.dateReject = other.dateReject == null ? null : other.dateReject.copy();
        this.dateApprove = other.dateApprove == null ? null : other.dateApprove.copy();
        this.uid = other.uid == null ? null : other.uid.copy();
        this.active = other.active == null ? null : other.active.copy();
        this.adOrganizationId = other.adOrganizationId == null ? null : other.adOrganizationId.copy();
        this.costCenterId = other.costCenterId == null ? null : other.costCenterId.copy();
        this.requisitionId = other.requisitionId == null ? null : other.requisitionId.copy();
        this.biddingTypeId = other.biddingTypeId == null ? null : other.biddingTypeId.copy();
        this.eventTypeId = other.eventTypeId == null ? null : other.eventTypeId.copy();
        this.adUserId = other.adUserId == null ? null : other.adUserId.copy();
    }

    @Override
    public MBiddingCriteria copy() {
        return new MBiddingCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getBiddingNo() {
        return biddingNo;
    }

    public void setBiddingNo(StringFilter biddingNo) {
        this.biddingNo = biddingNo;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getVendorSelection() {
        return vendorSelection;
    }

    public void setVendorSelection(StringFilter vendorSelection) {
        this.vendorSelection = vendorSelection;
    }

    public BigDecimalFilter getCeilingPrice() {
        return ceilingPrice;
    }

    public void setCeilingPrice(BigDecimalFilter ceilingPrice) {
        this.ceilingPrice = ceilingPrice;
    }

    public BigDecimalFilter getEstimatedPrice() {
        return estimatedPrice;
    }

    public void setEstimatedPrice(BigDecimalFilter estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }

    public StringFilter getDocumentAction() {
        return documentAction;
    }

    public void setDocumentAction(StringFilter documentAction) {
        this.documentAction = documentAction;
    }

    public StringFilter getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(StringFilter documentStatus) {
        this.documentStatus = documentStatus;
    }

    public BooleanFilter getApproved() {
        return approved;
    }

    public void setApproved(BooleanFilter approved) {
        this.approved = approved;
    }

    public BooleanFilter getProcessed() {
        return processed;
    }

    public void setProcessed(BooleanFilter processed) {
        this.processed = processed;
    }

    public LocalDateFilter getDateReject() {
        return dateReject;
    }

    public void setDateReject(LocalDateFilter dateReject) {
        this.dateReject = dateReject;
    }

    public LocalDateFilter getDateApprove() {
        return dateApprove;
    }

    public void setDateApprove(LocalDateFilter dateApprove) {
        this.dateApprove = dateApprove;
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

    public LongFilter getAdOrganizationId() {
        return adOrganizationId;
    }

    public void setAdOrganizationId(LongFilter adOrganizationId) {
        this.adOrganizationId = adOrganizationId;
    }

    public LongFilter getCostCenterId() {
        return costCenterId;
    }

    public void setCostCenterId(LongFilter costCenterId) {
        this.costCenterId = costCenterId;
    }

    public LongFilter getRequisitionId() {
        return requisitionId;
    }

    public void setRequisitionId(LongFilter requisitionId) {
        this.requisitionId = requisitionId;
    }

    public LongFilter getBiddingTypeId() {
        return biddingTypeId;
    }

    public void setBiddingTypeId(LongFilter biddingTypeId) {
        this.biddingTypeId = biddingTypeId;
    }

    public LongFilter getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(LongFilter eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public LongFilter getAdUserId() {
        return adUserId;
    }

    public void setAdUserId(LongFilter adUserId) {
        this.adUserId = adUserId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MBiddingCriteria that = (MBiddingCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(biddingNo, that.biddingNo) &&
            Objects.equals(name, that.name) &&
            Objects.equals(vendorSelection, that.vendorSelection) &&
            Objects.equals(ceilingPrice, that.ceilingPrice) &&
            Objects.equals(estimatedPrice, that.estimatedPrice) &&
            Objects.equals(documentAction, that.documentAction) &&
            Objects.equals(documentStatus, that.documentStatus) &&
            Objects.equals(approved, that.approved) &&
            Objects.equals(processed, that.processed) &&
            Objects.equals(dateReject, that.dateReject) &&
            Objects.equals(dateApprove, that.dateApprove) &&
            Objects.equals(uid, that.uid) &&
            Objects.equals(active, that.active) &&
            Objects.equals(adOrganizationId, that.adOrganizationId) &&
            Objects.equals(costCenterId, that.costCenterId) &&
            Objects.equals(requisitionId, that.requisitionId) &&
            Objects.equals(biddingTypeId, that.biddingTypeId) &&
            Objects.equals(eventTypeId, that.eventTypeId) &&
            Objects.equals(adUserId, that.adUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        biddingNo,
        name,
        vendorSelection,
        ceilingPrice,
        estimatedPrice,
        documentAction,
        documentStatus,
        approved,
        processed,
        dateReject,
        dateApprove,
        uid,
        active,
        adOrganizationId,
        costCenterId,
        requisitionId,
        biddingTypeId,
        eventTypeId,
        adUserId
        );
    }

    @Override
    public String toString() {
        return "MBiddingCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (biddingNo != null ? "biddingNo=" + biddingNo + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (vendorSelection != null ? "vendorSelection=" + vendorSelection + ", " : "") +
                (ceilingPrice != null ? "ceilingPrice=" + ceilingPrice + ", " : "") +
                (estimatedPrice != null ? "estimatedPrice=" + estimatedPrice + ", " : "") +
                (documentAction != null ? "documentAction=" + documentAction + ", " : "") +
                (documentStatus != null ? "documentStatus=" + documentStatus + ", " : "") +
                (approved != null ? "approved=" + approved + ", " : "") +
                (processed != null ? "processed=" + processed + ", " : "") +
                (dateReject != null ? "dateReject=" + dateReject + ", " : "") +
                (dateApprove != null ? "dateApprove=" + dateApprove + ", " : "") +
                (uid != null ? "uid=" + uid + ", " : "") +
                (active != null ? "active=" + active + ", " : "") +
                (adOrganizationId != null ? "adOrganizationId=" + adOrganizationId + ", " : "") +
                (costCenterId != null ? "costCenterId=" + costCenterId + ", " : "") +
                (requisitionId != null ? "requisitionId=" + requisitionId + ", " : "") +
                (biddingTypeId != null ? "biddingTypeId=" + biddingTypeId + ", " : "") +
                (eventTypeId != null ? "eventTypeId=" + eventTypeId + ", " : "") +
                (adUserId != null ? "adUserId=" + adUserId + ", " : "") +
            "}";
    }

}
