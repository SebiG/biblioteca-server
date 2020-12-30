package biblioteca.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the reviews database table.
 * 
 */
@Entity
@Table(name="reviews")
@NamedQuery(name="Review.findAll", query="SELECT r FROM Review r")
public class Review implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int reviewID;

	@Lob
	private String review;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="userID")
	private User user;

	//bi-directional many-to-one association to Book
	@ManyToOne
	@JoinColumn(name="bookID")
	private Book book;

	public Review() {
	}

	public int getReviewID() {
		return this.reviewID;
	}

	public void setReviewID(int reviewID) {
		this.reviewID = reviewID;
	}

	public String getReview() {
		return this.review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Book getBook() {
		return this.book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

}