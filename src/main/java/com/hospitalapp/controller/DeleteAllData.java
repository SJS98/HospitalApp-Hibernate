package com.hospitalapp.controller;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.hospitalapp.entity.Address;

public class DeleteAllData {
	public static void main(String[] args) {
		EntityManagerFactory emf =Persistence.createEntityManagerFactory("vikas");
		EntityManager em=emf.createEntityManager();
		EntityTransaction et = em.getTransaction();
		
		et.begin();

		et.commit();
		
	}
}
