package com.dynalar.dynalar.respository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dynalar.dynalar.model.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

	List<Appointment> findByDentistIdAndStartTimeBetween(Long dentistId, LocalDateTime start, LocalDateTime end);
}