package swt6.orm.dao;

import java.util.Collection;

import swt6.orm.domain.Employee;
import swt6.orm.domain.Project;

public class ProjectDaoImpl extends AbstractDao implements ProjectDao {

	@Override
	public Project findById(Long id) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub

	}

	@Override
	public Project addMember(Project project, Employee employee) {
		checkSessionAvailable();

		project.addMember(employee);
		
		project = saveProject(project);
		
		return project;
	}

}
