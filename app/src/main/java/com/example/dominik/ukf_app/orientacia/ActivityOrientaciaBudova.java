package com.example.dominik.ukf_app.orientacia;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.dominik.ukf_app.R;

/**
 * Created by Dominik on 04.04.2018.
 */

public class ActivityOrientaciaBudova extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orientacia_budova);

        TextView infoMiestnosti1 = (TextView)findViewById(R.id.infoMiestnosti1);
        TextView infoMiestnosti2 = (TextView)findViewById(R.id.infoMiestnosti2);
        if(getIntent() != null) {
            String info1 = getIntent().getStringExtra("info1");
            infoMiestnosti1.setText(info1);
            String info2 = getIntent().getStringExtra("info2");
            infoMiestnosti2.setText(info2);
        }
    }
}
