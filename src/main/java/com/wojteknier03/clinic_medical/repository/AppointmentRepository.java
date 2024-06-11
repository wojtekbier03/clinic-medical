package com.wojteknier03.clinic_medical.repository;

import com.wojteknier03.clinic_medical.model.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientId(Long patientId);
    boolean existsByStartTime(LocalDateTime startTime);
    Page<Appointment> findAll(Pageable pageable);
}
