package com.alsayer.core.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Results {
    @JsonProperty("custid")
    String custid;
    @JsonProperty("civilid")
    String civilid;
    @JsonProperty("name")
    String name;
    @JsonProperty("namearabic")
    String namearabic;
    @JsonProperty("mobile")
    String mobile;

    @Override
    public String toString() {
        return "Results{" +
                "custid='" + custid + '\'' +
                ", civilid='" + civilid + '\'' +
                ", name='" + name + '\'' +
                ", namearabic='" + namearabic + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }

    public String getCustid() {
        return custid;
    }

    public void setCustid(String custid) {
        this.custid = custid;
    }

    public String getCivilid() {
        return civilid;
    }

    public void setCivilid(String civilid) {
        this.civilid = civilid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamearabic() {
        return namearabic;
    }

    public void setNamearabic(String namearabic) {
        this.namearabic = namearabic;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
