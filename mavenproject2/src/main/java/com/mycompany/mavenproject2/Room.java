package com.mycompany.mavenproject2;


public class Room {
	private int current_num;
	private int desk_num;
	private String name;
	Desk desk[] = new Desk[3];
	
	public Room(String name, int current_num, int desk_num) {
		this.name = name;
		this.current_num = current_num;
		this.desk_num= desk_num;
//		for(int i = 0; i<desk_num; i++) {
//			desk[i] = new Desk(i+1);
//			System.out.println("SIRA OLUSTU");
//		}
		
	}
    
	
	public double dolulukOrani() {
		double doluluk = (current_num / desk_num) * 100;
		return doluluk;
	}


	public int getDesk_num() {
		return desk_num;
	}


	public String getName() {
		return name;
	}


	public Desk[] getDesk() {
		return desk;
	}
	
	public int getCurrent_num() {
		return current_num;
	}
        
	public void setCurrent_num(int current_num) {
		this.current_num = current_num;
	}
	
	
	
	
}