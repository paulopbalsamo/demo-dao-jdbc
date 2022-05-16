package application;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		SellerDao sellerDao = DaoFactory.createSellerDao();

		System.out.println("=== TESTE 1: seller findByID ====");

		Seller seller = sellerDao.findById(3);

		System.out.println(seller);

		System.out.println("=== TESTE 2: seller findByDepartment ====");

		Department department = new Department(2, null);
		List<Seller> list = sellerDao.findByDepartment(department);

		for (Seller seller2 : list) {
			System.out.println(seller2);
		}

		System.out.println(seller);

		System.out.println("=== TESTE 3: seller findByAll ====");

		list = sellerDao.findAll();

		for (Seller seller2 : list) {
			System.out.println(seller2);
		}

		System.out.println("=== TESTE 4: seller insert ====");
		Seller newSeller = new Seller(null, "Greg", "greg@hotmail.com", new Date(), 2000.00, department);
		sellerDao.insert(newSeller);

		System.out.println("INSERIDO! novo id = " + newSeller.getId());

		System.out.println("=== TESTE 5: seller update ====");
		seller = sellerDao.findById(2);
		seller.setNome("Marta Waine");
		sellerDao.update(seller);

		System.out.println("Update completo!");

		System.out.println("=== TESTE 6: seller delete ====");
		System.out.print("Digite a id do funcionario para deletar: ");
		int id = scanner.nextInt();
		
		sellerDao.deleteById(id);
		
		System.out.println("Delete completo!");

		scanner.close();
	}

}
