package models;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.time.LocalDate;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Product {
	@Retention(RetentionPolicy.RUNTIME)
    public @interface Order {
        int value();
    }
	
	public enum Types { BAKED , FISH, MEAT, MILKY, DETERGENTS, 
						DRINK, SWEETS, FRUITS, GROATS}
	
	@XStreamAlias("name")
	private String name;
	@XStreamAlias("price")
	private float price;
	@XStreamAlias("type")
	private Types type;
	@XStreamAlias("date")
	@JsonSerialize(using = serialization.dateSerializer.JsonLocalDateSerializer.class)
    @JsonDeserialize(using = serialization.dateSerializer.JsonLocalDateDeserializer.class)
	private LocalDate date;
	@XStreamAlias("barcode")
	private String barcode;
	@XStreamAlias("termin")
	@JsonSerialize(using = serialization.dateSerializer.JsonLocalDateSerializer.class)
    @JsonDeserialize(using = serialization.dateSerializer.JsonLocalDateDeserializer.class)
	private LocalDate termin;
	
	public Product() {
		name = null;
		price = 0;
		type = null;
		date = null;
		barcode = null;
		termin = null;
	}
	
	public Product(String name, float price, String type, LocalDate date, String barcode, LocalDate termin) {
		this.name = name;
		this.price = price;
		this.type = Types.valueOf(type);
		this.date = date;
		this.barcode = barcode;
		this.termin = termin;
	}
	@Order(value = 1)
	public void setName(String name) {
		this.name = name;
	}
	
	@Order(value = 1)
	public String getName() {
		return this.name;
	}
	
	@Order(value = 2)
	public void setPrice(float price) {
		this.price = price;
	}
	
	@Order(value = 2)
	public float getPrice() {
		return this.price;
	}
	
	@Order(value = 3)
	public void setType(String type) {
		this.type = Types.valueOf(type);
	}
	
	@Order(value = 3)
	public Types getType() {
		return this.type;
	}
	
	@Order(value = 4)
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	@Order(value = 4)
	public LocalDate getDate() {
		return this.date;
	}
	
	@Order(value = 5)
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	
	@Order(value = 5)
	public String getBarcode() {
		return this.barcode;
	}
	
	@Order(value = 6)
	public void setTermin(LocalDate termin) {
		this.termin = termin;
	}
	
	@Order(value = 6)
	public LocalDate getTermin() {
		return this.termin;
	}
	@JsonIgnore
	public boolean isOverdue() {
		if(LocalDate.now().compareTo(termin) > 0) 
			return true;
		else
			return false;
	}
	
	@Override
	public String toString() {
		return "Name: " + name + ".\n" + 
				"Price: " + price + ".\n" + 
				"Type: " + type + ".\n" + 
				"Date: " + date + ".\n" + 
				"Barcode: " + barcode + ".\n" + 
				"Termin: " + termin + ".\n";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((barcode == null) ? 0 : barcode.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + Float.floatToIntBits(price);
		result = prime * result + ((termin == null) ? 0 : termin.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object other) {
		Product product = (Product)other;
		if(!name.equals(product.getName())) return false;
		if(price != product.getPrice()) return false;
		if(type != product.getType()) return false;
		if(!date.equals(product.getDate())) return false;
		if(!barcode.equals(product.getBarcode())) return false;
		if(!termin.equals(product.getTermin())) return false;
		return true;
	}
}
