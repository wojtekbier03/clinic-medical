package com.wojteknier03.clinicmedical.repository;

import com.wojteknier03.clinicmedical.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
