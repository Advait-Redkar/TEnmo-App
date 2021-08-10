package com.techelevator.tenmo.model;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

public class User {

	private Integer id;
	private String username;

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername(Integer id) {
		return username;
	}

	public String getUsername () {return username;}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public ArrayList<User> getAllUsers(Principal principal){
		ArrayList<User> users = new ArrayList<>();
		return users;

	}
}
