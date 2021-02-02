package com.theradikalsoftware.alertacovid_19nl.retrofit.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class InsideJSONModel implements Parcelable {
    private String municipio;
    private int casosconfirmados;
    private String latlong;

    public String getMunicipio() { return municipio; }
    public int getCasosconfirmados() { return casosconfirmados; }
    public String getLatlong() { return latlong; }

    public void setMunicipio(String municipio) { this.municipio = municipio; }
    public void setCasosconfirmados(int casosconfirmados) { this.casosconfirmados = casosconfirmados; }
    public void setLatlong(String latlong) { this.latlong = latlong; }

    public LatLng getLatLongObject(){
        String datos[] = getLatlong().split(",");
        LatLng latLng = new LatLng(Double.parseDouble(datos[0]), Double.parseDouble(datos[1]));
        return latLng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
