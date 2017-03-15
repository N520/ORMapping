package swt6.orm.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.query.Query;

import swt6.orm.domain.Employee;
import swt6.orm.domain.PermanentEmployee;
import swt6.orm.domain.TemporaryEmployee;

public class EmployeeDaoImpl extends AbstractDao implements EmployeeDao {

	public Employee saveEmployee(Employee employee) {
		checkSessionAvailable();
		return saveEntity(employee);

	}

	@Override
	public Employee findById(Long id) {
		checkSessionAvailable();

		return session.get(Employee.class, id);
	}

	@Override
	public Collection<Employee> findAll() {
		checkSessionAvailable();

		List<Employee> l = session.createQuery("from Employee order by lastName", Employee.class).getResultList();

		return l;
	}

	@Override
	public void delete(Employee employee) {
		checkSessionAvailable();

		deleteEntity(employee);
	}

	@Override
	public Collection<PermanentEmployee> findAllPermanent() {
		checkSessionAvailable();
		Query<Employee> query = session.createQuery("from Employee e where e.class='P'", Employee.class);

		return query(query).stream().map(e -> (PermanentEmployee) e).collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public Collection<TemporaryEmployee> findAllTemporary() {
		checkSessionAvailable();
		Query<Employee> query = session.createQuery("from Employee e where e.class='T'", Employee.class);

		return query(query).stream().map(e -> (TemporaryEmployee) e).collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public Collection<Employee> query(Query<Employee> query) {
		return query.getResultList();
	}

}
