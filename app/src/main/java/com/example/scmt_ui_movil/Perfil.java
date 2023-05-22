package com.example.scmt_ui_movil;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scmt_ui_movil.interfaces.LoginAPI;
import com.example.scmt_ui_movil.modelos.LoginEnvio;
import com.example.scmt_ui_movil.modelos.PerfilConductor;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Perfil extends AppCompatActivity {
    private String idUsuario;
    private int rol;
    private  TextView nombreCompleto, numeroEmpleado, usuario, direccion, telefono, licencia, turno;
    private  TextView numeroEmpleado_pasajero, direccion_area, licencia_jefe, turno_;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        idUsuario = getIntent().getStringExtra("id");
        nombreCompleto = findViewById(R.id.nombre);
        nombreCompleto.setText(getIntent().getStringExtra("nombreCompleto"));
        rol = Integer.parseInt(getIntent().getStringExtra("rol"));
        consultar(idUsuario);
    }

    protected void consultar(String idUsuario){
        Retrofit usuarioR = new Retrofit.Builder().baseUrl("http://192.168.1.69:5000/").addConverterFactory(GsonConverterFactory.create()).build();
        LoginAPI usuarioAPI = usuarioR.create(LoginAPI.class);
        Call<PerfilConductor> call = usuarioAPI.Usuario(idUsuario);
        call.enqueue(new Callback<PerfilConductor>() {
            @Override
            public void onResponse(Call<PerfilConductor> call, Response<PerfilConductor> response) {
                try {
                    if(response.isSuccessful()){
                        PerfilConductor responseFromAPI = response.body();
                        Map<String,String> datos = responseFromAPI.getData();
                        estructura(datos);
                    }else{
                        Toast.makeText(Perfil.this,"Error al caragr los datos, intente de nuevo más tarde",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(Perfil.this,"Error al caragr los datos, intente de nuevo más tarde",Toast.LENGTH_SHORT).show();
                    System.out.println("Entro en este   "+e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<PerfilConductor> call, Throwable t) {
                Toast.makeText(Perfil.this,"Error al cargar los datos, intente de nuevo más tarde",Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
    }

    private void estructura(Map<String,String> datos){
        numeroEmpleado = findViewById(R.id.numero_empleado);
        usuario = findViewById(R.id.usuario);
        direccion = findViewById(R.id.direccion);
        telefono = findViewById(R.id.telefono);
        licencia = findViewById(R.id.licencia);
        turno = findViewById(R.id.turno);
        turno_ = findViewById(R.id.turno_);
        if(rol == 2) {
            numeroEmpleado.setText(datos.get("id_empleado"));
            usuario.setText(datos.get("usuario"));
            direccion.setText(datos.get("direccion"));
            telefono.setText(datos.get("telefono"));
            licencia.setText(datos.get("id_licencia"));
            turno.setVisibility(View.GONE);
            turno_.setVisibility(View.GONE);
        }else{
            numeroEmpleado_pasajero = findViewById(R.id.num_empleado_pasajero);
            numeroEmpleado_pasajero.setText("Numero de empleado");
            direccion_area = findViewById(R.id.direccion_area);
            direccion_area.setText("Area");
            licencia_jefe = findViewById(R.id.licencia_jefeinmediato);
            licencia_jefe.setText("Jefe inmediato");
            numeroEmpleado.setText(datos.get("id_pasajero"));
            usuario.setText(datos.get("usuario"));
            telefono.setText(datos.get("telefono"));
            direccion.setText(datos.get("area"));
            licencia.setText(datos.get("jefe_inmediato"));
            turno.setText(datos.get("turno"));
        }
    }
}