package com.jtc.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jtc.entity.CityEntety;

public interface CityRepo  extends JpaRepository<CityEntety, Integer>{

	
	public List<CityEntety> findByStateId(Integer stateId);
}
