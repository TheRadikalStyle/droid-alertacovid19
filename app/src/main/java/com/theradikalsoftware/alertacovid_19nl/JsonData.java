package com.theradikalsoftware.alertacovid_19nl;

public class JsonData implements Comparable {
    String municipio, casosConfirmados, ubicacion, lastModify;

    public String getMunicipio() { return municipio; }
    public String getCasosConfirmados() { return casosConfirmados; }
    public String getUbicacion() { return ubicacion; }
    public java.lang.String getLastModify() { return lastModify; }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
