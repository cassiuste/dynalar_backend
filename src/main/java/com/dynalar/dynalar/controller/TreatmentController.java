package com.dynalar.dynalar.controller;

import java.util.List;
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

import com.dynalar.dynalar.model.Treatment;
import com.dynalar.dynalar.respository.TreatmentRepository;

@RestController
@RequestMapping("/treatment")
public class TreatmentController {

	@Autowired
	private TreatmentRepository treatmentRepository;


	@GetMapping("/index")
	public ResponseEntity<List<Treatment>> getAllTreatments() {
		try {
			return ResponseEntity.ok(treatmentRepository.findAll());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Treatment> getTreatmentById(@PathVariable Long id) {
		try {
			Optional<Treatment> treatment = treatmentRepository.findById(id);
			if (treatment.isPresent()) {
				return ResponseEntity.ok(treatment.get());
			}
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}


	@PostMapping()
	public ResponseEntity<Treatment> createTreatment(@RequestBody Treatment treatment) {
		try {
			Treatment newTreatment = treatmentRepository.save(treatment);
			return ResponseEntity.status(HttpStatus.CREATED).body(newTreatment);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}


	@PutMapping("/update")
	public ResponseEntity<Treatment> updateTreatment(@RequestBody Treatment updatedTreatment) {
		try {
			Long id = updatedTreatment.getId();
			if (id == null) {
				return ResponseEntity.badRequest().build();
			}

			Optional<Treatment> existingOpt = treatmentRepository.findById(id);
			if (existingOpt.isEmpty()) {
				return ResponseEntity.notFound().build();
			}

			Treatment existingTreatment = existingOpt.get();

			existingTreatment.setName(updatedTreatment.getName());
			existingTreatment.setDescription(updatedTreatment.getDescription());
			existingTreatment.setDurationMinutes(updatedTreatment.getDurationMinutes());

			Treatment savedTreatment = treatmentRepository.save(existingTreatment);
			return ResponseEntity.ok(savedTreatment);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}


	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTreatment(@PathVariable Long id) {
		try {
			Optional<Treatment> treatment = treatmentRepository.findById(id);
			if (treatment.isPresent()) {
				treatmentRepository.deleteById(id);
				return ResponseEntity.noContent().build();
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}