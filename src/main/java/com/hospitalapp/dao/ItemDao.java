package com.hospitalapp.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.hospitalapp.entity.Encounter;
import com.hospitalapp.entity.Item;
import com.hospitalapp.entity.MedOrder;

public class ItemDao {
	EntityManager em;
	EntityTransaction et;
	ItemDao idao;

	public ItemDao(EntityManager em, EntityTransaction et) {
		this.em = em;
		this.et = et;
	}

	public void addItems(List<Item> items) {

		et.begin();

		for (Item item : items) {
			item.setId(getNewId());
			em.persist(item);
		}

		et.commit();

	}

	public void updateItem(Item item) {
		et.begin();
		em.merge(item);
		et.commit();
	}

	public int getNewId() {
		int newId = 0;
		while (true) {
			newId = (int) (Math.random() * 1000) + 1;
			Item item = em.find(Item.class, newId);
			if (item == null)
				return newId;
		}
	}

	public Item findById(int id) {
		return em.find(Item.class, id);
	}

	public void removeItem(Item item) {
		et.begin();
		em.remove(item);
		et.commit();
	}

	public List<Item> geAllItems() {
		return em.createQuery("from Item").getResultList();
	}
}
