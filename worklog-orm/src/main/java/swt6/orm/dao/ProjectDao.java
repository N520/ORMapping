package swt6.orm.dao;

import java.util.Collection;

import swt6.orm.domain.Employee;
import swt6.orm.domain.Module;
import swt6.orm.domain.Project;

public interface ProjectDao extends Dao {
	Project findById(Long id);

	Collection<Project> findAll();

	Project saveProject(Project project);

	void delete(Project project);

	Project addMember(Project project, Employee employee);

	Project removeMember(Project project, Employee employee);

	Project addModule(Project project, Module module);

	Project removeModule(Project project, Module module);
}
