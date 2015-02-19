package com.example.mariangeles.actividades;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mariangeles.actividades.clases.Actividad;
import com.example.mariangeles.actividades.clases.Grupo;
import com.example.mariangeles.actividades.clases.Profesor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Calendar;

public class Editar extends FragmentActivity {

    private String baseServidor="http://ieszv.x10.bz/restful/api/";

    private ArrayList<Profesor> profesores;
    private ArrayList<Grupo> grupos;
    private ArrayList<String> nomProfesor;
    private ArrayList<String> nomGrupo;
    private ArrayList<String> nomActividad;
    private Actividad act;

    private int idActividad;
    private int idGrupo;

    private Spinner spProfesor;
    private Spinner spGrupo;
    private EditText descripcion;

    private EditText lugarSalida;
    private static TextView horaSalida;
    private static TextView fechaSalida;

    private EditText lugarLlegada;
    private static TextView horaLlegada;
    private static TextView fechaLlegada;

    private RadioGroup tipo;
    private RadioButton complementaria;
    private RadioButton extraescolar;

    private static boolean salida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edicion);
        initComponents();
    }

    private void initComponents() {
        grupos = new ArrayList();
        profesores = new ArrayList();
        nomProfesor = new ArrayList<String>();
        nomGrupo = new ArrayList<String>();
        nomActividad = new ArrayList<String>();
        spProfesor = (Spinner) findViewById(R.id.spProfesor);
        spGrupo = (Spinner) findViewById(R.id.spGrupo);
        new SelectGrupos().execute();
        new SelectProfesores().execute();
        new SelectActividades().execute();

        descripcion = (EditText)findViewById(R.id.etDescripcion);
        tipo = (RadioGroup)findViewById(R.id.tipo);
        complementaria = (RadioButton)findViewById(R.id.rbComplementaria);
        extraescolar = (RadioButton)findViewById(R.id.rbExtraescolar);
        lugarSalida = (EditText)findViewById(R.id.etLugarSalida);
        horaSalida = (TextView)findViewById(R.id.etHoraSalida);
        fechaSalida = (TextView)findViewById(R.id.tvFechaSalida);

        lugarLlegada = (EditText)findViewById(R.id.etLugarLlegada);
        horaLlegada = (TextView)findViewById(R.id.etHoraLlegada);
        fechaLlegada = (TextView)findViewById(R.id.tvFechaLlegada);
    }

    /******************** botones *********************/

    public void guardar(View v){

        int idProfesor = profesores.get(nomProfesor.indexOf(spProfesor.getSelectedItem())).getId();
        idGrupo = grupos.get(nomGrupo.indexOf(spGrupo.getSelectedItem())).getId();

        act = new Actividad(0,
                idProfesor,
                getTipo(),
                fechaSalida.getText().toString() +" "+ horaSalida.getText().toString(),
                fechaLlegada.getText().toString() +" "+ horaLlegada.getText().toString(),
                lugarSalida.getText().toString(),
                lugarLlegada.getText().toString(),
                descripcion.getText().toString(),
                "mariam"
                );

        if(unica()) {
            new InsertActividad().execute();
            finish();
        }else
            Toast.makeText(getApplicationContext(), "Esta descripcion ya existe", Toast.LENGTH_SHORT).show();
    }

    private String getTipo() {
        if(tipo.getCheckedRadioButtonId() == complementaria.getId())
            return "complementaria";
        else{
            return "extraescolar";
        }
    }

    private boolean unica(){
        String s = descripcion.getText().toString();
        for (int i=0; i<nomActividad.size(); i++){
            if(nomActividad.get(i).equalsIgnoreCase(s))
                return false;
        }
        return true;
    }

    public void complementaria(View v){
        lugarLlegada.setEnabled(false);
        horaLlegada.setEnabled(false);
        fechaLlegada.setEnabled(false);
        extraescolar.setChecked(false);
    }

    public void extraescolar(View v){
        lugarLlegada.setEnabled(true);
        horaLlegada.setEnabled(true);
        fechaLlegada.setEnabled(true);
        complementaria.setChecked(false);
    }

    /************************ INSERT  ******************************/

    class InsertActividad extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... strings) {
            return ClienteRestFul.post(baseServidor+"actividad", act.getJson());
        }

        @Override
        protected void onPostExecute(String s) {
            idActividad = Integer.parseInt(s.substring(s.indexOf(":")+1,s.indexOf("}")));
            Log.v("idActividad", s + " ID: " +idActividad);
            new InsertActividadGrupo().execute();
            Toast.makeText(getApplicationContext(), s + " ID: " +idActividad, Toast.LENGTH_SHORT).show();
        }

        class InsertActividadGrupo extends AsyncTask<String, Integer, String>{

            @Override
            protected String doInBackground(String... strings) {
                //{"idactividad":"1", "idgrupo":"1"}
                JSONObject objetoJSON = new JSONObject();
                try {
                    objetoJSON.put("idactividad", idActividad);
                    objetoJSON.put("idgrupo", idGrupo);
                } catch (JSONException e) {
                }
                return ClienteRestFul.post(baseServidor+"actividadgrupo", objetoJSON);
            }

            @Override
            protected void onPostExecute(String s) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }
        }
    }


    /************************  SELECT  *****************************/

    class SelectProfesores extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return ClienteRestFul.get(baseServidor+"profesor");
        }

        @Override
        protected void onPostExecute(String s) {
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
                    nomProfesor.add(fila.getString("nombre") + " " + fila.getString("apellidos"));
                }
            } catch (JSONException e) {}

            ArrayAdapter<String> adapterP = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, nomProfesor);
            spProfesor.setAdapter(adapterP);
        }
    }

    class SelectGrupos extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return ClienteRestFul.get(baseServidor+"grupo");
        }

        @Override
        protected void onPostExecute(String s) {

            JSONTokener token = new JSONTokener(s);
            try {
                JSONArray array= new JSONArray(token);
                for(int i=0;i<array.length(); i++){
                    JSONObject fila = array.getJSONObject(i);
                    grupos.add( new Grupo(
                            fila.getInt("id"),
                            fila.getString("grupo")
                    ));
                    nomGrupo.add(fila.getString("grupo"));
                }
            } catch (JSONException e) {}

            ArrayAdapter<String> adapterG = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, nomGrupo);
            spGrupo.setAdapter(adapterG);
        }
    }

    class SelectActividades extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return ClienteRestFul.get(baseServidor+"actividad/mariam");
        }

        @Override
        protected void onPostExecute(String s) {
            JSONTokener token = new JSONTokener(s);
            try {
                JSONArray array= new JSONArray(token);
                for(int i=0;i<array.length(); i++){
                    JSONObject fila = array.getJSONObject(i);
                    nomActividad.add(fila.getString("descripcion"));
                }
            } catch (JSONException e) {}
        }
    }


    /*******************  TimePickerFragment ***************************/

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            if (salida == true)
                horaSalida.setText("Hora: "+hourOfDay + ":" + minute);
            else
                horaLlegada.setText("Hora: "+hourOfDay + ":" + minute);
        }
    }

    public void setHoraSalida(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "Hora");
        salida = true;
    }

    public void setHoraLlegada(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "Hora");
        salida = false;
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            if (salida == true)
                fechaSalida.setText("Fecha: "+year + "-" + month + "-" + day);
            else
                fechaLlegada.setText("Fecha: "+year + "-" + month + "-" + day);

        }
    }

    public void setFechaSalida(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "Fecha");
        salida = true;
    }

    public void setFechaLlegada(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "Fecha");
        salida = false;
    }
}

