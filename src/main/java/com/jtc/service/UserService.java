package com.jtc.service;

import java.util.Map;

import com.jtc.dto.LoginFromDTO;
import com.jtc.dto.RegisterFromDTO;
import com.jtc.dto.ResetPwdFromDTO;
import com.jtc.dto.UserDTO;

public interface UserService {

	 //all the country data from databases
   public  Map<Integer,String> getCountries(); 

   
   //provide country data based on country id
   public   Map<Integer,String> getStates(Integer countryId);

   //provide city data based on city id
   public Map<Integer,String> getCities(Integer stateId);

   public boolean duplicateEmailCheck(String email);

   public boolean saveUser(RegisterFromDTO regFromDTO);

   public  UserDTO login(LoginFromDTO loginFromDTO);   

   public boolean resetPwd (ResetPwdFromDTO resetPwd);
   
   //public UserDTO getUserByEmail(String email);
	
}
