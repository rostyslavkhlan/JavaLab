package models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import models.Product.Order;

public class Paydesk {
	private int idPaydesk;
	private Worker currentWorker;
	Map<LocalDate, ArrayList<Check>> history = new HashMap<LocalDate, ArrayList<Check>>();
	
	public Paydesk() {
		this.idPaydesk = 0;
		this.currentWorker = new Worker();
		history = null;
	}
	
	public Paydesk(int id, Worker worker) {
		this.idPaydesk = id;
		this.currentWorker = worker;
	}
	
	@Order(value = 1)
	public int getIdPaydesk() {
		return this.idPaydesk;
	}
	
	public void setCurrentWorker(Worker worker) {
		this.currentWorker = worker;
	}
	
	public Worker getCurrentWorker() {
		return this.currentWorker;
	}
	
	public Map<LocalDate, ArrayList<Check>> getHistory(){
		return history;
	}
	
	public void changeWorker(Worker worker) {
		this.currentWorker = worker;
	}
	
	public float getHistoryTotal() {
		return (float)history.values().stream().mapToDouble(f -> f.stream().mapToDouble(f1 -> f1.getTotal()).sum()).sum();
	}
	
	public float getHistoryTotal(LocalDate date) {
		return (float)history.get(date).stream().mapToDouble(f -> f.getTotal()).sum();
	}
	
	public void addToHistory(Check check) {
		if(history.containsKey(check.getDate())) {
			history.get(check.getDate()).add(check);
		}
		else {
			ArrayList<Check> listCheck = new ArrayList<Check>();
			listCheck.add(check);
			history.put(check.getDate(), listCheck);
		}
	}
	
	public String issueCheck(Check check, float money) {
		addToHistory(check);
		String result = "";
		float change = money - check.getTotal();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		result += "Paydesk #" + idPaydesk + ". " + 
						currentWorker.getLastName() + " " +
						currentWorker.getFirstName().toCharArray()[0] + "." +
						currentWorker.getSurName().toCharArray()[0] + "." + 
						"\n" + "----------------------" + "\n" + 
						"Date of purchase: " + check.getDate() + " " + check.getTime().format(formatter) + "\n\n" +
						check.toString() + 
						"\nReceived: " + money + 
						"\nChange: " + change + 
						"\n" + "----------------------" + "\n\n";
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currentWorker == null) ? 0 : currentWorker.hashCode());
		result = prime * result + ((history == null) ? 0 : history.hashCode());
		result = prime * result + idPaydesk;
		return result;
	}
}