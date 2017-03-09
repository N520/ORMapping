package swt6.orm.simple.hibernate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import swt6.simple.domain.Employee;

public class HibernateEmployeeManager {

	static String promptFor(BufferedReader in, String p) {
		System.out.print(p + "> ");
		try {
			return in.readLine();
		} catch (Exception e) {
			return promptFor(in, p);
		}
	}

	public static void main(String[] args) {
		DateFormat dfmt = new SimpleDateFormat("dd.MM.yyyy");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String availCmds = "commands: quit, insert, list, update, delete, help";

		System.out.println("Hibernate Employee Admin");
		System.out.println(availCmds);
		String userCmd = promptFor(in, "");

		try {
			while (!userCmd.equals("quit")) {
				switch (userCmd) {
				case "insert":
					try {
						saveEmployee(new Employee(promptFor(in, "firstName"), promptFor(in, "lastName"),
								dfmt.parse(promptFor(in, "dob (dd.mm.yyyy)"))));

					} catch (ParseException e) {

					}
					userCmd = promptFor(in, "");
					break;

				case "list":
					List<Employee> l = listEmployees();

					l.stream().sorted((e1, e2) -> {
						if (e1.getId() > e2.getId())
							return 1;
						if (e1.getId() < e2.getId())
							return -1;
						return 0;
					}).forEach((e) -> {
						System.out.println(e);
					});

					userCmd = promptFor(in, "");
					break;
				case "quit":
					userCmd = "quit";
					break;
				case "update":
					updateEmployee(Long.parseLong(promptFor(in, "id")), promptFor(in, "firstName"),
							promptFor(in, "lastName"), dfmt.parse(promptFor(in, "dob")));
					userCmd = promptFor(in, "");
					break;
				case "delete":
					deleteEmployee(Long.parseLong(promptFor(in, "id")));
					userCmd = promptFor(in, "");
					break;
				case "help":
					System.out.println(availCmds);
					userCmd = promptFor(in, "");
					break;
				default:
					System.err.println("Error: Invalid Command!");
					userCmd = promptFor(in, "");
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSessionFactory();
		}
	}

	private static boolean deleteEmployee(long emplId) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();
		// v1
		// Employee empl = session.find(Employee.class, emplId);
		// if (empl != null) {
		// session.remove(empl);
		// }
		// boolean deleted = empl != null;

		// v2
		Query<?> q = session.createQuery("delete from Employee e where e.id = :id");
		q.setParameter("id", emplId);
		boolean deleted = q.executeUpdate() > 0;

		tx.commit();
		return deleted;
	}

	private static boolean updateEmployee(long emplId, String firstName, String lastName, Date dob) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();
		Employee empl = session.find(Employee.class, emplId);
		if (empl != null) {
			empl.setFirstName(firstName);
			empl.setLastName(lastName);
			empl.setDateOfBirth(dob);
		}

		tx.commit();
		return empl != null;
	}

	public static List<Employee> listEmployees() {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();

		List<Employee> l = session.createQuery("Select e from Employee e order by lastName", Employee.class)
				.getResultList();

		tx.commit();
		return l;

	}

	// v2
	private static void saveEmployee(Employee employee) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();

		session.save(employee);

		tx.commit();

	}

	// V1
	// private static void saveEmployee(Employee employee) {
	// SessionFactory sessionFactory =
	// HibernateHelper.buildSessionFactory("hibernate-simple.cfg.xml");
	// Session session = sessionFactory.openSession();
	// Transaction tx = session.beginTransaction();
	//
	// session.save(employee);
	//
	// tx.commit();
	// session.close();
	// sessionFactory.close();
	// }

}
