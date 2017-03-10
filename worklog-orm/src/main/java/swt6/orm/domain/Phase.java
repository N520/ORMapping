package swt6.orm.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * untested Code
 * 
 * @author Rainer
 *
 */
@Entity
public class Phase implements Serializable {

	
	private enum PhaseDescriptor {
		ANALYSIS, IMPLEMENTATION, TEST, MAINTENANCE, OTHER
	}

	/**
	 * auto generated
	 */
	private static final long serialVersionUID = 451225272398476213L;

	@Id
	@GeneratedValue
	private Long id;

	@Enumerated(EnumType.STRING)
	private PhaseDescriptor name;

	public Phase() {
	}

	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}

	public PhaseDescriptor getName() {
		return name;
	}

	public void setName(PhaseDescriptor name) {
		this.name = name;
	}

}
