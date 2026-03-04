package com.dynalar.dynalar.model;

import java.util.Set;

import com.dynalar.dynalar.model.user.Dentist;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "treatment")
public class Treatment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private String description;
	
	private Integer durationMinutes;

    
	@ManyToMany(mappedBy = "treatments")
	@JsonIgnore 
	private Set<Dentist> dentist;
	
	public Treatment() {
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getDurationMinutes() {
		return durationMinutes;
	}

	public void setDurationMinutes(Integer durationMinutes) {
		this.durationMinutes = durationMinutes;
	}

	public Set<Dentist> getDentist() {
		return dentist;
	}

	public void setDentist(Set<Dentist> dentist) {
		this.dentist = dentist;
	}
	
}