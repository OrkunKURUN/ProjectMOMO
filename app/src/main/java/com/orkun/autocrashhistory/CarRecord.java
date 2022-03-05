package com.orkun.autocrashhistory;

public class CarRecord {
    public String vin, recordId, userName;

    public CarRecord(){}

    public CarRecord(String userName, String recordId, String vin){
        this.recordId = recordId;
        this.userName = userName;
        this.vin = vin;
    }

    public String getUserName() {
        return userName;
    }

    public String getRecordId() {
        return recordId;
    }

    public String getVin() {
        return vin;
    }
}
