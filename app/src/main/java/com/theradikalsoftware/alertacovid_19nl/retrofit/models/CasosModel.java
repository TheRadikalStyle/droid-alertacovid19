package com.theradikalsoftware.alertacovid_19nl.retrofit.models;

import java.util.List;

public class CasosModel {
    private String lastmodify;
    private List<InsideJSONModel> data;

    public String getLastmodify() { return lastmodify; }
    public List<InsideJSONModel> getData() { return data; }

    public void setLastmodify(String lastmodify) { this.lastmodify = lastmodify; }
    public void setData(List<InsideJSONModel> data) { this.data = data; }
}
