package com.example.mariangeles.actividades;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mariangeles.actividades.clases.Actividad;

import java.util.ArrayList;

public class Adaptador extends ArrayAdapter<Actividad> {

    private Context contexto;
    private ArrayList<Actividad> lista;
    private int recurso;

    private static LayoutInflater i;                                                                //Para que no lo cree cada vez que llama al GetView()

    public static class ViewHolder{
        public TextView tv1;
        public TextView tv2;
        public ImageView iv;
        public int posicion;
    }

    public Adaptador(Context context, int resource, ArrayList<Actividad> objects) {
        super(context, resource, objects);
        this.contexto = context;
        this.lista = objects;
        this.recurso = resource;

        this.i = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);       //CREAMOS INFLADOR
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh = null;
        if(convertView == null){
            convertView = i.inflate(recurso, null);
            vh = new ViewHolder();
            vh.tv1 = (TextView)convertView.findViewById(R.id.tv1);
            vh.tv2 = (TextView)convertView.findViewById(R.id.tv2);
            vh.iv = (ImageView)convertView.findViewById(R.id.iv);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tv1.setText(lista.get(position).getDescripcion());
        vh.tv2.setText(lista.get(position).getFechai());

        if(lista.get(position).getTipo().equals("extraescolar"))
            vh.iv.setImageResource(R.drawable.nube);
        else
            vh.iv.setImageResource(R.drawable.casa);
        vh.posicion = position;
        return convertView;
    }
}
