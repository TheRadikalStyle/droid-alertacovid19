package com.theradikalsoftware.alertacovid_19nl.retrofit.services;

import com.theradikalsoftware.alertacovid_19nl.retrofit.models.CasosModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CasosService {
    @GET("casos2.php")
    Call<CasosModel> getAllCasos();
}
