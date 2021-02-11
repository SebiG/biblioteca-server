package biblioteca.dao;

import java.util.List;

import javax.persistence.EntityManager;


import biblioteca.model.Review;

public interface ReviewDao {
	public List<Review> find(String reviewID);
	public EntityManager getEntityManager();
}
