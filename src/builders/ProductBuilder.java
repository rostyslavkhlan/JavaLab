package builders;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.Product;
import models.Product.Types;

public class ProductBuilder {
	public static final String NAME_PATTERN = "^[a-zA-Z0-9]{1,15}( [a-zA-Z0-9]{1,15})?{1,15}$";
	public static final String BARCODE_PATTERN = "^[0-9]{7}$";
	
	private String name;
	private float price;
	private String type;
	private LocalDate date;
	private String barcode;
	private LocalDate termin;
	
	public ProductBuilder(String name) {
		this.name = name;
	}
	
	public ProductBuilder setPrice(float price) {
		this.price = price;
		return this;
	}
	
	public ProductBuilder setType(String type) {
		this.type = type;
		return this;
	}
	
	public ProductBuilder setDate(LocalDate date) {
		this.date = date;
		return this;
	}
	
	public ProductBuilder setBarcode(String barcode) {
		this.barcode = barcode;
		return this;
	}
	
	public ProductBuilder setTermin(LocalDate termin) {
		this.termin = termin;
		return this;
	}
	
	
	public Product build() {
		Product product = new Product();
		
		Pattern namePattern = Pattern.compile(NAME_PATTERN);
		Pattern barcodePattern = Pattern.compile(BARCODE_PATTERN);
		
		Matcher nameMatch = namePattern.matcher(this.name);
		Matcher barcodeMatch = barcodePattern.matcher(this.barcode);
	
		if(!(nameMatch.matches()))
			throw new IllegalArgumentException("Enter correct name.");
		if(price <= 0 )
			throw new IllegalArgumentException("Enter correct price.");
		if(date.compareTo(LocalDate.now()) > 0)
			throw new IllegalArgumentException("Enter correct date.");
		if(termin.compareTo(date) < 0)
			throw new IllegalArgumentException("Enter correct termin.");
		try {
			Types.valueOf(type);
		} catch(IllegalArgumentException e) {
			throw new IllegalArgumentException("Enter correct type.");
		}
		if(!(barcodeMatch.matches()))
			throw new IllegalArgumentException("Enter correct barcode.");
		
		
		product.setName(name);
		product.setPrice(price);
		product.setType(type);
		product.setDate(date);
		product.setBarcode(barcode);
		product.setTermin(termin);

		return product;
	}
	
}