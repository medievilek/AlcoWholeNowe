package com.medi.alcowhole;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddKryteriumActivity extends AppCompatActivity {


    TextView textViewAlkoholName, textViewRating;
    SeekBar seekBarRating;
    Spinner spinnerKryteria;

    Button buttonAddKryterium;

    ListView listViewKryteria;

    DatabaseReference databaseKryteria;

    List<Kryterium> kryteria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kryterium);

        textViewAlkoholName = (TextView) findViewById(R.id.textViewAlkoholName);
        seekBarRating = (SeekBar) findViewById(R.id.seekBarRating);
        textViewRating = (TextView) findViewById(R.id.textViewRating);
        buttonAddKryterium = (Button) findViewById(R.id.buttonAddKryterium);
        listViewKryteria = (ListView) findViewById(R.id.listViewKryteria);
        spinnerKryteria = (Spinner) findViewById(R.id.spinnerKryteria);

        Intent intent = getIntent();

        kryteria = new ArrayList<>();

        String id = intent.getStringExtra(Program.ALKOHOL_ID);
        String name = intent.getStringExtra(Program.ALKOHOL_NAME);

        textViewAlkoholName.setText(name);

        databaseKryteria = FirebaseDatabase.getInstance().getReference("kryteria").child(id);

        buttonAddKryterium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveKryterium();
            }
        });
        seekBarRating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                textViewRating.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        listViewKryteria.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        listViewKryteria.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                Kryterium kryterium = kryteria.get(i);

                showUpdateDialog(kryterium.getKryteriumId(), kryterium.getKryteriumName());

                return true;
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        databaseKryteria.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                kryteria.clear();

                for(DataSnapshot kryteriumSnapshot : dataSnapshot.getChildren()){
                    Kryterium kryterium = kryteriumSnapshot.getValue(Kryterium.class);
                    kryteria.add(kryterium);
                }

                KryteriumList kryteriumListAdapter = new KryteriumList(AddKryteriumActivity.this, kryteria);
                listViewKryteria.setAdapter(kryteriumListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void saveKryterium(){
        String kryteriumName = spinnerKryteria.getSelectedItem().toString();
        int rating = seekBarRating.getProgress();

        if(!TextUtils.isEmpty(kryteriumName)){
            String id = databaseKryteria.push().getKey();

            Kryterium kryterium = new Kryterium(id, kryteriumName, rating);

            databaseKryteria.child(id).setValue(kryterium);

            Toast.makeText(this, "Kryterium zapisane poprawnie", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Nazwa kryterium nie może pozostać pusta", Toast.LENGTH_SHORT).show();
        }
    }

    private void showUpdateDialog(final String kryteriumId, String kryteriumName){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_dialog_kryterium, null);

        dialogBuilder.setView(dialogView);


        final Spinner spinnerKryteria = (Spinner) dialogView.findViewById(R.id.spinnerKryteria);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdate);
        final SeekBar seekBarRating = (SeekBar) dialogView.findViewById(R.id.seekBarRating);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDelete);

        dialogBuilder.setTitle("Aktualizacja Kryterium: " + kryteriumName);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String kryteriumName = spinnerKryteria.getSelectedItem().toString();
                int rating = seekBarRating.getProgress();


                updateKryterium(kryteriumId, kryteriumName, rating);
                alertDialog.dismiss();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteKryterium(kryteriumId);
                alertDialog.dismiss();
            }
        });


    }

    private void deleteKryterium(String kryteriumId) {
        DatabaseReference drKryteria = FirebaseDatabase.getInstance().getReference().child(kryteriumId);

        drKryteria.child(kryteriumId).removeValue();

        Toast.makeText(this,"Kryterium zostało usunięte", Toast.LENGTH_LONG).show();

    }


    private boolean updateKryterium(String id, String name, int rating){

       // DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("kryteria").child(id);

        Kryterium kryterium = new Kryterium(id, name, rating);

        databaseKryteria.child(id).setValue(kryterium);

        Toast.makeText(this, "Nazwa kryterium zaktualizowana", Toast.LENGTH_LONG).show();

        return true;
    }

}
