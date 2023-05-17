package com.mycompany.mavenproject2;

import java.util.ArrayList;
import java.util.Scanner;


public class Kutuphane {
	private static int line = 0;
	private static int line2 = 1;
	private int roomNumber = 2;
	Room room[] = new Room[2];
	private ArrayList<Member>user;
	private int userCount;
	private ArrayList<Member>userLine;
	
	//member dizisi
	
	Scanner input = new Scanner(System.in);
	
	
	public Kutuphane() {

		for(int i=0; i<roomNumber; i++) {
			room[i] = new Room(""+i+"");
		}
		
		userLine = new ArrayList<>();
		user = new ArrayList<>();
		this.userCount = 0;
		
		
	}
	
	public void dolulukOrani() {
		//double doluluk = 1;
		for(int i=0;i<roomNumber;i++) {
		//	doluluk = doluluk * room[i].dolulukOrani();
		//	System.out.println(doluluk);
			System.out.println(room[i].getName() + "'nin doluluk odani: " + room[i].dolulukOrani());
		}
	}
	
	public void takeBreak(Member member) {
		
		int x = member.takeBreak();
		
		if(member.isBanned()) {
			System.out.println("Banlandın lan sg");
			exitKutup(member);
		}else if((member.getBreak_left() == -1)||(x == 0)) {
			member.setBreak_left(3);
			exitKutup(member);
		}
	}

	
	public boolean addUser(Member aUser) {
		if(user.add(aUser)) {
			userCount++;
			return true;
		}else {
			return false;
		}
	}
	
	public boolean addLine(Member aUser) {
		
		if(userLine.add(aUser)) {
			line++;
			userLine.get(userLine.indexOf(aUser)).setLine(line);
			return true;
		}else {
			return false;
		}
	}
	
	public void removeLine(Member member) {
		for (Member aUser : userLine) {
			if(aUser.getLine()==member.getLine()) {
				userLine.remove(userLine.indexOf(aUser));
			}
		}
	}
	
	
	public void makeReservations(Member member) {  // girdi mail adresi
		
		int a;
		int b;
		int flag = 0;
		
		if(!member.isBanned()) {
		
			if(member.getLine() != 0) {
				System.out.println("Henuz siraniz gelmedi. Tekrar reservasyon yapamazsiniz.");
			}else {
		
				for(int i=0;i<room.length;i++ ) {    // ilk kontrol edilmeli aksi taktirde rooms[i] null olmasından dolayı !rooms[i].isAvailable() ifadesi hata verir.
		
					for(int j=0;j<room[i].desk.length; j++) {
						if(room[i].desk[j].isAvailable()) {
							a = i+1;
							b = j+1;
							System.out.println("room " + i + ", masa " + j);
							flag = 1;
						}
				
					}
			
		
				}
		
				if(flag==0) {
					addLine(member);
					System.out.println("Kutuphane su anda dolu. Sira numaraniz: " + line);
				}else {
					System.out.println("Masa seciniz.");
			
					a = input.nextInt();
					b = input.nextInt();
			
					member.setRoom(a);
					member.setDesk(b);
					member.setLine(0);
					room[a].desk[b].setAvailable(false);
					room[a].desk[b].setOwnerId(member.getId());
					room[a].setCurrent_num(1);
					System.out.println(room[a].getName() + " current people number: " + room[a].getCurrent_num());
			
			
					a++;
					b++;
			
					System.out.println("Yeriniz: room " + a + ", masa " + b + "!");
				}
		
			}
		}else {
			System.out.println("Banlisin ki");
		}
	}
	
	public void exitKutup(Member member) {
		
		int i = member.getRoom();
		int j = member.getDesk();
		
		member.setRoom(-1);
		member.setDesk(-1);
		member.setBreak_left(2);
		System.out.println(member);
		
		if(line-line2 >= 0) {
			for (Member user : this.userLine) {
				if(user.getLine() == line2) {
					user.setRoom(i);
					user.setDesk(j);
					user.setLine(0);
					removeLine(user);
					room[i].desk[j].setOwnerId(user.getId());
					System.out.println(room[i].desk[j]);
					
					System.out.println(line2 + ". siradaki kisi yeriniz: room " + user.getRoom() + ", masa " + user.getDesk() + "!" );
					
					
				}
			}
			line2++;
		}else {
			room[i].setCurrent_num(-1);
			room[i].desk[j].setAvailable(true);
			room[i].desk[j].setOwnerId("-1");
			System.out.println(room[i].desk[j]);
		}
		
		System.out.println("Current people number: " + room[i].getCurrent_num());
		
		
	}
	

	public Room[] getRooms() {
		return room;
	}

	public int getUserCount() {
		return userCount;
	}

	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}

	public int getRoomNumber() {
		return roomNumber;
	}

	public ArrayList<Member> getUser() {
		return user;
	}

	public ArrayList<Member> getUserLine() {
		return userLine;
	}
	
	
	
	
	
	
}
