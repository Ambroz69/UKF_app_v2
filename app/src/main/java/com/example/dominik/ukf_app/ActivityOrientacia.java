package com.example.dominik.ukf_app;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class ActivityOrientacia extends AppCompatActivity {
    private double hlavnaBudovaLong, hlavnaBudovaLat, katedraBotanikyLong, katedraBotanikyLat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orientacia);

        hlavnaBudovaLong = 18.091037;
        hlavnaBudovaLat = 48.308223;
        katedraBotanikyLong = 18.097379;
        katedraBotanikyLat = 48.300187;

        TextView infoMesto = (TextView) findViewById(R.id.infoMesto);
        TextView infoBudova = (TextView)findViewById(R.id.infoBudova);
        if(getIntent() != null) {
            String infoM = getIntent().getStringExtra("mesto");
            infoMesto.setText(infoM);
            String infoB = getIntent().getStringExtra("budova");
            infoBudova.setText(infoB);
        }
    }

    public void mapsButtonHlavnaBudova(View view) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q="+hlavnaBudovaLat+","+hlavnaBudovaLong);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    public void mapsButtonKatedraBotaniky(View view) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q="+katedraBotanikyLat+","+katedraBotanikyLong);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    public void detailBudovyButton(View view) {
        Intent intent = new Intent(ActivityOrientacia.this,ActivityOrientaciaBudova.class);
        intent.putExtra("info", "Tu bude mapa budovy a vysvetlenie");
        startActivity(intent);
    }

}