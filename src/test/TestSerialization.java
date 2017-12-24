package test;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import builders.ProductBuilder;
import builders.WorkerBuilder;
import models.Product;
import models.Worker;
import serialization.Serialization;

public class TestSerialization {
	File jsonTest = new File("../jsonTest");
	File xmlTest = new File("../xmlTest");
	ArrayList<Product> products = null;
	ArrayList<Worker> workers = null;
	
	@BeforeTest
	public void init() {
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
				setTermin(LocalDate.of(2018, 2, 3))
				.build();
		
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
		
		products = new ArrayList<Product>();
		products.add(product1);
		products.add(product2);
		workers = new ArrayList<Worker>();
		workers.add(worker1);
		workers.add(worker2);
	}
	
	
	private boolean equalsCollection(ArrayList<?> arrayList, ArrayList<?> expected) {
		for(int i = 0; i < arrayList.size(); i ++)
			if(!arrayList.get(i).equals(expected.get(i)))
				return false;
		return true;
	}
	
	@Test(dataProvider = "jsonSerialization")
	public void jsonSerializationTest(ArrayList<?> arrayList, ArrayList<?> expected) {
		assertTrue(equalsCollection(arrayList, expected));
	}
	@DataProvider
	public Object[][] jsonSerialization() throws Exception{
		ArrayList<Product> expectedProducts = new ArrayList<Product>();
		ArrayList<Worker> expectedWorkers = new ArrayList<Worker>();
		
		Serialization<Product> p = new serialization.JsonSerialize<Product>(Product.class);
		p.toFile(products, jsonTest);
		expectedProducts = p.fromFile(jsonTest);
		
		Serialization<Worker> w = new serialization.JsonSerialize<Worker>(Worker.class);
		w.toFile(workers, jsonTest);
		expectedWorkers = w.fromFile(jsonTest);
		
		return new Object[][] {{products, expectedProducts}, {workers, expectedWorkers}};
	}
	
	@Test(dataProvider = "xmlSerialization")
	public void xmlSerializationTest(ArrayList<?> arrayList, ArrayList<?> expected) {
		assertTrue(equalsCollection(arrayList, expected));
	}
	@DataProvider
	public Object[][] xmlSerialization() throws Exception{
		ArrayList<Product> expectedProducts = new ArrayList<Product>();
		ArrayList<Worker> expectedWorkers = new ArrayList<Worker>();
		
		Serialization<Product> p = new serialization.XmlSerialize<Product>(Product.class);
		p.toFile(products, xmlTest);
		expectedProducts = p.fromFile(xmlTest)
				;
		Serialization<Worker> w = new serialization.XmlSerialize<Worker>(Worker.class);
		w.toFile(workers, xmlTest);
		expectedWorkers = w.fromFile(xmlTest);
		
		return new Object[][] {{products, expectedProducts}, {workers, expectedWorkers}};
	}
}
