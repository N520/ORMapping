package swt6.orm.dao;

import java.util.Collection;

import swt6.orm.domain.Employee;
import swt6.orm.domain.PermanentEmployee;
import swt6.orm.domain.TemporaryEmployee;

//TODO figure out how relationships should be handeled 
public interface EmployeeDao extends Dao {
	Employee saveEmployee(Employee employee);

	boolean update(Employee employee);

	Employee findById(Long id);

	Collection<Employee> findAll();

	void delete(Employee employee);

	Collection<PermanentEmployee> findAllPermanent();

	Collection<TemporaryEmployee> findAllTemporary();
}
