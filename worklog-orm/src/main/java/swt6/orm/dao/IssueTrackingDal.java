package swt6.orm.dao;

import java.sql.Date;
import java.util.Collection;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import swt6.orm.domain.Employee;
import swt6.orm.domain.Issue;
import swt6.orm.domain.IssueType;
import swt6.orm.domain.LogbookEntry;
import swt6.orm.domain.Module;
import swt6.orm.domain.PermanentEmployee;
import swt6.orm.domain.Phase;
import swt6.orm.domain.PhaseDescriptor;
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
	public Collection<Employee> findEmployeesByName(String name) {
		employeeDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();

		Query<Employee> query = HibernateUtil.getCurrentSession()
				.createQuery("from Employee where firstname like :name or lastname like :name", Employee.class);
		query.setParameter("name", "%" + name + "%");
		Collection<Employee> employees = employeeDao.query(query);

		tx.commit();
		return employees;
	}

	// ---------------------------------------------------------------

	// ISSUESTUFF
	// ---------------------------------------------------------------

	public Issue findIssueById(Long id) {
		issueDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();
		Issue issue = issueDao.findById(id);
		tx.commit();

		return issue;
	}

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

	public Issue assignEmployeeToIssue(Employee employee, Issue issue) {
		issueDao.setSession(HibernateUtil.getCurrentSession());
		employeeDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();

		issue = issueDao.updateEmployee(issue, employee);
		tx.commit();
		return issue;
	}

	public void deleteIssue(Issue issue) {
		issueDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();

		issue.moveToProject(null);
		issueDao.delete(issue);
		tx.commit();
	}

	public Issue saveIssueEffort(Issue issue, IssueType state) {
		issueDao.setSession(HibernateUtil.getCurrentSession());
		issue.setState(state);
		return saveIssue(issue);
	}

	public Issue saveIssueEffort(Issue issue, int effort) {
		issueDao.setSession(HibernateUtil.getCurrentSession());
		issue.setEffort(effort);
		return saveIssue(issue);
	}

	public Issue saveIssueProgress(Issue issue, int progress) {
		issueDao.setSession(HibernateUtil.getCurrentSession());
		issue.setProgress(progress);
		return saveIssue(issue);
	}

	public Collection<Issue> findAllIssuesWithState(IssueType state) {
		Session session = HibernateUtil.getCurrentSession();
		issueDao.setSession(session);
		Transaction tx = session.beginTransaction();
		session.enableFilter("ISSUE_STATE_FILTER").setParameter("state", state.toString());

		Collection<Issue> issues = issueDao.findAll();

		tx.commit();
		return issues;
	}

	public Collection<Issue> findAllEmployeeIssuesWithState(Employee employee, IssueType state) {
		Session session = HibernateUtil.getCurrentSession();
		issueDao.setSession(session);
		Transaction tx = session.beginTransaction();
		session.enableFilter("ISSUE_STATE_FILTER").setParameter("state", state.toString());
		Query<Issue> query = session.createQuery("from Issue where employee = :employee", Issue.class);
		query.setParameter("employee", employee);
		Collection<Issue> issues = issueDao.query(query);

		tx.commit();
		return issues;
	}

	// ---------------------------------------------------------------

	// LOGBOOK STUFF
	// ---------------------------------------------------------------

	public LogbookEntry saveLogbookEntry(LogbookEntry logbookEntry) {
		logbookDao.setSession(HibernateUtil.getCurrentSession());
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
		// lb.setModule(null);
		lb = logbookDao.saveLogbookEntry(lb);
		logbookDao.deleteEntry(lb);
		tx.commit();
	}

	public LogbookEntry persistLogbookEntryWithData(Module m, Employee e, Phase p, LogbookEntry lb) {
		logbookDao.setSession(HibernateUtil.getCurrentSession());
		employeeDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();

		lb.setEmployee(e);
		lb.setModule(m);
		lb.setPhase(p);

		lb = logbookDao.saveLogbookEntry(lb);

		tx.commit();
		return lb;
	}

	public LogbookEntry persistLogbookEntryWithData(Module m, Employee e, PhaseDescriptor p, LogbookEntry lb) {
		return persistLogbookEntryWithData(m, e, new Phase(p), lb);
	}

	// TODO save individual stuff (Employee, Phase, Module)

	public Collection<LogbookEntry> findLogbookEntriesForEmployee(Employee empl) {
		logbookDao.setSession(HibernateUtil.getCurrentSession());
		employeeDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();

		Collection<LogbookEntry> entriesForEmployee = logbookDao.findForEmployee(empl);

		tx.commit();
		return entriesForEmployee;
	}

	public LogbookEntry saveWorktimeForEntry(LogbookEntry lb, Date start, Date end) {
		if (start == null)
			throw new IllegalStateException("starttime must not be null");
		if (lb.getEndTime() != null && end == null)
			throw new IllegalStateException("a fixed endTime cannot be set null again");

		lb.setStartTime(start);
		lb.setEndTime(end);
		return saveLogbookEntry(lb);
	}

	public LogbookEntry saveLogbookEntryPhase(LogbookEntry lb, Phase phase) {
		lb.setPhase(phase);
		return saveLogbookEntry(lb);
	}

	public LogbookEntry saveLogbookEntryPhase(LogbookEntry lb, PhaseDescriptor phase) {
		lb.setPhase(new Phase(phase));
		return saveLogbookEntry(lb);
	}

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

	public Project saveProjectLead(Project project, Employee lead) {
		projectDao.setSession(HibernateUtil.getCurrentSession());
		project.setProjectLeader(lead);
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();

		project = projectDao.saveProject(project);

		tx.commit();
		return project;

	}

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

	public Project saveProjectName(Project project, String name) {
		project.setName(name);
		return saveProject(project);
	}

	public Collection<Issue> findAllProjectIssueWithState(Project project, IssueType state) {
		issueDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = HibernateUtil.getCurrentSession().beginTransaction();
		HibernateUtil.getCurrentSession().enableFilter("ISSUE_STATE_FILTER").setParameter("state", state.toString());

		Query<Issue> query = HibernateUtil.getCurrentSession().createQuery("from Issue where project = :project",
				Issue.class);
		query.setParameter("project", project);

		Collection<Issue> issues = issueDao.query(query);
		tx.commit();

		return issues;

	}
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
