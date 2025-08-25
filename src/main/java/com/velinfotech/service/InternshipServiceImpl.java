package com.velinfotech.service;


import com.velinfotech.model.Internship;
import com.velinfotech.repository.InternshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InternshipServiceImpl {

    @Autowired
    private InternshipRepository internshipRepository;

    // CREATE
    public Internship createInternship(Internship internship) {
        return internshipRepository.save(internship);
    }

    // READ all
    public List<Internship> getAllInternships() {
        return internshipRepository.findAll();
    }

    // READ one
    public Internship getInternshipById(Long id) {
        return internshipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Internship not found with id: " + id));
    }

     //UPDATE
    public Internship updateInternship(Long id, Internship updatedInternship) {
        return internshipRepository.findById(id)
                .map(existing -> {
                    existing.setFullName(updatedInternship.getFullName());
                    existing.setEmail(updatedInternship.getEmail());
                    existing.setPhoneNumber(updatedInternship.getPhoneNumber());
                    existing.setCourse(updatedInternship.getCourse());
                    existing.setMessage(updatedInternship.getMessage());
                    return internshipRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Internship not found with id: " + id));
    }

//    public Internship updateInternship(Long id, Internship updatedInternship) {
//        return internshipRepository.findById(id)
//                .map(existing -> {
//                    // update only what you want
//                    if (updatedInternship.getFullName() != null) {
//                        existing.setFullName(updatedInternship.getFullName());
//                    }
//                    if (updatedInternship.getPhoneNumber() != null) {
//                        existing.setPhoneNumber(updatedInternship.getPhoneNumber());
//                    }
//                    if (updatedInternship.getCourse() != null) {
//                        existing.setCourse(updatedInternship.getCourse());
//                    }
//                    if (updatedInternship.getMessage() != null) {
//                        existing.setMessage(updatedInternship.getMessage());
//                    }
//
//                    // email is NOT updated here (kept same)
//                    return internshipRepository.save(existing);
//                })
//                .orElseThrow(() -> new RuntimeException("Internship not found with id: " + id));
//    }


    // DELETE
    public void deleteInternship(Long id) {
        if (!internshipRepository.existsById(id)) {
            throw new RuntimeException("Internship not found with id: " + id);
        }
        internshipRepository.deleteById(id);
    }
}
