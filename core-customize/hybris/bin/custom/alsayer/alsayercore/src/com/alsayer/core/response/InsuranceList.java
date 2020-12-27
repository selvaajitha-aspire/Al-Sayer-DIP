package com.alsayer.core.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InsuranceList {

    @JsonProperty("chassisNumber")
    String chassisNumber;

    @JsonProperty("dateOfExpiry")
    String dateOfExpiry;

    @JsonProperty("coverageInfo")
    String coverageInfo;

    @JsonProperty("policyNumber")
    String policyNumber;

    @JsonProperty("civilId")
    String civilId;

    @JsonProperty("plateNumber")
    String plateNumber;

    @JsonProperty("dateOfIssue")
    String dateOfIssue;

    @Override
    public String toString() {
        return "InsuranceList{" +
                "chassisNumber='" + chassisNumber + '\'' +
                ", dateOfExpiry='" + dateOfExpiry + '\'' +
                ", coverageInfo='" + coverageInfo + '\'' +
                ", policyNumber='" + policyNumber + '\'' +
                ", civilId='" + civilId + '\'' +
                ", plateNumber='" + plateNumber + '\'' +
                ", dateOfIssue='" + dateOfIssue + '\'' +
                '}';
    }

    public String getChassisNumber() {
        return chassisNumber;
    }

    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public String getDateOfExpiry() {
        return dateOfExpiry;
    }

    public void setDateOfExpiry(String dateOfExpiry) {
        this.dateOfExpiry = dateOfExpiry;
    }

    public String getCoverageInfo() {
        return coverageInfo;
    }

    public void setCoverageInfo(String coverageInfo) {
        this.coverageInfo = coverageInfo;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getCivilId() {
        return civilId;
    }

    public void setCivilId(String civilId) {
        this.civilId = civilId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(String dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }
}
