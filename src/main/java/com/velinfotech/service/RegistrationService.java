package com.velinfotech.service;

import com.velinfotech.model.LearningMode;
import com.velinfotech.model.Registration;

import java.util.List;
import java.util.Optional;

public interface RegistrationService {

    Registration create(Registration registration);

    List<Registration> findAll();  // newest first

    Optional<Registration> findById(Long id);

    List<Registration> search(LearningMode mode, String course);

    /** @return true if deleted, false if not found */
    boolean deleteById(Long id);
}
