package com.example.animalia.highscores;

import java.util.jar.Attributes.Name;

public class HighscoresUser {
	private int place;
	private String user;
	private String score;
	private String type;
	
	public int getPlace() {
		return place;
	}
	public void setPlace(int place) {
		this.place = place;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public HighscoresUser(int place, String user, String score, String type) {
		super();
		this.place = place;
		this.user = user;
		this.score = score;
		this.type = type;
	}
	
	@Override
	public String toString() {
		return place + ". " + user + " - " + score;
	}
	
}
