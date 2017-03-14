package swt6.orm.dao;

import org.hibernate.Session;

public interface Dao {
	void setSession(Session session);

	Session getSession();

}
