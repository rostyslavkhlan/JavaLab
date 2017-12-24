package test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import builders.ProductBuilder;
import builders.WorkerBuilder;
import database.Database;
import models.Check;
import models.Paydesk;
import models.Product;
import models.Worker;

public class TestDatabase {
	Product product1 = null;
	Product product2 = null;
	Product product3 = null;
	Product product4 = null;
	Product product5 = null;
	Check check1 = null;
	Check check2 = null;
	Worker worker1 = null;
	Worker worker2 = null;
	Paydesk paydesk1 = null;
	Database db = null;
	ArrayList<Product> products = null;
	Map<Product, Integer> mapProducts = null;
	
	@BeforeTest
	public void initDatabase() throws ClassNotFoundException, SQLException {
		db = new Database();
	}
	
	public void addObjectsToDatabase() throws ClassNotFoundException, SQLException {
		Database.dropAllTables();
		Database.createAllTables();
		
		try {
			product1 = new ProductBuilder("Apple").
					setPrice(10f).
					setType("FRUITS").
					setDate(LocalDate.of(2017, 10, 8)).
					setBarcode("1275944").
					setTermin(LocalDate.of(2018, 1, 1)).
					build();

			product2 = new ProductBuilder("Danon").
					setPrice(25.25f).
					setType("MILKY").
					setDate(LocalDate.of(2017, 5, 5)).
					setBarcode("7221326").
					setTermin(LocalDate.of(2018, 2, 3)).
					build();
			
			product3 = new ProductBuilder("Orange").
					setPrice(9.5f).
					setType("FRUITS").
					setDate(LocalDate.of(2017, 12, 8)).
					setBarcode("4556213").
					setTermin(LocalDate.of(2018, 1, 1)).
					build();

			product4 = new ProductBuilder("Bread").
					setPrice(11f).
					setType("BAKED").
					setDate(LocalDate.of(2017, 12, 14)).
					setBarcode("9721007").
					setTermin(LocalDate.of(2018, 1, 1)).
					build();
			
			product5 = new ProductBuilder("Gala").
					setPrice(45f).
					setType("DETERGENTS").
					setDate(LocalDate.of(2017, 10, 8)).
					setBarcode("1275944").
					setTermin(LocalDate.of(2017, 12, 1)).
					build();
			
			products = new ArrayList<Product>();
			//products.add(product1);
			//products.add(product2);
			products.add(product3);
			products.add(product4);
			products.add(product5);
			Random rnd = new Random(System.currentTimeMillis());
			mapProducts = new HashMap<Product, Integer>();
			for(Product p : products) {
				int value = 5 + rnd.nextInt(25);
				mapProducts.put(p, value);
			}
			db.addProducts(mapProducts);
			
			worker1 = new WorkerBuilder(1).
					setFirstName("Volodya").
					setLastName("Jugyl").
					setSurName("Zalischuk").
					setBirthdate(LocalDate.of(1998, 1, 15)).build();

			worker2 = new WorkerBuilder(2).
					setFirstName("Alyona").
					setLastName("Gunder").
					setSurName("Olegivna").
					setBirthdate(LocalDate.of(1997, 6, 17)).build();
			
			//db.addWorker(worker1);
			db.addWorker(worker2);
			
			check1 = new Check(1);
			check1.addProduct(product3);
			check1.addProduct(product4);
			check1.addProduct(product5);
			products.remove(1);
			check2 = new Check(2);
			check2.addProduct(product3);
			 check2.addProduct(product2);
			check2.addProduct(product1);
			paydesk1 = new Paydesk(1, worker1);
		} catch(IllegalArgumentException e) {
			System.out.println(e);
		}
	}
	public boolean equalsProductsCollection(ArrayList<Product> products, ArrayList<Product> expected) {
		for(int i = 0; i < products.size(); i ++)
			if(!products.get(i).equals(expected.get(i)))
				return false;
		return true;
	}
	
