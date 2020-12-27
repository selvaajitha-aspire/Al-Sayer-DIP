package com.alsayer.core.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProspectCustomerSet {

    @JsonProperty("ProspectCustomer")
    ProspectCustomer ProspectCustomer;

    @Override
    public String toString() {
        return "ProspectCusomerSet{" +
                "ProspectCustomer=" + ProspectCustomer +
                '}';
    }

    public com.alsayer.core.response.ProspectCustomer getProspectCustomer() {
        return ProspectCustomer;
    }

    public void setProspectCustomer(com.alsayer.core.response.ProspectCustomer prospectCustomer) {
        ProspectCustomer = prospectCustomer;
    }
}
