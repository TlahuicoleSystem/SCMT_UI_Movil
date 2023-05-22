package com.example.scmt_ui_movil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.scmt_ui_movil.interfaces.LoginAPI;
import com.example.scmt_ui_movil.modelos.LoginEnvio;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    EditText usuario;
    EditText contraseña;
    Button entrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usuario = findViewById(R.id.cTUsuario);
        contraseña = findViewById(R.id.cTContrasenia);
        entrar = findViewById(R.id.btniniciar_sesion);
        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usuario.getText().toString().isEmpty() && contraseña.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Por favor ingrese los datos", Toast.LENGTH_SHORT).show();
                    return;
                }
                postData(usuario.getText().toString(), contraseña.getText().toString());
            }
        });
    }

    private void postData(String usuariot, String contraseñaa) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.1.69:5000/").addConverterFactory(GsonConverterFactory.create()).build();
        LoginAPI retrofitAPI = retrofit.create(LoginAPI.class);
        LoginEnvio modal = new LoginEnvio(usuariot, contraseñaa);
        Call<LoginEnvio> call = retrofitAPI.LoginUsuario(modal);
        call.enqueue(new Callback<LoginEnvio>() {
            @Override
            public void onResponse(Call<LoginEnvio> call, Response<LoginEnvio> response) {
                Intent intent = new Intent(MainActivity.this,Inicio.class);
                try {
                    if (response.isSuccessful()){
                        LoginEnvio responseFromAPI = response.body();
                        Map<String,String> datos = responseFromAPI.getData();
                        int rol = Integer.parseInt(datos.get("trol_id"));
                        intent.putExtra("nombre", datos.get("nombre"));
                        intent.putExtra("primer_apellido", datos.get("primer_apellido"));
                        intent.putExtra("segundo_apellido", datos.get("segundo_apellido"));
                        intent.putExtra("id", datos.get("id"));
                        intent.putExtra("compania", datos.get("tcompania_id"));
                        intent.putExtra("fotografia", datos.get("fotografia"));
                        intent.putExtra("rol", rol);
                        if (rol == 1){
                            Toast.makeText(MainActivity.this,"Lo sentimos debe inicar sesion en Web",Toast.LENGTH_SHORT).show();
                        } else if (rol == 2) {
                            Toast.makeText(MainActivity.this,"Bienvenido: "+ datos.get("nombre"),Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        } else if (rol == 3) {
                            Toast.makeText(MainActivity.this, "Bienvenido: " + datos.get("nombre"), Toast.LENGTH_SHORT).show();
                            //startActivity(intent);
                        }
                    }else{
                        Toast.makeText(MainActivity.this,"Usuario o contraseña incorrectos",Toast.LENGTH_SHORT).show();
                        usuario.setText("");
                        contraseña.setText("");
                    }
                }catch (Exception e){
                    Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<LoginEnvio> call, Throwable t) {
                Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}