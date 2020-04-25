package com.example.firstproject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.firstproject.model.Patient;

@RestController
@CrossOrigin(origins = "*")
public class PatientRESTController {

	@Autowired
	private InMemoryPatientRepository repo;

	@GetMapping(value = "/patients")
	public List<Patient> getAll() {
		return (List<Patient>) repo.findAll();
	}

	@PostMapping(value = "/patients")
	public void addPatient(@RequestBody Patient patient) {
		repo.save(patient);
	}

	@GetMapping("/patient")
	public Patient getPatient(@RequestParam(name = "id") long id) {
		return repo.get(id);
	}
}
