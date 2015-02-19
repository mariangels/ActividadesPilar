package com.example.mariangeles.actividades.fragmentos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mariangeles.actividades.Adaptador;
import com.example.mariangeles.actividades.ClienteRestFul;
import com.example.mariangeles.actividades.Editar;
import com.example.mariangeles.actividades.R;
import com.example.mariangeles.actividades.clases.Actividad;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

public class FragmentoLista extends Fragment {

    private ArrayList<Actividad> misActividades;
    private ListView lista;
    private Adaptador adaptador;
    private int idBorrar;

    public FragmentoLista() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View v= inflater.inflate(R.layout.fragmento_lista, container, false);
        lista = (ListView) v.findViewById(R.id.listView);
        initComponents();

        registerForContextMenu(lista);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Actividad act= misActividades.get((int) l);
                mCallbacks.onItemSelected(act);
            }
        });
        return v;
    }

    /******************* CONTEXT MENU **************************/

    private void initComponents(){
        misActividades = new ArrayList();
        new SelectActividades().execute();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.long_click, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int id=item.getItemId();
        Actividad act= (Actividad)lista.getItemAtPosition(info.position);
        switch (id){
            case R.id.borrar:
                act_borrar(act.getId());
                break;
            case R.id.editar:
                act_editar(act);
                break;
        }
        return super.onContextItemSelected(item);
    }

    public void act_editar(Actividad act){
        Intent i = new Intent(getActivity(),Editar.class);
        Bundle b=new Bundle();
        b.putParcelable("actividad", act);
        i.putExtras(b);
        startActivityForResult(i, 1);
    }

    public void act_borrar(int id){
        idBorrar = id;
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("¿Estas seguro?");
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View vista = inflater.inflate(R.layout.dialogo_borrar, null);
        alert.setView(vista);
        alert.setPositiveButton("borrar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        borrar();
                    }
                });
        alert.setNegativeButton("cancelar", null);
        alert.show();
    }

    public void borrar(){
        new DeleteActividad().execute();
        tostada("Elemento eliminado");
    }

    public void tostada(String s){
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    /************************  SELECT  *****************************/

    class SelectActividades extends AsyncTask<String, Integer, String>{

        String baseServidor="http://ieszv.x10.bz/restful/api/";

        @Override
        protected String doInBackground(String... strings) {
            return ClienteRestFul.get(baseServidor+"actividad/mariam");
        }

        @Override
        protected void onPostExecute(String cosas) {

            getActividades(cosas);
            adaptador=new Adaptador(getActivity(),R.layout.elemento,misActividades);
            lista.setAdapter(adaptador);
        }

        public void getActividades(String cosas){
            JSONTokener token = new JSONTokener(cosas);
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
                }
            } catch (JSONException e) {}
        }
    }

    /************************  DELETE  *****************************/

    class DeleteActividad extends AsyncTask<String, Integer, String>{

        String baseServidor="http://ieszv.x10.bz/restful/api/";

        @Override
        protected String doInBackground(String... param) {
            return ClienteRestFul.delete(baseServidor+"actividad/"+ idBorrar);
        }

        @Override
        protected void onPostExecute(String s) {
            tostada(s);
            adaptador.notifyDataSetChanged();
        }
    }

    /**************** CALLBACK ****************/

    private Callbacks mCallbacks = CallbacksVacios;

    public interface Callbacks {
        public void onItemSelected(Actividad id);
    }

    private static Callbacks CallbacksVacios = new Callbacks() {
        @Override
        public void onItemSelected(Actividad id) {
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Error: La actividad debe implementar el callback del fragmento");
        }
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = CallbacksVacios;
    }
}
