package com.hospitalapp.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.hospitalapp.entity.Address;

public class AddressDao {

	private EntityManager em;
	private EntityTransaction et;

	public AddressDao(EntityManager em, EntityTransaction et) {
		this.em = em;
		this.et = et;
	}

	public void addAddress(Address address) {

		address.setId(getNewId());

		et.begin();

		em.persist(address);

		et.commit();
	}

	public int getNewId() {
		while (true) {
			int newId = (int) (Math.random() * 1000) + 1;
			Address address = em.find(Address.class, newId);
			if (address == null)
				return newId;
		}
	}

	public void updateAddress(Address address) {
		et.begin();
		em.merge(address);
		et.commit();
	}

	public void removeAddress(Address address) {
		et.begin();
		em.remove(address);
		et.commit();
	}

}
