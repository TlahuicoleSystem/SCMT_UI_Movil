package com.example.scmt_ui_movil;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

public class Inicio extends AppCompatActivity {
    private NavigationView navView;
    private String nombreCompleto;
    private String compania;
    private String idUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        idUsuario = getIntent().getStringExtra("id");
        compania = getIntent().getStringExtra("compania");
        nombreCompleto = getIntent().getStringExtra("nombre") + " " + getIntent().getStringExtra("primer_apellido") + " " + getIntent().getStringExtra("segundo_apellido");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);//Nav superior
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(nombreCompleto);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navView = (NavigationView) findViewById(R.id.navView);//Nav lateral
        navView.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        navView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        ImageView imageView  = navView.getHeaderView(0).findViewById(R.id.imgView);
        TextView nombre = navView.getHeaderView(0).findViewById(R.id.textView2);
        nombre.setText(nombreCompleto);
        TextView rol = navView.getHeaderView(0).findViewById(R.id.textView3);
        rol.setText("Conductor");
        String imgPerfil = getIntent().getStringExtra("fotografia");
        Glide.with(getApplicationContext()).load(imgPerfil).into(imageView);
        menu();//Lamamos al metododo que da funcionalidad al menu
    }

    public void menu(){
        Intent perfil = new Intent(Inicio.this,Perfil.class);
        perfil.putExtra("nombreCompleto",nombreCompleto);
        perfil.putExtra("id",idUsuario);
        perfil.putExtra("compania",compania);
        perfil.putExtra("rol",getIntent().getStringExtra("rol"));
        Intent registrarIncidenias = new Intent(Inicio.this,Incidencias.class);
        registrarIncidenias.putExtra("id",idUsuario);
        registrarIncidenias.putExtra("nombreCompleto",nombreCompleto);
        Intent visualizarQR = new Intent(Inicio.this,Registrar_Asistencia.class);
        visualizarQR.putExtra("id",idUsuario);
        Intent acercaDe = new Intent(Inicio.this, acercade.class);
        navView = (NavigationView) findViewById(R.id.navView);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.op1:
                        System.out.println("Entro en el 1");
                        startActivity(perfil);
                        break;
                    case R.id.op2:
                        System.out.println("Entro en el 2");
                        startActivity(registrarIncidenias);
                        break;
                    case R.id.op3:
                        System.out.println("Entro en el 3");
                        startActivity(visualizarQR);
                        break;
                    case R.id.op4:
                        System.out.println("Entro en el 4");
                        startActivity(acercaDe);
                        break;
                }
                return false;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }
}