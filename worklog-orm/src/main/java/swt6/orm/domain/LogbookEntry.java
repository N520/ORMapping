package swt6.orm.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class LogbookEntry implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Date startTime;
	private Date endTime;
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, optional = false)
	private Employee employee;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "name", column = @Column(name = "Phase")) })
	private Phase phase;

	@ManyToOne(optional = false)
	private Module module;

	public LogbookEntry() {

	}

	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public void attachEmployee(Employee employee) {
		// if this entry is linked to some employee remove this link.
		if (this.employee != null)
			this.employee.removeLogbookEntry(this);

		// bidirectinal link between this entry and employee
		if (employee != null) {
			employee.addLogbookEntry(this);
		}
		this.employee = employee;
	}

	public void detachEmployee() {	
		if (this.employee != null) {
			this.employee.removeLogbookEntry(this);
		}
		this.employee = null;

	}

	public LogbookEntry(Date startTime, Date endTime, Employee employee) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.employee = employee;
	}

	public Phase getPhase() {
		return phase;
	}

	public void setPhase(Phase phase) {
		this.phase = phase;
	}

	@Override
	public String toString() {
		return phase.getName() + ": " + startTime + " - " + endTime + " (" + employee.getLastName() + ")";
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	private static final long serialVersionUID = 8497603996267190243L;

}
