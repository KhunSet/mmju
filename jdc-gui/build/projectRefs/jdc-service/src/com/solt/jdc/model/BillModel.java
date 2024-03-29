package com.solt.jdc.model;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import com.solt.jdc.entity.Bill;
import com.solt.jdc.entity.Student;
import com.solt.jdc.entity.StudentJdc;

public class BillModel {
	
	@Inject
	private EntityManager em;

	@Transactional
	public Bill persist(Bill b) {
		// student
		Student st = b.getStudentJdc().getStudent();
		if(st.getId() == 0) {
			em.persist(st);
		}
		
		StudentJdc jdc = em.find(StudentJdc.class, b.getStudentJdc().getId());
		
		if(null == jdc) {
			em.persist(b.getStudentJdc());
		}
		
		em.persist(b);
		
		return b;
	}
}
