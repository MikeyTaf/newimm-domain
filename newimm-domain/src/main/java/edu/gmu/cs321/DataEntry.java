package edu.gmu.cs321;

import java.util.Date;

public class DataEntry {
    private String dataEntryID;
    private String petitionID;
    private String immigrantID;
    private Date entryDate;

    public DataEntry(String dataEntryID, String petitionID, String immigrantID, Date entryDate) {
        this.dataEntryID = dataEntryID;
        this.petitionID = petitionID;
        this.immigrantID = immigrantID;
        this.entryDate = entryDate;
    }

    public String getDataEntryID() {
        return dataEntryID;
    }

    public void setDataEntryID(String dataEntryID) {
        this.dataEntryID = dataEntryID;
    }

    public String getPetitionID() {
        return petitionID;
    }

    public void setPetitionID(String petitionID) {
        this.petitionID = petitionID;
    }

    public String getImmigrantID() {
        return immigrantID;
    }

    public void setImmigrantID(String immigrantID) {
        this.immigrantID = immigrantID;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public boolean createDataEntry() {
        return false;
    }

    public boolean updateDataEntry() {
        return false;
    }

}
