package com.dynalar.dynalar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dynalar.dynalar.model.patient.Patient;
import com.dynalar.dynalar.respository.PatientRepository;

@RestController
@RequestMapping("/patient")
public class PatientController {

	@Autowired
	private PatientRepository patientRepository;
	
	@GetMapping("/index")
	public @ResponseBody ResponseEntity<List<Patient>> getAllPatients() {
		try {
			return ResponseEntity.ok(patientRepository.findAll());
		} catch (Exception e) {
			return ResponseEntity.status(404).build();
		}
	}
	
	@PostMapping()
	public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
	    try {
	        if (patient.getMedicalRecord() != null) {
	            patient.getMedicalRecord().setPatient(patient); 
	        }
	        
	        Patient newPatient = patientRepository.save(patient); 
	        
	        return ResponseEntity.status(HttpStatus.CREATED).body(newPatient);	        
	    } catch (Exception e) {
	        e.printStackTrace(); 
			return ResponseEntity.badRequest().build();

	    }
	
	}
}
