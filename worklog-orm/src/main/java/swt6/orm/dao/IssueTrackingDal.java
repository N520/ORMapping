package swt6.orm.dao;

import java.util.Collection;

import org.hibernate.Transaction;

import swt6.orm.domain.Employee;
import swt6.orm.hibernate.HibernateUtil;

public class IssueTrackingDal {

	private EmployeeDao dao = new EmployeeDaoImpl();

	public Employee saveEmployee(Employee e) {
		dao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();

		e = dao.insert(e);
		tx.commit();
		return e;

	}

	public Employee findEmployeeById(Long id) {
		dao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();
		Employee employee = dao.findById(id);
		tx.commit();

		return employee;
	}

	public Collection<Employee> findAllEmployees() {
		dao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();
		Collection<Employee> employees = dao.findAll();
		tx.commit();

		return employees;
	}

	public void deleteEmployee(Employee e) {
		dao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();

		dao.delete(e);
		tx.commit();
	}
	
	
	public void close() {
		HibernateUtil.closeSessionFactory();
	}
}
