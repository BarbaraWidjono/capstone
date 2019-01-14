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
	public String homepage(Model model, Login login) {
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
	public String transitional(Model model, Text text) {
		//Read records from db
		List<Transitional> houses = (List<Transitional>) transitionalRepository.findAll();
		
		ArrayList<HashMap> coordinates = new ArrayList<HashMap>();
		
		for(Transitional house : houses) {
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
		model.addAttribute("heading", "Transitional Housing");
		model.addAttribute("stores", houses);
		return "results";
	}
	
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
		return "results";
	}
	
	@GetMapping(path="/clothing")
	public String clothing(Model model, Text text) {
		List<Clothing> clothes = (List<Clothing>) clothingRepository.findAll();
		
		ArrayList<HashMap> coordinates = new ArrayList<HashMap>();
		
		for(Clothing cloth : clothes) {
			//Create endpoint
			String baseURL = "https://maps.googleapis.com/maps/api/geocode/json?address=";
			String street = cloth.getStreet();
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
					"</div>", cloth.getName(), cloth.getStreet(), cloth.getPhone(), cloth.getInfo());
			location.put("info", infowindow);
			coordinates.add(location);
			System.out.println(coordinates);			
		}
		
		//passing data to template
		model.addAttribute("sources", coordinates);
		model.addAttribute("heading", "Clothing");
		model.addAttribute("stores", clothes);
		return "results";
	}
	
	@GetMapping(path="/clinic")
	public String clinic(Model model, Text text) {
		List<Clinic> clinics = (List<Clinic>) clinicRepository.findAll();
		
		ArrayList<HashMap> coordinates = new ArrayList<HashMap>();
		
		for(Clinic clinic : clinics) {
			//Create endpoint
			String baseURL = "https://maps.googleapis.com/maps/api/geocode/json?address=";
			String street = clinic.getStreet();
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
					"</div>", clinic.getName(), clinic.getStreet(), clinic.getPhone(), clinic.getInfo());
			location.put("info", infowindow);
			coordinates.add(location);
			System.out.println(coordinates);			
		}
		
		//passing data to template
		model.addAttribute("sources", coordinates);
		model.addAttribute("heading", "Clinics");
		model.addAttribute("stores", clinics);
		return "results";
	}
	
//	@PostMapping(path = "/addfoodpantry")
//	@ResponseStatus(value = HttpStatus.OK)
//	public RedirectView addFood(@ModelAttribute Food food, HttpSession session) {
//		Food newfood = new Food();
//		newfood.setName(food.getName());
//		newfood.setStreet(food.getStreet());
//		newfood.setCity(food.getCity());
//		newfood.setState(food.getState());
//		newfood.setZipcode(food.getZipcode());
//		newfood.setPhone(food.getPhone());
//		newfood.setInfo(food.getInfo());
//		newfood.setWebsite(food.getWebsite());
//		foodRepository.save(newfood);
	
//		session.setAttribute("mySessionAttribute", "tempuser");
//		return new RedirectView("/dashboard");
//	}
	
//	@PostMapping(path="/addfoodvoucher")
//	@ResponseBody
//	public RedirectView addFoodvoucher(@ModelAttribute Foodvoucher foodvoucher, HttpSession session) {
//		Foodvoucher newfood = new Foodvoucher();
//		newfood.setName(foodvoucher.getName());
//		newfood.setStreet(foodvoucher.getStreet());
//		newfood.setCity(foodvoucher.getCity());
//		newfood.setState(foodvoucher.getState());
//		newfood.setZipcode(foodvoucher.getZipcode());
//		newfood.setPhone(foodvoucher.getPhone());
//		newfood.setInfo(foodvoucher.getInfo());
//		newfood.setWebsite(foodvoucher.getWebsite());
//		foodvoucherRepository.save(newfood);
//		
//		session.setAttribute("mySessionAttribute", "tempuser");
//		return new RedirectView("/dashboard");
//	}
		
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