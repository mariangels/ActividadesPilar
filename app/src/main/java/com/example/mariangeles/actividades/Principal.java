package com.example.mariangeles.actividades;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mariangeles.actividades.clases.Actividad;
import com.example.mariangeles.actividades.clases.Grupo;
import com.example.mariangeles.actividades.clases.Profesor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

public class Principal extends Activity {

    private ArrayList<Actividad> misActividades;
    private ArrayList<Profesor> profesores;
    private ArrayList<Grupo> grupos;
    private ListView lista;
    private Adaptador adaptador;
    private int idGrupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad);
        initComponents();
    }

    private void initComponents(){
        misActividades = new ArrayList();
        grupos = new ArrayList();
        profesores = new ArrayList();
        lista = (ListView)findViewById(R.id.listView);
        new Descarga().execute();
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

    /************************  SELECT  *****************************/

    class Descarga extends AsyncTask<String, Integer, String[]>{

        String baseServidor="http://ieszv.x10.bz/restful/api/";

        @Override
        protected String[] doInBackground(String... strings) {
            String[] cosas = new String[3];
            cosas[0] = ClienteRestFul.get(baseServidor+"actividad/mariam");
            cosas[1] = ClienteRestFul.get(baseServidor+"profesor");
            cosas[2] = ClienteRestFul.get(baseServidor+"grupo");
            return cosas;
        }

        @Override
        protected void onPostExecute(String[] cosas) {
            getProfesores(cosas[1]);
            getGrupos(cosas[2]);

            getActividades(cosas);
            adaptador=new Adaptador(getApplicationContext(),R.layout.elemento,misActividades);
            lista.setAdapter(adaptador);
        }

        public void getActividades(String[] cosas){
            JSONTokener token = new JSONTokener(cosas[0]);
            try {
                //JSONObject objeto= new JSONObject(token);
                //o
                JSONArray array= new JSONArray(token);
                for(int i=0;i<array.length(); i++){
                    JSONObject fila = array.getJSONObject(i);
                    misActividades.add( new Actividad(
                            fila.getInt("id"),
                            fila.getInt("idprofesor"),
                            fila.getString("tipo"),
                            fila.getString("fechai"),
                            fila.getString("fechaf"),
                            fila.getString("lugari"),
                            fila.getString("lugarf"),
                            fila.getString("descripcion"),
                            fila.getString("alumno")
                    ));
                    //getGrupo(fila.getInt("id"))
                }
            } catch (JSONException e) {}
        }

        public void getGrupos(String s){
            JSONTokener token = new JSONTokener(s);
            try {
                JSONArray array= new JSONArray(token);
                for(int i=0;i<array.length(); i++){
                    JSONObject fila = array.getJSONObject(i);
                    grupos.add( new Grupo(
                            fila.getInt("id"),
                            fila.getString("grupo")
                    ));
                }
            } catch (JSONException e) {}
        }

        public void getProfesores(String s){
            JSONTokener token = new JSONTokener(s);
            try {
                JSONArray array= new JSONArray(token);
                for(int i=0;i<array.length(); i++){
                    JSONObject fila = array.getJSONObject(i);
                    profesores.add( new Profesor(
                            fila.getInt("id"),
                            fila.getString("nombre"),
                            fila.getString("apellidos"),
                            fila.getString("departamento")
                    ));
                }
            } catch (JSONException e) {}
        }

        public String getGrupo(int id){
            // a la nueva hebra le pasamos el id de la actividad
            new ObtenerGrupo().execute(id);
            for(int j=0;j<grupos.size(); j++ ){
                if(grupos.get(j).getId() == idGrupo){
                    return grupos.get(j).getGrupo();
                }
            }
            return "grupo no encontrado";
        }

        class ObtenerGrupo extends AsyncTask<Integer, Integer, String>{

            @Override
            protected String doInBackground(Integer... param) {
                return ClienteRestFul.get(baseServidor+"actividadgrupo/"+ param);
            }

            @Override
            protected void onPostExecute(String s) {
                Toast.makeText(Principal.this, s, Toast.LENGTH_SHORT).show();

                JSONTokener token = new JSONTokener(s);
                try {
                    JSONObject objeto= new JSONObject(token);
                    idGrupo = objeto.getInt("idGrupo");
                } catch (JSONException e) {}
            }
        }
    }
}
