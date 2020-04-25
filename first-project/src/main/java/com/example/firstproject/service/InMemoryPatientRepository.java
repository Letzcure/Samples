package com.example.firstproject.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.ComponentScan;

import com.example.firstproject.model.Patient;

@ComponentScan
public class InMemoryPatientRepository {

	private static long idCounter = 0l;

	private List<Patient> dataStore = new ArrayList<>();

	public void save(Patient entity) {
		entity.setId(++idCounter);
		dataStore.add(entity);
	}

	public Iterable<Patient> findAll() {
		return dataStore;
	}

	public Patient get(long id) {
		return dataStore.stream().filter(patient -> patient.getId() == id).findFirst().orElse(null);
	}
}
