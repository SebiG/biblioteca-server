package biblioteca.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the books database table.
 * 
 */
@Entity
@Table(name="books")
@NamedQuery(name="Book.findAll", query="SELECT b FROM Book b")
public class Book implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int bookID;

	private String author;

	private int stock;

	private String title;

	public Book() {
	}

	public int getBookID() {
		return this.bookID;
	}

	public void setBookID(int bookID) {
		this.bookID = bookID;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getStock() {
		return this.stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}