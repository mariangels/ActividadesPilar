package com.example.mariangeles.actividades.fragmentos;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mariangeles.actividades.ClienteRestFul;
import com.example.mariangeles.actividades.R;
import com.example.mariangeles.actividades.clases.Actividad;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class FragmentoDetalle extends Fragment {

    String baseServidor="http://ieszv.x10.bz/restful/api/";

    private ImageView img;
    private TextView tvDescripcion;
    private TextView tvSalir;
    private TextView tvLlegar;
    private TextView tvProfesor;
    private TextView tvGrupo;
    private TextView tvDepartamento;
    private Actividad act;

    private int idProf;
    private int idAct;
    private int idGru;

    public FragmentoDetalle() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmento_detalle, container, false);
        tvDescripcion  = (TextView) v.findViewById(R.id.tvDescripcion);
        tvSalir  = (TextView) v.findViewById(R.id.tvSalir);
        tvLlegar  = (TextView) v.findViewById(R.id.tvLlegar);
        tvProfesor  = (TextView) v.findViewById(R.id.tvProfesor);
        tvGrupo  = (TextView) v.findViewById(R.id.tvGrupo);
        tvDepartamento  = (TextView) v.findViewById(R.id.tvDepartamento);
        img = (ImageView) v.findViewById(R.id.imgView);

        Bundle b = getActivity().getIntent().getExtras();
        if(b!= null) {
            act = b.getParcelable("actividad");
            mostrar(act);
        }else{
            tvDescripcion.setText("");
            tvSalir.setText("");
            tvLlegar.setText("");
            tvProfesor.setText("");
            tvGrupo.setText("");
            tvDepartamento.setText("");
        }

        return v;
    }

    public void mostrar(Actividad act) {
        tvDescripcion.setText(act.getDescripcion());
        tvSalir.setText(
                "SALIDA: \n" +
                "           Hora: " + act.getFechai() + "\n" +
                "           Lugar: " + act.getLugari()
        );

        tvLlegar.setText(
                "LLEGADA: \n"+
                "           Hora: " + act.getFechai() + "\n" +
                "           Lugar: " + act.getLugarf()
        );
        tvGrupo.setText("GRUPO: No hay");
        idAct=act.getId();
        idProf=act.getIdProfesor();
        new SelectProfesor().execute();
        new SelectActividadGrupo().execute();

        if(act.getTipo().equals("extraescolar"))
            img.setImageResource(R.drawable.nube);
        else
            img.setImageResource(R.drawable.casa);
    }

    /************************  SELECT  *****************************/

    class SelectProfesor extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... param) {
            return ClienteRestFul.get(baseServidor + "profesor/"+ idProf);
        }

        @Override
        protected void onPostExecute(String s) {
            JSONTokener token = new JSONTokener(s);
            try {
                JSONObject objeto= new JSONObject(token);
                tvProfesor.setText("PROFESOR: "+ objeto.getString("nombre") +" "+ objeto.getString("apellidos"));
                tvDepartamento.setText("DEPARTAMENTO: "+ objeto.getString("departamento") );
            } catch (JSONException e) {}
        }
    }

    class SelectActividadGrupo extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... param) {
            return ClienteRestFul.get(baseServidor + "actividadgrupo/"+ idAct);
        }

        @Override
        protected void onPostExecute(String s) {
            JSONTokener token = new JSONTokener(s);
            try {
                //JSONObject objeto= new JSONObject(token);
                //o
                JSONArray array= new JSONArray(token);
                for(int i=0;i<array.length(); i++){
                    JSONObject fila = array.getJSONObject(i);
                    idGru=fila.getInt("idgrupo");
                    new SelectGrupo().execute();
                }
            } catch (JSONException e) {}
        }

        class SelectGrupo extends AsyncTask<Integer, Integer, String> {

            @Override
            protected String doInBackground(Integer... param) {
                return ClienteRestFul.get(baseServidor + "grupo/"+ idGru);
            }

            @Override
            protected void onPostExecute(String s) {
                JSONTokener token = new JSONTokener(s);
                try {
                    JSONObject objeto= new JSONObject(token);
                    tvGrupo.setText("GRUPO: "+objeto.getString("grupo"));
                } catch (JSONException e) {}
            }
        }
    }
}
