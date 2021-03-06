package com.bhp.opusb.service.dto;

import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.bhp.opusb.domain.MProjectInformation} entity.
 */
public class MProjectInformationDTO extends AbstractAuditingDTO {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Long id;

    private UUID uid;

    private Boolean active;


    private Long biddingId;
    private String biddingName;

    private Long adOrganizationId;
    private String adOrganizationName;

    private Long attachmentId;
    private String attachmentName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getBiddingId() {
        return biddingId;
    }

    public void setBiddingId(Long mBiddingId) {
        this.biddingId = mBiddingId;
    }

    public String getBiddingName() {
        return biddingName;
    }

    public void setBiddingName(String biddingName) {
        this.biddingName = biddingName;
    }

    public Long getAdOrganizationId() {
        return adOrganizationId;
    }

    public void setAdOrganizationId(Long aDOrganizationId) {
        this.adOrganizationId = aDOrganizationId;
    }

    public String getAdOrganizationName() {
        return adOrganizationName;
    }

    public void setAdOrganizationName(String adOrganizationName) {
        this.adOrganizationName = adOrganizationName;
    }

    public Long getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(Long cAttachmentId) {
        this.attachmentId = cAttachmentId;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MProjectInformationDTO mProjectInformationDTO = (MProjectInformationDTO) o;
        if (mProjectInformationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mProjectInformationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MProjectInformationDTO{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", active='" + isActive() + "'" +
            ", biddingId=" + getBiddingId() +
            ", adOrganizationId=" + getAdOrganizationId() +
            ", attachmentId=" + getAttachmentId() +
            "}";
    }
}
