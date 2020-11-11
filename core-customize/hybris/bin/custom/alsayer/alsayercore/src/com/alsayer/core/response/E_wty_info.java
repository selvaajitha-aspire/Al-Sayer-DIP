package com.alsayer.core.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;

public class E_wty_info {

    @JsonProperty("vhvin")
    String vhvin;

    @JsonProperty("kunnr")
    String kunnr;

    @JsonProperty("description")
    String description;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    @JsonProperty("wty_e_date")
    String wty_e_date;

    @Override
    public String toString() {
        return "WarrantyResult{" +
                "vhvin='" + vhvin + '\'' +
                ", kunnr='" + kunnr + '\'' +
                ", description='" + description + '\'' +
                ", wty_e_date='" + wty_e_date + '\'' +
                '}';
    }

    public String getVhvin() {
        return vhvin;
    }

    public void setVhvin(String vhvin) {
        this.vhvin = vhvin;
    }

    public String getKunnr() {
        return kunnr;
    }

    public void setKunnr(String kunnr) {
        this.kunnr = kunnr;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWty_e_date() {
        return wty_e_date;
    }

    public void setWty_e_date(String wty_e_date) {
        this.wty_e_date = wty_e_date;
    }
}
