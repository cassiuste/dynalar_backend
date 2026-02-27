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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dynalar.dynalar.model.Appointment;
import com.dynalar.dynalar.respository.AppointmentRepository;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

	@Autowired
	private AppointmentRepository appointmentRepository;

	@GetMapping("/index")
	public @ResponseBody ResponseEntity<List<Appointment>> getAllAppointments() {
		try {
			return ResponseEntity.ok(appointmentRepository.findAll());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(404).build();
		}
	}

	@PostMapping()
	public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
		try {
			Appointment newAppointment = appointmentRepository.save(appointment);
			return ResponseEntity.status(HttpStatus.CREATED).body(newAppointment);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}
	@GetMapping("/{id}")
	public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
		try {
			Appointment appointment = appointmentRepository.findById(id).orElse(null);
			if (appointment == null) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(appointment);
		} catch (Exception e) {
			return ResponseEntity.status(404).build();
		}
	}

	@PutMapping("/update")
	public ResponseEntity<Appointment> updateAppointment(@RequestBody Appointment updatedAppointment) {
		try {
			Long id = updatedAppointment.getId();
			if (id == null) {
				return ResponseEntity.badRequest().build();
			}

			Optional<Appointment> existingOpt = appointmentRepository.findById(id);
			if (existingOpt.isEmpty()) {
				return ResponseEntity.notFound().build();
			}

			Appointment existingAppointment = existingOpt.get();


			existingAppointment.setReason(updatedAppointment.getReason());
			existingAppointment.setDurationMinutes(updatedAppointment.getDurationMinutes());
			existingAppointment.setStartTime(updatedAppointment.getStartTime());
			existingAppointment.setEndTime(updatedAppointment.getEndTime());

			if (updatedAppointment.getTreatment() != null) {
				existingAppointment.setTreatment(updatedAppointment.getTreatment());
			}
			if (updatedAppointment.getDentist() != null) {
				existingAppointment.setDentist(updatedAppointment.getDentist());
			}
			if (updatedAppointment.getPatient() != null) {
				existingAppointment.setPatient(updatedAppointment.getPatient());
			}

			Appointment savedAppointment = appointmentRepository.save(existingAppointment);
			return ResponseEntity.ok(savedAppointment);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(404).build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
		try {
			Optional<Appointment> appointment = appointmentRepository.findById(id);
			if (appointment.isPresent()) {
				appointmentRepository.deleteById(id);
				return ResponseEntity.noContent().build();
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(404).build();
		}
	}
	
}