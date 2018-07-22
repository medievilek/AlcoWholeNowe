package com.medi.alcowhole;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class KryteriumList extends ArrayAdapter<Kryterium> {

    private Activity context;
    List<Kryterium> kryteria;

    public KryteriumList(Activity context, List<Kryterium> kryteria){

        super(context, R.layout.layout_kryterium_list, kryteria);
        this.context = context;
        this.kryteria = kryteria;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.layout_kryterium_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewRating = (TextView) listViewItem.findViewById(R.id.textViewRating);

        Kryterium kryterium = kryteria.get(position);

        textViewName.setText(kryterium.getKryteriumName());
        textViewRating.setText(String.valueOf(kryterium.getKryteriumRating()));

        return listViewItem;
    }
}
