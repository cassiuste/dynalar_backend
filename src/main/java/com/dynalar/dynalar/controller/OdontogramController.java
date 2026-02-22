package com.dynalar.dynalar.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dynalar.dynalar.model.odontogram.Odontogram;
import com.dynalar.dynalar.model.patient.Patient;
import com.dynalar.dynalar.respository.OdontogramRepository;
import com.dynalar.dynalar.respository.PatientRepository;

@RestController
@RequestMapping("/odontogram")
public class OdontogramController {

	@Autowired
	private OdontogramRepository odontogramRepository;
	
	@Autowired
	private PatientRepository patientRepository;
	
	
	@GetMapping("/patient/{patientId}")
	public ResponseEntity<Odontogram> getOdontogramByPatient(@PathVariable Long patientId) {
		try {
			Optional<Odontogram> odontogramOpt = odontogramRepository.findByPatientId(patientId);
			return ResponseEntity.ok(odontogramOpt.orElse(null));
		} catch (Exception e) {
			return ResponseEntity.status(404).build();
		}
	}
	
}
