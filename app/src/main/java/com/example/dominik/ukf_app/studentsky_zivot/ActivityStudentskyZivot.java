package com.example.dominik.ukf_app.studentsky_zivot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.GridLayout;

import com.example.dominik.ukf_app.R;


public class ActivityStudentskyZivot extends AppCompatActivity {

    GridLayout studentskyZivotGrid;
    String[] tab;
    int tabCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentsky_zivot);

        studentskyZivotGrid = (GridLayout) findViewById(R.id.studentskyZivotGrid);
        tabCount = 11;
        tab = new String[tabCount];

        if(getIntent() != null) {
            for (int i = 0; i < tabCount; i++) {
                tab[i] = ""+i;
                tab[i] = getIntent().getStringExtra("detailInfo"+i);
            }
        }

        setSingleEvent(studentskyZivotGrid);
    }

    private void setSingleEvent(GridLayout mainGrid) {

        for (int i = 0; i < tabCount; i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            final int tmp = i;
            CardView cardView = (CardView) mainGrid.getChildAt(tmp);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActivityStudentskyZivot.this, ActivityStudentskyZivotDetail.class);
                    intent.putExtra("info", tab[tmp]);
                    startActivity(intent);
                }
            });

        }
    }

}
