package com.mycompany.mavenproject2;

public class Desk {
	
	private int deskNo;
	private boolean isAvailable;
	private String ownerId;
	
	public Desk(int deskNo) {
		this.isAvailable = true;
		this.deskNo = deskNo;
		ownerId = "-1";
		
	}
	
	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public boolean isAvailable() {
		return isAvailable;
	}
	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public int getDeskNo() {
		return deskNo;
	}

	@Override
	public String toString() {
		return "Desk [deskNo=" + deskNo + ", isAvailable=" + isAvailable + ", ownerId=" + ownerId + "]";
	}

	
	
}