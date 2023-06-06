package com.example.scmt_ui_movil.interfaces;
import com.example.scmt_ui_movil.modelos.IncidenciaModel;
import com.example.scmt_ui_movil.modelos.ListaRutas;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IncidenciaAPI {
    @POST("scmt/insertarIncidencia")
    Call<IncidenciaModel> insertarIncidencia (@Body IncidenciaModel incidencia);
    @GET("scmt/consultarRutasConductor")
    Call<List<ListaRutas>> rutas (@Query("id") int id);
    @GET("scmt/consultarRutasIncidencias")
    Call<List<IncidenciaModel>> obtenerIncidencias(@Query("usuarioRuta") int usuarioRuta);
}
