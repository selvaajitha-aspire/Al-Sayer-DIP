package com.alsayer.core.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProspectCustomerSetResponse {

    @JsonProperty("ProspectCustomerSet")
    ProspectCustomerSet ProspectCustomerSet;

    @Override
    public String toString() {
        return "ProspectCustomerSetResponse{" +
                "ProspectCustomerSet=" + ProspectCustomerSet +
                '}';
    }

    public com.alsayer.core.response.ProspectCustomerSet getProspectCustomerSet() {
        return ProspectCustomerSet;
    }

    public void setProspectCustomerSet(com.alsayer.core.response.ProspectCustomerSet prospectCustomerSet) {
        ProspectCustomerSet = prospectCustomerSet;
    }
}
