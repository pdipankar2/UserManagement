package com.jtc.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jtc.entity.StateEntity;

public interface StateRepo extends JpaRepository<StateEntity, Integer> {

	
	public List<StateEntity> findByCountryId(Integer countryId);
	
}
