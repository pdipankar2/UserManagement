package com.jtc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.jtc.dto.LoginFromDTO;
import com.jtc.dto.RegisterFromDTO;
import com.jtc.dto.ResetPwdFromDTO;
import com.jtc.dto.UserDTO;
import com.jtc.entity.CityEntety;
import com.jtc.entity.CountryEntity;
import com.jtc.entity.StateEntity;
import com.jtc.entity.UserEntity;
import com.jtc.repo.CityRepo;
import com.jtc.repo.CountryRepo;
import com.jtc.repo.StateRepo;
import com.jtc.repo.UserRepo;
import com.jtc.util.EmailUitlService;

@Service
public class UserServiceImpl implements UserService {

	private CountryRepo countryRepo;

	private StateRepo stateRepo;

	private CityRepo cityRepo;

	private UserRepo userRepo;

	private EmailUitlService emailUitlService;

	public UserServiceImpl(CountryRepo countryRepo, StateRepo stateRepo, CityRepo cityRepo, UserRepo userRepo,
			EmailUitlService emailUitlService) {
		super();
		this.countryRepo = countryRepo;
		this.stateRepo = stateRepo;
		this.cityRepo = cityRepo;
		this.userRepo = userRepo;
		this.emailUitlService = emailUitlService;
	}

	@Override
	public Map<Integer, String> getCountries() {

		List<CountryEntity> countriesList = countryRepo.findAll();
		
		Map<Integer, String> countryMap = new HashMap<>();
		
		countriesList.stream().forEach(c -> {
			countryMap.put(c.getCountryId(), c.getCountryName());
		});
		return countryMap;
	}

	@Override
	public Map<Integer, String> getStates(Integer countryId) {

		Map<Integer, String> stateMap = new HashMap<>();

		List<StateEntity> stateList = stateRepo.findByCountryId(countryId);

		stateList.forEach(s -> {
			stateMap.put(s.getStateId(), s.getStateName());
		});

		return stateMap;
	}

	@Override
	public Map<Integer, String> getCities(Integer stateId) {

		Map<Integer, String> citysMap = new HashMap<>();

		List<CityEntety> byCitylist = cityRepo.findByStateId(stateId);

		byCitylist.forEach(c -> {

			citysMap.put(c.getCityId(), c.getCityName());
		});

		return citysMap;
	}

	@Override
	public boolean duplicateEmailCheck(String email) {

		UserEntity byEmail = userRepo.findByEmail(email);

		if (byEmail != null) {

			return true;
		} else {
			return false;

		}
	}

	@Override
	public boolean saveUser(RegisterFromDTO regFromDTO) {
		
		UserEntity entity = new UserEntity();
		BeanUtils.copyProperties(regFromDTO, entity); // copy data one object to another object

		CountryEntity country = countryRepo.findById(regFromDTO.getCityId()).orElse(null);

		entity.setCountry(country);

		StateEntity state = stateRepo.findById(regFromDTO.getStateId()).orElse(null);

		entity.setState(state);

		CityEntety city = cityRepo.findById(regFromDTO.getCityId()).orElse(null);
		entity.setCity(city);

		String randomPwd = generateRandomPwd();

		entity.setPwd(randomPwd);
		entity.setPwdUpdated("No");

		UserEntity saveUser = userRepo.save(entity);

		if (null != saveUser.getUserId()) {

			String subject = "Your Account Created";
			String body = " Your Password to Login :-" + randomPwd;
			String to = regFromDTO.getEmail();

			emailUitlService.sendEmail(subject, body, to);

			return true;

		}

		return false;
	}

	@Override
	public UserDTO login(LoginFromDTO loginFromDTO) {

		UserEntity userEntity = userRepo.findByEmailAndPwd(loginFromDTO.getEmail(), loginFromDTO.getPwd());

		if (userEntity != null) {
			UserDTO userDTO = new UserDTO();

			BeanUtils.copyProperties(userEntity, userDTO);
			return userDTO;

		}

		return null;
	}

	@Override
	public boolean resetPwd(ResetPwdFromDTO resetPwd) {

      String email = resetPwd.getEmail();
      
      UserEntity entity = userRepo.findByEmail(email);
      //setting new pwd
      entity.setPwd(resetPwd.getNewPwd());
      entity.setPwdUpdated("Yes");
      
      userRepo.save(entity); //UPSERT Method in data JPA
		
		return true;
	}

	private String generateRandomPwd() {

		String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
		String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		String ALL_CHARACTERS = LOWERCASE + UPPERCASE;

		Random random = new Random();

		StringBuffer password = new StringBuffer();

		for (int i = 0; i < 5; i++) {
			// give any number 0 to 51
			int randomIndex = random.nextInt(ALL_CHARACTERS.length());

			password.append(ALL_CHARACTERS.charAt(randomIndex));

		}

		return password.toString();
	}

}
