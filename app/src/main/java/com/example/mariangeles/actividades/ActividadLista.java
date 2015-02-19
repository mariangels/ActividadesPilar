package com.example.mariangeles.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.FragmentActivity;

import com.example.mariangeles.actividades.clases.Actividad;
import com.example.mariangeles.actividades.fragmentos.FragmentoDetalle;
import com.example.mariangeles.actividades.fragmentos.FragmentoLista;

public class ActividadLista extends FragmentActivity implements FragmentoLista.Callbacks{

    private boolean dosFragmentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponents();
    }

    private void initComponents(){
        setContentView(R.layout.actividad_lista);
        if (findViewById(R.id.fragmentoDetalle) != null) {
            dosFragmentos = true;
        }else{
            dosFragmentos = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.editar) {
            Intent i = new Intent(this,Editar.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Actividad actividad) {
        if (dosFragmentos) {
            FragmentoDetalle fragmento= (FragmentoDetalle) getSupportFragmentManager().findFragmentById(R.id.fragmentoDetalle);
            if (fragmento != null && fragmento.isInLayout()) {
                fragmento.mostrar(actividad);
            }

        }else{
            Intent i = new Intent(this, ActividadDetalle.class);
            i.putExtra("actividad", actividad);
            startActivity(i);
        }
    }
}

