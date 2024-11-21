package com.jtc.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jtc.dto.LoginFromDTO;
import com.jtc.dto.RegisterFromDTO;
import com.jtc.dto.ResetPwdFromDTO;
import com.jtc.dto.UserDTO;
import com.jtc.dto.quoterApiResponseDTO;
import com.jtc.service.UserService;
import com.jtc.service.dashbordService;

@Controller
public class UserController {

	private dashbordService dashbord;

	private UserService userService;

	public UserController(dashbordService dashbord, UserService userService) {
		super();
		this.dashbord = dashbord;
		this.userService = userService;
	}



	
	@GetMapping("/resetpwd_Page")
	public String loadResetPwdPage(@RequestParam("email")  String email, Model model) {

		ResetPwdFromDTO resetPwdFromDTO = new ResetPwdFromDTO();
		resetPwdFromDTO.setEmail(email);
		model.addAttribute("resetPwd", resetPwdFromDTO);

		return "reset-pwd";
	}

	@PostMapping("/resetPwd")
	public String handelPwdReset(ResetPwdFromDTO resetPwdFromDTO, Model model) {

		boolean resetPwd = userService.resetPwd(resetPwdFromDTO);
		if (resetPwd) {
			return "redirect:dashbord";
		}
		return "reset-pwd";
	}

	

	@GetMapping("/states/{countryId}") // read the data from UI
	@ResponseBody
	public Map<Integer, String> getStates(@PathVariable Integer countryId, Model model) {

		Map<Integer, String> statesMap = userService.getStates(countryId);

		System.out.println(statesMap);
		return statesMap;
	}

	@GetMapping("/cities/{stateId}")
	@ResponseBody // direct send the response to the UI without any pages
	public Map<Integer, String> getCities(@PathVariable Integer stateId, Model model) {

		Map<Integer, String> citiesMap = userService.getStates(stateId);

		model.addAttribute("countries", citiesMap);

		return citiesMap;
	}

	@GetMapping("/register")
	public String loadRegisterPage(Model model) {

		Map<Integer, String> countriesMap = userService.getCountries();

		model.addAttribute("countries", countriesMap);

		RegisterFromDTO registerFrom = new RegisterFromDTO();

		model.addAttribute("registerFrom", registerFrom);

		return "register";
	}

	// handel registration

	@PostMapping("/register")
	public String handelRegisterPage(@ModelAttribute ("fromDTO") RegisterFromDTO fromDTO, Model model) {

		boolean status = userService.duplicateEmailCheck(fromDTO.getEmail());

		if (status) {

			model.addAttribute("errMsg", "Duplicat Email Found User Already Register");

		} else {

			boolean saveUser = userService.saveUser(fromDTO);

			if (saveUser) {
				// user save

				model.addAttribute("succMsg", "Registration SuccessFull,Please check your email");

			} else {

				// failed to save

				model.addAttribute("errMsg", "User  Register Failed!");

			}

		}

		
		model.addAttribute("registerFrom", new RegisterFromDTO());

		model.addAttribute("countries", userService.getCountries());
		return "register";
	}
	
	
	
	@GetMapping("/")
	public String index(Model model) {

		LoginFromDTO loginFromDTO = new LoginFromDTO();
		model.addAttribute("loginFrom", loginFromDTO);
		return "login";
	}
	
	
	@PostMapping("/login")
	public String handelUserLogin(LoginFromDTO loginFromDTO, Model model) {

		UserDTO userDTO = userService.login(loginFromDTO);

		if (userDTO == null) {

			model.addAttribute("errMsg", "invalid credintial");
			model.addAttribute("loginFrom", new LoginFromDTO());

		} else {
			String pwdUpdated = userDTO.getPwdUpdated();
			if ("Yes".equals(pwdUpdated)) {
				// display dashbord

				return "redirect:dashbord";
			} else {
				// display reset pwd page
				return "redirect:reset-pwd?email=" + userDTO.getEmail();
				// return "redirect:reset_pwd";
			}
		}
		return "login";
	}

	
	@GetMapping("/dashbord")
	public String dashbord(Model model) {

		quoterApiResponseDTO quote = dashbord.getQuote();
		model.addAttribute("quote", quote);

		return "dashbord";
	}

}
