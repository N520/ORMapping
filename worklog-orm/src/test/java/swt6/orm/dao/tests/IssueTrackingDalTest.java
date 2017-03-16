package swt6.orm.dao.tests;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import swt6.orm.dao.EmployeeDao;
import swt6.orm.dao.EmployeeDaoImpl;
import swt6.orm.dao.IssueDao;
import swt6.orm.dao.IssueDaoImpl;
import swt6.orm.dao.IssueTrackingDal;
import swt6.orm.dao.LogBookDaoImpl;
import swt6.orm.dao.LogbookDao;
import swt6.orm.dao.ModuleDao;
import swt6.orm.dao.ModuleDaoImpl;
import swt6.orm.dao.ProjectDao;
import swt6.orm.dao.ProjectDaoImpl;
import swt6.orm.domain.Address;
import swt6.orm.domain.Employee;
import swt6.orm.domain.Issue;
import swt6.orm.domain.IssueType;
import swt6.orm.domain.LogbookEntry;
import swt6.orm.domain.Module;
import swt6.orm.domain.PermanentEmployee;
import swt6.orm.domain.PriorityType;
import swt6.orm.domain.Project;
import swt6.orm.domain.TemporaryEmployee;
import swt6.util.DateUtil;

public abstract class IssueTrackingDalTest {

	protected EmployeeDao employeeDao;
	protected IssueDao issueDao;
	protected LogbookDao logbookDao;
	protected ModuleDao moduleDao;
	protected ProjectDao projectDao;
	protected IssueTrackingDal dal;
	protected LogbookEntry lb1;
	protected LogbookEntry lb2;
	protected Employee empl1;

	protected Employee empl2;
	protected Project p;
	protected Module m;
	protected Issue issue;

	@Before
	public void setUp() throws Exception {
		employeeDao = new EmployeeDaoImpl();
		issueDao = new IssueDaoImpl();
		logbookDao = new LogBookDaoImpl();
		moduleDao = new ModuleDaoImpl();
		projectDao = new ProjectDaoImpl();
		dal = new IssueTrackingDal();

		lb1 = new LogbookEntry(DateUtil.getTime(8, 0), DateUtil.getTime(18, 0));
		lb2 = new LogbookEntry(DateUtil.getTime(8, 0), DateUtil.getTime(18, 0));
		empl1 = new PermanentEmployee("John", "Doe", new Date(), new Address("4300", "somewhre", "over the rainbow"),
				5000);

		empl2 = new TemporaryEmployee("Jane", "Doe", new Date(), new Address("4300", "somewhre", "over the rainbow"),
				"renting agency", 100.0, new Date(), DateUtil.getDate(2018, 01, 01));
		p = new Project("testproject1", empl1);
		m = new Module("module for p1");
		issue = new Issue(IssueType.NEW, PriorityType.NORMAL, 0, null, p);

	}

}
