package swt6.orm.dao;

import java.util.Collection;

import org.hibernate.query.Query;

import swt6.orm.domain.Module;

public interface ModuleDao extends Dao {
	Collection<Module> findAll();

	Module findById(Long id);

	Module saveModule(Module issue);


	void delete(Module issue);

	Collection<Module> query(Query<Module> query);

}
