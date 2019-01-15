package com.general.controller;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Cacheable;
import javax.servlet.http.HttpSession;

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
import com.general.entity.Record;
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
	
	@Autowired
	private FoodRepository foodRepository;
	
	@Autowired
	private FoodvoucherRepository foodvoucherRepository;
	
	@Autowired
	private TransitionalRepository transitionalRepository;
	
	@Autowired
	private HousingvoucherRepository housingvoucherRepository;
	
	@Autowired
	private ClothingRepository clothingRepository;
	
	@Autowired
	private ClinicRepository clinicRepository;
	
	
	@GetMapping(path = "/loginform")
	public String loginform(Model model, Login login) {
		return "loginform";
	}
	
	@PostMapping(path = "/login")
//	@ResponseStatus(value = HttpStatus.OK)
	public RedirectView login(@ModelAttribute Login login, HttpSession session) {
//		System.out.println("Entered name:" + login.getLoginname());
//		System.out.println("Entered password:" + login.getLoginpassword());
		String name = login.getLoginname();
		String pass = login.getLoginpassword();
		
		List<User> pair = userRepository.findByUsernameAndPassword(name, pass);
		System.out.println(pair);
		
		ArrayList compare = new ArrayList();
		System.out.println(compare);
		
		if(pair.size() == 0) {
			System.out.println("This pair does NOT exist");
			return new RedirectView("/");
		}else {
			session.setAttribute("mySessionAttribute", name);
			System.out.println("hellooooo" + session.getAttribute("mySessionAttribute"));
			return new RedirectView("/dashboard");
		}
	}
	
	
	@GetMapping(path = "/dashboard")
	public String dashboard(HttpSession session, Model model, Record record, Food food, Foodvoucher foodvoucher, Transitional transitional, Housingvoucher housingvoucher) {
		Object currentUser = session.getAttribute("mySessionAttribute");
		//prevent access through URL bar "/dashboard"
		if(currentUser == null) {
			return "homepage";
		}else {
			//reading data
			List<Food> foods = (List<Food>) foodRepository.findAll();
			List<Foodvoucher> foodvouchers = (List<Foodvoucher>) foodvoucherRepository.findAll();
			List<Transitional> houses = (List<Transitional>) transitionalRepository.findAll();
			List<Housingvoucher> housevouchers = (List<Housingvoucher>) housingvoucherRepository.findAll();
			
			//passing data to template
			model.addAttribute("foodpantries", foods);
			model.addAttribute("foodvouchers", foodvouchers);
			model.addAttribute("stores", houses);
			model.addAttribute("vouchers", housevouchers);
			return "dashboard";
		}		
	}
	
	@GetMapping(path = "/logout")
	public RedirectView logout(HttpSession session) {
		//clear session
		session.removeAttribute("mySessionAttribute");
		session.invalidate();
		
		return new RedirectView("/");
	}
	
	
}