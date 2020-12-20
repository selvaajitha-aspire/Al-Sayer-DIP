package com.alsayer.core.response.fsm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class p {

    @JsonProperty("location")
    location location;

    public com.alsayer.core.response.fsm.location getLocation() {
        return location;
    }

    public void setLocation(com.alsayer.core.response.fsm.location location) {
        this.location = location;
    }
}
