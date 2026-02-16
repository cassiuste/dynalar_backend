package com.dynalar.dynalar.model.patient;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "document")
public class Document {

	
	@OneToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    private String type;
    private String documentUrl;
    private LocalDateTime creationDate;

    public Document() {
    }

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public String getDocumentType() {
		return type;
	}

	public void setDocumentType(String documentType) {
		this.type = documentType;
	}

	public String getDocumentUrl() {
		return documentUrl;
	}

	public void setDocumentUrl(String documentUrl) {
		this.documentUrl = documentUrl;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}
    
    
}
