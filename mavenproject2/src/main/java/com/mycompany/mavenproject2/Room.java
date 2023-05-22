package com.mycompany.mavenproject2;

import java.util.List;

public class Room {
    private int room_no;
    private int current_num;
    private int desk_num;
    private String name;
    Desk desk[] = new Desk[3];
    private List<Desk> desks;
    public Room(){}
    public Room(String name, int room_no, int current_num, int desk_num) {
        this.room_no = room_no;
        this.name = name;
        this.current_num = current_num;
        this.desk_num = desk_num;
//		for(int i = 0; i<desk_num; i++) {
//			desk[i] = new Desk(i+1);
//			System.out.println("SIRA OLUSTU");
//		}

    }
    public int getRoom_no() {
        return room_no;
    }
    
    public void setRoom_no(int room_no){
        this.room_no = room_no;
    }
      public void setDesk_num(int desk_num){
        this.desk_num = desk_num;
    }
      public void setName(String name){
        this.name = name;
    }

    public List<Desk> getDesks() {
        return desks;
    }

    public void setDesks(List<Desk> desks) {
        this.desks = desks;
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
