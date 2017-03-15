package swt6.orm.dao;

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

public class IssueTrackingDalIssueTests {

	private IssueTrackingDal dal = new IssueTrackingDal();
	
	

	@Test
	public void deleteIssue() {

		System.out.println("------------ ISSUE STUFF---------------");
		Employee empl2 = dal.saveEmployee(
				new TemporaryEmployee("Jane", "Doe", new Date(), new Address("4300", "somewhre", "over the rainbow"),
						"renting agency", 100.0, new Date(), DateUtil.getDate(2018, 01, 01)));
		Project p = dal.saveProject(new Project("testproject1", empl2));
		Issue issue = new Issue(IssueType.NEW, PriorityType.NORMAL, 0, null, p);
		issue = dal.saveIssue(issue);


		assertNotNull(issue);
		assertEquals(dal.findAllIssues().size(), 1);
		dal.findAllIssues().forEach(i -> dal.deleteIssue(i));
		assertEquals(dal.findAllIssues().size(), 0);
		
//
//		dal.assignEmployeeToIssue(empl2, issue);
//
//		System.out.println(issue);
//
//		System.out.println("------------ ISSUE FILTER---------------");
//		dal.findAllIssuesWithState(IssueType.CLOSED).forEach(System.out::println);
//		;
//
//		System.out.println("------------ ISSUE DELETION ---------------");
//
//
//		dal.findAllIssuesWithState(IssueType.NEW).forEach(System.out::println);
	}

}
