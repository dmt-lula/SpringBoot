package com.example.accessingdatamysql;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

// https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html
public interface UserRepository extends CrudRepository<User_Tomek, Integer> {
    List<User_Tomek> findByEmail(String email);
    Optional<User_Tomek> findById(Integer id);
    Iterable<User_Tomek> findByName(String name);
    Iterable<User_Tomek> findByCity(String city);
}