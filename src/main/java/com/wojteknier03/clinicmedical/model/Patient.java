package com.wojteknier03.clinicmedical.model;

import com.wojteknier03.clinicmedical.dto.PatientDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String idCardNo;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate birthday;

    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointments;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private AppUser user;

    public Patient(Long id, String email, String idCardNo, String firstName, String lastName, String phoneNumber, LocalDate birthday, AppUser user) {
        this.id = id;
        this.email = email;
        this.idCardNo = idCardNo;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient)) return false;
        Patient patient = (Patient) o;
        return this.id != null && id.equals(patient.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", idCardNo='" + idCardNo + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", birthday=" + birthday +
                ", user=" + user +
                '}';
    }

    public void updateFromDto(PatientDto updatedPatientDto) {
        this.setEmail(updatedPatientDto.getEmail());
        this.setFirstName(updatedPatientDto.getFirstName());
        this.setLastName(updatedPatientDto.getLastName());
        this.setIdCardNo(updatedPatientDto.getIdCardNo());
        this.setPhoneNumber(updatedPatientDto.getPhoneNumber());
        this.setBirthday(updatedPatientDto.getBirthday());
    }
}
