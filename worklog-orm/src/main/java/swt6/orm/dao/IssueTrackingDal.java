package swt6.orm.dao;

import java.util.Collection;

import org.hibernate.Session;
import org.hibernate.Transaction;

import swt6.orm.domain.Employee;
import swt6.orm.domain.Issue;
import swt6.orm.domain.IssueType;
import swt6.orm.domain.LogbookEntry;
import swt6.orm.domain.Module;
import swt6.orm.domain.PermanentEmployee;
import swt6.orm.domain.Project;
import swt6.orm.domain.TemporaryEmployee;
import swt6.orm.hibernate.HibernateUtil;

public class IssueTrackingDal implements AutoCloseable {

	private EmployeeDao employeeDao = new EmployeeDaoImpl();
	private IssueDao issueDao = new IssueDaoImpl();
	private ProjectDao projectDao = new ProjectDaoImpl();
	private ModuleDao moduleDao = new ModuleDaoImpl();
	private LogbookDao logbookDao = new LogBookDaoImpl();

	// EMPLOYEE STUFF
	// ---------------------------------------------------------------
	public Employee saveEmployee(Employee e) {
		employeeDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();

		e = employeeDao.saveEmployee(e);
		tx.commit();
		return e;

	}

	public Employee findEmployeeById(Long id) {
		employeeDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();
		Employee employee = employeeDao.findById(id);
		tx.commit();

		return employee;
	}

	public Collection<Employee> findAllEmployees() {
		employeeDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();
		Collection<Employee> employees = employeeDao.findAll();
		tx.commit();

		return employees;
	}

	public void deleteEmployee(Employee e) {
		employeeDao.setSession(HibernateUtil.getCurrentSession());
		projectDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();
		// e = employeeDao.saveEmployee(e);

		for (Project p : e.getProjects()) {

			e.removeProject(p);
			// persist project without employee
			p = projectDao.saveProject(p);
		}

		e = employeeDao.saveEmployee(e);
		employeeDao.delete(e);
		tx.commit();
	}

	public Collection<PermanentEmployee> findAllPermanentEmployees() {
		employeeDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();

		Collection<PermanentEmployee> permanentEmployees = employeeDao.findAllPermanent();
		tx.commit();
		return permanentEmployees;
	}

	public Collection<TemporaryEmployee> findAllTemporaryEmployees() {
		employeeDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();

		Collection<TemporaryEmployee> temporaryEmployees = employeeDao.findAllTemporary();
		tx.commit();
		return temporaryEmployees;
	}

	/**
	 * finds employees with a matching first or lastname
	 * 
	 * @param Name
	 * @return
	 */
	public Collection<Employee> findByName(String name) {
		employeeDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();

		// TODO
		// Query<Employee> query = new
		// HibernateUtil().getCurrentSession().createQuery("")
		// dao.query(query)

		tx.commit();
		return null;
	}

	// ---------------------------------------------------------------

	// ISSUESTUFF
	// ---------------------------------------------------------------

	public Collection<Issue> findAllIssues() {
		issueDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();

		Collection<Issue> issues = issueDao.findAll();

		tx.commit();
		return issues;
	}

	public Issue saveIssue(Issue issue) {
		issueDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();

		issue = issueDao.saveIssue(issue);

		tx.commit();
		return issue;
	}

	public void assignEmployeeToIssue(Employee employee, Issue issue) {
		issueDao.setSession(HibernateUtil.getCurrentSession());
		employeeDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();

		issue = issueDao.updateEmployee(issue, employee);
		tx.commit();
	}

	public void deleteIssue(Issue issue) {
		issueDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();

		issue.moveToProject(null);
		issueDao.delete(issue);
		tx.commit();
	}

	// TODO setIssueState

	// TODO set Issueeffort

	// TODO set Issueprogress

	public Collection<Issue> findAllIssuesWithState(IssueType state) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();
		session.enableFilter("ISSUE_STATE_FILTER").setParameter("state", state.toString());

		issueDao.setSession(session);
		Collection<Issue> issues = issueDao.findAll();

