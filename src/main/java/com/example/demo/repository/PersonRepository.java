package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.domain.Person;

public interface PersonRepository extends CrudRepository<Person, Long> {
  
}
