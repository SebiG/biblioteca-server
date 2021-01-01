package biblioteca.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import biblioteca.model.Book;


public class BookDao extends GenericDao<Book> {

	private EntityManagerFactory factory;

	public BookDao(EntityManagerFactory factory) {
		super(Book.class);
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


	public List<Book> find(String bookID) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Book> q = cb.createQuery(Book.class);

		Root<Book> c = q.from(Book.class);
		ParameterExpression<String> paramName = cb.parameter(String.class);
		q.select(c).where(cb.equal(c.get("bookID"), paramName));
		TypedQuery<Book> query = em.createQuery(q);
		query.setParameter(paramName, bookID);

		List<Book> results = query.getResultList();
		return results;
	}
}
