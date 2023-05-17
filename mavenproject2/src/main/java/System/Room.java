package System;


public class Room {
	private int current_num = 0;
	private int desk_num = 3;
	private String name;
	Desk desk[] = new Desk[3];
	
	
	public Room(String name) {
		
		this.name = name;
		current_num = 0;
		for(int i = 0; i<desk_num; i++) {
			desk[i] = new Desk(i+1);
		}
		
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

	public void setCurrent_num(int x) {
		current_num = current_num + x;
	}
	
	
	
	
}
