package com.example.scmt_ui_movil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scmt_ui_movil.interfaces.AsistenciaAPI;
import com.example.scmt_ui_movil.interfaces.IncidenciaAPI;
import com.example.scmt_ui_movil.modelos.AsistenciaModel;
import com.example.scmt_ui_movil.modelos.IncidenciaModel;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LeerQR extends AppCompatActivity {
    private Button leer;
    private TextView salida;

    private int idRuta;
    private int idPasajero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leer_qr);
        salida = findViewById(R.id.salida);
        leer = findViewById(R.id.leerQR);
        idPasajero = Integer.parseInt(getIntent().getStringExtra("idPasajero"));
        System.out.println(fecha());
        System.out.println(hora());
        leer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrador = new IntentIntegrator(LeerQR.this);
                integrador.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrador.setPrompt("Lector QR");
                integrador.setCameraId(0);
                integrador.setBeepEnabled(true);
                integrador.setBarcodeImageEnabled(true);
                integrador.initiateScan();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
            if(result != null){
                if(result.getContents() == null){
                    Toast.makeText(LeerQR.this,"Lo sentimos, error al leer el QR",Toast.LENGTH_SHORT).show();
                }else{
                    String prueba = result.getContents();
                    String[] parts = prueba.split(",");
                    System.out.println(parts[0]);
                    idRuta = Integer.parseInt(parts[0]);
                    String par2 = parts[1].replace(",","");
                    String par3 = parts[2].replace(",","");
                    System.out.println(idRuta + par2 + par3);
                    if(par3.equals("56897")){
                        registrarAsistencia(idRuta);
                    }else{
                        Toast.makeText(LeerQR.this,"QR no valido",Toast.LENGTH_SHORT).show();
                    }
                }
            }else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }catch (Exception e){
            Toast.makeText(LeerQR.this, "Lo sentimos QR no valido", Toast.LENGTH_SHORT).show();
            salida.setText("Lo sentimso QR no valido");
        }


    }

    protected void registrarAsistencia(int idRutaa){
        String fecha = fecha();
        String hora = hora();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://scmtapis.azurewebsites.net/").addConverterFactory(GsonConverterFactory.create()).build();
        AsistenciaAPI retrofitAPI = retrofit.create(AsistenciaAPI.class);
        AsistenciaModel modal = new com.example.scmt_ui_movil.modelos.AsistenciaModel(idRutaa, idPasajero,1,fecha,hora);
        Call<AsistenciaModel> call = retrofitAPI.insertarAsistencia(modal);
        call.enqueue(new Callback<AsistenciaModel>() {
            @Override
            public void onResponse(Call<AsistenciaModel> call, Response<AsistenciaModel> response) {
                try {
                    if(response.isSuccessful()){
                        AsistenciaModel responseFromAPI = response.body();
                        Map<String,String> datos = responseFromAPI.getData();
                        Toast.makeText(LeerQR.this,"Asistencia registrada",Toast.LENGTH_SHORT).show();
                        salida.setText("Asistencia registrada");
                    }else{
                        Toast.makeText(LeerQR.this,"Error al caragr los datos, intente de nuevo más tarde",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(LeerQR.this,"Error al caragr los datos, intente de nuevo más tarde",Toast.LENGTH_SHORT).show();
                    System.out.println(e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<AsistenciaModel> call, Throwable t) {
                Toast.makeText(LeerQR.this,"Error al caragr los datos, intente de nuevo más tarde",Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
    }

    private String fecha (){
        int dia, mes , annio;
        String fecha = null;
        Calendar c = Calendar.getInstance();
        dia = c.get(Calendar.DATE);
        mes = c.get(Calendar.MONTH) + 1;
        annio = c.get(Calendar.YEAR);
        fecha = annio + "-"+ mes + "-" + dia;
        return fecha;
    }

    private String hora(){
        String horaa =  null;
        int hora, minuto,segundo;
        Calendar c = Calendar.getInstance();
        hora = c.get(Calendar.HOUR_OF_DAY);
        minuto = c.get(Calendar.MINUTE);
        segundo = c.get(Calendar.SECOND);
        horaa = hora + ":" + minuto + ":" + segundo;
        return horaa;
    }
    public void irAInicio(View view) { finish(); }
}