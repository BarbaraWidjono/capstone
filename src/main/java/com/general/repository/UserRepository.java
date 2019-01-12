package com.general.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.general.entity.User;

public interface UserRepository extends CrudRepository<User, Integer>{
	List<User> findByUsername(String Username);
	List<User> findByPassword(String Password);
	List<User> findByUsernameAndPassword(String Username, String Password);
}