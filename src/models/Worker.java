package models;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.time.LocalDate;
import java.time.Period;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Worker {
	@Retention(RetentionPolicy.RUNTIME)
    public @interface Order {
        int value();
    }
	
	@XStreamAlias("id")
	private int id;
	@XStreamAlias("firstName")
	private String firstName;
	@XStreamAlias("lastName")
	private String lastName;
	@XStreamAlias("surName")
	private String surName;
	@XStreamAlias("birthdate")
	@JsonSerialize(using = serialization.dateSerializer.JsonLocalDateSerializer.class)
    @JsonDeserialize(using = serialization.dateSerializer.JsonLocalDateDeserializer.class)
	private LocalDate birthdate;
	
	public Worker() {
		id = 0;
		firstName = null;
		lastName = null;
		surName = null;
		birthdate = null;
	}
	
	public Worker(int id, String firstName, String lastName, String surName, LocalDate birthdate) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.surName = surName;
		this.birthdate = birthdate;
	}
	
	@Order(value = 1)
	public void setId(int id) {
		this.id = id;
	}
	
	@Order(value = 1)
	public int getId() {
		return this.id;
	}
	
	@Order(value = 2)
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	@Order(value = 2)
	public String getFirstName() {
		return this.firstName;
	}
	
	@Order(value = 3)
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Order(value = 3)
	public String getLastName() {
		return this.lastName;
	}

	@Order(value = 4)
	public void setSurName(String surName) {
		this.surName = surName;
	}
	
	@Order(value = 4)
	public String getSurName() {
		return this.surName;
	}
	
	@Order(value = 5)
	public void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}
	
	@Order(value = 5)
	public LocalDate getBirthdate() {
		return this.birthdate;
	}
	
	@JsonIgnore
	public int WorkerAge() {
		return Period.between(getBirthdate(), LocalDate.now()).getYears();
	}
	
	@JsonIgnore
	public boolean isCanWork() {
		if(WorkerAge() > 50 || WorkerAge() < 18) 
			return false;
		else 
			return true;
	}
	
	@Override
	public String toString() {
		return  "Full name: " + firstName + " " + lastName + " " + surName + ". Birthdate: " + birthdate + "\n";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((birthdate == null) ? 0 : birthdate.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + id;
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((surName == null) ? 0 : surName.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object other) {
		Worker worker = (Worker)other;
		if(id != worker.getId()) return false;
		if(!firstName.equals(worker.getFirstName())) return false;
		if(!lastName.equals(worker.getLastName())) return false;
		if(!surName.equals(worker.getSurName())) return false;
		return true;
	}
}
