package com.example.scmt_ui_movil;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scmt_ui_movil.interfaces.IncidenciaAPI;
import com.example.scmt_ui_movil.modelos.IncidenciaModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class verIincidencias extends AppCompatActivity {

    private int idRuta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_iincidencias);
        consumirIncidencias(1);
    }

    protected void consumirIncidencias(int usuarioRuta){
        List<Map<String, String>> listaIncidencias = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://scmtapis.azurewebsites.net/").addConverterFactory(GsonConverterFactory.create()).build();
        IncidenciaAPI incidenciaAPI = retrofit.create(IncidenciaAPI.class);
        Call<List<IncidenciaModel>> call = incidenciaAPI.obtenerIncidencias(1);
        call.enqueue(new Callback<List<IncidenciaModel>>() {
            @Override
            public void onResponse(Call<List<IncidenciaModel>> call, Response<List<IncidenciaModel>> response) {
                try {
                    if (response.isSuccessful()){
                        List<IncidenciaModel> incidencias = response.body();
                        LinearLayout verIncidenciasContainer = findViewById(R.id.verIncidenciasContainer);
                        verIncidenciasContainer.removeAllViews();
                        for (IncidenciaModel incidencia:incidencias){
                            Map<String, String> mapaIncidencia = new HashMap<>();
                            mapaIncidencia.put("Nombre de la ruta: ", incidencia.getNombre_ruta());
                            mapaIncidencia.put("Nombre de la incidencia: ", incidencia.getNombre_incidente());
                            mapaIncidencia.put("Descripcion: ", incidencia.getDescripcion());
                            mapaIncidencia.put("Fecha/Hora: ", incidencia.getFecha() + " " + incidencia.getHora());
                            listaIncidencias.add(mapaIncidencia);
                            System.out.println(mapaIncidencia);

                            LinearLayout linearLayout = new LinearLayout(verIincidencias.this);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(0, 0, 0, 25);
                            linearLayout.setLayoutParams(layoutParams);
                            linearLayout.setOrientation(LinearLayout.VERTICAL);
                            linearLayout.setPadding(20, 10, 10, 10);
                            linearLayout.setBackground(getResources().getDrawable(R.drawable.rectangle));

                            TextView nombreRuta = new TextView(verIincidencias.this);
                            nombreRuta.setLayoutParams(new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT));
                            nombreRuta.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                            nombreRuta.setTextColor(Color.parseColor("#4ECDE6"));
                            nombreRuta.setText("Ruta: " + incidencia.getNombre_ruta());

                            TextView nombreInci = new TextView(verIincidencias.this);
                            nombreInci.setLayoutParams(new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT));
                            nombreInci.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                            nombreInci.setTextColor(Color.parseColor("#4ECDE6"));
                            nombreInci.setText("Nombre de la incidencia: " + incidencia.getNombre_incidente());

                            TextView descripcionInci = new TextView(verIincidencias.this);
                            descripcionInci.setLayoutParams(new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT));
                            descripcionInci.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                            descripcionInci.setTextColor(Color.parseColor("#4ECDE6"));
                            descripcionInci.setText("Descripcion: " + incidencia.getDescripcion());

                            TextView fechaHora = new TextView(verIincidencias.this);
                            fechaHora.setLayoutParams(new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT));
                            fechaHora.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                            fechaHora.setTextColor(Color.parseColor("#4ECDE6"));
                            fechaHora.setText("Fecha/Hora: " + incidencia.getFecha() + " " + incidencia.getHora());

                            linearLayout.addView(nombreRuta);
                            linearLayout.addView(nombreInci);
                            linearLayout.addView(descripcionInci);
                            linearLayout.addView(fechaHora);

                            verIncidenciasContainer.addView(linearLayout);
                        }
                    }else {
                        Toast.makeText(verIincidencias.this, "Error al cargar los datos, intente de nuevo mas tarde 1", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(verIincidencias.this, "Error al cargar los datos, intente de nuevo mas tarde 2", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<IncidenciaModel>> call, Throwable t) {
                Toast.makeText(verIincidencias.this, "Error al cargar los datos, intente de nuevo más tarde 3", Toast.LENGTH_SHORT).show();
            }
        });
    }
}