package com.alsayer.core.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InsuranceListSetResponse {

    @JsonProperty("InsuranceListSet")
     InsuranceListSet InsuranceListSet;

    @Override
    public String toString() {
        return "InsuranceListSetResponse{" +
                "InsuranceListSet=" + InsuranceListSet +
                '}';
    }

    public InsuranceListSet getInsuranceListSet() {
        return InsuranceListSet;
    }

    public void setInsuranceListSet(InsuranceListSet insuranceListSet) {
        InsuranceListSet = insuranceListSet;
    }
}
