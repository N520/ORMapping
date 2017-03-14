package swt6.orm.client;

import java.util.Date;

import swt6.orm.dao.IssueTrackingDal;
import swt6.orm.domain.Address;
import swt6.orm.domain.Employee;
import swt6.orm.domain.PermanentEmployee;


//TODO Unittests
public class Client {

	public static void main(String[] args) {

		IssueTrackingDal dal = new IssueTrackingDal();

		Employee empl1 = dal.saveEmployee(new PermanentEmployee("John", "Doe", new Date(),
				new Address("4300", "somewhre", "over the rainbow"), 5000));

		System.out.println(empl1);

		empl1.setFirstName("jack");

		empl1 = dal.saveEmployee(empl1);

		System.out.println(empl1);

		dal.deleteEmployee(empl1);

		dal.findAllEmployees().forEach(System.out::println);
		
		dal.close();

	}
}
