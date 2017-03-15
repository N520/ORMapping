package swt6.orm.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;
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
@Embeddable
public class Phase implements Serializable {

	/**
	 * auto generated
	 */
	private static final long serialVersionUID = 451225272398476213L;

	// private Long id;

	@Enumerated(EnumType.STRING)
	private PhaseDescriptor name;

	public Phase() {
	}

	public Phase(PhaseDescriptor name) {
		super();
		this.name = name;
	}

	public PhaseDescriptor getName() {
		return name;
	}

	public void setName(PhaseDescriptor name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name.toString();
	}

}
