package swt6.orm.dao;

import org.hibernate.Session;

public abstract class AbstractDao implements Dao {
	@SuppressWarnings("unchecked")
	protected <T> T saveEntity(T entity) {
		entity = (T) session.merge(entity);

		return entity;
	}

	

	protected <T> void deleteEntity(T entity) {
		session.delete(entity);
	}

	protected void checkSessionAvailable() {
		if (session == null || !session.isOpen())
			throw new IllegalStateException("session is invalid or null");
		
	}

	protected Session session;

	public void setSession(Session session) {
		this.session = session;
	}

	public Session getSession() {
		return session;
	}

}
