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

public class AlkoholList extends ArrayAdapter<Alkohol> {

    private Activity context;
    List<Alkohol> alkohols;

    public AlkoholList(Activity context, List<Alkohol> alkohols){

        super(context, R.layout.layout_alkohol_list, alkohols);
        this.context = context;
        this.alkohols = alkohols;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.layout_alkohol_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewGenre = (TextView) listViewItem.findViewById(R.id.textViewGenre);

        Alkohol alkohol = alkohols.get(position);

        textViewName.setText(alkohol.getAlkoholName());
        textViewGenre.setText(alkohol.getAlkoholGenre());

        return listViewItem;
    }
}
