package com.general.repository;

import org.springframework.data.repository.CrudRepository;

import com.general.entity.Clothing;

public interface ClothingRepository extends CrudRepository<Clothing, Integer>{
	
}