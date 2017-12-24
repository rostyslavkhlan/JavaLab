package database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;

import builders.ProductBuilder;
import builders.WorkerBuilder;
import models.Check;
import models.Paydesk;
import models.Product;
import models.Worker;

public class Database {
	private String url;
	private String uname;
	private String pass;
	private static Connection con;
	
	public Database() throws ClassNotFoundException, SQLException{
		url = "jdbc:mysql://localhost:3306/store";
		uname = "root";
		pass = "root";
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection(url, uname, pass);
	}
	
	public static Connection getConnection() {
		return con;
	}
	
	public static void setConnection(Connection con) {
		Database.con = con;
	}
	
	public ArrayList<Worker> getAllWorkers() throws SQLException{
		ArrayList<Worker> allWorkers = new ArrayList<Worker>();
		ResultSet rs = null;
		Statement st = null; 
		
		try {
			st = con.createStatement();
			rs = st.executeQuery("select * from worker");
			while(rs.next()){
				int id = rs.getInt("id_worker");
				String firstName = rs.getString("firstName");
				System.out.println("First name: " + firstName);
				String lastName = rs.getString("lastName");
				System.out.println("Last name: " + lastName);
				String surName = rs.getString("surName");
				System.out.println("Sure name: " + surName);
				LocalDate birthdate = rs.getDate("birthdate").toLocalDate();
				Worker worker = new WorkerBuilder(id).setFirstName(firstName).
								setLastName(lastName).setSurName(surName).setBirthdate(birthdate).build();
				allWorkers.add(worker);
			}
		} finally {
			if(rs != null)
				rs.close();
			if(st != null)
				st.close();
		}
		return allWorkers;
	}
	
	public void addWorkers(ArrayList<Worker> workers) throws SQLException {
		for(Worker w : workers)
			addWorker(w);
	}
	
