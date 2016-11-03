package tel_ran.employees.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import tel_ran.employees.dao.EmployeesOrm;
import tel_ran.employees.entities.Employee;

import java.util.logging.Level;
import java.util.logging.Logger;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EmployeesOrmTest {
	static AbstractApplicationContext ctx ;
	static EmployeesOrm employeesOrm;
	static Logger loggerHibernate = Logger.getLogger("org.hibernate");
	 

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		loggerHibernate.setLevel(Level.SEVERE);
		
		ctx = new FileSystemXmlApplicationContext("beans.xml");
		employeesOrm = ctx.getBean(EmployeesOrm.class);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		ctx.close();
	}
	private Employee gm = new Employee(1, "GM", 100000);
	private Employee manager1 = new Employee(2, "manager1", 50000);
	private Employee manager2 = new Employee(22, "manager2", 50000);
	private Employee employee3 = new Employee(3, "employee3", 25000);
	private Employee employee4 = new Employee(4, "employee4", 25000);
	private Employee employee5 = new Employee(5, "employee5", 25000);
	private Employee employee6 = new Employee(6, "employee6", 250000);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAdd() {
		employeesOrm.addGeneralManager(gm);
		employeesOrm.addEmployee(manager1, 1);
		employeesOrm.addEmployee(manager2 , 1);
		employeesOrm.addEmployee(employee3 , 2);
		employeesOrm.addEmployee(employee4 , 2);
		employeesOrm.addEmployee(employee5 , 22);
		employeesOrm.addEmployee(employee6 , 22);
	}
	@Test
	public void testGetGeneralManager(){
		Employee actual = employeesOrm.getGeneralManager();
		assertEquals(gm, actual);
		System.out.println("GM: " + actual);
	}
	@Test
	public void testGetSubordinates(){
		ArrayList<Employee> actual =  (ArrayList<Employee>) employeesOrm.getSubordinates(2);
		Employee[] arrActual = new Employee[actual.size()];
		arrActual = actual.toArray(arrActual);
		Arrays.sort(arrActual, (a,b)->a.getId()-b.getId());
		Employee[] arrExp = {employee3,employee4};
		Arrays.sort(arrExp, (a,b)->a.getId()-b.getId());
		assertArrayEquals(arrExp, arrActual);
		System.out.println("Subordinate: " + actual);
	}
	@Test
	public void testGetEmployees(){
		ArrayList<Employee> actual =  (ArrayList<Employee>) employeesOrm.getEmployees();
		Employee[] arrActual = new Employee[actual.size()];
		arrActual = actual.toArray(arrActual);
		Arrays.sort(arrActual, (a,b)->a.getId()-b.getId());
		Employee[] arrExp = {employee3,employee4,employee5,employee6};
		Arrays.sort(arrExp, (a,b)->a.getId()-b.getId());
		assertArrayEquals(arrExp, arrActual);
		System.out.println("Employees: " + actual);
	}
	@Test
	public void testLineManagers(){
		ArrayList<Employee> actual =  (ArrayList<Employee>) employeesOrm.getLineManagers();
		Employee[] arrActual = new Employee[actual.size()];
		arrActual = actual.toArray(arrActual);
		Arrays.sort(arrActual, (a,b)->a.getId()-b.getId());
		Employee[] arrExp = {manager1,manager2};
		Arrays.sort(arrExp, (a,b)->a.getId()-b.getId());
		System.out.println("LineManagers: " + actual);
		assertArrayEquals(arrExp, arrActual);
	}
	@Test
	public void testGetEmployeesWithSalaryGreaterThanManager(){
		ArrayList<Employee> actual =  (ArrayList<Employee>) employeesOrm.getEmployeesWithSalaryGreaterThanManager();
		Employee[] arrActual = new Employee[actual.size()];
		arrActual = actual.toArray(arrActual);
		Arrays.sort(arrActual, (a,b)->a.getId()-b.getId());
		Employee[] arrExp = {employee6};
		Arrays.sort(arrExp, (a,b)->a.getId()-b.getId());
		System.out.println("EmployeesWithSalaryGreaterThanManager: " + actual);
		assertArrayEquals(arrExp, arrActual);
	}
	@Test
	public void testRemoveGM(){
		employeesOrm.removeGeneralManager(1);
		assertEquals(Collections.EMPTY_LIST,employeesOrm.getEmployees());
		assertEquals(Collections.EMPTY_LIST,employeesOrm.getLineManagers());
	}
}
