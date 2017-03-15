package swt6.orm.dao;

import java.util.Collection;

import swt6.orm.domain.Employee;
import swt6.orm.domain.LogbookEntry;
import swt6.orm.domain.Module;
import swt6.orm.domain.Phase;

public interface LogbookDao extends Dao {

	LogbookEntry findById(Long id);

	Collection<LogbookEntry> findAll();

	Collection<LogbookEntry> findByPhase(Phase phase);

	LogbookEntry saveLogbookEntry(LogbookEntry entry);

	void deleteEntry(LogbookEntry entry);

	LogbookEntry assignEmployee(LogbookEntry lb, Employee empl1);

	LogbookEntry assignModule(LogbookEntry lb, Module m);

	Collection<LogbookEntry> findForEmployee(Employee e);
}
