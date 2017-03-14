package swt6.orm.hibernate;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import swt6.orm.domain.Address;
import swt6.orm.domain.Employee;
import swt6.orm.domain.LogbookEntry;
import swt6.orm.domain.PermanentEmployee;
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

	private static Employee assignProjectsToEmployee(Employee empl, Project... projects) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();

		empl = (Employee) session.merge(empl);

		for (Project p : projects) {
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
			TemporaryEmployee te = new TemporaryEmployee("Simone", "Schwanz", DateUtil.getDate(1994, 6, 21));
			pe.setAddress(new Address("4242", "somewhere", "street 1"));

			pe.setSalary(5000);
			te.setHourlyRate(100);
			te.setRenter("Microsoft");
			te.setStartDate(DateUtil.getDate(2017, 01, 01));
			te.setEndDate(DateUtil.getDate(2017, 12, 31));

			Employee empl1 = pe;
			Employee empl2 = te;

			LogbookEntry entry1 = new LogbookEntry("Anal - yse", DateUtil.getTime(10, 15), DateUtil.getTime(15, 30),
					null);
			LogbookEntry entry2 = new LogbookEntry("Test", DateUtil.getTime(10, 15), DateUtil.getTime(15, 30), null);
			LogbookEntry entry3 = new LogbookEntry("Implementieung", DateUtil.getTime(10, 15), DateUtil.getTime(15, 30),
					null);

			Project p1 = new Project("Office");
			Project p2 = new Project("EnterpriseServer");

			Address addr = new Address("4242", "St. Valentin", "Stifterstraße 24");
			te.setAddress(addr);

			pe.setAddress(addr);

			System.out.println("------ save mpl--------");
			empl1 = saveEntity(empl1);

			System.out.println("------ save mpl--------");
			te = saveEntity(te);

			System.out.println("------ list employee--------");
			listEmployees();

			System.out.println("------ addLogbookentries--------");
			empl1 = addLogbookentries(pe, entry1, entry3);
			empl2 = addLogbookentries(te, entry2);

			System.out.println("------ add Projects--------");
			empl1 = assignProjectsToEmployee(pe, p1);
			empl2 = assignProjectsToEmployee(te, p2);

			listEmployees();

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
				"select e from Employee e join e.logBookentries le where le.activity like :pattern", Employee.class);
		emplQuery.setParameter("pattern", "%Impl%");
		emplQuery.stream().forEach(System.out::println);

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
