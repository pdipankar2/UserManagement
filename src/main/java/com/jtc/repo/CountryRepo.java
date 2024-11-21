package com.jtc.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jtc.entity.CountryEntity;

public interface CountryRepo extends JpaRepository<CountryEntity, Integer> {

}
