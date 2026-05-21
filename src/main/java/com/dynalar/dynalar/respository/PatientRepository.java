package com.dynalar.dynalar.respository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dynalar.dynalar.model.patient.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

	Page<Patient> findByNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrDniContainingIgnoreCase(String name, String lastName, String dni, Pageable pageable);
	Page<Patient> findByNameStartingWithIgnoreCase(String initial, Pageable pageable);

}
