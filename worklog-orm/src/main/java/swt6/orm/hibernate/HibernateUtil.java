package swt6.orm.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import swt6.orm.domain.Employee;
import swt6.orm.domain.LogbookEntry;
import swt6.orm.domain.Phase;
import swt6.orm.domain.Project;

public class HibernateUtil {

	private static SessionFactory sessionFactory;
	private static Session session;

	/**
	 * creating a sessionFactory is timeconsuming, thus this should be done only
	 * once per database using this method
	 * 
	 * @return a threadsafe SessionFactory
	 */
	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			Configuration config = new Configuration().configure();
			config.addAnnotatedClass(Employee.class);
			config.addAnnotatedClass(LogbookEntry.class);
			config.addAnnotatedClass(Project.class);
			config.addAnnotatedClass(Phase.class);
			sessionFactory = config.buildSessionFactory();

		}
		return sessionFactory;
	}

	public static void closeSessionFactory() {
		if (sessionFactory != null) {
			sessionFactory.close();
			sessionFactory = null;
		}
	}

	public static Session getSession() {
		if (session == null)
			session = getSessionFactory().openSession();
		return session;
	}

	public static void closeSession() {
		if (session != null) {
			session.close();
			session = null;
		}
	}

	/**
	 * V s2ession is lightweight component but not threadsafe seperate
	 * connection opened for each thread Hibernate: getCurrentSession() delivers
	 * session for each thread Set Property in config file session is closed by
	 * comitting/rollback transaction
	 * 
	 * @return
	 */
	// S
	public static Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}

}
