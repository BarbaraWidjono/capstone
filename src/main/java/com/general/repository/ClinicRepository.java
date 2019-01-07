package com.general.repository;

import org.springframework.data.repository.CrudRepository;

import com.general.entity.Clinic;

public interface ClinicRepository extends CrudRepository<Clinic, Integer>{
	
}