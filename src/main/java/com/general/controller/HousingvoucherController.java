package com.general.controller;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
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
public class HousingvoucherController{
	
	public static final String ACCOUNT_SID = "AC7c293b1b5fe37e56b91c8d9f9b3a7841";
	public static final String AUTH_TOKEN = "0ec8395d9aeba626c45725cf80228bc9";
	
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
	
	@GetMapping(path="/housingvoucher")
	public String housingvoucher(Model model, Text text) {
		//read db
		List<Housingvoucher> houses = (List<Housingvoucher>) housingvoucherRepository.findAll();
		
		ArrayList<HashMap> coordinates = new ArrayList<HashMap>();
		
		for(Housingvoucher house : houses) {
			//Create endpoint
			String baseURL = "https://maps.googleapis.com/maps/api/geocode/json?address=";
			String street = house.getStreet();
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
					"</div>", house.getName(), house.getStreet(), house.getPhone(), house.getInfo());
			location.put("info", infowindow);
			coordinates.add(location);
			System.out.println(coordinates);			
		}
		
		//passing data to template
		model.addAttribute("sources", coordinates);
		model.addAttribute("heading", "Housing Vouchers");
		model.addAttribute("stores", houses);
		return "housingvoucher";
	}
	
	@PostMapping(path = "/texthousingvoucher")
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
	    	return new RedirectView("/housingvoucher");
	    }else {
	    	return new RedirectView("/housingvoucher");
	    }	
	}
	
	@PostMapping(path="/addhousingvoucher")
	@ResponseBody
	public RedirectView addhousingvoucher(@ModelAttribute Housingvoucher housingvoucher, HttpSession session) {
		Housingvoucher house = new Housingvoucher();
		house.setName(housingvoucher.getName());
		house.setStreet(housingvoucher.getStreet());
		house.setCity(housingvoucher.getCity());
		house.setState(housingvoucher.getState());
		house.setZipcode(housingvoucher.getZipcode());
		house.setPhone(housingvoucher.getPhone());
		house.setInfo(housingvoucher.getInfo());
		house.setWebsite(housingvoucher.getWebsite());
		housingvoucherRepository.save(house);
		
		session.setAttribute("mySessionAttribute", "tempuser");
		return new RedirectView("/dashboard");
	}
	
	@GetMapping("/deletehousingvoucher/{id}")
	public String deleteHousingvoucher(@PathVariable("id") Integer id, Model model, Food food, Foodvoucher foodvoucher, Transitional transitional, Housingvoucher housingvoucher, Clothing clothing, Clinic clinic) {
		housingvoucherRepository.deleteById(id);
		
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
		
		List<Clinic> clinics = (List<Clinic>) clinicRepository.findAll();
		model.addAttribute("clinics", clinics);
		
		return "dashboard";
	}
}
	