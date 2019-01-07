package com.general.repository;

import org.springframework.data.repository.CrudRepository;

import com.general.entity.Food;

public interface FoodRepository extends CrudRepository<Food, Integer>{
	
}