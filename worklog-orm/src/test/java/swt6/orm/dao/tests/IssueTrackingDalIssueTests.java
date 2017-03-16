package swt6.orm.dao.tests;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import junit.framework.AssertionFailedError;
import swt6.orm.domain.Address;
import swt6.orm.domain.Employee;
import swt6.orm.domain.Issue;
import swt6.orm.domain.IssueType;
import swt6.orm.domain.PriorityType;
import swt6.orm.domain.Project;
import swt6.orm.domain.TemporaryEmployee;
import swt6.util.DateUtil;

public class IssueTrackingDalIssueTests extends IssueTrackingDalTest {

	@Test
	public void deleteIssue() {
		initializeIssue();
		assertNotNull(issue);
		assertEquals(dal.findAllIssues().size(), 1);
		cleanUp();
		assertEquals(dal.findAllIssues().size(), 0);

		//
		// dal.assignEmployeeToIssue(empl2, issue);
		//
		// System.out.println(issue);
		//
		// System.out.println("------------ ISSUE FILTER---------------");
		// dal.findAllIssuesWithState(IssueType.CLOSED).forEach(System.out::println);
		// ;
		//
		// System.out.println("------------ ISSUE DELETION ---------------");
		//
		//
		// dal.findAllIssuesWithState(IssueType.NEW).forEach(System.out::println);
	}

	private void cleanUp() {

		dal.findAllIssues().forEach(i -> dal.deleteIssue(i));
		dal.findAllProjects().forEach(p -> dal.deleteProject(p));
		dal.findAllEmployees().forEach(d -> dal.deleteEmployee(d));
	}

	private void initializeIssue() {
		empl1 = dal.saveEmployee(empl1);
		empl2 = dal.saveEmployee(empl2);
		p.setProjectLeader(empl2);
		p = dal.saveProject(p);
		issue.setProject(p);
		issue = dal.saveIssue(issue);
	}

	@Test
	public void saveIssueTest() {
		initializeIssue();
		assertNotNull(issue.getId());
		Issue newIssue = dal.findIssueById(issue.getId());
		assertEquals(issue.getId(), newIssue.getId());

		// rollback
		cleanUp();
	}

	@Test
	public void findIssueByIdTest() {
		initializeIssue();

		assertNotNull(issue.getId());
		Issue newIssue = dal.findIssueById(issue.getId());
		assertNotNull(newIssue.getId());
		assertEquals(issue.getId(), newIssue.getId());

		// rollback
		cleanUp();
	}

	@Test
	public void findAllIssuesTest() {
		initializeIssue();

		assertNotNull(issue.getId());
		assertEquals(dal.findAllIssues().size(), 1);
		Issue newIssue = dal.saveIssue(new Issue(IssueType.NEW, PriorityType.HIGH, 0, empl1, p));
		assertEquals(dal.findAllIssues().size(), 2);

		// rollback
		cleanUp();
	}

	@Test
	public void assignEmployeeToIssueTest() {
		initializeIssue();

		assertNotNull(issue.getId());
		assertEquals(dal.findAllIssues().size(), 1);
		issue = dal.assignEmployeeToIssue(empl1, issue);
		assertNotNull(issue.getEmployee());
		assertEquals(dal.findAllIssues().size(), 1);
		assertEquals(dal.findAllEmployees().size(), 2);
		issue = dal.assignEmployeeToIssue(null, issue);
		assertNull(issue.getEmployee());
		assertEquals(dal.findAllIssues().size(), 1);
		assertEquals(dal.findAllEmployees().size(), 2);

		cleanUp();
		assertEquals(dal.findAllIssues().size(), 0);
		assertEquals(dal.findAllEmployees().size(), 0);
		assertEquals(dal.findAllProjects().size(), 0);
	}

	@Test
	public void findIssuesWithStateTest() {
		initializeIssue();

		Issue issue2 = dal.saveIssue(new Issue(IssueType.CLOSED, PriorityType.LOW, 0, empl1, p));
		assertEquals(2, dal.findAllEmployees().size());

		assertEquals(1, dal.findAllIssuesWithState(IssueType.NEW).size());
		assertEquals(1, dal.findAllIssuesWithState(IssueType.CLOSED).size());
		assertEquals(0, dal.findAllIssuesWithState(IssueType.REJECTED).size());
		assertEquals(2, dal.findAllIssues().size());

		cleanUp();

	}

	@Test
	public void findAllProjectIssueWithState() {
		initializeIssue();
		Issue issue2 = dal.saveIssue(new Issue(IssueType.CLOSED, PriorityType.LOW, 0, empl1, p));

		assertEquals(1, dal.findAllProjectIssueWithState(p, IssueType.NEW).size());
		assertEquals(1, dal.findAllProjectIssueWithState(p, IssueType.CLOSED).size());
		assertEquals(2, p.getIssues().size());

		cleanUp();
	}

	@Test
	public void findAllEmployeeIssuesWithStateTest() {
		initializeIssue();
		Issue issue2 = dal.saveIssue(new Issue(IssueType.CLOSED, PriorityType.LOW, 0, empl1, p));
		issue = dal.assignEmployeeToIssue(empl1, issue);
		issue2 = dal.assignEmployeeToIssue(empl1, issue2);

		assertEquals(1, dal.findAllEmployeeIssuesWithState(empl1, IssueType.NEW).size());
		assertEquals(1, dal.findAllEmployeeIssuesWithState(empl1, IssueType.CLOSED).size());

		cleanUp();
	}

	@Test
	public void saveIssueEffortTest() {
		initializeIssue();

		assertEquals(issue.getEffort(), 0);
		issue = dal.saveIssueEffort(issue, 5);
		Issue newIssue = dal.findIssueById(issue.getId());
		assertEquals(issue.getId(), newIssue.getId());
		assertEquals(newIssue.getEffort(), 5);
		assertEquals(issue.getEffort(), 5);

		cleanUp();
	}

	@Test
	public void saveIssueProgressTest() {
		initializeIssue();

		assertEquals(issue.getProgress(), 0);
		issue = dal.saveIssueProgress(issue, 5);
		Issue newIssue = dal.findIssueById(issue.getId());
		assertEquals(issue.getId(), newIssue.getId());
		assertEquals(newIssue.getProgress(), 5);
		assertEquals(issue.getProgress(), 5);

		cleanUp();
	}
}
