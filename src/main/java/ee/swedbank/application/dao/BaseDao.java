package ee.swedbank.application.dao;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

import ee.swedbank.application.entity.BaseEntity;

public abstract class BaseDao {

	@Autowired
	private EntityManager entityManager;

	protected Session getSession() {
		return (Session) entityManager.getDelegate();
	}

	public <T extends BaseEntity> void save(final T object){
		getSession().save(object);
	}

}
