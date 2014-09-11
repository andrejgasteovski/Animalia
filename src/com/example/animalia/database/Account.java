package com.example.animalia.database;

public class Account {
	private long id;
	private String username;
	private String name;
	private int points;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	
	public Account(){}
	
	public Account(long id, String username, String name, int points) {
		super();
		this.id = id;
		this.username = username;
		this.name = name;
		this.points = points;
	}
	
	
	
}
