package biblioteca.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import biblioteca.model.User;

public class UserDao extends GenericDao<User> {

	private EntityManagerFactory factory;

	public UserDao(EntityManagerFactory factory) {
		super(User.class);
		this.factory = factory;
	}

	@Override
	public EntityManager getEntityManager() {
		try {
			return factory.createEntityManager();
		} catch (Exception ex) {
			System.out.println("The entity manager cannot be created!");
			return null;
		}
	}

	// for login
	public List<User> find(String userName) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> q = cb.createQuery(User.class);

		Root<User> c = q.from(User.class);
		ParameterExpression<String> paramName = cb.parameter(String.class);
		q.select(c).where(cb.equal(c.get("userName"), paramName));
		TypedQuery<User> query = em.createQuery(q);
		query.setParameter(paramName, userName);

		List<User> results = query.getResultList();
		return results;
	}
	
	public List<User> findByID(String userID) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> q = cb.createQuery(User.class);

		Root<User> c = q.from(User.class);
		ParameterExpression<String> paramName = cb.parameter(String.class);
		q.select(c).where(cb.equal(c.get("userID"), paramName));
		TypedQuery<User> query = em.createQuery(q);
		query.setParameter(paramName, userID);

		List<User> results = query.getResultList();
		return results;
	}
}
