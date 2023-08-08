package com.example.scmt_ui_movil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scmt_ui_movil.interfaces.IncidenciaAPI;
import com.example.scmt_ui_movil.modelos.ListaRutas;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class compartir_ubicacion extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private Button enviar_ubicacion;
    private Button terminar_ubicacion;
    private int idUsuario;
    private int idRuta;
    private String nombreRuta;
    private Spinner sPruta;
    private TextView texto_salida;
    DatabaseReference myDataBase = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compartir_ubicacion);
        Map<String, Object> datos_ubicacion = new HashMap<>();
        idUsuario = Integer.parseInt(getIntent().getStringExtra("id"));
        consumirRutas(idUsuario);
        texto_salida = findViewById(R.id.textoSalida);
        enviar_ubicacion = findViewById(R.id.enviar);
        enviar_ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtener_ubicacion();
            }
        });
        terminar_ubicacion = findViewById(R.id.detener);
        terminar_ubicacion.setVisibility(View.GONE);
        terminar_ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                terminarUbicacion(String.valueOf(idRuta));
            }
        });
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    for (android.location.Location location : locationResult.getLocations()) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        // Enviar la ubicación a través de una solicitud a Firebase
                        datos_ubicacion.put("estado", 1);
                        datos_ubicacion.put("latitud", location.getLatitude());
                        datos_ubicacion.put("longitud", location.getLongitude());
                        myDataBase.child("ubicaciones").child(String.valueOf(idRuta)).setValue(datos_ubicacion);
                        texto_salida.setText("Estas compartiendo la ubicacion de la ruta: " + nombreRuta);
                        sPruta.setEnabled(false);
                        enviar_ubicacion.setVisibility(View.GONE);
                        terminar_ubicacion.setVisibility(View.VISIBLE);

                    }
                }
            }
        };
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000); // Actualización de ubicación cada 5 segundos
    }

    private void consumirRutas(int idUsuario) {
        ArrayList<String> listaRuta = new ArrayList<String>();
        Retrofit usuarioR = new Retrofit.Builder().baseUrl("https://scmtapis.azurewebsites.net/").addConverterFactory(GsonConverterFactory.create()).build();
        IncidenciaAPI rutasAPI = usuarioR.create(IncidenciaAPI.class);
        Call<List<ListaRutas>> call = rutasAPI.rutas(idUsuario);
        call.enqueue(new Callback<List<ListaRutas>>() {
            @Override
            public void onResponse(Call<List<ListaRutas>> call, Response<List<ListaRutas>> response) {
                try {
                    if (response.isSuccessful()) {
                        List responseFromAPI = response.body();
                        for (int i = 0; i < responseFromAPI.size(); i++) {
                            ListaRutas datos = (ListaRutas) responseFromAPI.get(i);
                            String idNombre = datos.getId() + " - " + datos.getNombre();
                            listaRuta.add(idNombre);
                        }
                        llenarRutas(listaRuta);
                    } else {
                        Toast.makeText(compartir_ubicacion.this, "Error al caragr los datos, intente de nuevo más tarde 1", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(compartir_ubicacion.this, "Error al caragr los datos, intente de nuevo más tarde 2", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ListaRutas>> call, Throwable t) {
                Toast.makeText(compartir_ubicacion.this, "Error al cargar los datos, intente de nuevo más tarde", Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
    }

    public void llenarRutas(ArrayList lista) {
        sPruta = findViewById(R.id.ruta);
        sPruta.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sPruta.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        adapterView.getSelectedItem();
        switch (adapterView.getId()) {
            case R.id.ruta:
                String rutacompleta = adapterView.getSelectedItem().toString();
                String[] parts = rutacompleta.split(" ");
                String part1 = parts[0]; // 123
                idRuta = Integer.parseInt(part1);
                nombreRuta = rutacompleta;
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(compartir_ubicacion.this, "Selecciona una ruta", Toast.LENGTH_SHORT).show();
    }



    private void obtener_ubicacion() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this ,new String[]{ Manifest.permission.ACCESS_FINE_LOCATION },MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            return;
        }/*
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object

                        }
                    }
                });*/
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void terminarUbicacion(String idRuta) {
        fusedLocationClient.removeLocationUpdates(locationCallback);
        Map<String, Object> datos_ubicacion = new HashMap<>();
        datos_ubicacion.put("estado", 0);
        datos_ubicacion.put("latitud", 0);
        datos_ubicacion.put("longitud", 0);
        myDataBase.child("ubicaciones").child(idRuta).setValue(datos_ubicacion);
        texto_salida.setText("");
        sPruta.setEnabled(true);
        enviar_ubicacion.setVisibility(View.VISIBLE);
        terminar_ubicacion.setVisibility(View.GONE);
    }
    public void irAInicio(View view) { finish(); }

}