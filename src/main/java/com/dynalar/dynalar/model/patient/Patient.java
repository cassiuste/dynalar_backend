package com.dynalar.dynalar.model.patient;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "patient")
public class Patient {

	@OneToOne
    @JoinColumn(name = "medical_record_id", unique = true)
    private MedicalRecord medicalRecord;

    private String name;
    private String lastName;
    private String password;
    private String email;
    private String dni;
    private String socialSecurityNumber;
    private String phone;
    private Boolean treatmentConsent;
    private Boolean anesthesiaConsent;
    private String billing;

    @OneToOne(mappedBy = "patient")
    private Document documents;
    
    public Patient() {
	}

	public MedicalRecord getMedicalRecord() {
		return medicalRecord;
	}

	public void setMedicalRecord(MedicalRecord medicalRecord) {
		this.medicalRecord = medicalRecord;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getSocialSecurityNumber() {
		return socialSecurityNumber;
	}

	public void setSocialSecurityNumber(String socialSecurityNumber) {
		this.socialSecurityNumber = socialSecurityNumber;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Boolean getTreatmentConsent() {
		return treatmentConsent;
	}

	public void setTreatmentConsent(Boolean treatmentConsent) {
		this.treatmentConsent = treatmentConsent;
	}

	public Boolean getAnesthesiaConsent() {
		return anesthesiaConsent;
	}

	public void setAnesthesiaConsent(Boolean anesthesiaConsent) {
		this.anesthesiaConsent = anesthesiaConsent;
	}

	public String getBilling() {
		return billing;
	}

	public void setBilling(String billing) {
		this.billing = billing;
	}

	public Document getDocuments() {
		return documents;
	}

	public void setDocuments(Document documents) {
		this.documents = documents;
	}
    
    
}
