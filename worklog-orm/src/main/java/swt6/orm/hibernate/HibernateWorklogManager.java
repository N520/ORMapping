package swt6.orm.hibernate;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import swt6.orm.domain.Address;
import swt6.orm.domain.Employee;
import swt6.orm.domain.LogbookEntry;
import swt6.orm.domain.Project;
import swt6.util.DateUtil;

public class HibernateWorklogManager {

	public static List<Employee> listEmployees() {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();

		List<Employee> l = session.createQuery("Select e from Employee e order by lastName", Employee.class)
				.getResultList();

		l.stream().forEach((e) -> {
			System.out.println(e);
			if (e.getLogBookentries().size() > 0) {
				System.out.println("  loobookentries: ");
				e.getLogBookentries().stream().forEach((lb) -> {
					System.out.println("    " + lb);
				});
			}
			if (e.getProjects().size() > 0) {
				System.out.println("  projects: ");
				e.getProjects().stream().forEach((p) -> {
					System.out.println("    " + p);
				});
			}
		});

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

	// v1
	// private static Employee saveEmployee(Employee employee) {
	// Session session = HibernateUtil.getCurrentSession();
	// Transaction tx = session.beginTransaction();
	//
	// employee = (Employee) session.merge(employee);
	//
	// tx.commit();
	//
	// return employee;
	// }

	public static void main(String[] args) {
		try {
			System.out.println("------ create Schema --------");
			HibernateUtil.getSessionFactory();

			Employee empl1 = new Employee("Simon", "Schwarz", DateUtil.getDate(1994, 6, 21));
			Employee empl2 = new Employee("Simone", "Schwanz", DateUtil.getDate(1994, 6, 21));

			LogbookEntry entry1 = new LogbookEntry("Anal - yse", DateUtil.getTime(10, 15), DateUtil.getTime(15, 30),
					null);
			LogbookEntry entry2 = new LogbookEntry("Test", DateUtil.getTime(10, 15), DateUtil.getTime(15, 30), null);
			LogbookEntry entry3 = new LogbookEntry("Implementieung", DateUtil.getTime(10, 15), DateUtil.getTime(15, 30),
					null);

			Project p1 = new Project("Office");
			Project p2 = new Project("EnterpriseServer");

			Address addr = new Address("4300", "St. Valentin", "Stifterstraße 24");
			empl1.setAddress(addr);

			System.out.println("------ save mpl--------");
			empl1 = saveEntity(empl1);

			System.out.println("------ save mpl--------");
			empl2 = saveEntity(empl2);

			System.out.println("------ list employee--------");
			listEmployees();

			System.out.println("------ addLogbookentries--------");
			empl1 = addLogbookentries(empl1, entry1, entry3);
			empl2 = addLogbookentries(empl2, entry2);

			System.out.println("------ add Projects--------");
			empl1 = assignProjectsToEmployee(empl1, p1);
			empl2 = assignProjectsToEmployee(empl2, p2);

			listEmployees();
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
}
