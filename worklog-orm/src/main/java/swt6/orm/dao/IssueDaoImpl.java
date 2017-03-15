package swt6.orm.dao;

import java.util.Collection;
import java.util.List;

import org.hibernate.query.Query;

import swt6.orm.domain.Employee;
import swt6.orm.domain.Issue;
import swt6.orm.domain.IssueType;

public class IssueDaoImpl extends AbstractDao implements IssueDao {

	@Override
	public Collection<Issue> findAll() {
		checkSessionAvailable();

		List<Issue> l = session.createQuery("from Issue ", Issue.class).getResultList();

		return l;

	}

	@Override
	public Issue findById(Long id) {
		return session.get(Issue.class, id);

	}

	@Override
	public Issue saveIssue(Issue issue) {
		checkSessionAvailable();
		Issue newIssue = saveEntity(issue);
		return newIssue;
	}

	@Override
	public Issue updateEmployee(Issue issue, Employee employee) {
		issue.attachEmployee(employee);
		return saveIssue(issue);
	}

	@Override
	public void delete(Issue issue) {
		checkSessionAvailable();
		deleteEntity(issue);
	}

	@Override
	public Collection<Issue> query(Query<Issue> query) {
		checkSessionAvailable();
		return query.getResultList();
	}

}
