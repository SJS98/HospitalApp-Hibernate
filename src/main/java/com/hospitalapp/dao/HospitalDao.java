package com.hospitalapp.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.hospitalapp.entity.Branch;
import com.hospitalapp.entity.Hospital;

public class HospitalDao {

	private EntityManager em;
	private EntityTransaction et;

	public HospitalDao(EntityManager em, EntityTransaction et) {
		this.em = em;
		this.et = et;
	}

	public void saveHospital(Hospital hospital) {

		hospital.setId(getNewId());

		et.begin();
		em.persist(hospital);
		et.commit();
	}

	public void updateHospital(Hospital hospital) {
		et.begin();
		em.merge(hospital);
		et.commit();
	}

	public List<Hospital> getAllHospitals() {
		Query q = em.createQuery("from Hospital");
		return q.getResultList();
	}

	public Hospital findById(int id) {
		return em.find(Hospital.class, id);
	}

	public int getNewId() {
		while (true) {
			int newId = (int) (Math.random() * 1000) + 1;
			Hospital hospital = em.find(Hospital.class, newId);
			if (hospital == null)
				return newId;
		}
	}

	public void removeHospital(Hospital hospital) {
		et.begin();
		em.remove(hospital);
		et.commit();
	}

}
