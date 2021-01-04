package com.alsayer.core.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class InsuranceListSetResponse {

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
