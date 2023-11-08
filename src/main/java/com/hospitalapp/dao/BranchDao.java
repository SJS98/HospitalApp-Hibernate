package com.hospitalapp.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.hospitalapp.entity.Branch;
import com.hospitalapp.entity.Encounter;

public class BranchDao {
	private EntityManager em;
	private EntityTransaction et;
	private AddressDao adao;
	private EncounterDao edao;

	public BranchDao(EntityManager em, EntityTransaction et) {
		this.em = em;
		this.et = et;
		adao = new AddressDao(em, et);
		edao = new EncounterDao(em, et);
	}

	public void addBranch(Branch branch) {
		et.begin();
		branch.setId(getNewId());
		em.persist(branch);
		et.commit();
	}

	public int getNewId() {
		while (true) {
			int newId = (int) (Math.random() * 1000) + 1;
			Branch branch = em.find(Branch.class, newId);
			if (branch == null)
				return newId;
		}
	}

	public Branch findById(int id) {
		Branch branch = em.find(Branch.class, id);
		return branch;
	}

	public void updateBranch(Branch branch) {
		et.begin();

		em.merge(branch);

		et.commit();
	}

	public void removeBranch(Branch branch) {
		et.begin();
		em.remove(branch);
		et.commit();
	}

}
