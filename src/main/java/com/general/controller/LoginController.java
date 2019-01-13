package com.general.controller;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.general.repository.UserRepository;
import com.general.entity.User;
import com.general.entity.Clinic;
import com.general.entity.Clothing;
import com.general.entity.Food;
import com.general.entity.Foodvoucher;
import com.general.entity.Geometry;
import com.general.entity.Housingvoucher;
import com.general.entity.Login;
import com.general.entity.Response;
import com.general.entity.Transitional;
import com.general.entity.Text;

import com.general.repository.ClinicRepository;
import com.general.repository.ClothingRepository;
import com.general.repository.FoodRepository;
import com.general.repository.FoodvoucherRepository;
import com.general.repository.HousingvoucherRepository;
import com.general.repository.TransitionalRepository;

import com.google.gson.Gson; 
import com.google.gson.GsonBuilder; 


@Controller
public class LoginController{
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping(path = "/loginform")
	public String loginform(Model model, Login login) {
		return "loginform";
	}
	
	@PostMapping(path = "/login")
	@ResponseStatus(value = HttpStatus.OK)
	public void login(@ModelAttribute Login login) {
		System.out.println("Entered name:" + login.getLoginname());
		System.out.println("Entered password:" + login.getLoginpassword());
		String name = login.getLoginname();
		String pass = login.getLoginpassword();
		
		List<User> pair = userRepository.findByUsernameAndPassword(name, pass);
		System.out.println(pair);
		
		ArrayList compare = new ArrayList();
		System.out.println(compare);
		
		if(pair.size() == 0) {
			System.out.println("This pair does NOT exist");
		}else {
			System.out.println("This pair exists");
		}
	}
}