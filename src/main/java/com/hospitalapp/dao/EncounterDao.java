package com.hospitalapp.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.hospitalapp.entity.Encounter;

public class EncounterDao {
	EntityManager em;
	EntityTransaction et;
	MedOrderDao mdao;

	public EncounterDao(EntityManager em, EntityTransaction et) {
		this.em = em;
		this.et = et;
		this.mdao = new MedOrderDao(em, et);
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

	public Encounter findById(int id) {
		return em.find(Encounter.class, id);
	}

	public void addEncounter(Encounter encounter) {
		encounter.setId(getNewId());

		et.begin();

		em.persist(encounter);

		et.commit();
	}

	public void updateEncounter(Encounter encounter) {
		et.begin();
		
		em.remove(encounter);
		
		et.commit();
	}

	public void removeEncounter(Encounter encounter) {
		et.begin();
		em.remove(encounter);
		et.commit();
	}

}
