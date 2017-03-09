package swt6.simple.domain;

import java.util.Date;

public class Employee {
	private Long id;
	private String firstName;
	private String lastName;
	private Date dateOfBirth;

	// Classes persisted with Hibernate must have default constructor
	public Employee() {

	}

	public Employee(String firstName, String lastName, Date dateOfBirth) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
	}

	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	@Override
	public String toString() {
		return String.format("%d: %s, %s (%s)", getId(), getFirstName(), getLastName(), getDateOfBirth());
	}
	
}
