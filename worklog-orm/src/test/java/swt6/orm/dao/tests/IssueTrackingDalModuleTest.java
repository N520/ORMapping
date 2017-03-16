package swt6.orm.dao.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import swt6.orm.domain.Module;

public class IssueTrackingDalModuleTest extends IssueTrackingDalTest {

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	private void initialize() {
		empl1 = dal.saveEmployee(empl1);
		empl2 = dal.saveEmployee(empl2);
		p.setProjectLeader(empl2);
		p = dal.saveProject(p);
		m = dal.saveModule(m);

	}

	private void cleanUp() {
		dal.findAllProjects().forEach(e -> dal.deleteProject(e));
		dal.findAllEmployees().forEach(e -> dal.deleteEmployee(e));
		dal.findAllModules().forEach(e -> dal.deleteModule(e));

	}

	@Test
	public void findAllModulesTest() {
		initialize();

		try {
			assertEquals(1, dal.findAllModules().size());
			Module m1 = new Module("someName");
			p = dal.addModuleToProject(m1, p);
			assertEquals(2, dal.findAllModules().size());
			// since we add it to the project there is no "easy" way to get the
			// persistent object
			assertNull(m1.getId());
		} finally {
			cleanUp();
		}

	}

	@Test
	public void findModuleByIdTest() {
		initialize();

		try {
			assertNotNull(dal.findModuleById(m.getId()));
			assertNull(dal.findModuleById(41243L));
		} finally {
			cleanUp();
		}

	}

	@Test
	public void saveModuleTest() {
		initialize();

		try {
			assertEquals(1, dal.findAllModules().size());
			Module m1 = dal.saveModule(new Module("someName"));
			assertEquals(2, dal.findAllModules().size());
			assertNotNull(m1.getId());
		} finally {
			cleanUp();
		}

	}

	@Test
	public void deleteModuleTest() {
		initialize();

		try {
			dal.deleteModule(m);
			assertEquals(0, dal.findAllModules().size());
			assertNull(dal.findEmployeeById(m.getId()));
		} finally {
			cleanUp();
		}
	}

}
