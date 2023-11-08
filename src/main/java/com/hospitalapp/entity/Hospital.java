package com.hospitalapp.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Hospital {

	@Id
	private int id;

	private String name;
	private String managerName;

	@OneToMany
	private List<Branch> branchs;

	public Hospital() {
	}

	public Hospital(int id, String name, List<Branch> branchs) {
		this.id = id;
		this.name = name;
		this.branchs = branchs;
	}

	public Hospital(Branch setAddress) {
		
	}

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

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public List<Branch> getBranchs() {
		return branchs;
	}

	public void setBranchs(List<Branch> branchs) {
		this.branchs = branchs;
	}

}
