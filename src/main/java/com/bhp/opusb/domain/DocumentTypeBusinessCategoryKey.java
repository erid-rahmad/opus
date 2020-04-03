package com.bhp.opusb.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DocumentTypeBusinessCategoryKey {

    @Column(name = "document_type_id")
    private Long documentId;

    @Column(name = "business_category_id")
    private Long businessCategoryId;

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public Long getBusinessCategoryId() {
        return businessCategoryId;
    }

    public void setBusinessCategoryId(Long businessCategoryId) {
        this.businessCategoryId = businessCategoryId;
    }
    
}