package biblioteca.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import biblioteca.model.Review;

public class ReviewDaoImpl  extends GenericDao<Review> implements ReviewDao {

	private EntityManagerFactory factory;
	
	public ReviewDaoImpl(EntityManagerFactory factory) {
		super(Review.class);
		this.factory = factory;
	}
	@Override
	public List<Review> find(String reviewID) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Review> q = cb.createQuery(Review.class);

		Root<Review> c = q.from(Review.class);
		ParameterExpression<String> paramName = cb.parameter(String.class);
		q.select(c).where(cb.equal(c.get("reviewID"), paramName));
		TypedQuery<Review> query = em.createQuery(q);
		query.setParameter(paramName, reviewID);

		List<Review> results = query.getResultList();
		return results;
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

}
