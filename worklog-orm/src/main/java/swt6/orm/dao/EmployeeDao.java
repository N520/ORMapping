package swt6.orm.dao;

import java.util.Collection;

import swt6.orm.domain.Employee;


//TODO figure out how relationships should be handeled -> remove from domain class?
public interface EmployeeDao extends Dao{
	Employee insert(Employee employee);

	boolean update(Employee employee);

	Employee findById(Long id);

	Collection<Employee> findAll();
	
	void delete(Employee employee);
}
