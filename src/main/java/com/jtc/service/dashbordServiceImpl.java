package com.jtc.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jtc.dto.quoterApiResponseDTO;

@Service
public class dashbordServiceImpl implements dashbordService {

	private String quoteApiURL = "https://dummyjson.com/quotes/random";

	@Override
	public quoterApiResponseDTO getQuote() {

		RestTemplate rt = new RestTemplate();

		//send HTTP get req and store REsponsesr into quoterApiResponseDTO object
		ResponseEntity<quoterApiResponseDTO> forEntity =
				
				rt.getForEntity(quoteApiURL, quoterApiResponseDTO.class);

		quoterApiResponseDTO body = forEntity.getBody();

		return body;
	}

}
