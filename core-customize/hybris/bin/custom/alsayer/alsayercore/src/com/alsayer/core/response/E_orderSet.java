package com.alsayer.core.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class E_orderSet {

    @JsonProperty("E_order")
    E_order E_order;

    @Override
    public String toString() {
        return "E_orderSet{" +
                "E_order=" + E_order +
                '}';
    }

    public com.alsayer.core.response.E_order getE_order() {
        return E_order;
    }

    public void setE_order(com.alsayer.core.response.E_order e_order) {
        E_order = e_order;
    }
}
