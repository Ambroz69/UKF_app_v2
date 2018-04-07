package com.example.dominik.ukf_app.studentsky_zivot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.dominik.ukf_app.R;

/**
 * Created by Dominik on 06.03.2018.
 */

public class ActivityStudentskyZivotDetail extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentsky_zivot_detail);


        WebView moznostiStudiaDetail = (WebView)findViewById(R.id.studentskyZivotDetail);
        if(getIntent() != null)
        {
            String info = getIntent().getStringExtra("info");
            moznostiStudiaDetail.loadDataWithBaseURL("", info, "text/html","utf-8", "");
        }

    }
}
