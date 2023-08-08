package com.example.scmt_ui_movil;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.scmt_ui_movil.modelos.UbicacionModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.scmt_ui_movil.databinding.ActivityUbicacionMapaBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Ubicacion_mapa extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private ActivityUbicacionMapaBinding binding;
    private Marker tempMarkers;
    private Marker realMarkers;
    private String ruta;
    private String nombreRuta;
    DatabaseReference myDataBase;
    DatabaseReference mRootChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUbicacionMapaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ruta = getIntent().getStringExtra("ruta");
        nombreRuta = getIntent().getStringExtra("nombreRuta");
        myDataBase = FirebaseDatabase.getInstance().getReference();

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            mRootChild = myDataBase.child("ubicaciones").child(ruta);
            mRootChild.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if (realMarkers != null){
                            realMarkers.remove();
                        }

                        Double latitud;
                        Double longitud;
                        UbicacionModel ubicacion = snapshot.getValue(UbicacionModel.class);
                        int estado = ubicacion.getEstado();
                        System.out.println(ubicacion.getEstado() + " " +ubicacion.getLatitud());
                        if(estado == 1){
                            latitud = ubicacion.getLatitud();
                            longitud = ubicacion.getLongitud();
                            marcadores(latitud,longitud);
                        }else{
                            Toast.makeText(Ubicacion_mapa.this, "Lo sentimos, ruta no iniciada", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        System.out.println(e);
                        Toast.makeText(Ubicacion_mapa.this,"Lo sentimos no existen datos", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Ubicacion_mapa.this,"Lo sentimos no existen datos", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(this,"Lo sentimos no existen datos", Toast.LENGTH_SHORT).show();
        }

    }

    public void marcadores (Double latitud, Double longitud){
        MarkerOptions marcadores = new MarkerOptions();
        marcadores.position(new LatLng(latitud,longitud));
        marcadores.title(nombreRuta);
        marcadores.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_bus));
        tempMarkers = mMap.addMarker(marcadores);
        realMarkers = null;
        realMarkers = tempMarkers;
    }
}