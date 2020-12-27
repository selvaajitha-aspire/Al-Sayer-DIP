package com.alsayer.core.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProspectCustomer {

    @JsonProperty("EnName")
    String EnName;
    @JsonProperty("mobile")
    String mobile;
    @JsonProperty("ArName")
    String ArName;
    @JsonProperty("civilId")
    String civilId;
    @JsonProperty("bpNumber")
    String bpNumber;
    @JsonProperty("email")
    String email;

    @Override
    public String toString() {
        return "ProspectCustomer{" +
                "EnName='" + EnName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", ArName='" + ArName + '\'' +
                ", civilId='" + civilId + '\'' +
                ", bpNumber='" + bpNumber + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public String getEnName() {
        return EnName;
    }

    public void setEnName(String enName) {
        EnName = enName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getArName() {
        return ArName;
    }

    public void setArName(String arName) {
        ArName = arName;
    }

    public String getCivilId() {
        return civilId;
    }

    public void setCivilId(String civilId) {
        this.civilId = civilId;
    }

    public String getBpNumber() {
        return bpNumber;
    }

    public void setBpNumber(String bpNumber) {
        this.bpNumber = bpNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
