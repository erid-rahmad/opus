package com.bhp.opusb.service.dto;

import java.util.Objects;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import com.bhp.opusb.domain.enumeration.VendorType;

/**
 * A DTO for the {@link com.bhp.opusb.domain.CVendor} entity.
 */
public class CVendorDTO extends AbstractAuditingDTO {
    
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String taxIdNo;

    @NotNull
    private String taxIdName;

    private Boolean branch;

    @NotNull
    private String email;

    @NotNull
    private String phone;

    private String fax;

    private String website;

    @NotNull
    private VendorType type;

    @NotNull
    private String paymentCategory;

    @NotNull
    private String approvalStatus;

    private UUID uid;

    private Boolean active;


    private Long taxIdFileId;

    private Long adOrganizationId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaxIdNo() {
        return taxIdNo;
    }

    public void setTaxIdNo(String taxIdNo) {
        this.taxIdNo = taxIdNo;
    }

    public String getTaxIdName() {
        return taxIdName;
    }

    public void setTaxIdName(String taxIdName) {
        this.taxIdName = taxIdName;
    }

    public Boolean isBranch() {
        return branch;
    }

    public void setBranch(Boolean branch) {
        this.branch = branch;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public VendorType getType() {
        return type;
    }

    public void setType(VendorType type) {
        this.type = type;
    }

    public String getPaymentCategory() {
        return paymentCategory;
    }

    public void setPaymentCategory(String paymentCategory) {
        this.paymentCategory = paymentCategory;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
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

    public Long getTaxIdFileId() {
        return taxIdFileId;
    }

    public void setTaxIdFileId(Long cAttachmentId) {
        this.taxIdFileId = cAttachmentId;
    }

    public Long getAdOrganizationId() {
        return adOrganizationId;
    }

    public void setAdOrganizationId(Long aDOrganizationId) {
        this.adOrganizationId = aDOrganizationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CVendorDTO cVendorDTO = (CVendorDTO) o;
        if (cVendorDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cVendorDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CVendorDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", taxIdNo='" + getTaxIdNo() + "'" +
            ", taxIdName='" + getTaxIdName() + "'" +
            ", branch='" + isBranch() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", fax='" + getFax() + "'" +
            ", website='" + getWebsite() + "'" +
            ", type='" + getType() + "'" +
            ", paymentCategory='" + getPaymentCategory() + "'" +
            ", approvalStatus='" + getApprovalStatus() + "'" +
            ", uid='" + getUid() + "'" +
            ", active='" + isActive() + "'" +
            ", taxIdFileId=" + getTaxIdFileId() +
            ", adOrganizationId=" + getAdOrganizationId() +
            "}";
    }
}
