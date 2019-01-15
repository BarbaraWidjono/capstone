package com.general.controller;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
public class FoodvoucherController{
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
	
	@GetMapping(path="/foodvouchers")
	public String foodVouchers(Model model, Text text) {
		//Read from db
		List<Foodvoucher> foods = (List<Foodvoucher>) foodvoucherRepository.findAll();
		
		ArrayList<HashMap> coordinates = new ArrayList<HashMap>();
		
		for(Foodvoucher food : foods) {
			//Create endpoint
			String baseURL = "https://maps.googleapis.com/maps/api/geocode/json?address=";
			String street = food.getStreet();
			String endURL = ",+Seattle,+WA&key=AIzaSyDY4Cy_ubPYVZrVyzU3Ylrxg63bwe0xZn8";
			String endpoint = baseURL + street + endURL;
			System.out.println(endpoint);
			
			//api call
			RestTemplate restTemplate = new RestTemplate();
			String resourceURL = endpoint;
			ResponseEntity<String> responsey = restTemplate.getForEntity(resourceURL, String.class);
			String jsonString = responsey.getBody();
			
			GsonBuilder builder = new GsonBuilder();
			builder.setPrettyPrinting();
			Gson gson = builder.create();
			Response response = gson.fromJson(jsonString, Response.class);
			
			//Convert coordinates to string
			Double latitude = response.getResults().get(0).getGeometry().getLocation().getLat();
			String latitudeString = String.valueOf(latitude);			
			Double longitude = response.getResults().get(0).getGeometry().getLocation().getLng();
			String longitudeString = String.valueOf(longitude);
			
			//Insert gps coordinates
			HashMap<String, String> location = new HashMap<String, String>();
			location.put("lat", latitudeString);
			location.put("lng", longitudeString);

			//Create infowindows
			String infowindow = String.format("<div id=\"content\"><div id=\"siteNotice\"></div>\n" + 
					"<h1 id=\"firstHeading\" class=\"firstHeading\">%s</h1>\n" + 
					"<div id=\"bodyContent\">\n" + 
					"<p>%s</p> \n" + 
					"<p>%s</p> \n" + 
					"<p>%s</p> \n" + 
					"</div>\n" + 
					"</div>", food.getName(), food.getStreet(), food.getPhone(), food.getInfo());
			location.put("info", infowindow);
			coordinates.add(location);
			System.out.println(coordinates);			
		}
				
		//passing data to template
		model.addAttribute("sources", coordinates);
		model.addAttribute("heading", "Food Vouchers");
		model.addAttribute("stores", foods);
		return "foodvouchers";
	}
	
	@PostMapping(path = "/textfoodvouchers")
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
	    	return new RedirectView("/foodvouchers");
	    }else {
	    	return new RedirectView("/foodvouchers");
	    }	
	}
	
	@PostMapping(path="/addfoodvoucher")
	@ResponseBody
	public RedirectView addFoodvoucher(@ModelAttribute Foodvoucher foodvoucher, HttpSession session) {
		Foodvoucher newfood = new Foodvoucher();
		newfood.setName(foodvoucher.getName());
		newfood.setStreet(foodvoucher.getStreet());
		newfood.setCity(foodvoucher.getCity());
		newfood.setState(foodvoucher.getState());
		newfood.setZipcode(foodvoucher.getZipcode());
		newfood.setPhone(foodvoucher.getPhone());
		newfood.setInfo(foodvoucher.getInfo());
		newfood.setWebsite(foodvoucher.getWebsite());
		foodvoucherRepository.save(newfood);
		
		session.setAttribute("mySessionAttribute", "tempuser");
		return new RedirectView("/dashboard");
	}
	
	@GetMapping("/deletefoodvoucher/{id}")
	public String deleteFoodvoucher(@PathVariable("id") Integer id, Model model, Food food, Foodvoucher foodvoucher, Transitional transitional, Housingvoucher housingvoucher, Clothing clothing) {
		foodvoucherRepository.deleteById(id);
		
		List<Food> foods = (List<Food>) foodRepository.findAll();
		model.addAttribute("foodpantries", foods);
		
		List<Foodvoucher> foodvouchers = (List<Foodvoucher>) foodvoucherRepository.findAll();
		model.addAttribute("foodvouchers", foodvouchers);
		
		List<Transitional> houses = (List<Transitional>) transitionalRepository.findAll();
		model.addAttribute("stores", houses);
		
		List<Housingvoucher> housevouchers = (List<Housingvoucher>) housingvoucherRepository.findAll();
		model.addAttribute("vouchers", housevouchers);
		
		List<Clothing> clothes = (List<Clothing>) clothingRepository.findAll();
		model.addAttribute("clothes", clothes);
		
		return "dashboard";
	}
}