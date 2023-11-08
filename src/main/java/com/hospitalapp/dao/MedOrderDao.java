package com.hospitalapp.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.hospitalapp.entity.Encounter;
import com.hospitalapp.entity.Item;
import com.hospitalapp.entity.MedOrder;

public class MedOrderDao {
	EntityManager em;
	EntityTransaction et;
	ItemDao idao;

	public MedOrderDao(EntityManager em, EntityTransaction et) {
		this.em = em;
		this.et = et;
		this.idao = new ItemDao(em, et);
	}

	public int getNewId() {
		int newId = 0;
		while (true) {
			newId = (int) (Math.random() * 1000) + 1;
			Encounter branch = em.find(Encounter.class, newId);
			if (branch == null)
				return newId;
		}
	}

	public MedOrder findById(int id) {
		return em.find(MedOrder.class, id);
	}

	public void addMedOrder(MedOrder medOrder) {

		et.begin();

		medOrder.setId(getNewId());

		em.persist(medOrder);
		et.commit();
	}

	public void removeMedOrder(MedOrder medOrder) {
		et.begin();
		em.remove(medOrder);
		et.commit();
	}

	public void updateMedOrder(MedOrder medOrder) {
		et.begin();
		em.merge(medOrder);
		et.commit();
	}
}
