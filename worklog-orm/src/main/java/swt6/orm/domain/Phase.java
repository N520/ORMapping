package swt6.orm.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

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

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private PhaseDescriptor name;

	public Phase() {
		name = PhaseDescriptor.OTHER;
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
