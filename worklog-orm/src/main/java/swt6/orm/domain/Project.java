package swt6.orm.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

@Entity
public class Project implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;
	private String name;

	@ManyToMany(cascade = CascadeType.ALL) // ????ß
	private Set<Employee> members = new HashSet<>();

	// TODO create issue entity (one project - many issures)

	// TODO create Project Leader (one to one)
	@OneToOne
	private Employee projectLeader;
	
	public Long getId() {
		return id;
	}

	public Project() {
	}

	public Project(String name) {
		this.name = name;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Employee> getMembers() {
		return members;
	}

	public void setMembers(Set<Employee> members) {
		this.members = members;
	}

	public void addMember(Employee empl) {
		if (empl == null)
			throw new IllegalArgumentException("employee must not be null");
		empl.getProjects().add(this);
		this.members.add(empl);
	}

	public String toString() {
		return name;
	}
}
