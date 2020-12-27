package com.alsayer.core.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InsuranceListSet {

    @JsonProperty("InsuranceList")
    List<InsuranceList> InsuranceList;

    @Override
    public String toString() {
        return "InsuranceListSet{" +
                "InsuranceList=" + InsuranceList +
                '}';
    }

    public List<InsuranceList> getInsuranceList() {
        return InsuranceList;
    }

    public void setInsuranceList(List<InsuranceList> insuranceList) {
        InsuranceList = insuranceList;
    }
}
