package test;

import static org.testng.Assert.assertEquals;

import java.time.LocalDate;

import org.testng.annotations.Test;

import models.Product;

import org.testng.annotations.DataProvider;

public class TestProduct {
  @Test(dataProvider = "testProvider")
  public void myTest(Product product, boolean b) {
	  assertEquals(product.isOverdue(),b);
  }
  
  @DataProvider
  public Object[][] testProvider(){
	  Product a = new Product("Milka", 100.50f, "MILKY", LocalDate.of(2002, 1, 31), "4521376", LocalDate.of(2017, 10, 11));
	  Product b = new Product("Hlib", 50.50f, "BAKED", LocalDate.of(2002, 1, 31), "4521376", LocalDate.of(2020, 1, 26));
	  return new Object[][] { { a, true } , { b, false } };
  }
  
}