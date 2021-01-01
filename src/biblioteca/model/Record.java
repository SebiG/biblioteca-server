package biblioteca.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the records database table.
 * 
 */
@Entity
@Table(name="records")
@NamedQuery(name="Record.findAll", query="SELECT r FROM Record r")
public class Record implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int recordID;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	private int state;

	//bi-directional many-to-one association to Book
	@ManyToOne
	@JoinColumn(name="bookID")
	private Book book;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="bookingRequestBy")
	private User user;

	public Record() {
	}

	public Record(User user, Book book, int state) {
		super();
		this.state = state;
		this.book = book;
		this.user = user;
		this.date = new Date();
	}

	public int getRecordID() {
		return this.recordID;
	}

	public void setRecordID(int recordID) {
		this.recordID = recordID;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getState() {
		return this.state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Book getBook() {
		return this.book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}