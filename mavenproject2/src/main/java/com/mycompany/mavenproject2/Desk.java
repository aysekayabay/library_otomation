package com.mycompany.mavenproject2;

import org.bson.Document;
import org.bson.types.ObjectId;

public class Desk {

    private int deskNo;
    private boolean isAvailable;
    private ObjectId ownerId;

    public Desk(int deskNo) {
        this.isAvailable = true;
        this.deskNo = deskNo;
        ownerId = null;

    }

    public Document toDocument() {
        return new Document()
                .append("deskNo", deskNo)
                .append("isAvailable", isAvailable)
                .append("ownerId", ownerId);
    }

    public ObjectId getOwnerId() {
        return ownerId;
    }
    
    public void setOwnerId(ObjectId ownerId) {
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
