package com.example.android.conexionbd;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static android.R.attr.resource;

/**
 * Created by jlixerkun on 12/03/17.
 */

public class CursoAdapter extends ArrayAdapter<Curso> {

    public CursoAdapter(Activity context, ArrayList<Curso> cursos) {
        super(context, 0, cursos);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_curso, parent, false);
        }

        // Get the {@link Curso} object located at this position in the list
        Curso currentCurso = getItem(position);

        TextView cursoNombre = (TextView) listItemView.findViewById(R.id.nombre_curso_tv);

        cursoNombre.setText(currentCurso.getNombre());

        return listItemView;
    }
}
