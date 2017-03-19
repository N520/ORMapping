package swt6.orm.client;

import java.util.Date;

import swt6.orm.dao.IssueTrackingDal;
import swt6.orm.domain.Address;
import swt6.orm.domain.Employee;
import swt6.orm.domain.Issue;
import swt6.orm.domain.IssueType;
import swt6.orm.domain.LogbookEntry;
import swt6.orm.domain.Module;
import swt6.orm.domain.PermanentEmployee;
import swt6.orm.domain.PhaseDescriptor;
import swt6.orm.domain.PriorityType;
import swt6.orm.domain.Project;
import swt6.orm.domain.TemporaryEmployee;
import swt6.util.DateUtil;
import swt6.util.HibernateUtil;

public class Client {

	public static void main(String[] args) {

		System.out.println("###### IssueTracking Console Client ######");

		try (IssueTrackingDal dal = new IssueTrackingDal()) {
			HibernateUtil.setConfigFile("hibernate.cfg-mysql.xml");

			System.out.println("------------ Creating Employees ---------------");
			System.out.println("+++ start Transaction +++");
			Employee empl1 = dal.saveEmployee(new PermanentEmployee("John", "Doe", new Date(),
					new Address("4300", "somewhre", "over the rainbow"), 5000));
			System.out.println("+++ end Transaction +++");

			System.out.println("+++ start Transaction +++");
			Employee empl2 = dal.saveEmployee(new TemporaryEmployee("Jane", "Doe", new Date(),
					new Address("4300", "somewhre", "over the rainbow"), "renting agency", 100.0, new Date(),
					DateUtil.getDate(2018, 01, 01)));
			System.out.println("+++ end Transaction +++");

			System.out.println("+++ start Transaction +++");
			Employee empl3 = dal.saveEmployee(new TemporaryEmployee("Samantha", "Fancypants", new Date(),
					new Address("4300", "somewhre", "over the rainbow"), "renting agency", 100.0, new Date(),
					DateUtil.getDate(2018, 01, 01)));
			System.out.println("+++ end Transaction +++");

			System.out.println("------------ Adjusting Employee Name ---------------");
			empl1.setFirstName("jack");
			System.out.println("+++ start Transaction +++");
			empl1 = dal.saveEmployee(empl1);
			System.out.println("+++ end Transaction +++");

			System.out.println("------------ Deleting Employee ---------------");
			System.out.println("+++ start Transaction +++");
			dal.deleteEmployee(empl3);
			System.out.println("+++ end Transaction +++");

			System.out.println("------------ Printing all Employees ---------------");
			System.out.println("+++ start Transaction +++");
			dal.findAllEmployees().forEach(System.out::println);
			System.out.println("+++ end Transaction +++");

			System.out.println("------------ Printing Permanent Employees ---------------");
			System.out.println("+++ start Transaction +++");

			dal.findAllPermanentEmployees().forEach(System.out::println);
			System.out.println("+++ end Transaction +++");

			System.out.println("------------ Printing Temporary Employees ---------------");
			System.out.println("+++ start Transaction +++");

			dal.findAllTemporaryEmployees().forEach(System.out::println);
			System.out.println("+++ end Transaction +++");

			System.out.println("------------ CREATING Project ---------------");
			System.out.println("+++ start Transaction +++");
			Project p = dal.saveProject(new Project("testproject1", empl1));
			System.out.println("+++ end Transaction +++");
			System.out.println("+++ start Transaction +++");

			System.out.println("------------ Adding Employees Project ---------------");

			p = dal.addEmployeeToProject(empl1, p);
			System.out.println("+++ end Transaction +++");
			System.out.println("+++ start Transaction +++");

			p = dal.addEmployeeToProject(empl2, p);
			System.out.println("+++ end Transaction +++");

			System.out.println("------------ Saving Project ---------------");
			System.out.println("+++ startTransaction +++");
			p = dal.saveProject(p);
			System.out.println("+++ end Transaction +++");

			System.out.println("------------ Creating Module---------------");
			System.out.println("+++ startTransaction +++");
			Module m = dal.saveModule(new Module("module for p1"));
			System.out.println("+++ end Transaction +++");

			System.out.println("------------ Adding Module ---------------");
			System.out.println("+++ startTransaction +++");
			p = dal.addModuleToProject(m, p);
			System.out.println("+++ end Transaction +++");

			System.out.println("------------ Get up to date project ---------------");
			System.out.println("+++ start Transaction +++");
			p = dal.findProjectById(p.getId());
			System.out.println("+++ end Transaction +++");

			System.out.println("------------ Create ISSUE ---------------");
			System.out.println("+++ start Transaction +++");
			Issue issue = new Issue(IssueType.NEW, PriorityType.NORMAL, 0, null, p);
			issue = dal.saveIssue(issue);
			System.out.println("+++ end Transaction +++");

			System.out.println("------------ Assign Employee to ISSUE ---------------");
			System.out.println("+++ start Transaction +++");
			dal.assignEmployeeToIssue(empl2, issue);
			System.out.println("+++ end Transaction +++");

			System.out.println("------------ ISSUE FILTER---------------");
			System.out.println("+++ start Transaction +++");

			dal.findAllIssuesWithState(IssueType.CLOSED).forEach(System.out::println);
			System.out.println("+++ end Transaction +++");

			System.out.println("------------ LOGBOOK Creation ---------------");
			LogbookEntry lb1 = new LogbookEntry(DateUtil.getTime(8, 0), DateUtil.getTime(18, 0));
			LogbookEntry lb2 = new LogbookEntry(DateUtil.getTime(8, 0), DateUtil.getTime(18, 0));
			System.out.println("+++ start Transaction +++");

			lb1 = dal.persistLogbookEntryWithData(m, empl1, PhaseDescriptor.ANALYSIS, lb1);
			System.out.println("+++ end Transaction +++");
			System.out.println("+++ start Transaction +++");
			lb2 = dal.persistLogbookEntryWithData(m, empl1, PhaseDescriptor.IMPLEMENTATION, lb2);
			System.out.println("+++ end Transaction +++");

			System.out.println("------------ ALL ISSUES OF PROJECT WITH STATE ---------------");
			System.out.println("+++ start Transaction +++");

			dal.findAllProjectIssuesWithState(p, IssueType.CLOSED).forEach(System.out::println);
			System.out.println("+++ end Transaction +++");

			System.out.println("------------ ALL EMPLOYEES WITH NAME ---------------");
			System.out.println("+++ start Transaction +++");
			dal.findEmployeesByName("jack").forEach(System.out::println);
			System.out.println("+++ end Transaction +++");

			System.out.println("------------ LOGBOOKENTRY DELETION ---------------");
			System.out.println("+++ start Transaction +++");
			dal.deleteLogbookEntry(lb2);
			System.out.println("+++ end Transaction +++");

		}

	}
}
