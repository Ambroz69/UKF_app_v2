package com.example.dominik.ukf_app;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;

import com.example.dominik.ukf_app.db_connect.StudijnyProgram;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.List;


public class ActivityMoznostiStudiaMenu extends AppCompatActivity {

    GridLayout moznostiStudiaGrid;
    private List<StudijnyProgram>studijnyProgramList;
    MaterialSearchView searchView;
    CardView[] karta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moznosti_studia_menu);

        moznostiStudiaGrid = (GridLayout) findViewById(R.id.moznostiStudiaGrid);
        studijnyProgramList = new ArrayList<StudijnyProgram>();
        studijnyProgramList = (ArrayList) getIntent().getParcelableArrayListExtra("studijne_programy");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setTitleTextColor(Color.BLACK);

        setSingleEvent(moznostiStudiaGrid);
        karta = new CardView[studijnyProgramList.size()];

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                setSingleEvent(moznostiStudiaGrid);
            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                setSingleEvent(moznostiStudiaGrid);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText!= null) {
                    for(int i = 0; i <studijnyProgramList.size(); i++) {
                        karta[i] = (CardView) moznostiStudiaGrid.getChildAt(i);
                        if (studijnyProgramList.get(i).getNazov().toLowerCase().contains(newText.toLowerCase()))
                            karta[i].setVisibility(View.VISIBLE);
                        else
                            karta[i].setVisibility(View.GONE);
                    }
                }
                return true;
            }


        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
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
