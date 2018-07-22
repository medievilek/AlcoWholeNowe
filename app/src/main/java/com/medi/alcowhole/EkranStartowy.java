package com.medi.alcowhole;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class EkranStartowy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ekran_startowy);

        Button buttonOProgramie = (Button)findViewById(R.id.buttonOProgramie);
        Button buttonOcenPiwo = (Button)findViewById(R.id.buttonOcenPiwo);
        Button buttonDoUstalenia = (Button)findViewById(R.id.buttonDoUstalenia);

        buttonOProgramie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(EkranStartowy.this,ActivitySplash.class); // zamienić to potem na utworzonoą klasę oNas.class <-- stworzyć
                startActivity(intent);
            }
        });
        buttonOcenPiwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(EkranStartowy.this,Program.class);
                startActivity(intent);
            }
        });
        buttonDoUstalenia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(EkranStartowy.this,Program.class); // zamienić to potem na utworzonoą klasę jakasDodatkowaOpcja.class <-- stworzyć
                startActivity(intent);
            }
        });



    }
}

