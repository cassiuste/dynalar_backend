package com.dynalar.dynalar.model.odontogram;

import java.time.LocalDateTime;
import java.util.List;

import com.dynalar.dynalar.model.patient.Patient;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "odontogram")
public class Odontogram {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private LocalDateTime creationDate;
	
	@ManyToOne
	@JoinColumn(name = "patient_id")
	private Patient patient;
	
	@OneToMany(mappedBy = "odontogram", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ToothCondition> toothConditions;
    
    public Odontogram() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public Patient getPatientId() {
		return patient;
	}

	public void setPatientId(Patient patientId) {
		this.patient = patientId;
	}

	public List<ToothCondition> getToothConditions() {
		return toothConditions;
	}

	public void setToothConditions(List<ToothCondition> toothConditions) {
		this.toothConditions = toothConditions;
	}

}
