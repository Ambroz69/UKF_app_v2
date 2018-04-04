package com.example.dominik.ukf_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Dominik on 04.04.2018.
 */

public class ActivityOrientaciaBudova extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orientacia_budova);

        TextView infoMiestnosti = (TextView)findViewById(R.id.infoMiestnosti);
        if(getIntent() != null) {
            String info = getIntent().getStringExtra("info");
            infoMiestnosti.setText(info);
        }
    }
}
