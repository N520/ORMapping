package swt6.orm.client;

import java.util.Date;

import swt6.orm.dao.IssueTrackingDal;
import swt6.orm.domain.Address;
import swt6.orm.domain.Employee;
import swt6.orm.domain.Issue;
import swt6.orm.domain.IssueType;
import swt6.orm.domain.Module;
import swt6.orm.domain.PermanentEmployee;
import swt6.orm.domain.PriorityType;
import swt6.orm.domain.Project;
import swt6.orm.domain.TemporaryEmployee;
import swt6.util.DateUtil;

//TODO Unittests
public class Client {

	public static void main(String[] args) {

		try (IssueTrackingDal dal = new IssueTrackingDal()) {

			Employee empl1 = dal.saveEmployee(new PermanentEmployee("John", "Doe", new Date(),
					new Address("4300", "somewhre", "over the rainbow"), 5000));

			Employee empl2 = dal.saveEmployee(new TemporaryEmployee("Jane", "Doe", new Date(),
					new Address("4300", "somewhre", "over the rainbow"), "renting agency", 100.0, new Date(),
					DateUtil.getDate(2018, 01, 01)));

			System.out.println(empl1);

			empl1.setFirstName("jack");

			empl1 = dal.saveEmployee(empl1);

			System.out.println(empl1);

			// dal.deleteEmployee(empl1);

			dal.findAllEmployees().forEach(System.out::println);

			System.out.println("finding al permanent Employees");
			dal.findAllPermanentEmployees().forEach(System.out::println);

			System.out.println("finding al temporary Employees");
			dal.findAllTemporaryEmployees().forEach(System.out::println);

			System.out.println("------------ PROJECT STUFF ---------------");

			Project p = dal.saveProject(new Project("testproject1", empl1));

			p = dal.addEmployeeToProject(empl1, p);
			p = dal.addEmployeeToProject(empl2, p);
			//

			p = dal.saveProject(p);

			Module m = dal.saveModule(new Module("module for p1"));

			p = dal.addModuleToProject(m, p);
			m = dal.findModuleById(m.getId());
			
			dal.findAllModules().stream().forEach(System.err::println);
			;

			p = dal.removeModuleFromProject(m, p);
			dal.findAllModules().stream().forEach(System.err::println);
			;

			p = dal.saveProject(p);

			System.out.println("------------ REMOVING EMPLOYEE ---------------");

			p = dal.removeEmployeeFromProject(empl1, p);

			p = dal.findAllProjects().iterator().next();
			dal.findAllProjects().forEach(System.out::println);

			System.out.println("------------ ISSUE STUFF---------------");
			Issue issue = new Issue(IssueType.NEW, PriorityType.NORMAL, 0, null, p);
			issue = dal.saveIssue(issue);

			System.out.println(issue);

			dal.assignEmployeeToIssue(empl2, issue);

			System.out.println(issue);

			System.out.println("------------ ISSUE FILTER---------------");
			dal.findAllIssuesWithState(IssueType.CLOSED).forEach(System.out::println);
			;

			System.out.println("------------ ISSUE DELETION ---------------");

			// dal.findAllIssues().forEach(i -> dal.deleteIssue(i));

			dal.findAllIssuesWithState(IssueType.NEW).forEach(System.out::println);

		}

	}
}
