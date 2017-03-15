package swt6.orm.dao;

import java.util.Collection;

import org.hibernate.query.Query;

import swt6.orm.domain.Employee;
import swt6.orm.domain.PermanentEmployee;
import swt6.orm.domain.TemporaryEmployee;

public interface EmployeeDao extends Dao {
	Employee saveEmployee(Employee employee);

	Employee findById(Long id);

	Collection<Employee> findAll();

	void delete(Employee employee);

	Collection<PermanentEmployee> findAllPermanent();

	Collection<TemporaryEmployee> findAllTemporary();

	Collection<Employee> query(Query<Employee> query);

}
