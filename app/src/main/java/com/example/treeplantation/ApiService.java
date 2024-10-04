package com.example.treeplantation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {

    @GET("user/getPlantations")
    Call<List<Plantation>> getPlantations(
            @Header("Authorization") String token
    );
}
