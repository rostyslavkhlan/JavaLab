package main;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import builders.ProductBuilder;
import builders.WorkerBuilder;
import database.Database;
import models.Check;
import models.Paydesk;
import models.Product;
import models.Worker;
import serialization.Serialization;
			

public class Main {	
	public static void main(String args[]) throws Exception {
		Random rnd = new Random(System.currentTimeMillis());
		try {
			Product product1 = new ProductBuilder("Apple").
									setPrice(10f).
									setType("FRUITS").
									setDate(LocalDate.of(2017, 10, 8)).
									setBarcode("1275944").
									setTermin(LocalDate.of(2018, 1, 1)).
									build();
			
			Product product2 = new ProductBuilder("Danon").
									setPrice(25.25f).
									setType("MILKY").
									setDate(LocalDate.of(2017, 5, 5)).
									setBarcode("7221326").
									setTermin(LocalDate.of(2018, 2, 3)).
									build();
			
			Worker worker1 = new WorkerBuilder(1).
									setFirstName("Volodya").
									setLastName("Jugyl").
									setSurName("Zalischuk").
									setBirthdate(LocalDate.of(1998, 1, 15)).build();
			
			Worker worker2 = new WorkerBuilder(2).
									setFirstName("Alyona").
									setLastName("Gunder").
									setSurName("Olegivna").
									setBirthdate(LocalDate.of(1997, 6, 17)).build();
			
			Worker worker3 = new WorkerBuilder(3).
									setFirstName("Mishya").
									setLastName("Onuskiv").
									setSurName("Andriyovych").
									setBirthdate(LocalDate.of(1997, 10, 7)).build();
			
			ArrayList<Worker> workers = new ArrayList<Worker>();
			workers.add(worker1);
			workers.add(worker2);
			workers.add(worker3);
			System.out.println(product1.isOverdue());
			System.out.println(product1.toString());
			System.out.println(product2.toString());
			ArrayList<Product> products = new ArrayList<Product>();
			products.add(product1);
			products.add(product2);
			Check check1 = new Check(1);
			check1.addProduct(product1);
			check1.addProduct(product2);
			Check check2 = new Check(2);
			check2.addProduct(product1);
			check2.addProduct(product2);
			check2.addProduct(product1);
			check2.addProduct(product2);
			check2.addProduct(product2);
			check2.addProduct(product2);
			Paydesk paydesk1 = new Paydesk(1, worker1);
			System.out.println(paydesk1.issueCheck(check1, 200));
			System.out.println(paydesk1.issueCheck(check2, 200));
			
			Map<LocalDate, ArrayList<Check>> checkHistory = paydesk1.getHistory();
			ArrayList<Check> checklist = new ArrayList<Check>();
			checklist = checkHistory.get(LocalDate.now());
			Serialization<Check> t = new serialization.JsonSerialize<Check>(Check.class);
			t.toFile(checklist, new File("../check.json"));
			
			ArrayList<Product> pr = new ArrayList<Product>();
			pr.add(product1);
			pr.add(product2);
			Serialization<Product> p = new serialization.JsonSerialize<Product>(Product.class);
			p.toFile(pr, new File("../product.json"));
			
			Serialization<Product> txt = new serialization.TxtInputOutput<Product>(Product.class);
			txt.toFile(products, new File("../worker.txt"));
			System.out.println(txt.fromFile(new File("../product.txt")));
			//System.out.println(paydesk1.getHistory());
			//paydesk1.issueCheck(check2, 200);
			//System.out.println(paydesk1.getHistoryTotal());
			//System.out.println(paydesk1.getHistoryTotal(LocalDate.now()));
			//System.out.println(paydesk1.issueCheck(check1, 200));
			//System.out.println(paydesk1.issueCheck(check2, 300));
			//System.out.println(check2);
			//System.out.println(paydesk1.getHistory());
			
			
			/*System.out.println("---------------JSON & XML---------------------");
			Serialization<Product> temp;
			temp = new serialization.JsonSerialize<Product>(Product.class);
			temp.toFile(products, new File("../serialization.json"));
			System.out.println(temp.fromFile(new File("../serialization.json")));
			
			temp = new serialization.XmlSerialize<Product>(Product.class);
			temp.toFile(products, new File("../serialization.xml"));
			System.out.println(temp.fromFile(new File("../serialization.xml")));*/
			
			System.out.println("---------------SQL---------------------");
			Serialization<Product> temp;
			temp = new serialization.XmlSerialize<Product>(Product.class);
			products = temp.fromFile(new File("../serialization.xml"));
			Map<Product, Integer> mapProducts = new HashMap<Product, Integer>();
			for(Product prod : products) {
				int value = 5 + rnd.nextInt(25);
				mapProducts.put(prod, value);
			}
			
			Database db = new Database();
			//db.addProduct(product1, 2);
			//db.addProduct(product2, 4);
			//db.updateProduct(product1, product2);
			//db.createAllTables();
			//db.dropAllTables();
			db.addProducts(mapProducts);
			//db.AddWorkers(workers);
			//db.makePurchase(paydesk1, check1);
			//db.makePurchase(paydesk1, check2);
			
			//ArrayList<Product> productsFromDB = db.getAllProducts();
			//for(Product p : productsFromDB)
			//	System.out.println(p);
			//ArrayList<Product> overdueProducts = db.getAllOverdueProducts();
			//for(Product p : overdueProducts)
			//	System.out.println(p);
		}
		catch(IllegalArgumentException e) {
			System.out.println(e);
		}
	}
}
