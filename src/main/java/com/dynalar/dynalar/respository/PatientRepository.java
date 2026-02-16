package com.dynalar.dynalar.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dynalar.dynalar.model.patient.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

}
