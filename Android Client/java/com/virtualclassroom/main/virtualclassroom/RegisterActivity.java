package com.virtualclassroom.main.virtualclassroom;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.virtualclassroom.main.ServerRequest;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
public class RegisterActivity extends AppCompatActivity {
    EditText email, password, name, branch;
    Button login, register;
    String emailtxt, passwordtxt, nametxt, branchtxt, k;
    List<NameValuePair> params;
    SharedPreferences pref;
    ProgressDialog progressDialog;
    String jsonstr;
 TextView error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        if (b != null)
            k = (String) b.get("name");
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        name = (EditText) findViewById(R.id.name);
        branch = (EditText) findViewById(R.id.branch);
        error=(TextView)findViewById(R.id.error);
        register = (Button) findViewById(R.id.registerbtn);
        login = (Button) findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regactivity = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(regactivity);
                finish();
            }
        });


        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                    Register();
  }
        });
        login.setVisibility(View.INVISIBLE);
    }


    public void Register() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final Thread t = new Thread() {
            public void run() {
                JSONObject json;
                emailtxt = email.getText().toString();
                passwordtxt = password.getText().toString();
                nametxt = name.getText().toString();
                branchtxt = branch.getText().toString();
                params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("email", emailtxt));
                params.add(new BasicNameValuePair("password", passwordtxt));
                params.add(new BasicNameValuePair("name", nametxt));
                params.add(new BasicNameValuePair("branch", branchtxt));

                ServerRequest sr = new ServerRequest();
                if (k.toUpperCase().equals("FACULTY"))
                    json = sr.getJSON("http://192.168.43.101:8080/registerFaculty", params);

                else
                    json = sr.getJSON("http://192.168.43.101:8080/register", params);
                //JSONObject json = sr.getJSON("http://192.168.56.1:8080/register",params);

                if (json != null) {
                    try {
                        jsonstr = json.getString("response");


                        // Log.d("Hello", jsonstr);
                    } catch (JSONException e) {
                        jsonstr=e.getMessage();
                       //Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(getApplication(),  jsonstr, Toast.LENGTH_SHORT).show();
                        if(jsonstr.contains("Sucessfully"))
                        {
                            email.setText("");
                            password.setText("");
                            name.setText("");
                            branch.setText("");

                        }
                        else
                        {
                            email.setText("");
                            password.setText("");
                        }
                    }
                });

            }
        };
        t.setDaemon(true);
        t.start();

    }
}





