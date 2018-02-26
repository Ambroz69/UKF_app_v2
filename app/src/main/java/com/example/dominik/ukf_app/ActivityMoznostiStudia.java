package com.example.dominik.ukf_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ActivityMoznostiStudia extends AppCompatActivity {

    GridLayout moznostiStudiaGrid;
    String[] tab;
    int tabCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moznosti_studia);

        moznostiStudiaGrid = (GridLayout) findViewById(R.id.moznostiStudiaGrid);
        tabCount = 10;
        tab = new String[tabCount];

        if(getIntent() != null) {
            for (int i = 0; i<tabCount; i++) {
                tab[i] = ""+i;
                tab[i] = getIntent().getStringExtra("tab"+i);
            }
        }

        setSingleEvent(moznostiStudiaGrid);
    }

    private void setSingleEvent(GridLayout mainGrid) {

        for (int i = 0; i < tabCount; i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            final int tmp = i;
            CardView cardView = (CardView) mainGrid.getChildAt(tmp);
            cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ActivityMoznostiStudia.this, ActivityMoznostiStudiaDetail.class);
                            intent.putExtra("info", tab[tmp]);
                            startActivity(intent);
                        }
             });

        }
    }
}
