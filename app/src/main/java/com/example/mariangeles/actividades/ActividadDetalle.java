package com.example.mariangeles.actividades;

import android.os.Bundle;
import android.content.Intent;
import android.view.MenuItem;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;

import com.example.mariangeles.actividades.fragmentos.FragmentoDetalle;

public class ActividadDetalle extends FragmentActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_detalle);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            Bundle b = new Bundle();
            b.putParcelable("actividad", getIntent().getParcelableExtra("actividad"));
            FragmentoDetalle fragment = new FragmentoDetalle();
            fragment.setArguments(b);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentoDetalle,fragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpTo(this, new Intent(this, ActividadLista.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
