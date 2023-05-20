package com.mycompany.mavenproject2;

import java.util.Scanner;

public class Member {

    //private static int lineCounter = 0;
    private String id;
    private String name;
    private String surname;
    private String email;
    private int credit;
    private int desk;
    private int room;
    private int line;
    private int break_left;
    private boolean banned;

    Scanner input = new Scanner(System.in);

    public Member(String name, String surname, String id, String email, int credit, int break_left, boolean banned, int line, int desk, int room) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.id = id;
        this.credit = credit;
        this.banned = banned;
        this.break_left = break_left;
        this.line = line;
        this.desk = desk;
        this.room = room;

    }

    public Member(String name, String surname, String id, String email) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.id = id;
        credit = 5; // bu değişcek
        this.banned = false;
        break_left = 2; // bu değişcek
        line = 0;
        desk = -1;
        room = -1;

    }

    public int breakStart() {
        System.out.println("Break starts");
        int start = input.nextInt();
        return start;
    }

    public int breakEnd() {
        System.out.println("Break ends");
        int end = input.nextInt();           //-1 girilirse dönmemiş demektir.
        return end;
    }

    public int takeBreak() {

        int breakStart = this.breakStart();
        break_left--;
        int breakEnd;
        if (break_left > -1) {
            breakEnd = this.breakEnd();
        } else {
            System.out.println("Ara hakkiniz kalmamistir.");
            return 1;
        }

        System.out.println("Break left " + this.break_left);
        //System.out.println("credit left " + this.credit)
        if (breakEnd == -1) {
            return 0;
        } else if ((breakEnd == -1) || (breakEnd - breakStart > 30)) {
            credit = credit - 2;
        } else if (breakEnd - breakStart > 15) {
            credit--;
        }
        System.out.println("credit left " + this.credit);
        if (this.credit <= 0) {
            banned = true;
        }

        return 1;

    }

    public int getBreak_left() {
        return break_left;
    }

    public void setBreak_left(int break_left) {
        this.break_left = break_left;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public String getSurname() {
        return surname;
    }

    public String getId() {
        return id;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int point) {
        credit = credit + point;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getDesk() {
        return desk;
    }

    public void setDesk(int deskNo) {
        this.desk = deskNo;
    }

    public int getRoom() {
        return room;
    }

    public void setRoom(int roomNo) {
        this.room = roomNo;
    }

    //public void newLine(int x) {
    //	line = line + x;
    //}
    public String toString() {

        String info = "\nName: ";
        info = info + getName();
        info = info + "\nSurname: " + getSurname();
        info = info + "\nCredit: " + getCredit();
        info = info + "\nBreak left: " + getBreak_left();
        if (isBanned()) {
            info = info + "\nBANNED!";
        } else {
            info = info + "\n line: " + line;
        }

        return info;
    }

}
