package com.virtualclassroom.main.virtualclassroom;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

public class ResponseDetails extends AppCompatActivity {
TextView email,contents;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pref = getSharedPreferences("AppPref", MODE_PRIVATE);

       email=(TextView)findViewById(R.id.email);
        contents=(TextView)findViewById(R.id.contents);
        email.setText(pref.getString("email","Anonymous").trim());
        contents.setText(pref.getString("contents","").trim());

    }

}
