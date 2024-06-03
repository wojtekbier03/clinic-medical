package com.wojteknier03.clinic_medical.repository;

import com.wojteknier03.clinic_medical.model.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicRepository extends JpaRepository<Clinic, Long> {
}
