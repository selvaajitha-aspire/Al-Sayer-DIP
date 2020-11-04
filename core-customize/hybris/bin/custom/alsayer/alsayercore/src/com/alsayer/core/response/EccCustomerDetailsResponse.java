package com.alsayer.core.response;
import com.alsayer.core.response.NavCust;
import com.fasterxml.jackson.annotation.JsonProperty;


public class EccCustomerDetailsResponse {


    @JsonProperty("name")
    String name;
    @JsonProperty("civilid")
    String civilid;
    @JsonProperty("mobile")
    String mobile;
    @JsonProperty("Kunnr")
    String Kunnr;
    @JsonProperty("NavCust")
    NavCust NavCust;

    @Override
    public String toString() {
        return "EccResponse{" +
                "name='" + name + '\'' +
                ", civilid='" + civilid + '\'' +
                ", mobile='" + mobile + '\'' +
                ", Kunnr='" + Kunnr + '\'' +
                ", NavCust=" + NavCust +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCivilid() {
        return civilid;
    }

    public void setCivilid(String civilid) {
        this.civilid = civilid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getKunnr() {
        return Kunnr;
    }

    public void setKunnr(String Kunnr) {
        Kunnr = Kunnr;
    }

    public com.alsayer.core.response.NavCust getNavCust() {
        return NavCust;
    }

    public void setNavCust(NavCust navCust) {
        NavCust = navCust;
    }

}
