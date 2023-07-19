package com.example.scmt_ui_movil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
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
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    EditText usuario;
    EditText contraseña;
    Button entrar;
    Button huella;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usuario = findViewById(R.id.cTUsuario);
        contraseña = findViewById(R.id.cTContrasenia);
        entrar = findViewById(R.id.btniniciar_sesion);

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(MainActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                        "Error de Autenticacion: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                iniciarAutenticacionBiometrica();
                Toast.makeText(getApplicationContext(),
                        "Autenticacion correcta", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(),
                        "Falla en la autenticacion", Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Inicio de sesion por huella dactilar")
                .setNegativeButtonText("Usa la contraseña de tu cuenta")
                .build();

        huella = findViewById(R.id.btnhuella);
        huella.setOnClickListener(view -> {
            biometricPrompt.authenticate(promptInfo);
        });

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("¿Desea guardar y recordar los datos de inicio de sesion?")
                        .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String usuarioSave = usuario.getText().toString();
                                String contraseniaSave = contraseña.getText().toString();

                                if (usuarioSave.isEmpty() && contraseniaSave.isEmpty()){
                                    Toast.makeText(MainActivity.this, "Por favor ingrese los datos", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("username", usuarioSave);
                                editor.putString("password", contraseniaSave);
                                editor.apply();

                                postData(usuarioSave, contraseniaSave);
                            }
                        })
                        .setNegativeButton("No recordar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String usuarioSave = usuario.getText().toString();
                                String contraseniaSave = contraseña.getText().toString();

                                if (usuarioSave.isEmpty() && contraseniaSave.isEmpty()){
                                    Toast.makeText(MainActivity.this, "Por favor ingrese los datos", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                postData(usuarioSave, contraseniaSave);
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void iniciarAutenticacionBiometrica(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString("username", "");
        String savedPassword = sharedPreferences.getString("username", "");

        if (!savedUsername.isEmpty() && !savedPassword.isEmpty()){
            postData(savedUsername, savedPassword);
        }else{
            Toast.makeText(getApplicationContext(), "No se encontraron datos de inicio de sesion guardados", Toast.LENGTH_SHORT).show();
        }
    }

    private void postData(String usuariot, String contraseñaa) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://scmtapis.azurewebsites.net/").addConverterFactory(GsonConverterFactory.create()).build();
        LoginAPI retrofitAPI = retrofit.create(LoginAPI.class);
        LoginEnvio modal = new LoginEnvio(usuariot, contraseñaa);
        Call<LoginEnvio> call = retrofitAPI.LoginUsuario(modal);
        call.enqueue(new Callback<LoginEnvio>() {
            @Override
            public void onResponse(Call<LoginEnvio> call, Response<LoginEnvio> response) {
                Intent intent = new Intent(MainActivity.this,Inicio.class);
                Intent pasajero = new Intent(MainActivity.this,inicioPasajero.class);
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
                        intent.putExtra("rol", datos.get("trol_id"));

                        pasajero.putExtra("nombre", datos.get("nombre"));
                        pasajero.putExtra("primer_apellido", datos.get("primer_apellido"));
                        pasajero.putExtra("segundo_apellido", datos.get("segundo_apellido"));
                        pasajero.putExtra("id", datos.get("id"));
                        pasajero.putExtra("compania", datos.get("tcompania_id"));
                        pasajero.putExtra("fotografia", datos.get("fotografia"));
                        pasajero.putExtra("rol", datos.get("trol_id"));
                        if (rol == 1){
                            Toast.makeText(MainActivity.this,"Lo sentimos debe inicar sesion en Web",Toast.LENGTH_SHORT).show();
                        } else if (rol == 2) {
                            Toast.makeText(MainActivity.this,"Bienvenido: "+ datos.get("nombre"),Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        } else if (rol == 3) {
                            Toast.makeText(MainActivity.this, "Bienvenido: " + datos.get("nombre"), Toast.LENGTH_SHORT).show();
                            startActivity(pasajero);
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