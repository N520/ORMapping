package swt6.orm.dao;

import java.util.Collection;

import org.hibernate.query.Query;

import swt6.orm.domain.Employee;
import swt6.orm.domain.Issue;
import swt6.orm.domain.IssueType;

public interface IssueDao extends Dao {

	Collection<Issue> findAll();

	Issue findById(Long id);

	Issue saveIssue(Issue issue);

	Issue updateEmployee(Issue issue, Employee employee);

	void delete(Issue issue);

	Collection<Issue> findByState(IssueType state);

	Collection<Issue> query(Query<Issue> query);

}
