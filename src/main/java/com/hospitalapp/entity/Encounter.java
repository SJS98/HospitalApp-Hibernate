package com.hospitalapp.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Encounter {

	@Id
	private int id;
	private String date;
	private String causeOfEncountering;
	private String dischargeDate;

	/*
	 * id;
	 * CauseOfEncountering;
	 * date;
	 * dischargeDate;
	*/

	@OneToMany
	private List<MedOrder> medOrders;

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	


	public String getCauseOfEncountering() {
		return causeOfEncountering;
	}

	public void setCauseOfEncountering(String causeOfEncountering) {
		this.causeOfEncountering = causeOfEncountering;
	}

	public List<MedOrder> getMedOrders() {
		return medOrders;
	}

	public void setMedOrders(List<MedOrder> medOrders) {
		this.medOrders = medOrders;
	}

	public String getDischargeDate() {
		return dischargeDate;
	}

	public void setDischargeDate(String dischargeDate) {
		this.dischargeDate = dischargeDate;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}
     
}
