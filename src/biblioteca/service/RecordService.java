package biblioteca.service;

import java.util.List;
import javax.persistence.Persistence;
import biblioteca.dao.RecordDaoImpl;
import biblioteca.model.Record;

public class RecordService {
	private RecordDaoImpl recordDao;

	public RecordService() {
		try {
			recordDao = new RecordDaoImpl(Persistence.createEntityManagerFactory("BibliotecaServer"));
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void addRecord(Record newRecord) {
		recordDao.create(newRecord);
	}

	public void updateRecord(Record updatedRecord) {
		recordDao.update(updatedRecord);
	}
	
	public void reserve(Record updateRecord) {
		recordDao.update(updateRecord);
	}

	public void updateRecordState(String recordID, Integer state) throws Exception {
		Record r = this.findRecord(recordID);
		r.setState(state);
		recordDao.update(r);
	}
	
	public List<Record> getAllRecords() {
		return recordDao.findAll();
	}

	public Record findRecord(String recordID) throws Exception {
		List<Record> records = recordDao.find(recordID);
		if (records.size() == 0) {
			throw new Exception("Record not found!");
		}
		Record u = records.get(0);
		return u;
	}
}
