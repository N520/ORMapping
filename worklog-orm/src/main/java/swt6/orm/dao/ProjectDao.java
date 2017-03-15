package swt6.orm.dao;

import java.util.Collection;

import swt6.orm.domain.Employee;
import swt6.orm.domain.Project;

public interface ProjectDao extends Dao {
	Project findById(Long id);

	Collection<Project> findAll();

	Project saveProject(Project project);

	void delete(Project project);

	Project addMember(Project project, Employee employee);
}
