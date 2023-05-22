package com.example.scmt_ui_movil.interfaces;

import com.example.scmt_ui_movil.Perfil;
import com.example.scmt_ui_movil.modelos.LoginEnvio;
import com.example.scmt_ui_movil.modelos.PerfilConductor;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LoginAPI {
    @POST("scmt/consultarU")
    Call<LoginEnvio> LoginUsuario(@Body LoginEnvio loginEnvio);

    @GET("scmt/consultarUsuario")
    Call<PerfilConductor> Usuario (@Query("id") String id);
}
