package com.hospitalapp.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import com.hospitalapp.entity.Person;

public class PersonDao {

	EntityManager em;
	EntityTransaction et;
	EncounterDao edao;

	public PersonDao(EntityManager em, EntityTransaction et) {
		this.em = em;
		this.et = et;
		this.edao = new EncounterDao(em, et);
	}

	public Person findById(int id) {
		Person person = em.find(Person.class, id);
		return person;
	}

	public int getNewId() {
		int newId = 0;
		while (true) {
			newId = (int) (Math.random() * 1000) + 1;
			Person person = em.find(Person.class, newId);
			if (person == null)
				return newId;
		}
	}

	public void addPerson(Person person) {
		person.setId(getNewId());

		et.begin();
		em.persist(person);
		et.commit();
	}

	public List<Person> getAllPerson() {
		Query q = em.createQuery("from Person");
		return q.getResultList();
	}

	public void checkAndSavePerson(Person person) {
		if (person.getId() == 0)
			person.setId(getNewId());

		et.begin();
		em.merge(person);
		et.commit();
	}

	public void removePerson(Person person) {
		et.begin();
		em.remove(person);
		et.commit();
	}

}
