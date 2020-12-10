package com.alsayer.core.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class E_ser_info {

    @JsonProperty("kunnr")
    String kunnr;

    @JsonProperty("vhvin")
    String vhvin;

    @JsonProperty("Mileage")
    String Mileage;

    @JsonProperty("Service_Type")
    String Service_Type;

    @JsonProperty("ServiceDesc")
    String ServiceDesc;

    @JsonProperty("Audat")
    String Audat;

    @JsonProperty("Werks_n")
    String Werks_n;

    @JsonProperty("Inv_Amt")
    String Inv_Amt;

    @JsonProperty("Location")
    String Location;

    @Override
    public String toString() {
        return "E_ser_info{" +
                "kunnr='" + kunnr + '\'' +
                ", vhvin='" + vhvin + '\'' +
                ", Mileage='" + Mileage + '\'' +
                ", Service_Type='" + Service_Type + '\'' +
                ", ServiceDesc='" + ServiceDesc + '\'' +
                ", Audat='" + Audat + '\'' +
                ", Werks_n='" + Werks_n + '\'' +
                ", Inv_Amt='" + Inv_Amt + '\'' +
                ", Location='" + Location + '\'' +
                '}';
    }

    public String getKunnr() {
        return kunnr;
    }

    public void setKunnr(String kunnr) {
        this.kunnr = kunnr;
    }

    public String getVhvin() {
        return vhvin;
    }

    public void setVhvin(String vhvin) {
        this.vhvin = vhvin;
    }

    public String getMileage() {
        return Mileage;
    }

    public void setMileage(String mileage) {
        Mileage = mileage;
    }

    public String getService_Type() {
        return Service_Type;
    }

    public void setService_Type(String service_Type) {
        Service_Type = service_Type;
    }

    public String getServiceDesc() {
        return ServiceDesc;
    }

    public void setServiceDesc(String serviceDesc) {
        ServiceDesc = serviceDesc;
    }

    public String getAudat() {
        return Audat;
    }

    public void setAudat(String audat) {
        Audat = audat;
    }

    public String getWerks_n() {
        return Werks_n;
    }

    public void setWerks_n(String werks_n) {
        Werks_n = werks_n;
    }

    public String getInv_Amt() {
        return Inv_Amt;
    }

    public void setInv_Amt(String inv_Amt) {
        Inv_Amt = inv_Amt;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }
}
