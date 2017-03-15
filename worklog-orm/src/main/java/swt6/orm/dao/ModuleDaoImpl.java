package swt6.orm.dao;

import java.util.Collection;
import java.util.List;

import org.hibernate.query.Query;

import swt6.orm.domain.Module;

public class ModuleDaoImpl extends AbstractDao implements ModuleDao {

	@Override
	public Collection<Module> findAll() {
		checkSessionAvailable();

		List<Module> l = session.createQuery("from Module ", Module.class).getResultList();

		return l;
	}

	@Override
	public Module findById(Long id) {
		checkSessionAvailable();
		return session.get(Module.class, id);

	}

	@Override
	public Module saveModule(Module module) {
		checkSessionAvailable();
		return saveEntity(module);
	}

	@Override
	public void delete(Module module) {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<Module> query(Query<Module> query) {
		// TODO Auto-generated method stub
		return null;
	}

}
