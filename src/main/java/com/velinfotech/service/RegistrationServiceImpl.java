package com.velinfotech.service;

import com.velinfotech.model.LearningMode;
import com.velinfotech.model.Registration;
import com.velinfotech.repository.RegistrationRepository;
//import com.velinfotech.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RegistrationServiceImpl{


        @Autowired
        private RegistrationRepository repository;  // Dependency Injection

        // Create
        public Registration saveRegistration(Registration registration) {
            return repository.save(registration);
        }

        // Read All
        public List<Registration> getAllRegistrations() {
            return repository.findAll();
        }

        // Read By Id
        public Optional<Registration> getRegistrationById(Long id) {
            return repository.findById(id);
        }

        // Update
        public Registration updateRegistration(Long id, Registration updated) {
            return repository.findById(id)
                    .map(existing -> {
                        existing.setName(updated.getName());
                        existing.setEmail(updated.getEmail());
                        existing.setMobile(updated.getMobile());
                        existing.setCourse(updated.getCourse());
                        existing.setMode(updated.getMode());
                        existing.setMessage(updated.getMessage());
                        return repository.save(existing);
                    })
                    .orElseThrow(() -> new RuntimeException("Registration not found with id " + id));
        }

        // Delete
        public void deleteRegistration(Long id) {
            repository.deleteById(id);
        }
    }

//    package com.velinfotech.service;
//
//import com.velinfotech.model.Registration;
//import com.velinfotech.repository.RegistrationRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//    @Service
//    public class RegistrationService {

//@Service
//@Transactional
//public class RegistrationServiceImpl implements RegistrationService {

//    private static final Sort NEWEST_FIRST = Sort.by(Sort.Direction.DESC, "createdAt");
//
//    private final RegistrationRepository repo;
//
//    public RegistrationServiceImpl(RegistrationRepository repo) {
//        this.repo = repo;
//    }
//
//    @Override
//    public Registration create(Registration registration) {
//        return repo.save(registration);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<Registration> findAll() {
//        return repo.findAll(NEWEST_FIRST);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public Optional<Registration> findById(Long id) {
//        return repo.findById(id);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<Registration> search(LearningMode mode, String course) {
//        boolean hasMode = mode != null;
//        boolean hasCourse = course != null && !course.isBlank();
//
//        if (hasMode && hasCourse) {
//            return repo.findByModeAndCourseContainingIgnoreCaseOrderByCreatedAtDesc(mode, course);
//        } else if (hasMode) {
//            return repo.findByModeOrderByCreatedAtDesc(mode);
//        } else if (hasCourse) {
//            return repo.findByCourseContainingIgnoreCaseOrderByCreatedAtDesc(course);
//        } else {
//            return repo.findAll(NEWEST_FIRST);
//        }
//    }
//
//    @Override
//    public boolean deleteById(Long id) {
//        if (!repo.existsById(id)) return false;
//        repo.deleteById(id);
//        return true;
//    }

