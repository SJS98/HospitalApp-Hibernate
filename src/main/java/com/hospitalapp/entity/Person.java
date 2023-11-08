package com.hospitalapp.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Person {
	@Id
	private int id;
	
	private String name;

	@OneToMany
	private List<Encounter> enli;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Encounter> getEnli() {
		return enli;
	}

	public void setEnli(List<Encounter> enli) {
		this.enli = enli;
	}
	
	
	
}
