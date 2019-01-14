package com.general.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.general.entity.Food;

public interface FoodRepository extends CrudRepository<Food, Integer>{
	
}