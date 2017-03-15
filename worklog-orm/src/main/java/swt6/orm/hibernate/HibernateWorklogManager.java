package swt6.orm.hibernate;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import swt6.orm.domain.Address;
import swt6.orm.domain.Employee;
import swt6.orm.domain.Issue;
import swt6.orm.domain.IssueType;
import swt6.orm.domain.LogbookEntry;
import swt6.orm.domain.Module;
import swt6.orm.domain.PermanentEmployee;
import swt6.orm.domain.Phase;
import swt6.orm.domain.PriorityType;
import swt6.orm.domain.Project;
import swt6.orm.domain.TemporaryEmployee;
import swt6.util.DateUtil;

public class HibernateWorklogManager {

	public static List<Employee> listEmployees() {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();

		List<Employee> l = session.createQuery("Select e from Employee e order by lastName", Employee.class)
				.getResultList();

		for (Employee e : l) {
			System.out.println(e);
		}

		tx.commit();

		return l;

	}

	// v2
	@SuppressWarnings("unchecked")
	private static <T> T saveEntity(T entity) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();
		entity = (T) session.merge(entity);

		tx.commit();
		return entity;

	}

	private static Employee assignProjectsToEmployee(Employee empl, Employee lead, Project... projects) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();

		empl = (Employee) session.merge(empl);

		for (Project p : projects) {
			p.setProjectLeader(lead);
			empl.addProject(p);
		}

		tx.commit();
		return empl;
	}

	public static void main(String[] args) {
		try {
			System.out.println("------ create Schema --------");
			HibernateUtil.getSessionFactory();

			PermanentEmployee pe = new PermanentEmployee("Simon", "Schwarz", DateUtil.getDate(1994, 6, 21));
			TemporaryEmployee te = new TemporaryEmployee("Simone", "Schwanz", DateUtil.getDate(1994, 6, 21),
					new Address("4242", "somewhere", "street 1"));
			pe.setAddress(new Address("4242", "somewhere", "street 1"));

			pe.setSalary(5000);
			te.setHourlyRate(100);
			te.setRenter("Microsoft");
			te.setStartDate(DateUtil.getDate(2017, 01, 01));
			te.setEndDate(DateUtil.getDate(2017, 12, 31));
			Address addr = new Address("4242", "St. Valentin", "Stifterstraße 24");

			te.setAddress(addr);

			pe.setAddress(addr);

			Employee empl1 = pe;
			Employee empl2 = te;

			LogbookEntry entry1 = new LogbookEntry(DateUtil.getTime(10, 15), DateUtil.getTime(15, 30), null);
			LogbookEntry entry2 = new LogbookEntry(DateUtil.getTime(10, 15), DateUtil.getTime(15, 30), null);
			//
			//
//			entry1.setPhase(new Phase("Analysis"));
//			entry2.setPhase(new Phase("IMPLEMENTATION"));

			Project p1 = new Project("Office");
			Project p2 = new Project("EnterpriseServer");

			Issue issue = new Issue(IssueType.NEW, PriorityType.NORMAL, 0, null, p1);

//			Module mod1 = new Module("part1", p1);
//			Module mod2 = new Module("part1.1", p2);

			// issue = saveEntity(issue);

			System.out.println("------ save mpl--------");
			empl1 = saveEntity(empl1);

			System.out.println("------ save mpl--------");
			empl2 = saveEntity(empl2);

			System.out.println("------ list employee--------");
			listEmployees();

			System.out.println("------ add Projects--------");

			empl1 = assignProjectsToEmployee(empl1, empl1, p1);
			empl2 = assignProjectsToEmployee(empl2, empl1, p2);

			System.out.println("------ add modules to LogBooks--------");
//			entry1 = addModuleToEntry(entry1, mod1);
//			entry2 = addModuleToEntry(entry2, mod2);

			System.out.println("------ addLogbookentries--------");

			empl1 = addLogbookentries(empl1, entry1);
			empl2 = addLogbookentries(empl2, entry2);

			listEmployees();
			empl1 = addIssues(empl1, issue);

			testFetchingStrategies();
			System.out.println("------ listLogbookEntriesForEmployee --------");
			listLogbookEntriesOfEmployee(empl1);
			listLogbookEntriesOfEmployee(empl2);

			System.out.println("------ testJoinQuery --------");
			testJoinQuery();

			System.out.println("------ testFetchJoinQuery--------");
			testFetchJoinQuery();

			System.out.println("------ testCriteriaQuery --------");
			// testCriteriaQuery(empl1);
			// testCriteriaQuery(empl2);
		} finally {
			HibernateUtil.closeSessionFactory();
		}
	}

	private static LogbookEntry addModuleToEntry(LogbookEntry entry1, Module mod1) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();

		entry1.setModule(mod1);

		entry1 = (LogbookEntry) session.merge(entry1);

		tx.commit();
		return entry1;
	}

	private static Project assignLeadToProject(Employee pe, Project p1) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();

		p1.setProjectLeader(pe);
		pe = (Employee) session.merge(pe);

		tx.commit();
		return p1;
	}

	private static Employee addIssues(Employee empl, Issue issue) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();

		empl = (Employee) session.merge(empl);

		empl.addIssue(issue);

		tx.commit();
		return empl;
	}

	private static Employee addLogbookentries(Employee empl, LogbookEntry... entries) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();

		empl = (Employee) session.merge(empl);

		for (LogbookEntry entry : entries) {
			empl.addLogbookEntry(entry);
		}

		tx.commit();
		return empl;
	}

	private static void testFetchingStrategies() {
		// prepare: fetch valid ids for employee and logbookentry

		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();

		Long entryId = null;
		Long emplId = null;
		try {
			Optional<LogbookEntry> entry = session.createQuery("select le from LogbookEntry le", LogbookEntry.class)
					.setMaxResults(1).stream().findAny();
			if (!entry.isPresent())
				return;
			entryId = entry.get().getId();

			Optional<Employee> empl = session.createQuery("select e from Employee e", Employee.class).setMaxResults(1)
					.stream().findAny();
			if (!empl.isPresent())
				return;
			emplId = empl.get().getId();
		} finally {
			tx.commit();
		}

		System.out.println("############################################");

		session = HibernateUtil.getCurrentSession();
		tx = session.beginTransaction();

		System.out.println("###> Fetching LogbookEtnry ...");
		LogbookEntry entry = session.get(LogbookEntry.class, entryId);
		System.out.println("###> Fetched LogbookEtnry ...");
		Employee empl1 = entry.getEmployee();
		System.out.println("###> Fetched associated employee...");
		System.out.println(empl1);
		System.out.println("###> accessed associated employee...");

		tx.commit();

		System.out.println("############################################");

		session = HibernateUtil.getCurrentSession();
		tx = session.beginTransaction();

		System.out.println("###> Fetching Employee ...");
		empl1 = session.get(Employee.class, emplId);
		System.out.println("###> Fetched LogbookEtnry ...");
		Set<LogbookEntry> entries = empl1.getLogBookentries();
		System.out.println("###> Fetched associated entries...");
		for (LogbookEntry e : entries)
			System.out.println("  " + e);
		System.out.println("###> accessed associated entries...");
		tx.commit();

		System.out.println("############################################");
	}

	private static void listLogbookEntriesOfEmployee(Employee empl) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();

		System.out.println("logbook entries of employee: " + empl.getLastName());

		// Remember! HQL quereis refer to java (domain) objects
		// v1 SQL injection!!!
		// Query<LogbookEntry> query = session.createQuery("from LogbookEntry
		// where employee.id = " + empl.getId(),
		// LogbookEntry.class);

		// v2 unnamed parameter query
		// Query<LogbookEntry> query = session.createQuery("from LogbookEntry
		// where employee.id = ?", LogbookEntry.class);
		// query.setParameter(0, empl.getId());

		// v3 named parameter query
		// Query<LogbookEntry> query = session.createQuery("from LogbookEntry
		// where employee.id = :id", LogbookEntry.class);
		// query.setParameter("id", empl.getId());

		// v4 parameters can refer to domain objects
		Query<LogbookEntry> query = session.createQuery("from LogbookEntry where employee=:empl", LogbookEntry.class);
		query.setParameter("empl", empl);

		query.stream().forEach(System.out::println);

		tx.commit();

	}

	private static void testJoinQuery() {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();

		// v1 navigate through n:1 relationship using a querxy
		// Query<LogbookEntry> query = session.createQuery(
		// "select le from LogbookEntry le where le.employee.address.zipCode =
		// :zipcode", LogbookEntry.class);
		//
		// query.setParameter("zipcode", "4300");
		// query.stream().forEach(System.out::println);

		Query<Employee> emplQuery = session.createQuery(
				"select e from Employee e join e.logBookentries le where le.phase.name like :pattern", Employee.class);
		emplQuery.setParameter("pattern", "%Impl%");
		emplQuery.getResultList().stream().forEach(l -> System.out.println(l.getFirstName()));

		tx.commit();
	}

	private static void testFetchJoinQuery() {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();

		Query<Employee> emplQuery = session
				.createQuery("select distinct e from Employee e join fetch e.logBookentries le", Employee.class);
		emplQuery.getResultList().stream().forEach(System.out::println);

		tx.commit();
	}

	// private static void testCriteriaQuery(Employee empl) {
	// Session session = HibernateUtil.getCurrentSession();
	// Transaction tx = session.beginTransaction();
	//
	// CriteriaBuilder cb = session.getCriteriaBuilder();
	// CriteriaQuery<LogbookEntry> allEntriesQuery =
	// cb.createQuery(LogbookEntry.class);
	// Root<LogbookEntry> rootLe = allEntriesQuery.from(LogbookEntry.class);
	// ParameterExpression<Employee> employeeParam =
	// cb.parameter(Employee.class);
	//
	// // v1
	// // CriteriaQuery<LogbookEntry> entriesQuery =
	// // allEntriesQuery.select(rootLe)
	// // .where(cb.equal(rootLe.get("employee"), employeeParam));
	// // v2
	// CriteriaQuery<LogbookEntry> entriesQuery = allEntriesQuery.select(rootLe)
	// .where(cb.equal(rootLe.get(LogbookEntry_.employee), employeeParam));
	// Query<LogbookEntry> query = session.createQuery(entriesQuery);
	// query.setParameter(employeeParam, empl);
	//
	// query.stream().forEach(System.out::println);
	//
	// tx.commit();
	// }

}
