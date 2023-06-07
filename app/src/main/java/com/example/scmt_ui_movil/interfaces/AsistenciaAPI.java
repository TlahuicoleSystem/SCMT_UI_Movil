package com.example.scmt_ui_movil.interfaces;

import com.example.scmt_ui_movil.modelos.AsistenciaModel;
import com.example.scmt_ui_movil.modelos.IncidenciaModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AsistenciaAPI {

    @POST("scmt/insertarAsistencia")
    Call<AsistenciaModel> insertarAsistencia (@Body AsistenciaModel asistencia);
}
