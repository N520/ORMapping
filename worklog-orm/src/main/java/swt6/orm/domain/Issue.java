package swt6.orm.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Issue implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@Enumerated(EnumType.STRING)
	private IssueType state;

	@Enumerated(EnumType.STRING)
	private PriorityType priority;

	@Column(nullable = true)
	private int effort;

	@Column(length = 3)
	private int progress;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, optional = true)
	private Employee employee;

	@ManyToOne(optional = false)
	private Project project;

	public Issue() {
	}

	public Issue(IssueType state, PriorityType priority, int progress, Employee employee, Project project) {
		super();
		this.state = state;
		this.priority = priority;
		this.progress = progress;
		this.employee = employee;
		this.project = project;
	}

	public IssueType getState() {
		return state;
	}

	public void setState(IssueType state) {
		this.state = state;
	}

	public PriorityType getPriority() {
		return priority;
	}

	public void setPriority(PriorityType priority) {
		this.priority = priority;
	}

	public int getEffort() {
		return effort;
	}

	public void setEffort(int effort) {
		this.effort = effort;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Project getProject() {
		return project;
	}

	public Long getId() {
		return id;
	}

	public void attachEmployee(Employee empl) {
		if (employee != null)
			employee.removeIssue(this);

		if (empl != null) {
			empl.addIssue(this);
		}

		this.employee = empl;
	}

	public void detachEmployee() {
		if (employee != null)
			employee.removeIssue(this);
		employee = null;
	}

	/**
	 * removes the issue from the current project and attaches it to another
	 * 
	 * @param project
	 */
	public void moveToProject(Project project) {
		if (this.project != null)
			this.project.removeIssue(this);

		if (project != null)
			project.addIssue(this);
		this.project = project;
	}

}
