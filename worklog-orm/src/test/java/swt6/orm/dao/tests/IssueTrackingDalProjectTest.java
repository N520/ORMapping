package swt6.orm.dao.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import swt6.orm.domain.Issue;
import swt6.orm.domain.IssueType;
import swt6.orm.domain.PriorityType;
import swt6.orm.domain.Project;

public class IssueTrackingDalProjectTest extends IssueTrackingDalTest {

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	private void initialize() {
		empl1 = dal.saveEmployee(empl1);
		empl2 = dal.saveEmployee(empl2);
		p.setProjectLeader(empl2);
		p = dal.saveProject(p);
		issue.setProject(p);
		issue = dal.saveIssue(issue);
		m = dal.saveModule(m);
	}

	private void cleanUp() {
		dal.findAllIssues().forEach(i -> dal.deleteIssue(i));
		dal.findAllProjects().forEach(p -> dal.deleteProject(p));
		dal.findAllEmployees().forEach(d -> dal.deleteEmployee(d));
		dal.findAllModules().forEach(m -> dal.deleteModule(m));
	}

	@Test
	public void findAllProjectsTest() {
		initialize();

		try {
			assertEquals(1, dal.findAllProjects().size());
			p = dal.saveProject(new Project("some other project", empl2));
			assertEquals(2, dal.findAllProjects().size());
		} finally {
			cleanUp();
			assertEquals(0, dal.findAllProjects().size());
		}
	}

	@Test
	public void findProjectByIdTest() {
		initialize();
		try {

			assertNotNull(dal.findProjectById(p.getId()));
			assertNull(dal.findProjectById(4123L));

		} finally {
			cleanUp();
		}
	}

	@Test
	public void saveProjectTest() {
		initialize();

		try {
			p = dal.saveProject(new Project("some other project", empl2));
			assertEquals(2, dal.findAllProjects().size());
			Project p2 = dal.findProjectById(p.getId());
			assertEquals(p2.getName(), p.getName());
		} finally {

			cleanUp();
		}
	}

	@Test
	public void deleteProjectTest() {
		initialize();
		try {
			dal.deleteProject(p);
			assertEquals(0, dal.findAllProjects().size());
		} finally {
			cleanUp();

		}
	}

	@Test
	public void assignEmployeeToProjectTest() {
		initialize();

		try {
			p = dal.addEmployeeToProject(empl1, p);
			assertEquals(1, p.getMembers().size());
			assertEquals(2, dal.findAllEmployees().size());
		} finally {
			cleanUp();
		}
	}

	@Test
	public void removeEmployeeFromProjectTest() {
		initialize();
		try {
			p = dal.addEmployeeToProject(empl1, p);
			assertEquals(1, p.getMembers().size());
			assertEquals(2, dal.findAllEmployees().size());

			empl1 = dal.findEmployeeById(empl1.getId());
			p = dal.removeEmployeeFromProject(empl1, p);
			assertEquals(0, p.getMembers().size());
			assertEquals(2, dal.findAllEmployees().size());
		} finally {
			cleanUp();
		}
		cleanUp();
	}

	@Test
	public void saveProjectLeadTest() {
		initialize();
		try {
			p = dal.saveProjectLead(p, empl2);
			assertEquals(empl2.getId(), p.getProjectLeader().getId());
			p = dal.saveProjectLead(p, empl1);
			assertEquals(empl1.getId(), p.getProjectLeader().getId());

			assertEquals(2, dal.findAllEmployees().size());
		} finally {

			cleanUp();
		}
	}

	@Test
	public void addModuleToProjectTest() {
		initialize();
		try {
			p = dal.addModuleToProject(m, p);
			assertEquals(1, p.getModules().size());
			assertEquals(1, dal.findAllModules().size());
		} finally {
			cleanUp();
		}
	}

	@Test
	public void removeModuleFromProjectTest() {
		initialize();

		try {
			p = dal.addModuleToProject(m, p);
			assertEquals(1, p.getModules().size());
			assertEquals(1, dal.findAllModules().size());

			m = dal.findModuleById(m.getId());
			p = dal.removeModuleFromProject(m, p);
			assertEquals(0, p.getModules().size());
			assertEquals(1, dal.findAllModules().size());
		} finally {
			cleanUp();
		}
	}

	@Test
	public void saveProjectNameTest() {
		initialize();
		try {
			p = dal.saveProjectName(p, "super awesome project");
			assertEquals("super awesome project", p.getName());
		} finally {
			cleanUp();
		}
	}

	@Test
	public void findAllProjectIssuesWithStateTest() {
		initialize();
		try {
			Issue issue2 = dal.saveIssue(new Issue(IssueType.OPEN, PriorityType.LOW, 0, empl1, p));
			assertEquals(1, dal.findAllProjectIssuesWithState(p, IssueType.OPEN).size());
			assertEquals(1, dal.findAllProjectIssuesWithState(p, IssueType.NEW).size());
			assertEquals(2, dal.findAllProjectIssues(p).size());
		} finally {

			cleanUp();
		}
	}

}
