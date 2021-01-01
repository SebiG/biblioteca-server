package biblioteca.model;

import java.io.Serializable;
import javax.persistence.*;

import com.google.gson.annotations.Expose;

import java.util.List;


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
	@Expose
	private int bookID;
	
	@Expose
	private String author;

	@Expose
	private int stock;

	@Expose
	private String title;

	//bi-directional many-to-one association to Record
	@OneToMany(mappedBy="book")
	private List<Record> records;

	//bi-directional many-to-one association to Review
	@OneToMany(mappedBy="book")
	private List<Review> reviews;

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

	public List<Record> getRecords() {
		return this.records;
	}

	public void setRecords(List<Record> records) {
		this.records = records;
	}

	public Record addRecord(Record record) {
		getRecords().add(record);
		record.setBook(this);

		return record;
	}

	public Record removeRecord(Record record) {
		getRecords().remove(record);
		record.setBook(null);

		return record;
	}

	public List<Review> getReviews() {
		return this.reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public Review addReview(Review review) {
		getReviews().add(review);
		review.setBook(this);

		return review;
	}

	public Review removeReview(Review review) {
		getReviews().remove(review);
		review.setBook(null);

		return review;
	}

	public void decrementStock() {
		setStock(getStock() - 1);
	}

}