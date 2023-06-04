package com.example.scmt_ui_movil;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.scmt_ui_movil.interfaces.IncidenciaAPI;
import com.example.scmt_ui_movil.modelos.ListaRutas;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Registrar_Asistencia extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private int idUsuario;
    private int idRuta;
    private Spinner sPruta;
    private ImageView ivCodigoQR;
    private Button generar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_asistencia);
        idUsuario = Integer.parseInt(getIntent().getStringExtra("id"));
        consumirRutas(idUsuario);
        ivCodigoQR = findViewById(R.id.ivCodigoQR);
        generar = findViewById(R.id.generarQR);
        generar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.encodeBitmap(
                            "idRuta:" + idRuta +
                            " idUsuarioPasajero:" +idUsuario,
                            BarcodeFormat.QR_CODE,
                            350,
                            350
                    );
                    ivCodigoQR.setImageBitmap(bitmap);
                }catch (Exception e){
                    Toast.makeText(Registrar_Asistencia.this,"Lo sentimos, error al generar el QR",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void consumirRutas(int idUsuario){
        ArrayList<String> listaRuta = new ArrayList<String>();
        Retrofit usuarioR = new Retrofit.Builder().baseUrl("https://scmtapis.azurewebsites.net/").addConverterFactory(GsonConverterFactory.create()).build();
        IncidenciaAPI rutasAPI = usuarioR.create(IncidenciaAPI.class);
        Call<List<ListaRutas>> call = rutasAPI.rutas(idUsuario);
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
                        Toast.makeText(Registrar_Asistencia.this,"Error al caragr los datos, intente de nuevo más tarde 1",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(Registrar_Asistencia.this,"Error al caragr los datos, intente de nuevo más tarde 2",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<ListaRutas>> call, Throwable t) {
                Toast.makeText(Registrar_Asistencia.this,"Error al cargar los datos, intente de nuevo más tarde",Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
    }
    public void llenarRutas(ArrayList lista){
        sPruta = findViewById(R.id.ruta);
        sPruta.setOnItemSelectedListener(this);
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
                String part1 = parts[0];
                idRuta = Integer.parseInt(part1);
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(Registrar_Asistencia.this,"Selecciona una ruta",Toast.LENGTH_SHORT).show();
    }
}