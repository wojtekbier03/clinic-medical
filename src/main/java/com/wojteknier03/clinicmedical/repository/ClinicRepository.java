package com.wojteknier03.clinicmedical.repository;

import com.wojteknier03.clinicmedical.model.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicRepository extends JpaRepository<Clinic, Long> {
}
