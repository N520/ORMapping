package swt6.orm.dao.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import swt6.orm.domain.LogbookEntry;
import swt6.orm.domain.Phase;
import swt6.orm.domain.PhaseDescriptor;
import swt6.util.DateUtil;

public class IssueTrackingDalLogbookTest extends IssueTrackingDalTest {

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	private void initialize() {
		empl1 = dal.saveEmployee(empl1);
		empl2 = dal.saveEmployee(empl2);
		m = dal.saveModule(m);
		lb1 = dal.persistLogbookEntryWithData(m, empl1, PhaseDescriptor.IMPLEMENTATION, lb1);
		lb2 = dal.persistLogbookEntryWithData(m, empl2, PhaseDescriptor.IMPLEMENTATION, lb2);
	}

	private void cleanUp() {
		dal.findAllLogbookEntries().forEach(lb -> dal.deleteLogbookEntry(lb));
		dal.findAllEmployees().forEach(e -> dal.deleteEmployee(e));
		dal.findAllModules().forEach(m -> dal.deleteModule(m));

	}

	@Test
	public void findAllLogbookEntriesTest() {
		initialize();

		try {
			assertEquals(2, dal.findAllLogbookEntries().size());
		} finally {
			cleanUp();
			assertEquals(0, dal.findAllLogbookEntries().size());
		}

	}

	@Test
	public void findLogbookEntryByIdTest() {
		initialize();

		try {
			LogbookEntry newLb = dal.findLogbookEntryById(lb1.getId());
			assertNotNull(newLb);
			assertEquals(newLb.getId(), lb1.getId());
			newLb = dal.findLogbookEntryById(591239L);
			assertNull(newLb);
		} finally {
			cleanUp();
		}

	}

	@Test
	public void saveLogbookEntryTest() {
		initialize();

		try {
			lb1.setPhase(new Phase(PhaseDescriptor.ANALYSIS));
			lb1 = dal.saveLogbookEntry(lb1);
			assertEquals(lb1.getPhase().getName(), (PhaseDescriptor.ANALYSIS));
			assertEquals(2, dal.findAllLogbookEntries().size());
		} finally {
			cleanUp();
		}

	}

	@Test
	public void deleteLogbookEntryTest() {
		initialize();
		try {
			dal.deleteLogbookEntry(lb1);
			assertEquals(1, dal.findAllLogbookEntries().size());
			lb1 = dal.findLogbookEntryById(lb1.getId());
			assertNull(lb1);
		} finally {
			cleanUp();
		}

	}

	@Test
	public void findAllLogbookentriesforEmployeeTest() {
		initialize();

		try {
			assertEquals(1, dal.findLogbookEntriesForEmployee(empl1).size());
			assertEquals(1, dal.findLogbookEntriesForEmployee(empl2).size());
			lb1 = dal.persistLogbookEntryWithData(m, empl1, PhaseDescriptor.IMPLEMENTATION,
					new LogbookEntry(new Date(), null));
			assertEquals(2, dal.findLogbookEntriesForEmployee(empl1).size());
		} finally {
			cleanUp();
		}

	}

	@Test
	public void persistLogbookEntryTest() {
		initialize();
		try {
			lb1 = dal.persistLogbookEntryWithData(m, empl1, PhaseDescriptor.IMPLEMENTATION,
					new LogbookEntry(new Date(), null));
			assertEquals(2, dal.findAllEmployees().size());
			assertEquals(3, dal.findAllLogbookEntries().size());
		} finally {
			cleanUp();
		}

	}

	@Test
	public void saveLogbookEntryPhaseTest() {
		initialize();

		try {
			lb1 = dal.saveLogbookEntryPhase(lb1, PhaseDescriptor.ANALYSIS);
			assertEquals(2, dal.findAllLogbookEntries().size());
			assertEquals(PhaseDescriptor.ANALYSIS, lb1.getPhase().getName());
		} finally {
			cleanUp();
		}

	}

	@Test
	public void saveWorktimeForEntryTest() {
		initialize();

		try {
			lb1 = dal.saveWorktimeForEntry(lb1, DateUtil.getDate(2017, 1, 1), new Date());
			assertEquals(2, dal.findAllLogbookEntries().size());
			assertEquals(DateUtil.getDate(2017, 1, 1), lb1.getStartTime());
		} finally {
			cleanUp();
		}
	}
}
