package swt6.orm.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import swt6.orm.domain.Address;
import swt6.orm.domain.Employee;
import swt6.orm.domain.Issue;
import swt6.orm.domain.LogbookEntry;
import swt6.orm.domain.Module;
import swt6.orm.domain.PermanentEmployee;
import swt6.orm.domain.Phase;
import swt6.orm.domain.Project;
import swt6.orm.domain.TemporaryEmployee;

public class HibernateUtil {

	private static SessionFactory sessionFactory;
	private static Session session;

	private static String configFile = "";

	public static void setConfigFile(String configFile) {
		HibernateUtil.configFile = configFile;
	}

	/**
	 * creating a sessionFactory is timeconsuming, thus this should be done only
	 * once per database using this method
	 * 
	 * @return a threadsafe SessionFactory
	 */
	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			Configuration config;
			if (configFile.equals(""))
				config = new Configuration().configure();
			else
				config = new Configuration().configure(configFile);
				
				
			config.addAnnotatedClass(Employee.class);
			config.addAnnotatedClass(LogbookEntry.class);
			config.addAnnotatedClass(Project.class);
			config.addAnnotatedClass(Phase.class);
			config.addAnnotatedClass(Address.class);
			config.addAnnotatedClass(PermanentEmployee.class);
			config.addAnnotatedClass(TemporaryEmployee.class);
			config.addAnnotatedClass(Issue.class);
			config.addAnnotatedClass(Module.class);
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
