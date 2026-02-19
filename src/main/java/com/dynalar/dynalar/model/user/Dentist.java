package com.dynalar.dynalar.model.user;

import java.util.Set;

import com.dynalar.dynalar.model.Treatment;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "dentist")
public class Dentist extends User {

	private Boolean mondayMorning;
	private Boolean mondayAfternoon;
	private Boolean tuesdayMorning;
	private Boolean tuesdayAfternoon;
	private Boolean wednesdayMorning;
	private Boolean wednesdayAfternoon;
	private Boolean thursdayMorning;
	private Boolean thursdayAfternoon;
	private Boolean fridayMorning;
	private Boolean fridayAfternoon;

	@ManyToMany
	@JoinTable(name = "dentist_treatment", joinColumns = @JoinColumn(name = "dentist_id"), inverseJoinColumns = @JoinColumn(name = "treatment_id"))
	private Set<Treatment> treatments;

	public Dentist() {

	}

	public boolean isMondayMorning() {
		return mondayMorning;
	}

	public void setMondayMorning(boolean mondayMorning) {
		this.mondayMorning = mondayMorning;
	}

	public boolean isMondayAfternoon() {
		return mondayAfternoon;
	}

	public void setMondayAfternoon(boolean mondayAfternoon) {
		this.mondayAfternoon = mondayAfternoon;
	}

	public boolean isTuesdayMorning() {
		return tuesdayMorning;
	}

	public void setTuesdayMorning(boolean tuesdayMorning) {
		this.tuesdayMorning = tuesdayMorning;
	}

	public boolean isTuesdayAfternoon() {
		return tuesdayAfternoon;
	}

	public void setTuesdayAfternoon(boolean tuesdayAfternoon) {
		this.tuesdayAfternoon = tuesdayAfternoon;
	}

	public boolean isWednesdayMorning() {
		return wednesdayMorning;
	}

	public void setWednesdayMorning(boolean wednesdayMorning) {
		this.wednesdayMorning = wednesdayMorning;
	}

	public boolean isWednesdayAfternoon() {
		return wednesdayAfternoon;
	}

	public void setWednesdayAfternoon(boolean wednesdayAfternoon) {
		this.wednesdayAfternoon = wednesdayAfternoon;
	}

	public boolean isThursdayMorning() {
		return thursdayMorning;
	}

	public void setThursdayMorning(boolean thursdayMorning) {
		this.thursdayMorning = thursdayMorning;
	}

	public boolean isThursdayAfternoon() {
		return thursdayAfternoon;
	}

	public void setThursdayAfternoon(boolean thursdayAfternoon) {
		this.thursdayAfternoon = thursdayAfternoon;
	}

	public boolean isFridayMorning() {
		return fridayMorning;
	}

	public void setFridayMorning(boolean fridayMorning) {
		this.fridayMorning = fridayMorning;
	}

	public boolean isFridayAfternoon() {
		return fridayAfternoon;
	}

	public void setFridayAfternoon(boolean fridayAfternoon) {
		this.fridayAfternoon = fridayAfternoon;
	}

	public Set<Treatment> getTreatments() {
		return treatments;
	}

	public void setTreatments(Set<Treatment> treatments) {
		this.treatments = treatments;
	}

}
