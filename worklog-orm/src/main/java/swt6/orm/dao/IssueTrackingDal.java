package swt6.orm.dao;

import java.util.Collection;

import org.hibernate.Transaction;

import swt6.orm.domain.Employee;
import swt6.orm.hibernate.HibernateUtil;

public class IssueTrackingDal {

	private EmployeeDao dao = new EmployeeDaoImpl();

	// EMPLOYEE STUFF
	// ---------------------------------------------------------------
	public Employee saveEmployee(Employee e) {
		dao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();

		e = dao.saveEmployee(e);
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

	// TODO getIssuesForEMpl
	// ---------------------------------------------------------------

	// ISSUESTUFF
	// ---------------------------------------------------------------

	// TODO setIssueState

	// TODO set Issueeffort

	// TODO set Issueprogress

	// TODO get all issues with state

	// TODO get all issues with certain state and employee

	// ---------------------------------------------------------------

	// LOGBOOK STUFF
	// ---------------------------------------------------------------

	// TODO new LogbookEntry
	// TODO update Time For Entry
	// TODO update Module
	// TODO update Phase

	// ---------------------------------------------------------------

	// PROJECT STUFF
	// ---------------------------------------------------------------

	// TODO new Project
	// TODO add/remove Members
	// TODO update Lead
	// TODO add/remove Modules (keep at least 1 module!)
	// TODO update Name
	// TODO get issues of project by state
	// TODO sum effort/progress of issues by state
	// ---------------------------------------------------------------

	public void close() {
		HibernateUtil.closeSessionFactory();
	}
}
