package swt6.orm.dao;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import swt6.orm.dao.tests.IssueTrackingDalEmployeeTest;
import swt6.orm.dao.tests.IssueTrackingDalIssueTests;
import swt6.orm.dao.tests.IssueTrackingDalLogbookTest;
import swt6.orm.dao.tests.IssueTrackingDalModuleTest;
import swt6.orm.dao.tests.IssueTrackingDalProjectTest;

@RunWith(Suite.class)

@Suite.SuiteClasses({ IssueTrackingDalEmployeeTest.class, IssueTrackingDalIssueTests.class,
		IssueTrackingDalLogbookTest.class, IssueTrackingDalModuleTest.class, IssueTrackingDalProjectTest.class, })
public class AllTests {
}
