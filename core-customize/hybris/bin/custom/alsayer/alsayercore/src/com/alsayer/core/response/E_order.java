package com.alsayer.core.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class E_order {

    @JsonProperty("KUNNR")
    String KUNNR;
    @JsonProperty("VBELN")
    String VBELN;
    @JsonProperty("REQTYPE")
    String REQTYPE;
    @JsonProperty("GOVERNANCE")
    String GOVERNANCE;
    @JsonProperty("WERKS")
    String WERKS;
    @JsonProperty("LATITUDE")
    String LATITUDE;
    @JsonProperty("LONGITUDE")
    String LONGITUDE;
    @JsonProperty("ZIPCODE")
    String ZIPCODE;
    @JsonProperty("MILEAGE")
    String MILEAGE;
    @JsonProperty("STREET")
    String STREET;
    @JsonProperty("PAID_SERVICE")
    String PAID_SERVICE;
    @JsonProperty("RETURN")
    String RETURN;
    @JsonProperty("REMARKS1")
    String REMARKS1;
    @JsonProperty("VHVIN")
    String VHVIN;
    @JsonProperty("COUNTRY")
    String COUNTRY;
    @JsonProperty("ISSUETYPE")
    String ISSUETYPE;


    @Override
    public String toString() {
        return "E_order{" +
                "KUNNR='" + KUNNR + '\'' +
                ", VBELN='" + VBELN + '\'' +
                ", REQTYPE='" + REQTYPE + '\'' +
                ", GOVERNANCE='" + GOVERNANCE + '\'' +
                ", WERKS='" + WERKS + '\'' +
                ", LONGITUDE='" + LONGITUDE + '\'' +
                ", ZIPCODE='" + ZIPCODE + '\'' +
                ", MILEAGE='" + MILEAGE + '\'' +
                ", STREET='" + STREET + '\'' +
                ", PAID_SERVICE='" + PAID_SERVICE + '\'' +
                ", RETURN='" + RETURN + '\'' +
                ", REMARKS1='" + REMARKS1 + '\'' +
                ", VHVIN='" + VHVIN + '\'' +
                ", COUNTRY='" + COUNTRY + '\'' +
                ", ISSUETYPE='" + ISSUETYPE + '\'' +
                ", LATITUDE='" + LATITUDE + '\'' +
                '}';
    }

    public String getKUNNR() {
        return KUNNR;
    }

    public void setKUNNR(String KUNNR) {
        this.KUNNR = KUNNR;
    }

    public String getVBELN() {
        return VBELN;
    }

    public void setVBELN(String VBELN) {
        this.VBELN = VBELN;
    }

    public String getREQTYPE() {
        return REQTYPE;
    }

    public void setREQTYPE(String REQTYPE) {
        this.REQTYPE = REQTYPE;
    }

    public String getGOVERNANCE() {
        return GOVERNANCE;
    }

    public void setGOVERNANCE(String GOVERNANCE) {
        this.GOVERNANCE = GOVERNANCE;
    }

    public String getWERKS() {
        return WERKS;
    }

    public void setWERKS(String WERKS) {
        this.WERKS = WERKS;
    }

    public String getLONGITUDE() {
        return LONGITUDE;
    }

    public void setLONGITUDE(String LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

    public String getZIPCODE() {
        return ZIPCODE;
    }

    public void setZIPCODE(String ZIPCODE) {
        this.ZIPCODE = ZIPCODE;
    }

    public String getMILEAGE() {
        return MILEAGE;
    }

    public void setMILEAGE(String MILEAGE) {
        this.MILEAGE = MILEAGE;
    }

    public String getSTREET() {
        return STREET;
    }

    public void setSTREET(String STREET) {
        this.STREET = STREET;
    }

    public String getPAID_SERVICE() {
        return PAID_SERVICE;
    }

    public void setPAID_SERVICE(String PAID_SERVICE) {
        this.PAID_SERVICE = PAID_SERVICE;
    }

    public String getRETURN() {
        return RETURN;
    }

    public void setRETURN(String RETURN) {
        this.RETURN = RETURN;
    }

    public String getREMARKS1() {
        return REMARKS1;
    }

    public void setREMARKS1(String REMARKS1) {
        this.REMARKS1 = REMARKS1;
    }

    public String getVHVIN() {
        return VHVIN;
    }

    public void setVHVIN(String VHVIN) {
        this.VHVIN = VHVIN;
    }

    public String getCOUNTRY() {
        return COUNTRY;
    }

    public void setCOUNTRY(String COUNTRY) {
        this.COUNTRY = COUNTRY;
    }

    public String getISSUETYPE() {
        return ISSUETYPE;
    }

    public void setISSUETYPE(String ISSUETYPE) {
        this.ISSUETYPE = ISSUETYPE;
    }

    public String getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(String LATITUDE) {
        this.LATITUDE = LATITUDE;
    }
}
