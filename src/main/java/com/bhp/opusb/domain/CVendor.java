package com.bhp.opusb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import com.bhp.opusb.domain.enumeration.VendorType;

/**
 * A CVendor.
 */
@Entity
@Table(name = "c_vendor")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CVendor extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "tax_id_no", nullable = false)
    private Long taxIdNo;

    @NotNull
    @Column(name = "tax_id_name", nullable = false)
    private String taxIdName;

    @Column(name = "branch")
    private Boolean branch;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "fax")
    private String fax;

    @Column(name = "website")
    private String website;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private VendorType type;

    @NotNull
    @Column(name = "payment_category", nullable = false)
    private String paymentCategory;

    @NotNull
    @Column(name = "approval_status", nullable = false)
    private String approvalStatus;

    @Column(name = "uid")
    private UUID uid;

    @Column(name = "active")
    private Boolean active;

    @OneToOne
    @JoinColumn(unique = true)
    private CAttachment taxIdFile;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("cVendors")
    private ADOrganization adOrganization;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public CVendor name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTaxIdNo() {
        return taxIdNo;
    }

    public CVendor taxIdNo(Long taxIdNo) {
        this.taxIdNo = taxIdNo;
        return this;
    }

    public void setTaxIdNo(Long taxIdNo) {
        this.taxIdNo = taxIdNo;
    }

    public String getTaxIdName() {
        return taxIdName;
    }

    public CVendor taxIdName(String taxIdName) {
        this.taxIdName = taxIdName;
        return this;
    }

    public void setTaxIdName(String taxIdName) {
        this.taxIdName = taxIdName;
    }

    public Boolean isBranch() {
        return branch;
    }

    public CVendor branch(Boolean branch) {
        this.branch = branch;
        return this;
    }

    public void setBranch(Boolean branch) {
        this.branch = branch;
    }

    public String getEmail() {
        return email;
    }

    public CVendor email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public CVendor phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public CVendor fax(String fax) {
        this.fax = fax;
        return this;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getWebsite() {
        return website;
    }

    public CVendor website(String website) {
        this.website = website;
        return this;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public VendorType getType() {
        return type;
    }

    public CVendor type(VendorType type) {
        this.type = type;
        return this;
    }

    public void setType(VendorType type) {
        this.type = type;
    }

    public String getPaymentCategory() {
        return paymentCategory;
    }

    public CVendor paymentCategory(String paymentCategory) {
        this.paymentCategory = paymentCategory;
        return this;
    }

    public void setPaymentCategory(String paymentCategory) {
        this.paymentCategory = paymentCategory;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public CVendor approvalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
        return this;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public UUID getUid() {
        return uid;
    }

    public CVendor uid(UUID uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public Boolean isActive() {
        return active;
    }

    public CVendor active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public CAttachment getTaxIdFile() {
        return taxIdFile;
    }

    public CVendor taxIdFile(CAttachment cAttachment) {
        this.taxIdFile = cAttachment;
        return this;
    }

    public void setTaxIdFile(CAttachment cAttachment) {
        this.taxIdFile = cAttachment;
    }

    public ADOrganization getAdOrganization() {
        return adOrganization;
    }

    public CVendor adOrganization(ADOrganization aDOrganization) {
        this.adOrganization = aDOrganization;
        return this;
    }

    public void setAdOrganization(ADOrganization aDOrganization) {
        this.adOrganization = aDOrganization;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @PrePersist
    public void assignUUID() {
        this.uid = UUID.randomUUID();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CVendor)) {
            return false;
        }
        return id != null && id.equals(((CVendor) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CVendor{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", taxIdNo=" + getTaxIdNo() +
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
            "}";
    }
}