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

	
}