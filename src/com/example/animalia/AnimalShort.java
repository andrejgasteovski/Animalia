package com.example.animalia;

import java.io.Serializable;

public class AnimalShort implements Serializable{
	private String id;
	private String name;
	private String link;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public AnimalShort(String id, String name, String link) {
		super();
		this.id = id;
		this.name = name;
		this.link = link;
	}
	@Override
	public String toString() {
		return name;
	}
}
