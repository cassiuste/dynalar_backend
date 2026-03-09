package com.dynalar.dynalar.controller;

import java.time.LocalDateTime;
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

import com.dynalar.dynalar.dto.AutoAssignRequest;
import com.dynalar.dynalar.model.Appointment;
import com.dynalar.dynalar.respository.AppointmentRepository;
import com.dynalar.dynalar.respository.DentistRepository;
import com.dynalar.dynalar.respository.PatientRepository;
import com.dynalar.dynalar.respository.TreatmentRepository;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

	@Autowired
	private AppointmentRepository appointmentRepository;
	
	@Autowired
	private PatientRepository patientRepository;
	
	@Autowired
	private TreatmentRepository treatmentRepository;
	
	@Autowired
	private DentistRepository dentistRepository;

	// ---------------------------------------------------------
	// MÉTODO INTELIGENTE: AUTO-ASIGNAR CITA
	// ---------------------------------------------------------
	@PostMapping("/auto-assign")
	public ResponseEntity<?> autoAssignAppointment(@RequestBody AutoAssignRequest request) {
		try {
			// 1. Obtener Paciente y Tratamiento
			com.dynalar.dynalar.model.patient.Patient patient = patientRepository.findById(request.getPatientId()).orElse(null);
			com.dynalar.dynalar.model.Treatment treatment = treatmentRepository.findById(request.getTreatmentId()).orElse(null);
			
			if (patient == null || treatment == null) {
				return ResponseEntity.badRequest().body("Paciente o Tratamiento no encontrado.");
			}

			LocalDateTime requestedStart = request.getRequestedTime();
			int duration = treatment.getDurationMinutes();
			LocalDateTime requestedEnd = requestedStart.plusMinutes(duration);
			LocalDateTime requestedEndWithCleaning = requestedEnd.plusMinutes(15); // 15 mins extra de limpieza

			// 2. Definir Horarios fijos de la Clínica
			java.time.LocalTime morningStart = java.time.LocalTime.of(9, 0);
			java.time.LocalTime morningEnd = java.time.LocalTime.of(14, 0);
			java.time.LocalTime afternoonStart = java.time.LocalTime.of(15, 0);
			java.time.LocalTime afternoonEnd = java.time.LocalTime.of(20, 0);

			java.time.LocalTime reqStartStr = requestedStart.toLocalTime();
			java.time.LocalTime reqEndStr = requestedEndWithCleaning.toLocalTime();

			boolean isMorning = (reqStartStr.compareTo(morningStart) >= 0 && reqEndStr.compareTo(morningEnd) <= 0);
			boolean isAfternoon = (reqStartStr.compareTo(afternoonStart) >= 0 && reqEndStr.compareTo(afternoonEnd) <= 0);

			if (!isMorning && !isAfternoon) {
				return ResponseEntity.badRequest().body("La cita más el tiempo de limpieza se sale del horario laboral de la clínica.");
			}

			// 3. Buscar todos los dentistas que hacen este tratamiento
			List<com.dynalar.dynalar.model.user.Dentist> qualifiedDentists = dentistRepository.findByTreatments_Id(treatment.getId());

			// 4. Filtrar y encontrar el primero disponible
			com.dynalar.dynalar.model.user.Dentist selectedDentist = null;
			java.time.DayOfWeek dayOfWeek = requestedStart.getDayOfWeek();

			for (com.dynalar.dynalar.model.user.Dentist dentist : qualifiedDentists) {
				// Comprobar si trabaja ese día y ese turno (Usamos Boolean.TRUE.equals para evitar nulos)
				boolean worksShift = false;
				switch (dayOfWeek) {
					case MONDAY: worksShift = isMorning ? Boolean.TRUE.equals(dentist.getMondayMorning()) : Boolean.TRUE.equals(dentist.getMondayAfternoon()); break;
					case TUESDAY: worksShift = isMorning ? Boolean.TRUE.equals(dentist.getTuesdayMorning()) : Boolean.TRUE.equals(dentist.getTuesdayAfternoon()); break;
					case WEDNESDAY: worksShift = isMorning ? Boolean.TRUE.equals(dentist.getWednesdayMorning()) : Boolean.TRUE.equals(dentist.getWednesdayAfternoon()); break;
					case THURSDAY: worksShift = isMorning ? Boolean.TRUE.equals(dentist.getThursdayMorning()) : Boolean.TRUE.equals(dentist.getThursdayAfternoon()); break;
					case FRIDAY: worksShift = isMorning ? Boolean.TRUE.equals(dentist.getFridayMorning()) : Boolean.TRUE.equals(dentist.getFridayAfternoon()); break;
					default: worksShift = false;
				}

				if (worksShift) {
					// Comprobar si tiene la agenda libre ese día a esa hora
					LocalDateTime startOfDay = requestedStart.toLocalDate().atStartOfDay();
					LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);
					
					List<Appointment> existingAppointments = appointmentRepository.findByDentistIdAndStartTimeBetween(dentist.getId(), startOfDay, endOfDay);
					
					boolean hasOverlap = false;
					for (Appointment app : existingAppointments) {
						// El overlap tiene en cuenta los 15 mins extra de limpieza de la cita ya existente
						LocalDateTime appEndWithCleaning = app.getEndTime().plusMinutes(15);
						
						// Lógica de solapamiento: (StartA < EndB) y (EndA > StartB)
						if (requestedStart.isBefore(appEndWithCleaning) && requestedEndWithCleaning.isAfter(app.getStartTime())) {
							hasOverlap = true;
							break;
						}
					}

					if (!hasOverlap) {
						selectedDentist = dentist;
						break; // ¡Encontramos al primer doctor disponible! Rompemos el bucle.
					}
				}
			}

			if (selectedDentist == null) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Sin Doctores Disponibles el dia y hora seleccionada. Pruebe otro dia u otra hora.");
			}

			// 5. Crear y guardar la cita con el doctor asignado automáticamente
			Appointment newAppointment = new Appointment();
			newAppointment.setPatient(patient);
			newAppointment.setTreatment(treatment);
			newAppointment.setDentist(selectedDentist);
			newAppointment.setDurationMinutes(duration);
			newAppointment.setStartTime(requestedStart);
			newAppointment.setEndTime(requestedEnd); // Guardamos la hora médica real. La limpieza es implícita.
			newAppointment.setReason("Cita Auto-Agendada");

			Appointment savedAppointment = appointmentRepository.save(newAppointment);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(savedAppointment);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno procesando la cita.");
		}
	}


	// ---------------------------------------------------------
	// MÉTODOS CRUD ESTÁNDAR
	// ---------------------------------------------------------

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
	
	
	@PostMapping("/available-slots")
	public ResponseEntity<?> getAvailableSlots(@RequestBody com.dynalar.dynalar.dto.SlotRequest request) {
		try {
			com.dynalar.dynalar.model.Treatment treatment = treatmentRepository.findById(request.getTreatmentId()).orElse(null);
			if (treatment == null) {
				return ResponseEntity.badRequest().body("Tratamiento no encontrado.");
			}

			int totalDuration = treatment.getDurationMinutes() + 15; // Duración + Limpieza
			List<com.dynalar.dynalar.model.user.Dentist> qualifiedDentists = dentistRepository.findByTreatments_Id(treatment.getId());

			// Formato de respuesta: Map<Fecha, Lista de Horas> -> Ejemplo: {"2026-03-09": ["09:00", "09:30"]}
			java.util.Map<String, java.util.List<String>> availableSlotsPerDay = new java.util.TreeMap<>();

			// Bucle día a día desde la fecha de inicio hasta la fecha fin
			java.time.LocalDate currentDate = request.getStartDate();
			while (!currentDate.isAfter(request.getEndDate())) {
				java.time.DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
				java.util.List<String> dailySlots = new java.util.ArrayList<>();

				// Generar posibles huecos cada 30 minutos
				java.time.LocalTime[] possibleTimes = {
					java.time.LocalTime.of(9, 0), java.time.LocalTime.of(9, 30), java.time.LocalTime.of(10, 0), java.time.LocalTime.of(10, 30),
					java.time.LocalTime.of(11, 0), java.time.LocalTime.of(11, 30), java.time.LocalTime.of(12, 0), java.time.LocalTime.of(12, 30),
					java.time.LocalTime.of(13, 0), java.time.LocalTime.of(13, 30),
					// Turno de tarde
					java.time.LocalTime.of(15, 0), java.time.LocalTime.of(15, 30), java.time.LocalTime.of(16, 0), java.time.LocalTime.of(16, 30),
					java.time.LocalTime.of(17, 0), java.time.LocalTime.of(17, 30), java.time.LocalTime.of(18, 0), java.time.LocalTime.of(18, 30),
					java.time.LocalTime.of(19, 0), java.time.LocalTime.of(19, 30)
				};

				for (java.time.LocalTime slotTime : possibleTimes) {
					java.time.LocalDateTime slotStart = LocalDateTime.of(currentDate, slotTime);
					java.time.LocalDateTime slotEnd = slotStart.plusMinutes(totalDuration);
					
					boolean isMorningSlot = slotStart.getHour() < 14 && slotEnd.toLocalTime().compareTo(java.time.LocalTime.of(14, 0)) <= 0;
					boolean isAfternoonSlot = slotStart.getHour() >= 15 && slotEnd.toLocalTime().compareTo(java.time.LocalTime.of(20, 0)) <= 0;

					if (!isMorningSlot && !isAfternoonSlot) continue; // Si el hueco se sale del horario, lo saltamos

					boolean isSlotAvailable = false;

					// Comprobar si al menos UN dentista puede coger este hueco
					for (com.dynalar.dynalar.model.user.Dentist dentist : qualifiedDentists) {
						boolean worksShift = false;
						switch (dayOfWeek) {
							case MONDAY: worksShift = isMorningSlot ? Boolean.TRUE.equals(dentist.getMondayMorning()) : Boolean.TRUE.equals(dentist.getMondayAfternoon()); break;
							case TUESDAY: worksShift = isMorningSlot ? Boolean.TRUE.equals(dentist.getTuesdayMorning()) : Boolean.TRUE.equals(dentist.getTuesdayAfternoon()); break;
							case WEDNESDAY: worksShift = isMorningSlot ? Boolean.TRUE.equals(dentist.getWednesdayMorning()) : Boolean.TRUE.equals(dentist.getWednesdayAfternoon()); break;
							case THURSDAY: worksShift = isMorningSlot ? Boolean.TRUE.equals(dentist.getThursdayMorning()) : Boolean.TRUE.equals(dentist.getThursdayAfternoon()); break;
							case FRIDAY: worksShift = isMorningSlot ? Boolean.TRUE.equals(dentist.getFridayMorning()) : Boolean.TRUE.equals(dentist.getFridayAfternoon()); break;
							default: worksShift = false;
						}

						if (worksShift) {
							// Verificar que NO hay solapamiento con otras citas de este dentista
							LocalDateTime startOfDay = currentDate.atStartOfDay();
							LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);
							List<Appointment> existingApps = appointmentRepository.findByDentistIdAndStartTimeBetween(dentist.getId(), startOfDay, endOfDay);
							
							boolean hasOverlap = false;
							for (Appointment app : existingApps) {
								LocalDateTime appEndWithCleaning = app.getEndTime().plusMinutes(15);
								if (slotStart.isBefore(appEndWithCleaning) && slotEnd.isAfter(app.getStartTime())) {
									hasOverlap = true;
									break;
								}
							}

							if (!hasOverlap) {
								isSlotAvailable = true;
								break; // Encontramos un doctor libre, el hueco es válido
							}
						}
					}

					// Si encontramos al menos un doctor libre para esta hora, añadimos la hora a la lista del día
					if (isSlotAvailable) {
						// Formateamos la hora para que el frontend la vea bonita (ej: "09:00")
						dailySlots.add(String.format("%02d:%02d", slotTime.getHour(), slotTime.getMinute()));
					}
				}

				// Solo añadimos el día al JSON si tiene horas disponibles
				if (!dailySlots.isEmpty()) {
					availableSlotsPerDay.put(currentDate.toString(), dailySlots);
				}

				// Avanzamos al día siguiente
				currentDate = currentDate.plusDays(1);
			}

			return ResponseEntity.ok(availableSlotsPerDay);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error calculando los huecos.");
		}
	}
}