package com.alsayer.core.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class E_Vehicle_Info {

    @JsonProperty("Status")
    String Status;

    @JsonProperty("kunnr")
    String kunnr;

    @JsonProperty("VHVIN")
    String VHVIN;

    @JsonProperty("MODLINE")
    String MODLINE;

    @JsonProperty("MODYEAR")
    String MODYEAR;

    @JsonProperty("civilid")
    String civilid;

    @JsonProperty("DBM_LICEXT")
    String DBM_LICEXT;

    @Override
    public String toString() {
        return "E_Vehicle_Info{" +
                "Status='" + Status + '\'' +
                ", kunnr='" + kunnr + '\'' +
                ", VHVIN='" + VHVIN + '\'' +
                ", MODLINE='" + MODLINE + '\'' +
                ", MODYEAR='" + MODYEAR + '\'' +
                ", civilid='" + civilid + '\'' +
                ", DBM_LICEXT='" + DBM_LICEXT + '\'' +
                '}';
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getKunnr() {
        return kunnr;
    }

    public void setKunnr(String kunnr) {
        this.kunnr = kunnr;
    }

    public String getVHVIN() {
        return VHVIN;
    }

    public void setVHVIN(String VHVIN) {
        this.VHVIN = VHVIN;
    }

    public String getMODLINE() {
        return MODLINE;
    }

    public void setMODLINE(String MODLINE) {
        this.MODLINE = MODLINE;
    }

    public String getMODYEAR() {
        return MODYEAR;
    }

    public void setMODYEAR(String MODYEAR) {
        this.MODYEAR = MODYEAR;
    }

    public String getCivilid() {
        return civilid;
    }

    public void setCivilid(String civilid) {
        this.civilid = civilid;
    }

    public String getDBM_LICEXT() {
        return DBM_LICEXT;
    }

    public void setDBM_LICEXT(String DBM_LICEXT) {
        this.DBM_LICEXT = DBM_LICEXT;
    }
}
