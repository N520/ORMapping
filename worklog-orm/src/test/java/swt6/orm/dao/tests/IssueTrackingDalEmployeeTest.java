package swt6.orm.dao.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import swt6.orm.domain.Address;
import swt6.orm.domain.Employee;
import swt6.orm.domain.PermanentEmployee;
import swt6.orm.domain.TemporaryEmployee;

public class IssueTrackingDalEmployeeTest extends IssueTrackingDalTest {

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	private void initialize() {
		empl1 = dal.saveEmployee(empl1);
		empl2 = dal.saveEmployee(empl2);

	}

	private void cleanUp() {
		dal.findAllEmployees().forEach(e -> dal.deleteEmployee(e));
	}

	@Test
	public void findAllEmployeesTest() {
		initialize();

		try {
			assertEquals(2, dal.findAllEmployees().size());
		} finally {
			cleanUp();
			assertEquals(0, dal.findAllEmployees().size());
		}

	}

	@Test
	public void findEmployeesByIdTest() {
		initialize();

		try {
			Employee newEmpl = dal.findEmployeeById(empl1.getId());
			assertNotNull(newEmpl);
			assertEquals(newEmpl.getId(), empl1.getId());
		} finally {
			cleanUp();
		}

	}

	@Test
	public void saveEmployeeTest() {
		try {
			assertNull(empl1.getId());
			empl1 = dal.saveEmployee(empl1);
			assertNotNull(empl1.getId());
			Employee newEmpl = dal.findEmployeeById(empl1.getId());
			assertNotNull(newEmpl);
			assertEquals(empl1.getId(), newEmpl.getId());
		} finally {
			cleanUp();
		}

	}

	@Test
	public void deleteEmployeeTest() {
		initialize();
		try {
			dal.deleteEmployee(empl1);
			assertEquals(dal.findEmployeeById(empl1.getId()), null);
		} finally {
			cleanUp();
		}
	}

	@Test
	public void findAllPermanentEmployeesTest() {
		initialize();
		try {
			assertEquals(1, dal.findAllPermanentEmployees().size());
			Employee newEmpl = dal.saveEmployee(
					new PermanentEmployee("another", "empl", new Date(), new Address("4400", "dsad", "asd"), 7));
			assertEquals(2, dal.findAllPermanentEmployees().size());
		} finally {
			cleanUp();
		}

	}

	@Test
	public void findAllTemporaryEmployeesTest() {
		initialize();
		
		try {
			assertEquals(1, dal.findAllTemporaryEmployees().size());
			Employee newEmpl = dal.saveEmployee(new TemporaryEmployee("das", "ist", new Date(),
					new Address("der", "schuh", "des"), "Manitu", 5, new Date(), null));
			assertEquals(2, dal.findAllTemporaryEmployees().size());
		} finally {
			cleanUp();
		}
		
	}

	@Test
	public void findEmployeeByNameTest() {
		initialize();

		try {
			assertEquals(1, dal.findEmployeesByName("John").size());
			assertEquals(1, dal.findEmployeesByName("Jane").size());
			assertEquals(2, dal.findEmployeesByName("Doe").size());
			assertEquals(0, dal.findEmployeesByName("wddasd").size());
		} finally {
			cleanUp();
		}
		
	}

}
