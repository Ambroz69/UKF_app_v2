package com.example.dominik.ukf_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.webkit.WebView;
import android.widget.TextView;


public class ActivityPodmienkyPrijatia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podmienky_prijatia);

        WebView infoPodmienkyPrijatia = (WebView)findViewById(R.id.infoPodmienkyPrijatia);
        if(getIntent() != null)
        {
            String info = getIntent().getStringExtra("info");
            infoPodmienkyPrijatia.loadData(info,"text/html; charset=utf-8","utf-8");
        }
    }

}