	public void addWorker(Worker worker) throws SQLException {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("insert into worker values(?,?,?,?,?);");
			st.setInt(1, worker.getId());
			st.setString(2, worker.getFirstName());
			st.setString(3, worker.getLastName());
			st.setString(4, worker.getSurName());
			st.setDate(5, Date.valueOf(LocalDate.of(worker.getBirthdate().getYear(), 
						worker.getBirthdate().getMonth(), worker.getBirthdate().getDayOfMonth())));
			st.execute();
		} finally {
			if(st != null)
				st.close();
		}
	}
	
	public void deleteWorkerByiD(int id) throws SQLException {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("delete from worker where id=?;");
			st.setInt(1, id);
			st.execute();
		} finally {
			if(st != null)
				st.close();
		}
	}
	
	public int getCountOfWorkers() throws SQLException {
		ResultSet rs = null;
		Statement st = null; 
		try {
			st = con.createStatement();
			rs = st.executeQuery("select count(*) as workercount from worker");
			rs.next();
			return rs.getInt("workercount");
		} finally {
			if(rs != null)
				rs.close();
				st.close();
		}
	}

	public ArrayList<Product> getAllProducts() throws SQLException{
		ArrayList<Product> allProducts = new ArrayList<Product>();
		ResultSet rs = null;
		Statement st = null; 
		
		try {
			st = con.createStatement();
			rs = st.executeQuery("select * from product");
			while(rs.next()){
				String name = rs.getString("name");
				float price = rs.getFloat("price");
				String type = rs.getString("type");
				LocalDate date = rs.getDate("date").toLocalDate();
				String barcode = rs.getString("barcode");
				LocalDate termin = rs.getDate("termin").toLocalDate();
				
				Product product = new ProductBuilder(name).setPrice(price).setType(type).
									setDate(date).setBarcode(barcode).setTermin(termin).build();
				allProducts.add(product);
			}
		} finally {
			if(rs != null)
				rs.close();
			if(st != null)
				st.close();
		}
		return allProducts;
	}
	
	public boolean isProductExist(Product product) throws SQLException {
		ResultSet rs = null;
		PreparedStatement st = null; 
		try {
			st = con.prepareStatement("select * from product where product.name=?");
			st.setString(1, product.getName());
			rs = st.executeQuery();
			if(rs.next())
				return true;
			return false;
		} finally {
			if(rs != null)
				rs.close();
			if(st != null)
				st.close();
		}
	}
	
	public void addExistingProduct(Product product, int count) throws SQLException {
		PreparedStatement st = null;
			try {
				st = con.prepareStatement("update product set count=count+? where product.name=?");
				st.setInt(1, count);
				st.setString(2, product.getName());
				st.execute();
			} finally {
				if(st != null)
					st.close();
			}	
	}
	
	public void addNewProduct(Product product, int count) throws SQLException {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("insert into product (name,count,price,type,date,barcode,termin) " + 
					"values(?,?,?,?,?,?,?)");
			st.setString(1, product.getName());
			st.setInt(2, count);
			st.setFloat(3, product.getPrice());
			st.setString(4, String.valueOf(product.getType()));
			st.setDate(5, Date.valueOf(LocalDate.of(product.getDate().getYear(), 
						product.getDate().getMonth(), product.getDate().getDayOfMonth())));
			st.setString(6, product.getBarcode());
			st.setDate(7, Date.valueOf(LocalDate.of(product.getTermin().getYear(), 
						product.getTermin().getMonth(), product.getTermin().getDayOfMonth())));
			st.execute();
		} finally {
			if(st != null)
				st.close();
		}
	}
	
	public void addProduct(Product product, int count) throws SQLException {
		if(isProductExist(product)) {
			addExistingProduct(product, count);
		}
		else {
			addNewProduct(product, count);
		}
	}
	
	public void addProducts(Map<Product, Integer> products) throws SQLException {
		for(Map.Entry<Product, Integer> p : products.entrySet())
			addProduct(p.getKey(),p.getValue());
	}
	
	public void addProducts(ArrayList<Product> products) throws SQLException {
		for(Product p : products)
			addProduct(p,1);
	}
	
	public void updateProduct(Product product) throws SQLException {
		if(isProductExist(product))
			throw new IllegalArgumentException("This product already in database!");
		
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("update product set name=?, price=?, type=?, date=?, barcode=?, termin=? where product.name=?;");
			st.setString(1, product.getName());
			st.setFloat(2, product.getPrice());
			st.setString(3, String.valueOf(product.getType()));
			st.setDate(4, Date.valueOf(LocalDate.of(product.getDate().getYear(), 
					product.getDate().getMonth(), product.getDate().getDayOfMonth())));
			st.setString(5, product.getBarcode());
			st.setDate(6, Date.valueOf(LocalDate.of(product.getTermin().getYear(), 
					product.getTermin().getMonth(), product.getTermin().getDayOfMonth())));
			st.setString(7, product.getName());
			st.execute();
		} finally {
			if(st != null)
				st.close();
		}
	}
		
	public Product getProductByName(String name) throws SQLException {
		ResultSet rs = null;
		Statement st = null; 
		try {
			st = con.createStatement();
			rs = st.executeQuery("select * from product where name = '" + name + "';");
			rs.next();
			String pname = rs.getString("name");
			float price = rs.getFloat("price");
			String type = rs.getString("type");
			LocalDate date = rs.getDate("date").toLocalDate();
			String barcode = rs.getString("barcode");
			LocalDate termin = rs.getDate("termin").toLocalDate();
			
			Product product = new ProductBuilder(pname).setPrice(price).setType(type).
								setDate(date).setBarcode(barcode).setTermin(termin).build();
			return product;
			
		} finally {
			if(rs != null)
				rs.close();
			if(st != null)
				st.close();
		}
	}
	
	public void deleteProductByiD(int id) throws SQLException {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("delete from product where id_product=?;");
			st.setInt(1, id);
			st.execute();
		} finally {
			if(st != null)
				st.close();
		}
	}
	
	public void deleteProductByName(String name) throws SQLException {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("delete from product where product.name=?;");
			st.setString(1, name);
			st.execute();
		} finally {
			if(st != null)
				st.close();
		}
	}
	
	public void makePurchase(Paydesk paydesk, Check check) throws SQLException {
		ArrayList<Product> products = check.getProducts();
		PreparedStatement st = null;
		try {
			for(Product p : products) {
				
				st = con.prepareStatement("update product set count = count - 1 where barcode=?");
				st.setString(1, p.getBarcode());
				st.execute();
			}
		} finally {
			if(st != null)
				st.close();
		}
		
		try {
			st = con.prepareStatement("insert into listcheck values(?,?,?,?,?)");
			st.setInt(1, check.getIdCheck());
			st.setDate(2, Date.valueOf(LocalDate.of(check.getDate().getYear(), check.getDate().getMonth(), check.getDate().getDayOfMonth())));
			st.setTime(3, Time.valueOf(LocalTime.of(check.getTime().getHour(), check.getTime().getMinute(), check.getTime().getSecond())));
			st.setFloat(4, check.getTotal());
			st.setInt(5, paydesk.getCurrentWorker().getId());
			st.execute();
		} finally {
			if(st != null)
				st.close();
		}
		
		try {
			for(Product p : products) {
				st = con.prepareStatement("insert into check_product_id(id_check, id_product) select id_check, " +
											"id_product from listcheck, product where id_check=? and barcode=?");
				st.setInt(1, check.getIdCheck());
				st.setString(2, p.getBarcode());
				st.execute();
			}
		} finally {
			if(st != null)
				st.close();
		}
	}
	
	public ArrayList<Product> getAllOverdueProducts() throws SQLException{
		ArrayList<Product> overdueProducts = new ArrayList<Product>();
		for(Product p : getAllProducts()) 
			if(p.isOverdue())
				overdueProducts.add(p);
		return overdueProducts;
	}
	
	public int getCountOfProducts() throws SQLException {
		ResultSet rs = null;
		Statement st = null; 
		try {
			st = con.createStatement();
			rs = st.executeQuery("select count(*) as productcount from product");
			rs.next();
			return rs.getInt("productcount");
		} finally {
			if(rs != null)
				rs.close();
				st.close();
		}
	}
	
	public static void createTableWorker() throws ClassNotFoundException, SQLException {
		Statement st = con.createStatement();
		String query = "create table worker(id_worker int auto_increment primary key not null," + 
					"firstName varchar(50), lastName varchar(50), surName varchar(50), birthdate date);";
		st.execute(query);
		st.close();
	}
	
	public static void createTableProduct() throws ClassNotFoundException, SQLException {
		Statement st = con.createStatement();
		String query = "create table product(id_product int auto_increment primary key," + 
				"name varchar(50), count int, price float(5,2), type varchar(50)," + 
				"date date, barcode varchar(50), termin date);";
		st.execute(query);
		st.close();
	}
	
	public static void createTableListCheck() throws ClassNotFoundException, SQLException {
		Statement st = con.createStatement();
		String query = "create table listCheck(id_check int auto_increment primary key, " + 
				"date date, time varchar(50), total  float(5,2), " + 
				"id_worker int not null, foreign key(id_worker) references worker(id_worker));";
		st.execute(query);
		st.close();
	}
	
	public static void createTableCheckProductId() throws ClassNotFoundException, SQLException {
		Statement st = con.createStatement();
		String query = " create table check_product_id" + 
				"(id_check int not null, id_product int not null, foreign key(id_check) references listCheck(id_check)," + 
				" foreign key(id_product) references product(id_product));";
		st.execute(query);
		st.close();
	}
	
	public static void createAllTables() throws ClassNotFoundException, SQLException {
		createTableWorker();
		createTableProduct();
		createTableListCheck();
		createTableCheckProductId();
	}
	
	public static void dropAllTables() throws ClassNotFoundException, SQLException {
		Statement st = con.createStatement();
		st.execute("drop table check_product_id;");
		st.execute("drop table listcheck;");
		st.execute("drop table product;");
		st.execute("drop table worker;");
		st.close();
	}
}
