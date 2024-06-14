package com.wojteknier03.clinicmedical.repository;


import com.wojteknier03.clinicmedical.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findById(Long id);
    Optional<AppUser> findByUsername(String username);
}
