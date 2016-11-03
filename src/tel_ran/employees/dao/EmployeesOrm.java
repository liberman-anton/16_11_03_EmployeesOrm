package tel_ran.employees.dao;

import javax.persistence.*;

import org.springframework.transaction.annotation.Transactional;

import tel_ran.employees.entities.Employee;

public class EmployeesOrm {
	
	@PersistenceContext(unitName = "springHibernate")//, 
//			type=PersistenceContextType.TRANSACTION)
	EntityManager em;
	
	@Transactional
	public boolean addGeneralManager(Employee employee){
		if(employee == null || 
				em.find(Employee.class, employee.getId()) != null)
			return false;
		em.persist(employee);
		return true;
	}
	@Transactional
	public boolean addEmployee(Employee employee,int managerId){
		if(employee == null || 
				em.find(Employee.class, employee.getId()) != null)
			return false;
		Employee manager = em.find(Employee.class,managerId);
		if(manager == null)
			return false;
		employee.setManager(manager);
		em.persist(employee);
		return true;
	}
	public Employee getGeneralManager(){
		Query query = em.createQuery("select e from Employee e where e.manager is null"); 
		return (Employee) query.getResultList().get(0);
	}
	public Iterable<Employee> getSubordinates(int id){
		Query query = em.createQuery("select s from Employee e join e.subordinates s where s.manager=" + id );
		return query.getResultList();
		
	}
	public Iterable<Employee> getLineManagers(){
		Query query = em.createQuery("select e from Employee e where e.subordinates is not empty and e.manager is not null" );
		return query.getResultList();
	}
	public Iterable<Employee> getEmployees(){
		Query query = em.createQuery("select e from Employee e where e.subordinates is empty" );
		return query.getResultList();
	}
	public Iterable<Employee> getEmployeesWithSalaryGreaterThanManager(){
		Query queryM = em.createQuery("select e.salary from Employee e where e.subordinates is not empty and e.manager is not null");
		int maxSalaryofManager = (int) queryM.getResultList().get(0);
		Query query = em.createQuery("select e from Employee e where e.subordinates is empty and e.salary > " + maxSalaryofManager);
//				"max(select e from Employee e where e.subordinates is not empty and e.manager is not null)");
		return query.getResultList();
	}
	@Transactional
	public void removeGeneralManager(int id){
		Employee gm = em.find(Employee.class, id);
		if(gm == null) return;
		em.remove(gm);
	}
}
