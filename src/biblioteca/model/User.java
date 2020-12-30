package biblioteca.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the users database table.
 * 
 */
@Entity
@Table(name="users")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int userID;

	private int role;

	private String userName;

	private String userPassword;

	//bi-directional many-to-one association to Record
	@OneToMany(mappedBy="user1")
	private List<Record> records1;

	//bi-directional many-to-one association to Record
	@OneToMany(mappedBy="user2")
	private List<Record> records2;

	//bi-directional many-to-one association to Review
	@OneToMany(mappedBy="user")
	private List<Review> reviews;

	public User() {
	}

	public int getUserID() {
		return this.userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public int getRole() {
		return this.role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return this.userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public List<Record> getRecords1() {
		return this.records1;
	}

	public void setRecords1(List<Record> records1) {
		this.records1 = records1;
	}

	public Record addRecords1(Record records1) {
		getRecords1().add(records1);
		records1.setUser1(this);

		return records1;
	}

	public Record removeRecords1(Record records1) {
		getRecords1().remove(records1);
		records1.setUser1(null);

		return records1;
	}

	public List<Record> getRecords2() {
		return this.records2;
	}

	public void setRecords2(List<Record> records2) {
		this.records2 = records2;
	}

	public Record addRecords2(Record records2) {
		getRecords2().add(records2);
		records2.setUser2(this);

		return records2;
	}

	public Record removeRecords2(Record records2) {
		getRecords2().remove(records2);
		records2.setUser2(null);

		return records2;
	}

	public List<Review> getReviews() {
		return this.reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public Review addReview(Review review) {
		getReviews().add(review);
		review.setUser(this);

		return review;
	}

	public Review removeReview(Review review) {
		getReviews().remove(review);
		review.setUser(null);

		return review;
	}

}