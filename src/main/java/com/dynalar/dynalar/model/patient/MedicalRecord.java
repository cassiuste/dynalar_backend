package com.dynalar.dynalar.model.patient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "medical_record")
public class MedicalRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String allergies;
    private String medication;
    private String familyHistory;
    private String infectiousDeceases;
    private String deceases;

    @OneToOne(mappedBy = "medicalRecord")
    @JsonIgnore 
    private Patient patient;
    
    
    @Column(columnDefinition = "TEXT")
    private String signatureBase64;
    
    
    public MedicalRecord() {
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAllergies() {
		return allergies;
	}

	public void setAllergies(String allergies) {
		this.allergies = allergies;
	}

	public String getMedication() {
		return medication;
	}

	public void setMedication(String medication) {
		this.medication = medication;
	}

	public String getFamilyHistory() {
		return familyHistory;
	}

	public void setFamilyHistory(String familyHistory) {
		this.familyHistory = familyHistory;
	}

	public String getInfectiousDeceases() {
		return infectiousDeceases;
	}

	public void setInfectiousDeceases(String infectiousDecease) {
		this.infectiousDeceases = infectiousDecease;
	}

	public String getDeceases() {
		return deceases;
	}

	public void setDeceases(String deceases) {
		this.deceases = deceases;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	public String getSignatureBase64() {
	    return signatureBase64;
	}

	public void setSignatureBase64(String signatureBase64) {
	    this.signatureBase64 = signatureBase64;
	}
    
}
