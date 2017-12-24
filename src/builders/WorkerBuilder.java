package builders;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.Worker;

public class WorkerBuilder {
	public static final String NAME_PATTERN ="^[A-Z][a-z]{2,30}$";
	public static final int MIN_WORKER_AGE = 18;
	public static final int MAX_WORKER_AGE = 50;		
	
	private int id;
	private String firstName;
	private String lastName;
	private String surName;
	private LocalDate birthdate;
	
	public WorkerBuilder(int id) {
		this.id = id;
	}
	
	public WorkerBuilder setFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}
	
	public WorkerBuilder setLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public WorkerBuilder setSurName(String surName) {
		this.surName = surName;
		return this;
	}
	
	public WorkerBuilder setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
		return this;
	}
	
	public Worker build() {
		Worker worker = new Worker();
		
		Pattern NamePattern = Pattern.compile(NAME_PATTERN);
		
		Matcher firstNameMatch = NamePattern.matcher(this.firstName);
		Matcher lastNameMatch = NamePattern.matcher(this.lastName);
		Matcher surNameMatch = NamePattern.matcher(this.surName);
		
		if(id < 0)
			throw new IllegalArgumentException("Enter correct id.");
		if(!(firstNameMatch.matches()))
			throw new IllegalArgumentException("Enter correct first name.");
		if(!(lastNameMatch.matches()))
			throw new IllegalArgumentException("Enter correct last name.");
		if(!(surNameMatch.matches()))
			throw new IllegalArgumentException("Enter correct sure name.");
		if(Period.between(birthdate, LocalDate.now()).getYears() > MAX_WORKER_AGE || 
				Period.between(birthdate, LocalDate.now()).getYears() < MIN_WORKER_AGE )
			throw new IllegalArgumentException("This worker can not work in this store.");
		worker.setId(id);
		worker.setFirstName(firstName);
		worker.setLastName(lastName);
		worker.setSurName(surName);
		worker.setBirthdate(birthdate);
		
		return worker;
	}
}
