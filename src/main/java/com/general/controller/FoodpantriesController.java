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
public class FoodpantriesController{
	
	public static final String ACCOUNT_SID = "";
	public static final String AUTH_TOKEN = "";
	
	@Autowired
	private FoodRepository foodRepository;
	
	@GetMapping(path="/foodPantries")
	public String foodPantries(Model model, Text text) {
		//Read from db
		List<Food> foods = (List<Food>) foodRepository.findAll();
		
		ArrayList<HashMap> coordinates = new ArrayList<HashMap>();
		
		for(Food food : foods) {
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
		model.addAttribute("heading", "Food Pantries");
		model.addAttribute("stores", foods);
		return "foodpantries";
	}
	
	@PostMapping(path = "/textfoodpantries")
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
	    	return new RedirectView("/foodPantries");
	    }else {
	    	return new RedirectView("/foodPantries");
	    }	
	}
}