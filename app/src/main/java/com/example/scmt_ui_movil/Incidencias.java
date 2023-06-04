package com.example.scmt_ui_movil;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.scmt_ui_movil.interfaces.IncidenciaAPI;
import com.example.scmt_ui_movil.interfaces.LoginAPI;
import com.example.scmt_ui_movil.modelos.IncidenciaModel;
import com.example.scmt_ui_movil.modelos.ListaRutas;
import com.example.scmt_ui_movil.modelos.LoginEnvio;
import com.example.scmt_ui_movil.modelos.PerfilConductor;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Incidencias extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private int idUsuario;
    private int idRuta;
    private EditText cTnombre;
    private Spinner sPruta;
    private EditText cTdescripcion;
    private Button insertar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidencias);
        cTnombre = findViewById(R.id.cTnombre);
        cTdescripcion = findViewById(R.id.cTdescripcion);
        idUsuario = Integer.parseInt(getIntent().getStringExtra("id"));
        consumirRutas(idUsuario);
        insertar = findViewById(R.id.enviar);
        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarDatos() == true){
                    insertarIncidencia(idUsuario,idRuta,cTnombre.getText().toString(),cTdescripcion.getText().toString());
                }else{
                    Toast.makeText(Incidencias.this,"Verifica los datos",Toast.LENGTH_SHORT).show();
                    cTnombre.setText("");
                    cTdescripcion.setText("");
                }

            }
        });
    }
    private void consumirRutas(int idUsuario){
        ArrayList<String> listaRuta = new ArrayList<String>();
        Retrofit usuarioR = new Retrofit.Builder().baseUrl("https://scmtapis.azurewebsites.net/").addConverterFactory(GsonConverterFactory.create()).build();
        IncidenciaAPI rutasAPI = usuarioR.create(IncidenciaAPI.class);
        Call <List<ListaRutas>> call = rutasAPI.rutas(idUsuario);
        call.enqueue(new Callback<List<ListaRutas>>() {
            @Override
            public void onResponse(Call<List<ListaRutas>> call, Response<List<ListaRutas>> response) {
                try {
                    if(response.isSuccessful()){
                        List responseFromAPI = response.body();
                        for(int i = 0; i < responseFromAPI.size();i++) {
                            ListaRutas datos = (ListaRutas) responseFromAPI.get(i);
                            String idNombre = datos.getId() + " - " + datos.getNombre();
                            listaRuta.add(idNombre);
                        }
                        llenarRutas(listaRuta);
                    }else{
                        Toast.makeText(Incidencias.this,"Error al caragr los datos, intente de nuevo más tarde 1",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(Incidencias.this,"Error al caragr los datos, intente de nuevo más tarde 2",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<ListaRutas>> call, Throwable t) {
                Toast.makeText(Incidencias.this,"Error al cargar los datos, intente de nuevo más tarde",Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
    }
    public void llenarRutas(ArrayList lista){
        sPruta = findViewById(R.id.ruta);
        sPruta.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sPruta.setAdapter(adapter);
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        adapterView.getSelectedItem();
        switch (adapterView.getId())
        {
            case R.id.ruta:
                String rutacompleta = adapterView.getSelectedItem().toString();
                String[] parts = rutacompleta.split(" ");
                String part1 = parts[0]; // 123
                idRuta = Integer.parseInt(part1);
                break;
            default:
                break;
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(Incidencias.this,"Selecciona una ruta",Toast.LENGTH_SHORT).show();
    }
    private void insertarIncidencia(int idUsuario,int idRuta,String nombre, String descripcion){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://scmtapis.azurewebsites.net/").addConverterFactory(GsonConverterFactory.create()).build();
        IncidenciaAPI retrofitAPI = retrofit.create(IncidenciaAPI.class);
        IncidenciaModel modal = new com.example.scmt_ui_movil.modelos.IncidenciaModel(idRuta, idUsuario, nombre,descripcion,"2023-05-09","07:30:00");
        Call<IncidenciaModel> call = retrofitAPI.insertarIncidencia(modal);
        call.enqueue(new Callback<IncidenciaModel>() {
            @Override
            public void onResponse(Call<IncidenciaModel> call, Response<IncidenciaModel> response) {
                try {
                    if(response.isSuccessful()){
                        IncidenciaModel responseFromAPI = response.body();
                        Map<String,String> datos = responseFromAPI.getData();
                        Toast.makeText(Incidencias.this,"Incidencia enviada",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(Incidencias.this,"Error al caragr los datos, intente de nuevo más tarde",Toast.LENGTH_SHORT).show();
                        cTdescripcion.setText("");
                        cTnombre.setText("");
                    }

                }catch (Exception e){
                    Toast.makeText(Incidencias.this,"Error al caragr los datos, intente de nuevo más tarde",Toast.LENGTH_SHORT).show();
                    System.out.println(e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<IncidenciaModel> call, Throwable t) {
                Toast.makeText(Incidencias.this,"Error al caragr los datos, intente de nuevo más tarde",Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
    }
    private boolean validarDatos(){
        cTnombre.getText().toString().isEmpty();
        cTdescripcion.getText().toString().isEmpty();
        boolean estadoNombre = false;
        boolean estadoDescripcion = false;
        if(cTnombre.length() > 5 && cTnombre.length() < 40){
            estadoNombre = true;
        }
        if(cTdescripcion.length() > 15 && cTdescripcion.length() < 150){
            estadoDescripcion = true;
        }
        if(estadoDescripcion == true && estadoNombre == true){
            return true;
        }else{
            return false;
        }
    }
}