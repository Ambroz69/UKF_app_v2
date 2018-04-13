package com.example.dominik.ukf_app.moznosti_studia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;

import com.example.dominik.ukf_app.MainActivity;
import com.example.dominik.ukf_app.R;

/**
 * Created by Dominik on 14.03.2018.
 */

public class ActivityMoznostiStudiaStudijnyProgram extends AppCompatActivity {

    String obsah, detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moznosti_studia_studijny_program);

        WebView studijnyProgramObsah = (WebView)findViewById(R.id.moznostiStudiaStudijnyProgram);
        if(getIntent() != null)
        {
            detail = getIntent().getStringExtra("detail");
            obsah = getIntent().getStringExtra("obsah");
            studijnyProgramObsah.loadData(obsah,"text/html; charset=utf-8","utf-8");
        }
    }

    public void detailButton(View view) {
        Intent intent = new Intent(ActivityMoznostiStudiaStudijnyProgram.this,ActivityMoznostiStudiaDetail.class);
        intent.putExtra("detail", detail);
        startActivity(intent);
    }
}
