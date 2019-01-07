package com.general.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.general.entity.Food;
import com.general.entity.Foodvoucher;
import com.general.entity.Transitional;
import com.general.repository.FoodRepository;
import com.general.repository.FoodvoucherRepository;
import com.general.repository.TransitionalRepository;

@Controller
public class HomepageController{
	
	@Autowired
	private FoodRepository foodRepository;
	
	@Autowired
	private FoodvoucherRepository foodvoucherRepository;
	
	@Autowired
	private TransitionalRepository transitionalRepository;
	
	@GetMapping(path = "/")
	public String homepage() {
		return "homepage";
	}
	
	@GetMapping(path="/foodPantries")
	public String foodPantries(Model model) {
		
		List<Food> foods = (List<Food>) foodRepository.findAll();

		model.addAttribute("heading", "Food Pantries");
		model.addAttribute("stores", foods);
		return "results";
	}
	
	@GetMapping(path="/foodvouchers")
	public String foodVouchers(Model model) {
		List<Foodvoucher> foods = (List<Foodvoucher>) foodvoucherRepository.findAll();

		model.addAttribute("heading", "Food Vouchers");
		model.addAttribute("stores", foods);
		return "results";
	}
	
	@GetMapping(path="/transitional")
	public String transitional(Model model) {
		List<Transitional> houses = (List<Transitional>) transitionalRepository.findAll();

		model.addAttribute("heading", "Transitional Housing");
		model.addAttribute("stores", houses);
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
}