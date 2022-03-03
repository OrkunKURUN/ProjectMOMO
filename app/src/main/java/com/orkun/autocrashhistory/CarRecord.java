package com.orkun.autocrashhistory;

public class CarRecord {
    public int recordId, userId;
    public String vin;

    public CarRecord(){}

    public CarRecord(int recordId, int userId, String vin){
        this.recordId = recordId;
        this.userId = userId;
        this.vin = vin;
    }

    public int getUserId() {
        return userId;
    }

    public int getRecordId() {
        return recordId;
    }

    public String getVin() {
        return vin;
    }
}
