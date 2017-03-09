package swt6.orm.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Employee implements Serializable {

	private static final long serialVersionUID = 4982742211001582409L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String firstName;
	private String lastName;
	private Date dateOfBirth;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "employee")
	private Set<LogbookEntry> logBookentries = new HashSet<>();
	// v1
	// when no annotation give Address is serialized (if serzializabel)
	// v2
	// address is Embedded in address column
	// TODO make Address own entity
	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "zipCode", column = @Column(name = "adress_zipCode")) })
	// v3
	// seperate Table
	// @OnetoOne
	private Address address;

	@ManyToMany(cascade = CascadeType.ALL)
	private Set<Project> projects = new HashSet<>();
	
	//TODO connect with issues (one employee many issues)

	// Classes persisted with Hibernate must have default constructor
	public Employee() {

	}

	public Employee(String firstName, String lastName, Date dateOfBirth) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
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

	public Set<LogbookEntry> getLogBookentries() {
		return logBookentries;
	}

	public Set<Project> getProjects() {
		return projects;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}

	public void addProject(Project project) {
		if (project == null)
			throw new IllegalArgumentException("project must not be null");
		project.getMembers().add(this);
		this.projects.add(project);
	}

	public void addLogbookEntry(LogbookEntry entry) {
		if (entry.getEmployee() != null) {
			entry.getEmployee().removeLogbookEntry(entry);
		}

		logBookentries.add(entry);
		this.logBookentries.add(entry);
		entry.setEmployee(this);
	}

	public void removeLogbookEntry(LogbookEntry entry) {
		logBookentries.remove(entry);
		entry.setEmployee(null);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(String.format("%d: %s, %s (%s)", getId(), getFirstName(), getLastName(), getDateOfBirth()));
		if (address != null)
			sb.append(", " + getAddress());
		return sb.toString();
	}

}
