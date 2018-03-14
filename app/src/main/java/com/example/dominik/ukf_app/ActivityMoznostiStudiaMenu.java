package com.example.dominik.ukf_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.GridLayout;

import com.example.dominik.ukf_app.db_connect.StudijnyProgram;

import java.util.ArrayList;
import java.util.List;


public class ActivityMoznostiStudiaMenu extends AppCompatActivity {

    GridLayout moznostiStudiaGrid;
    private List<StudijnyProgram>studijnyProgramList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moznosti_studia_menu);

        moznostiStudiaGrid = (GridLayout) findViewById(R.id.moznostiStudiaGrid);
        studijnyProgramList = new ArrayList<StudijnyProgram>();
        studijnyProgramList = (ArrayList) getIntent().getParcelableArrayListExtra("studijne_programy");

        setSingleEvent(moznostiStudiaGrid);
    }

    private void setSingleEvent(GridLayout mainGrid) {

        for (int i = 0; i < studijnyProgramList.size(); i++) {
            final int tmp = i;
            CardView cardView = (CardView) mainGrid.getChildAt(tmp);
            cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ActivityMoznostiStudiaMenu.this, ActivityMoznostiStudiaStudijnyProgram.class);
                            intent.putExtra("obsah", studijnyProgramList.get(tmp).getObsah());
                            intent.putExtra("detail",studijnyProgramList.get(tmp).getDetail());
                            startActivity(intent);
                        }
             });

        }
    }
}
