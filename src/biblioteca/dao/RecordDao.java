package biblioteca.dao;

import java.util.List;

import javax.persistence.EntityManager;


import biblioteca.model.Record;

public interface RecordDao {
	public List<Record> find(String recordID);
	public EntityManager getEntityManager();
}
