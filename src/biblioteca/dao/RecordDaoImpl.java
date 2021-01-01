package biblioteca.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import biblioteca.model.Record;

public class RecordDaoImpl  extends GenericDao<Record> implements RecordDao {

	private EntityManagerFactory factory;
	
	public RecordDaoImpl(EntityManagerFactory factory) {
		super(Record.class);
		this.factory = factory;
	}
	@Override
	public List<Record> find(String recordID) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Record> q = cb.createQuery(Record.class);

		Root<Record> c = q.from(Record.class);
		ParameterExpression<String> paramName = cb.parameter(String.class);
		q.select(c).where(cb.equal(c.get("recordID"), paramName));
		TypedQuery<Record> query = em.createQuery(q);
		query.setParameter(paramName, recordID);

		List<Record> results = query.getResultList();
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