	public boolean equalsWorkersCollection(ArrayList<Worker> workers, ArrayList<Worker> expected) {
		for(int i = 0; i < workers.size(); i ++)
			if(!workers.get(i).equals(expected.get(i)))
				return false;
		return true;
	}
	
	@Test(dataProvider = "addProducts")
	public void addProductsTest(int firstCount, int secondCount) {
		assertNotEquals(firstCount, secondCount);
	}
	@DataProvider
	public Object[][] addProducts() throws SQLException, ClassNotFoundException {
		ArrayList<Product> test = new ArrayList<Product>();
		addObjectsToDatabase();
		test.add(product1);
		test.add(product2);
		int first = db.getCountOfProducts();
		db.addProducts(test);
		int second = db.getCountOfProducts();
		return new Object[][]{{first,second}};
	}
	
	@Test(dataProvider = "addWorkers")
	public void addWorkersTest(int firstCount, int secondCount) {
		assertNotEquals(firstCount, secondCount);
	}
	@DataProvider
	public Object[][] addWorkers() throws SQLException{
		int first = db.getCountOfWorkers();
		db.addWorker(worker1);
		int second = db.getCountOfWorkers();
		return new Object[][]{{first,second}};
	}
	
	@Test(dataProvider = "getProducts")
	public void getProductsTest(ArrayList<Product> products, ArrayList<Product> expected) {
		assertTrue(equalsProductsCollection(products, expected));
	}
	@DataProvider
	public Object[][] getProducts() throws ClassNotFoundException, SQLException {
		ArrayList<Product> test = new ArrayList<Product>();
		test.add(product1);
		test.add(product2);
		test.add(product3);
		ArrayList<Product> out = new ArrayList<Product>();
		Database.dropAllTables();
		Database.createAllTables();
		db.addProducts(test);
		out = db.getAllProducts();
		return new Object[][] {{test, out}};
	}
	
	@Test(dataProvider = "getWorkers")
	public void getWorkersTest(ArrayList<Worker> workers, ArrayList<Worker> expected) {
		assertTrue(equalsWorkersCollection(workers, expected));
	}
	@DataProvider
	public Object[][] getWorkers() throws ClassNotFoundException, SQLException {
		ArrayList<Worker> in = new ArrayList<Worker>();
		ArrayList<Worker> out = new ArrayList<Worker>();
		in.add(worker1);
		in.add(worker2);
		Database.dropAllTables();
		Database.createAllTables();
		db.addWorkers(in);
		out = db.getAllWorkers();
		return new Object[][] {{in, out}};
	}
	
	@Test(dataProvider = "getOverdueProducts")
	public void getOverdueProductsTest(ArrayList<Product> products, ArrayList<Product> expected) {
		assertEquals(equalsProductsCollection(products, expected),false);
	}
	
	@DataProvider
	public Object[][] getOverdueProducts() throws ClassNotFoundException, SQLException{
		Database.dropAllTables();
		Database.createAllTables();
		db.addProducts(mapProducts);
		ArrayList<Product> in = new ArrayList<Product>();
		ArrayList<Product> out = new ArrayList<Product>();
		in.add(product4);
		out = db.getAllOverdueProducts();
		return new Object[][] {{in, out}};
	}
	
	/*
	@Test(dataProvider = "updateProduct")
	public void updateProductTest(Product test, Product expected) {
		assertTrue(test.equals(expected));
	}
	@DataProvider
	public Object[][] updateProduct() throws ClassNotFoundException, SQLException{
		Product test = new ProductBuilder("Bread").
							setPrice(15f).
							setType("BAKED").
							setDate(LocalDate.of(2017, 12, 14)).
							setBarcode("9721007").
							setTermin(LocalDate.of(2018, 1, 1)).
							build();
		
		Database.dropAllTables();
		Database.createAllTables();
		db.addProducts(mapProducts);
		db.updateProduct(product4, test);
		Product product = db.getProductByName(test.getName());
		return new Object[][] { { test, product } };
	}*/
}
