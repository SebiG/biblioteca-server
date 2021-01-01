package biblioteca.service;

import java.util.List;
import javax.persistence.Persistence;
import biblioteca.dao.BookDao;
import biblioteca.model.Book;

public class BookService {
	private BookDao bookDao;

	public BookService() {
		try {
			bookDao = new BookDao(Persistence.createEntityManagerFactory("BibliotecaServer"));
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void addBook(Book newBook) {
		bookDao.create(newBook);
	}

	public void updateBook(Book updatedBook) {
		bookDao.update(updatedBook);
	}
	
	public void reserve(Book updatedBook) {
		bookDao.update(updatedBook);
	}

	public List<Book> getAllBooks() {
		return bookDao.findAll();
	}

	public Book findRecord(String recordID) throws Exception {
		List<Book> records = bookDao.find(recordID);
		if (records.size() == 0) {
			throw new Exception("Book not found!");
		}
		Book u = records.get(0);
		return u;
	}

	public Book findBook(String bookID) throws Exception {
		List<Book> books = bookDao.find(bookID);
		if (books.size() == 0) {
			throw new Exception("Book not found!");
		}
		Book b = books.get(0);
		return b;
	}
}
