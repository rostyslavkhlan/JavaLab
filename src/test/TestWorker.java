package test;

import static org.testng.Assert.assertEquals;

import java.time.LocalDate;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import models.Worker;

public class TestWorker {
	
  @Test(dataProvider = "getAge")
  public void getAgeTest(int age, int expected) {
	  assertEquals(age, expected);
  }
  @DataProvider
  public Object[][] getAge(){
	  Worker worker1 = new Worker(1, "Vadym", "Konotopsky", "Andriyovych", LocalDate.of(1998, 1, 1));
	  Worker worker2 = new Worker(1, "Igor", "Lypa", "Olegovych", LocalDate.of(1996, 12, 12));
	  return new Object[][] {{worker1.WorkerAge(), 19}, {worker2.WorkerAge(), 21}};
  }
  
  @Test(dataProvider = "isCanWork")
  public void isCanWorkTest(boolean a, boolean b) {
	  assertEquals(a, b);
  }
  @DataProvider
  public Object[][] isCanWork(){
	  Worker worker1 = new Worker(1, "Vadym", "Konotopsky", "Andriyovych", LocalDate.of(1998, 1, 1));
	  Worker worker2 = new Worker(1, "Igor", "Lypa", "Olegovych", LocalDate.of(2007, 12, 12));
	  return new Object[][] {{worker1.isCanWork(), true}, {worker2.isCanWork(), false}};
  }
}
