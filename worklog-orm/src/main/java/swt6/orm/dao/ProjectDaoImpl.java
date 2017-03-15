package swt6.orm.dao;

import java.util.Collection;

import swt6.orm.domain.Employee;
import swt6.orm.domain.Module;
import swt6.orm.domain.Project;

public class ProjectDaoImpl extends AbstractDao implements ProjectDao {

	@Override
	public Project findById(Long id) {
		return session.get(Project.class, id);
	}

	@Override
	public Collection<Project> findAll() {
		checkSessionAvailable();

		return session.createQuery("from Project", Project.class).getResultList();
	}

	@Override
	public Project saveProject(Project project) {
		checkSessionAvailable();
		return saveEntity(project);
	}

	@Override
	public void delete(Project project) {
		checkSessionAvailable();
		deleteEntity(project);
	}

	@Override
	public Project addMember(Project project, Employee employee) {
		checkSessionAvailable();

		project.addMember(employee);

		project = saveProject(project);

		return project;
	}

	@Override
	public Project removeMember(Project project, Employee employee) {
		checkSessionAvailable();

		project.removeMember(employee);

		project = saveProject(project);

		return project;
	}

	@Override
	public Project addModule(Project project, Module module) {
		checkSessionAvailable();

		project.addModule(module);

		project = saveProject(project);

		return project;

	}

	@Override
	public Project removeModule(Project project, Module module) {
		checkSessionAvailable();

		project.removeModule(module);

		project = saveProject(project);

		return project;
	}

}
