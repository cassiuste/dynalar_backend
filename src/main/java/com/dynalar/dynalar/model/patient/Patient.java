package com.dynalar.dynalar.model.patient;

import java.util.List;

import com.dynalar.dynalar.model.odontogram.Odontogram;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import com.dynalar.dynalar.model.odontogram.Odontogram;
import com.dynalar.dynalar.model.Appointment;

@Entity
@Table(name = "patient")
public class Patient {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    private String name;
    private String lastName;
    private String email;
    private String dni;
    private String socialSecurityNumber;
    private String phone;
    private Boolean treatmentConsent;
    private Boolean anesthesiaConsent;
    private String billing;
    

    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL)
    private Odontogram odontogram;

    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "medical_record_id", unique = true)
    private MedicalRecord medicalRecord;

    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private Odontogram odontogram;
    
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents;
    
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments;
    
    public Patient() {
	}

    
    public Patient(MedicalRecord medicalRecord, String name, String lastName, String email, String dni)
    {
    	this.medicalRecord = medicalRecord;
		this.name = name;
		this.lastName = lastName;
		this.email = email;
		this.dni = dni;
		// Se crea solo un odontograma
		this.odontogram = new Odontogram();
		this.odontogram.setPatient(this);
    }


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


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
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


	public MedicalRecord getMedicalRecord() {
		return medicalRecord;
	}


	public void setMedicalRecord(MedicalRecord medicalRecord) {
		this.medicalRecord = medicalRecord;
	}


	public List<Document> getDocuments() {
		return documents;
	}


	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

	public Odontogram getOdontogram() {
		return odontogram;
	}

	public void setOdontogram(Odontogram odontogram) {
		this.odontogram = odontogram;
	}    
}
