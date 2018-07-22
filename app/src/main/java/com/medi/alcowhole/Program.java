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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Program extends AppCompatActivity {

    public static final String ALKOHOL_NAME = "alkoholname";
    public static final String ALKOHOL_ID = "alkoholid";

    EditText editTextName;
    Button buttonAddAlkohol;
    Spinner spinnerGenres;

    DatabaseReference databaseAlkohols;

    ListView listViewAlkohols;

    List<Alkohol> alkohols;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.program);

        databaseAlkohols = FirebaseDatabase.getInstance().getReference("alkohole");

        //getting values form xml by id
        editTextName = (EditText) findViewById(R.id.editTextName);
        spinnerGenres = (Spinner) findViewById(R.id.spinnerGenres);
        listViewAlkohols = (ListView) findViewById(R.id.listViewAlkohols);

        buttonAddAlkohol = (Button) findViewById(R.id.buttonAddAlkohol);

        alkohols = new ArrayList<>();
        buttonAddAlkohol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calling the method addAlkohol()
                //the method is defined below
                //this method is actually performing the write operation
                addAlkohol();
            }
        });

        listViewAlkohols.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Alkohol alkohol = alkohols.get(i);

                Intent intent = new Intent(getApplicationContext(), AddKryteriumActivity.class);

                intent.putExtra(ALKOHOL_ID, alkohol.getAlkoholId());
                intent.putExtra(ALKOHOL_NAME, alkohol.getAlkoholName());

                startActivity(intent);
            }
        });

        listViewAlkohols.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                Alkohol alkohol = alkohols.get(i);

                showUpdateDialog(alkohol.getAlkoholId(), alkohol.getAlkoholName());

                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseAlkohols.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                alkohols.clear();

                for(DataSnapshot alkoholSnapshot : dataSnapshot.getChildren()){
                    Alkohol alkohol = alkoholSnapshot.getValue(Alkohol.class);

                    alkohols.add(alkohol);
                }

                AlkoholList adapter = new AlkoholList(Program.this, alkohols);
                listViewAlkohols.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showUpdateDialog(final String alkoholId, String alkoholName){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_dialog_alkohol, null);

        dialogBuilder.setView(dialogView);


        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdate);
        final Spinner spinnerGenres = (Spinner) dialogView.findViewById(R.id.spinnerGenres);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDelete);

        dialogBuilder.setTitle("Aktualizacja Piwa: " + alkoholName);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String genre = spinnerGenres.getSelectedItem().toString();

                if(TextUtils.isEmpty(name)){
                    editTextName.setError("Musisz podać nazwę");
                    return;
                }

                updateAlkohol(alkoholId, name, genre);
                alertDialog.dismiss();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAlkohol(alkoholId);
                alertDialog.dismiss();
            }
        });


    }

    private void deleteAlkohol(String alkoholId) {
        DatabaseReference drAlkohol = FirebaseDatabase.getInstance().getReference("alkohole").child(alkoholId);
        DatabaseReference drKryteria = FirebaseDatabase.getInstance().getReference("kryteria").child(alkoholId);

        drAlkohol.removeValue();
        drKryteria.removeValue();

        Toast.makeText(this,"Piwo zostało usunięte", Toast.LENGTH_LONG).show();

    }

    private boolean updateAlkohol(String id, String name, String genre){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("alkohole").child(id);

        Alkohol alkohol = new Alkohol(id, name, genre);

        databaseReference.setValue(alkohol);

        Toast.makeText(this, "Nazwa piwa zaktualizowana", Toast.LENGTH_LONG).show();

        return true;
    }

    private void addAlkohol(){
        String name = editTextName.getText().toString().trim(); //Zadaniem metody trim jest pozbycie się wszystkich białych znaków na początku i końcu stringa.
        String genre = spinnerGenres.getSelectedItem().toString();

        if (!TextUtils.isEmpty(name)) { //Jeśli wprowadzono nazwę alkoholu, zostanie nadany unikatowy ID oraz wyswietli toat/info że utworzono alcohol. Jeśli nazwa nie została wprowadzona, wyskoczy powiadomienie.

           String id =  databaseAlkohols.push().getKey(); //push kieruje na górę stosu

            Alkohol alkohol = new Alkohol(id, name, genre); // tworzenie objektu alkoholu

            databaseAlkohols.child(id).setValue(alkohol); // zapisywanie alkoholu

            editTextName.setText("");

            Toast.makeText(this, "Piwo zostało dodane", Toast.LENGTH_LONG).show(); // wyświetlenie że piwo zostało dodane

        }else{

            Toast.makeText(this, "Nazwij piwo przed jego wprowadzeniem", Toast.LENGTH_SHORT).show();
        }



    }
}
