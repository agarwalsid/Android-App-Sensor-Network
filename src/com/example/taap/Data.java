package com.example.taap;

public class Data {

	//private variables
	int id;
	String dt;
	String temper;
	String condi;
	
	// Empty constructor
	public Data(){
		
	}
	// constructor
	public Data(int id, String dt, String temper, String condi){
		this.id = id;
		this.dt = dt;
		this.temper = temper;
		this.condi = condi;
	}
	
	// constructor
	public Data(String dt, String temper, String condi){
		this.dt = dt;
		this.temper = temper;
		this.condi = condi;
	}
	// getting ID
	public int getID(){
		return this.id;
	}
	
	// setting id
	public void setID(int id){
		this.id = id;
	}
	
	// getting name
	public String getDt(){
		return this.dt;
	}
	
	// setting name
	public void setDt(String dt){
		this.dt = dt;
	}
	
	// getting phone number
	public String getTemper(){
		return this.temper;
	}
	
	// setting phone number
	public void setTemper(String temper){
		this.temper = temper;
	}
	
	// getting phone number
		public String getCondi(){
			return this.condi;
		}
		
		// setting phone number
		public void setCondi(String condi){
			this.condi = condi;
		}

}
