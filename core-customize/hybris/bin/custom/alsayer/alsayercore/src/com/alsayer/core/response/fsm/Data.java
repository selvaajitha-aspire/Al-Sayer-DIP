package com.alsayer.core.response.fsm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Data {
    @Override
    public String toString() {
        return "Data{" +
                "p=" + p +
                ", sas=" + sas +
                '}';
    }

    @JsonProperty("p")
    p p;
    @JsonProperty("sas")
    sas sas;

    public com.alsayer.core.response.fsm.p getP() {
        return p;
    }

    public void setP(com.alsayer.core.response.fsm.p p) {
        this.p = p;
    }

    public com.alsayer.core.response.fsm.sas getSas() {
        return sas;
    }

    public void setSas(com.alsayer.core.response.fsm.sas sas) {
        this.sas = sas;
    }

}
