package test;

import static org.testng.Assert.assertEquals;

import java.sql.SQLException;
import java.time.LocalDate;

import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;

import models.Check;
import models.Product;

public class TestCheck {
	
  @Test(dataProvider = "getTotal")
  public void getTotalTest(Check check, float total) {
	  assertEquals(check.getTotal(), total);
  }
  @DataProvider
  public Object[][] getTotal() throws ClassNotFoundException, SQLException{
	  Product a = new Product("Milka", 100.50f, "MILKY", LocalDate.of(2002, 1, 31), "4521376", LocalDate.of(2017, 10, 11));
	  Product b = new Product("Hlib", 50.50f, "BAKED", LocalDate.of(2002, 1, 31), "4521376", LocalDate.of(2015, 1, 26));
	  Check check = new Check(1);
	  check.addProduct(a);
	  check.addProduct(b);
	  return new Object[][] { { check, 151.0f } };
  } 
  
  @Test(dataProvider = "getProductCount")
  public void getProductCountTest(int count, int expected) {
	  assertEquals(count, expected);
  }
  @DataProvider
  public Object[][] getProductCount() throws ClassNotFoundException, SQLException{
	  Product a = new Product("Milka", 100.50f, "MILKY", LocalDate.of(2002, 1, 31), "4521376", LocalDate.of(2017, 10, 11));
	  Product b = new Product("Hlib", 50.50f, "BAKED", LocalDate.of(2002, 1, 31), "4521376", LocalDate.of(2015, 1, 26));
	  Check check = new Check(1);
	  check.addProduct(a);
	  check.addProduct(a);
	  check.addProduct(a);
	  check.addProduct(b);
	  check.addProduct(b);
	  return new Object[][] { { check.getProductCount(a), 3 }, { check.getProductCount(b), 2 } };
  }
}