package swt6.orm.dao;

import java.util.Collection;
import java.util.List;

import org.hibernate.Session;

import swt6.orm.domain.Employee;

public class EmployeeDaoImpl extends AbstractDao implements EmployeeDao {

	public Employee insert(Employee employee) {
		checkSessionAvailable();
		return saveEntity(employee);

	}

	@Override
	public boolean update(Employee employee) {
		// TODO decide if necessary
		return false;
	}

	@Override
	public Employee findById(Long id) {
		checkSessionAvailable();

		return session.get(Employee.class, id);
	}

	@Override
	public Collection<Employee> findAll() {
		checkSessionAvailable();

		List<Employee> l = session.createQuery("Select e from Employee e order by lastName", Employee.class)
				.getResultList();

		return l;
	}

	@Override
	public void delete(Employee employee) {
		checkSessionAvailable();
		deleteEntity(employee);

	}

}
