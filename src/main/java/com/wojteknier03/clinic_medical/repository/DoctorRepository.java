package com.wojteknier03.clinic_medical.repository;

import com.wojteknier03.clinic_medical.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
