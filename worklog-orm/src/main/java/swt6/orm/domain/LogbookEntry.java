package swt6.orm.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
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
	private String activity;
	private Date startTime;
	private Date endTime;
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, optional = false)
	private Employee employee;

	public LogbookEntry() {

	}

	public Long getId() {
		return id;
	}

	private void setId(Long id) {
		this.id = id;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
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
		// v1
		// attachEmployee(null);

		// v2
		if (this.employee != null) {
			this.employee.removeLogbookEntry(this);
		}
		this.employee = null;

	}

	public LogbookEntry(String activity, Date startTime, Date endTime, Employee employee) {
		this.activity = activity;
		this.startTime = startTime;
		this.endTime = endTime;
		this.employee = employee;
	}

	@Override
	public String toString() {
		return activity + ": " + startTime + " - " + endTime + " (" + employee.getLastName() + ")";
	}

	private static final long serialVersionUID = 8497603996267190243L;

}
