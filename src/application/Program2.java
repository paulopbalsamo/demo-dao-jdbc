package application;

import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {
	
	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
		
		System.out.println("==== Test Insert ====");
		
		Department department = new Department(27, "nome");
		departmentDao.insert(department);
		
		System.out.println(department);
		
		System.out.println("==== Test Update ====");
		System.out.print("Qual o id do departamento que deseja alterar? : ");
		int id = scanner.nextInt();
			
		department.setId(id);
		department.setName("Nome Atualizado");
		
		departmentDao.update(department);
		
		System.out.println("==== Atualizado ====");
		
		System.out.println("==== Test findById ====");
				
		 department =  departmentDao.findById(4);
		System.out.println(department);
		
		System.out.println("==== Test findAll ====");
	
		List<Department> list = departmentDao.findAll();
		
		for (Department department2 : list) {
			System.out.println(department2);
		}
		
		System.out.println("==== Test Delete ====");
		
		departmentDao.deleteById(26);
		
		
		
		
		scanner.close();
		
	}

}
