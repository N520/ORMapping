package swt6.orm.dao;

import java.util.Date;
import java.util.HashSet;
import java.util.ArrayList;
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
	public Employee saveEmployee(Employee employee) {
		employeeDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = null;
		try {
			tx = HibernateUtil.getCurrentSession().beginTransaction();
			employee = employeeDao.saveEmployee(employee);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return employee;

	}

	public Employee findEmployeeById(Long id) {
		employeeDao.setSession(HibernateUtil.getCurrentSession());
		Employee employee = null;
		Transaction tx = null;
		try {
			tx = HibernateUtil.getCurrentSession().beginTransaction();
			employee = employeeDao.findById(id);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return employee;
	}

	public Collection<Employee> findAllEmployees() {
		employeeDao.setSession(HibernateUtil.getCurrentSession());
		Collection<Employee> employees = new ArrayList<>();
		Transaction tx = null;
		try {
			tx = HibernateUtil.getCurrentSession().beginTransaction();
			employees = employeeDao.findAll();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return employees;
	}

	public void deleteEmployee(Employee employee) {
		employeeDao.setSession(HibernateUtil.getCurrentSession());
		projectDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = null;
		try {
			tx = HibernateUtil.getCurrentSession().beginTransaction();
			for (Project p : employee.getProjects()) {
				employee.removeProject(p);
				if (employee.getId().equals(p.getProjectLeader().getId()))
					throw new RuntimeException(
							employee + " is Leader of project " + p + " assign a new Leader before deleting");
				p = projectDao.saveProject(p);
			}
			employee = employeeDao.saveEmployee(employee);
			employeeDao.delete(employee);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
	}

	public Collection<PermanentEmployee> findAllPermanentEmployees() {
		employeeDao.setSession(HibernateUtil.getCurrentSession());
		Collection<PermanentEmployee> permanentEmployees = new ArrayList<>();
		Transaction tx = null;
		try {
			tx = HibernateUtil.getCurrentSession().beginTransaction();
			permanentEmployees = employeeDao.findAllPermanent();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return permanentEmployees;
	}

	public Collection<TemporaryEmployee> findAllTemporaryEmployees() {
		employeeDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = null;
		Collection<TemporaryEmployee> temporaryEmployees = new ArrayList<>();
		try {
			tx = HibernateUtil.getCurrentSession().beginTransaction();
			temporaryEmployees = employeeDao.findAllTemporary();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
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

		Transaction tx = null;
		Collection<Employee> employees = new ArrayList<>();

		try {
			tx = HibernateUtil.getCurrentSession().beginTransaction();
			Query<Employee> query = HibernateUtil.getCurrentSession()
					.createQuery("from Employee where firstname like :name or lastname like :name", Employee.class);
			query.setParameter("name", "%" + name + "%");
			employees = employeeDao.query(query);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return employees;
	}

	// ---------------------------------------------------------------

	// ISSUESTUFF
	// ---------------------------------------------------------------

	public Issue findIssueById(Long id) {
		issueDao.setSession(HibernateUtil.getCurrentSession());
		Issue issue = null;
		Transaction tx = null;
		try {
			tx = HibernateUtil.getCurrentSession().beginTransaction();
			issue = issueDao.findById(id);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return issue;
	}

	public Collection<Issue> findAllIssues() {
		issueDao.setSession(HibernateUtil.getCurrentSession());
		Collection<Issue> issues = new ArrayList<>();

		Transaction tx = null;
		try {
			tx = HibernateUtil.getCurrentSession().beginTransaction();
			issues = issueDao.findAll();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return issues;
	}

	public Issue saveIssue(Issue issue) {
		issueDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = null;
		try {
			tx = HibernateUtil.getCurrentSession().beginTransaction();
			issue = issueDao.saveIssue(issue);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return issue;
	}

	public Issue assignEmployeeToIssue(Employee employee, Issue issue) {
		issueDao.setSession(HibernateUtil.getCurrentSession());
		employeeDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = null;
		try {
			tx = HibernateUtil.getCurrentSession().beginTransaction();
			issue = issueDao.updateEmployee(issue, employee);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return issue;
	}

	public void deleteIssue(Issue issue) {
		issueDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = null;
		try {
			tx = HibernateUtil.getCurrentSession().beginTransaction();
			issue.moveToProject(null);
			issueDao.delete(issue);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
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
		Collection<Issue> issues = new ArrayList<>();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.enableFilter("ISSUE_STATE_FILTER").setParameter("state", state.toString());
			issues = issueDao.findAll();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return issues;
	}

	public Collection<Issue> findAllEmployeeIssuesWithState(Employee employee, IssueType state) {
		Session session = HibernateUtil.getCurrentSession();
		issueDao.setSession(session);
		Collection<Issue> issues = new ArrayList<>();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.enableFilter("ISSUE_STATE_FILTER").setParameter("state", state.toString());
			Query<Issue> query = session.createQuery("from Issue where employee = :employee", Issue.class);
			query.setParameter("employee", employee);
			issues = issueDao.query(query);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return issues;
	}

	// ---------------------------------------------------------------

	// LOGBOOK STUFF
	// ---------------------------------------------------------------

	public LogbookEntry saveLogbookEntry(LogbookEntry logbookEntry) {
		logbookDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = null;
		try {
			tx = HibernateUtil.getCurrentSession().beginTransaction();
			logbookEntry = logbookDao.saveLogbookEntry(logbookEntry);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return logbookEntry;

	}

	public LogbookEntry findLogbookEntryById(Long id) {
		logbookDao.setSession(HibernateUtil.getCurrentSession());
		LogbookEntry entry = null;
		Transaction tx = null;
		try {
			tx = HibernateUtil.getCurrentSession().beginTransaction();
			entry = logbookDao.findById(id);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return entry;
	}

	public Collection<LogbookEntry> findAllLogbookEntries() {
		logbookDao.setSession(HibernateUtil.getCurrentSession());
		Collection<LogbookEntry> employees = new ArrayList<>();
		Transaction tx = null;
		try {
			tx = HibernateUtil.getCurrentSession().beginTransaction();
			employees = logbookDao.findAll();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return employees;
	}

	public void deleteLogbookEntry(LogbookEntry lb) {
		logbookDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = null;
		try {
			tx = HibernateUtil.getCurrentSession().beginTransaction();
			lb = logbookDao.saveLogbookEntry(lb);
			logbookDao.deleteEntry(lb);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
	}

	public LogbookEntry persistLogbookEntryWithData(Module m, Employee employee, Phase p, LogbookEntry lb) {
		logbookDao.setSession(HibernateUtil.getCurrentSession());
		employeeDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = null;
		try {
			tx = HibernateUtil.getCurrentSession().beginTransaction();
			lb.setEmployee(employee);
			lb.setModule(m);
			lb.setPhase(p);
			lb = logbookDao.saveLogbookEntry(lb);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return lb;
	}

	public LogbookEntry persistLogbookEntryWithData(Module m, Employee e, PhaseDescriptor p, LogbookEntry lb) {
		return persistLogbookEntryWithData(m, e, new Phase(p), lb);
	}

	public Collection<LogbookEntry> findLogbookEntriesForEmployee(Employee empl) {
		logbookDao.setSession(HibernateUtil.getCurrentSession());
		employeeDao.setSession(HibernateUtil.getCurrentSession());
		Collection<LogbookEntry> entriesForEmployee = new ArrayList<>();
		Transaction tx = null;
		try {
			tx = HibernateUtil.getCurrentSession().beginTransaction();
			entriesForEmployee = logbookDao.findForEmployee(empl);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
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
		Transaction tx = null;
		try {
			tx = HibernateUtil.getCurrentSession().beginTransaction();
			project = projectDao.saveProject(project);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return project;
	}

	public void deleteProject(Project p) {
		issueDao.setSession(HibernateUtil.getCurrentSession());
		employeeDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = null;
		try {

			findAllProjectIssues(p).forEach(i -> deleteIssue(i));
			for (Employee e : p.getMembers()) {
				System.out.println(p.getId() + " " + e);
				deleteEmployee(e);
				Thread.sleep(1000);
			}
			projectDao.setSession(HibernateUtil.getCurrentSession());
			tx = HibernateUtil.getCurrentSession().beginTransaction();

			projectDao.delete(p);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}

	}

	public Project findProjectById(Long id) {
		projectDao.setSession(HibernateUtil.getCurrentSession());
		Project p = null;
		Transaction tx = null;
		try {
			tx = HibernateUtil.getCurrentSession().beginTransaction();
			p = projectDao.findById(id);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return p;
	}

	public Collection<Project> findAllProjects() {
		projectDao.setSession(HibernateUtil.getCurrentSession());
		Collection<Project> projects = new HashSet<>();
		Transaction tx = null;
		try {
			tx = HibernateUtil.getCurrentSession().beginTransaction();
			projects = projectDao.findAll();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return projects;
	}

	public Project addEmployeeToProject(Employee employee, Project project) {
		projectDao.setSession(HibernateUtil.getCurrentSession());
		employeeDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = null;
		try {
			tx = HibernateUtil.getCurrentSession().beginTransaction();
			project = projectDao.addMember(project, employee);
			employee = employeeDao.saveEmployee(employee);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return project;
	}

	public Project removeEmployeeFromProject(Employee employee, Project project) {
		projectDao.setSession(HibernateUtil.getCurrentSession());
		employeeDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = null;
		try {
			tx = HibernateUtil.getCurrentSession().beginTransaction();
			project = projectDao.removeMember(project, employee);
			employee = employeeDao.saveEmployee(employee);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return project;
	}

	public Project saveProjectLead(Project project, Employee lead) {
		projectDao.setSession(HibernateUtil.getCurrentSession());
		project.setProjectLeader(lead);
		Transaction tx = null;
		try {
			tx = HibernateUtil.getCurrentSession().beginTransaction();
			project = projectDao.saveProject(project);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return project;

	}

	public Project addModuleToProject(Module module, Project project) {
		projectDao.setSession(HibernateUtil.getCurrentSession());
		moduleDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = null;
		try {
			tx = HibernateUtil.getCurrentSession().beginTransaction();
			project = projectDao.addModule(project, module);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return project;
	}

	public Project removeModuleFromProject(Module module, Project project) {
		projectDao.setSession(HibernateUtil.getCurrentSession());
		moduleDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = null;
		try {
			tx = HibernateUtil.getCurrentSession().beginTransaction();
			project = projectDao.removeModule(project, module);
			module = moduleDao.saveModule(module);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return project;
	}

	public Project saveProjectName(Project project, String name) {
		project.setName(name);
		return saveProject(project);
	}

	public Collection<Issue> findAllProjectIssuesWithState(Project project, IssueType state) {
		issueDao.setSession(HibernateUtil.getCurrentSession());
		Collection<Issue> issues = new HashSet<>();
		Transaction tx = null;
		try {
			tx = HibernateUtil.getCurrentSession().beginTransaction();
			HibernateUtil.getCurrentSession().enableFilter("ISSUE_STATE_FILTER").setParameter("state",
					state.toString());
			Query<Issue> query = HibernateUtil.getCurrentSession().createQuery("from Issue where project = :project",
					Issue.class);
			query.setParameter("project", project);
			issues = issueDao.query(query);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return issues;

	}

	public Collection<Issue> findAllProjectIssues(Project project) {
		issueDao.setSession(HibernateUtil.getCurrentSession());
		Collection<Issue> issues = new HashSet<>();
		Transaction tx = null;
		try {
			tx = HibernateUtil.getCurrentSession().beginTransaction();
			Query<Issue> query = HibernateUtil.getCurrentSession().createQuery("from Issue where project = :project",
					Issue.class);
			query.setParameter("project", project);
			issues = issueDao.query(query);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return issues;

	}
	// TODO sum effort/progress of issues by state
	// ---------------------------------------------------------------

	// MODULE STUFF
	// ---------------------------------------------------------------
	public Module saveModule(Module module) {
		moduleDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = null;
		try {
			tx = HibernateUtil.getCurrentSession().beginTransaction();
			module = moduleDao.saveModule(module);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return module;
	}

	public Collection<Module> findAllModules() {
		moduleDao.setSession(HibernateUtil.getCurrentSession());
		Collection<Module> modules = new HashSet<>();
		Transaction tx = null;
		try {
			tx = HibernateUtil.getCurrentSession().beginTransaction();
			modules = moduleDao.findAll();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return modules;
	}

	public Module findModuleById(Long id) {

		moduleDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = null;
		Module module = null;
		try {
			tx = HibernateUtil.getCurrentSession().beginTransaction();
			module = moduleDao.findById(id);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();

		}

		return module;
	}

	public void deleteModule(Module m) {
		moduleDao.setSession(HibernateUtil.getCurrentSession());
		projectDao.setSession(HibernateUtil.getCurrentSession());
		Transaction tx = null;
		try {
			tx = HibernateUtil.getCurrentSession().beginTransaction();
			if (m.getProject() == null) {
				moduleDao.delete(m);
			} else {
				Project p = m.getProject();
				p = projectDao.removeModule(p, m);
				m = moduleDao.saveModule(m);
				moduleDao.delete(m);
			}
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
	}
	// ---------------------------------------------------------------

	public void close() {
		HibernateUtil.closeSessionFactory();
	}

}
