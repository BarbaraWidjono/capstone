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
public class HomepageController{
	public static final String ACCOUNT_SID = "";
	public static final String AUTH_TOKEN = "";
	
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
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping(path = "/")
	public String homepage() {
		return "homepage";
	}
	
	@PostMapping(path = "/text")
//	@ResponseStatus(value = HttpStatus.OK)
	public RedirectView message(@ModelAttribute Text text) {
		
		String textBody = text.getName() + " " + text.getAddress() + " " + text.getResnumber() + " " + text.getInfo();
		String receiver = "+1" + text.getNumber();
		
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

	    Message message = Message.creator(new PhoneNumber(receiver),
	        new PhoneNumber("+12064389389"), 
	        textBody).create();
	   
	    //Insert Error Handling
	    //System.out.println(message.getSid());
	    //System.out.println(message.getErrorCode());
	    if(message.getErrorCode() == null) {
	    	return new RedirectView("/");
	    }else {
	    	return new RedirectView("/");
	    }	
	}
	
	@GetMapping(path="/transitional")
	public String transitional(Model model) {
		List<Transitional> houses = (List<Transitional>) transitionalRepository.findAll();

		model.addAttribute("heading", "Transitional Housing");
		model.addAttribute("stores", houses);
		return "results";
	}
	
	@GetMapping(path="/housingvoucher")
	public String housingvoucher(Model model) {
		List<Housingvoucher> houses = (List<Housingvoucher>) housingvoucherRepository.findAll();

		model.addAttribute("heading", "Housing Vouchers");
		model.addAttribute("stores", houses);
		return "results";
	}
	
	@GetMapping(path="/clothing")
	public String clothing(Model model) {
		List<Clothing> clothes = (List<Clothing>) clothingRepository.findAll();

		model.addAttribute("heading", "Clothing");
		model.addAttribute("stores", clothes);
		return "results";
	}
	
	@GetMapping(path="/clinic")
	public String clinic(Model model) {
		List<Clinic> clinics = (List<Clinic>) clinicRepository.findAll();

		model.addAttribute("heading", "Clinics");
		model.addAttribute("stores", clinics);
		return "results";
	}
	
	//http://localhost:8080/addfoodpantry?name=***&street=***&city=***&state=***&zipcode=***&phone=***&info=***&website=***
	@GetMapping(path="/addfoodpantry")
	@ResponseBody
	public void addFood(@RequestParam String name, @RequestParam String street, @RequestParam String city, @RequestParam String state, @RequestParam String zipcode, @RequestParam String phone, @RequestParam String info, @RequestParam String website) {
		Food food = new Food();
		food.setName(name);
		food.setStreet(street);
		food.setCity(city);
		food.setState(state);
		food.setZipcode(zipcode);
		food.setPhone(phone);
		food.setInfo(info);
		food.setWebsite(website);
		foodRepository.save(food);
	}
	
	//http://localhost:8080/addfoodvoucher?name=***&street=***&city=***&state=***&zipcode=***&phone=***&info=***&website=***
	@GetMapping(path="/addfoodvoucher")
	@ResponseBody
	public void addFoodvoucher(@RequestParam String name, @RequestParam String street, @RequestParam String city, @RequestParam String state, @RequestParam String zipcode, @RequestParam String phone, @RequestParam String info, @RequestParam String website) {
		Foodvoucher food = new Foodvoucher();
		food.setName(name);
		food.setStreet(street);
		food.setCity(city);
		food.setState(state);
		food.setZipcode(zipcode);
		food.setPhone(phone);
		food.setInfo(info);
		food.setWebsite(website);
		foodvoucherRepository.save(food);
	}
		
		//http://localhost:8080/addtransitional?name=***&street=***&city=***&state=***&zipcode=***&phone=***&info=***&website=***
		@GetMapping(path="/addtransitional")
		@ResponseBody
		public void addTransitional(@RequestParam String name, @RequestParam String street, @RequestParam String city, @RequestParam String state, @RequestParam String zipcode, @RequestParam String phone, @RequestParam String info, @RequestParam String website) {
			Transitional house = new Transitional();
			house.setName(name);
			house.setStreet(street);
			house.setCity(city);
			house.setState(state);
			house.setZipcode(zipcode);
			house.setPhone(phone);
			house.setInfo(info);
			house.setWebsite(website);
			transitionalRepository.save(house);
		}
		
		//http://localhost:8080/addhousingvoucher?name=***&street=***&city=***&state=***&zipcode=***&phone=***&info=***&website=***
		@GetMapping(path="/addhousingvoucher")
		@ResponseBody
		public void addhousingvoucher(@RequestParam String name, @RequestParam String street, @RequestParam String city, @RequestParam String state, @RequestParam String zipcode, @RequestParam String phone, @RequestParam String info, @RequestParam String website) {
			Housingvoucher house = new Housingvoucher();
			house.setName(name);
			house.setStreet(street);
			house.setCity(city);
			house.setState(state);
			house.setZipcode(zipcode);
			house.setPhone(phone);
			house.setInfo(info);
			house.setWebsite(website);
			housingvoucherRepository.save(house);
		}
		
		//http://localhost:8080/addclothing?name=***&street=***&city=***&state=***&zipcode=***&phone=***&info=***&website=***
		@GetMapping(path="/addclothing")
		@ResponseBody
		public void addclothing(@RequestParam String name, @RequestParam String street, @RequestParam String city, @RequestParam String state, @RequestParam String zipcode, @RequestParam String phone, @RequestParam String info, @RequestParam String website) {
			Clothing clothes = new Clothing();
			clothes.setName(name);
			clothes.setStreet(street);
			clothes.setCity(city);
			clothes.setState(state);
			clothes.setZipcode(zipcode);
			clothes.setPhone(phone);
			clothes.setInfo(info);
			clothes.setWebsite(website);
			clothingRepository.save(clothes);
		}
		
		//http://localhost:8080/addclinic?name=***&street=***&city=***&state=***&zipcode=***&phone=***&info=***&website=***
		@GetMapping(path="/addclinic")
		@ResponseBody
		public void addclinic(@RequestParam String name, @RequestParam String street, @RequestParam String city, @RequestParam String state, @RequestParam String zipcode, @RequestParam String phone, @RequestParam String info, @RequestParam String website) {
			Clinic office = new Clinic();
			office.setName(name);
			office.setStreet(street);
			office.setCity(city);
			office.setState(state);
			office.setZipcode(zipcode);
			office.setPhone(phone);
			office.setInfo(info);
			office.setWebsite(website);
			clinicRepository.save(office);
		}
}