		tx.commit();
		return issues;
	}

	// TODO get all issues with certain state and employee

	// TODO get all issues with certain state of project

	// ---------------------------------------------------------------

	// LOGBOOK STUFF
	// ---------------------------------------------------------------

	public LogbookEntry saveLogbookEntry(LogbookEntry logbookEntry) {
		logbookDao.setSession(HibernateUtil.getCurrentSession());
		employeeDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();

		logbookEntry = logbookDao.saveLogbookEntry(logbookEntry);

		tx.commit();
		return logbookEntry;

	}

	public LogbookEntry findLogbookEntryById(Long id) {
		logbookDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();
		LogbookEntry entry = logbookDao.findById(id);
		tx.commit();

		return entry;
	}

	public Collection<LogbookEntry> findAllLogbookEntries() {
		logbookDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();
		Collection<LogbookEntry> employees = logbookDao.findAll();
		tx.commit();

		return employees;
	}

	public void deleteLogbookEntry(LogbookEntry lb) {
		logbookDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();
		// e = employeeDao.saveEmployee(e);

		// TODO resolve relationships

		lb = logbookDao.saveLogbookEntry(lb);
		logbookDao.deleteEntry(lb);
		tx.commit();
	}

	public LogbookEntry persistLogbookEntryWithData(Module m, Employee e, LogbookEntry lb) {
		logbookDao.setSession(HibernateUtil.getCurrentSession());
		employeeDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();

		lb.setEmployee(e);
		lb.setModule(m);

		lb = logbookDao.saveLogbookEntry(lb);

		tx.commit();
		return lb;
	}

	public Collection<LogbookEntry> findLogbookEntriesForEmployee(Employee empl) {
		logbookDao.setSession(HibernateUtil.getCurrentSession());
		employeeDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();

		Collection<LogbookEntry> entriesForEmployee = logbookDao.findForEmployee(empl);
		
		tx.commit();
		return entriesForEmployee;
	}

	// TODO update Time For Entry
	// TODO update Phase

	// ---------------------------------------------------------------

	// PROJECT STUFF
	// ---------------------------------------------------------------

	public Project saveProject(Project project) {
		projectDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();

		project = projectDao.saveProject(project);

		tx.commit();
		return project;
	}

	public Project findProjectById(Long id) {
		projectDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();

		Project p = projectDao.findById(id);

		tx.commit();
		return p;
	}

	public Collection<Project> findAllProjects() {
		projectDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();

		Collection<Project> projects = projectDao.findAll();

		tx.commit();
		return projects;
	}

	public Project addEmployeeToProject(Employee employee, Project project) {
		projectDao.setSession(HibernateUtil.getCurrentSession());
		employeeDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();

		// employee = employeeDao.saveEmployee(employee);
		project = projectDao.addMember(project, employee);
		employee = employeeDao.saveEmployee(employee);
		// project.addMember(employee);
		// project = projectDao.saveProject(project);

		tx.commit();
		return project;
	}

	public Project removeEmployeeFromProject(Employee employee, Project project) {
		projectDao.setSession(HibernateUtil.getCurrentSession());
		employeeDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();

		project = projectDao.removeMember(project, employee);
		employee = employeeDao.saveEmployee(employee);

		tx.commit();
		return project;
	}

	// TODO update Lead

	public Project addModuleToProject(Module module, Project project) {
		projectDao.setSession(HibernateUtil.getCurrentSession());
		moduleDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();

		project = projectDao.addModule(project, module);
		module = moduleDao.saveModule(module);

		tx.commit();
		return project;
	}

	public Project removeModuleFromProject(Module module, Project project) {
		projectDao.setSession(HibernateUtil.getCurrentSession());
		moduleDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();

		project = projectDao.removeModule(project, module);
		module = moduleDao.saveModule(module);
		tx.commit();
		return project;
	}

	// TODO update Name
	// TODO get issues of project by state
	// TODO sum effort/progress of issues by state
	// ---------------------------------------------------------------

	// MODULE STUFF
	// ---------------------------------------------------------------
	public Module saveModule(Module module) {
		moduleDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();

		module = moduleDao.saveModule(module);

		tx.commit();
		return module;
	}

	public Collection<Module> findAllModules() {
		moduleDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();
		Collection<Module> modules = moduleDao.findAll();
		tx.commit();

		return modules;
	}

	public Module findModuleById(Long id) {

		moduleDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();
		Module module = moduleDao.findById(id);
		tx.commit();

		return module;
	}

	public void deleteModule(Module m) {
		moduleDao.setSession(HibernateUtil.getCurrentSession());
		projectDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();
		if (m.getProject() == null) {
			moduleDao.delete(m);
		} else {
			Project p = m.getProject();
			p = projectDao.removeModule(p, m);
			m = moduleDao.saveModule(m);
			moduleDao.delete(m);
		}

		tx.commit();
	}
	// ---------------------------------------------------------------

	public void close() {
		HibernateUtil.closeSessionFactory();
	}

}
