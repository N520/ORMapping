package swt6.orm.dao;

import java.util.Collection;

import org.hibernate.query.Query;

import swt6.orm.domain.Employee;
import swt6.orm.domain.LogbookEntry;
import swt6.orm.domain.Module;
import swt6.orm.domain.Phase;

public class LogBookDaoImpl extends AbstractDao implements LogbookDao {

	@Override
	public LogbookEntry findById(Long id) {
		checkSessionAvailable();
		return session.get(LogbookEntry.class, id);
	}

	@Override
	public Collection<LogbookEntry> findAll() {
		checkSessionAvailable();
		return session.createQuery("from LogbookEntry", LogbookEntry.class).getResultList();
	}

	@Override
	public Collection<LogbookEntry> findByPhase(Phase phase) {
		checkSessionAvailable();
		Query<LogbookEntry> lbQuery = session.createQuery("from logBookEntries lb lb.phase.name like :pattern",
				LogbookEntry.class);
		lbQuery.setParameter("pattern", phase.getName());
		return lbQuery.getResultList();
	}

	@Override
	public LogbookEntry saveLogbookEntry(LogbookEntry entry) {
		checkSessionAvailable();

		entry = saveEntity(entry);

		return entry;
	}

	@Override
	public void deleteEntry(LogbookEntry entry) {
		checkSessionAvailable();
		deleteEntity(entry);
	}

	@Override
	public LogbookEntry assignEmployee(LogbookEntry lb, Employee empl1) {
		checkSessionAvailable();
		lb.attachEmployee(empl1);
		return lb;
	}

	@Override
	public LogbookEntry assignModule(LogbookEntry lb, Module m) {
		checkSessionAvailable();
		lb.setModule(m);
		return lb;
	}

}
