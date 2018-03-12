package com.virtualclassroom.main.virtualclassroom;

import android.app.ProgressDialog;
import android.inputmethodservice.KeyboardView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.virtualclassroom.main.ServerRequest;


public class LoginActivity extends AppCompatActivity {
    EditText email,password,res_email,code,newpass;
    Button login,cont,cont_code,cancel,cancel1,register,forpass;
    String emailtxt,passwordtxt,email_res_txt,code_txt,npass_txt;
    List<NameValuePair> params;
    SharedPreferences pref;
    Dialog reset;
    ServerRequest sr;
    String k,jsonstr=null;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = getSharedPreferences("AppPref", MODE_PRIVATE);
        sr = new ServerRequest();
         Intent i=getIntent();
        Bundle b=i.getExtras();
        if(b!=null) {
            k = (String) b.get("name");
        this.setTitle(k+" Login ");
        }

        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.loginbtn);
        register = (Button)findViewById(R.id.register);
        forpass = (Button)findViewById(R.id.forgotpass);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regactivity = new Intent(LoginActivity.this,RegisterActivity.class);
                regactivity.putExtra("name",LoginActivity.this.k);
                startActivity(regactivity);

            }
        });


        login.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                try {

                 Login();

                }catch (Exception e)
                {
                    Toast.makeText(getApplication(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
                }
        });

        forpass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                reset = new Dialog(LoginActivity.this);
                reset.setTitle("Reset Password");
                reset.setContentView(R.layout.reset_pass_init);
                cont = (Button)reset.findViewById(R.id.resbtn);
                cancel = (Button)reset.findViewById(R.id.cancelbtn);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reset.dismiss();
                    }
                });
                res_email = (EditText)reset.findViewById(R.id.email);

                cont.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        email_res_txt = res_email.getText().toString();

                        params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("email", email_res_txt));

                        //  JSONObject json = sr.getJSON("http://192.168.56.1:8080/api/resetpass", params);
                        JSONObject json = sr.getJSON("http://192.168.43.101:8080/api/resetpass", params);

                        if (json != null) {
                            try {
                                String jsonstr = json.getString("response");
                                if(json.getBoolean("res")){
                                    Log.e("JSON", jsonstr);
                                    Toast.makeText(getApplication(), jsonstr, Toast.LENGTH_LONG).show();
                                    reset.setContentView(R.layout.reset_pass_code);
                                    cont_code = (Button)reset.findViewById(R.id.conbtn);
                                    code = (EditText)reset.findViewById(R.id.code);
                                    newpass = (EditText)reset.findViewById(R.id.npass);
                                    cancel1 = (Button)reset.findViewById(R.id.cancel);
                                    cancel1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            reset.dismiss();
                                        }
                                    });
                                    cont_code.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            code_txt = code.getText().toString();
                                            npass_txt = newpass.getText().toString();
                                            Log.e("Code",code_txt);
                                            Log.e("New pass",npass_txt);
                                            params = new ArrayList<NameValuePair>();
                                            params.add(new BasicNameValuePair("email", email_res_txt));
                                            params.add(new BasicNameValuePair("code", code_txt));
                                            params.add(new BasicNameValuePair("newpass", npass_txt));

                                            JSONObject json = sr.getJSON("http://192.168.43.101:8080/api/resetpass/chg", params);
                                            //   JSONObject json = sr.getJSON("http://192.168.56.1:8080/api/resetpass/chg", params);

                                            if (json != null) {
                                                try {

                                                    String jsonstr = json.getString("response");
                                                    if(json.getBoolean("res")){
                                                        reset.dismiss();
                                                        Toast.makeText(getApplication(),jsonstr,Toast.LENGTH_LONG).show();

                                                    }else{
                                                        Toast.makeText(getApplication(),jsonstr,Toast.LENGTH_LONG).show();

                                                    }
                                                } catch (JSONException e) {
                                                    Toast.makeText(getApplication(), "Check Your Internet Connection - "+e.getMessage(), Toast.LENGTH_SHORT).show();

                                                }
                                            }

                                        }
                                    });
                                }else{

                                    Toast.makeText(getApplication(),jsonstr,Toast.LENGTH_LONG).show();

                                }
                            } catch (JSONException e) {
                                Toast.makeText(getApplication(), "Check Your Internet Connection - "+e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }

                    }
                });


                reset.show();
            }
        });
    }





    public void Login() {
        emailtxt = email.getText().toString().trim();
        passwordtxt = password.getText().toString().trim();
        progressDialog=new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final Thread t = new Thread() {
            public void run() {
                JSONObject json;
                params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("email", emailtxt));
                params.add(new BasicNameValuePair("password", passwordtxt));
                ServerRequest sr = new ServerRequest();
                if(LoginActivity.this.k.toUpperCase().equals("FACULTY"))
                    json = sr.getJSON("http://192.168.43.101:8080/loginFaculty",params);
                else
                    json = sr.getJSON("http://192.168.43.101:8080/login", params);
                if (json != null) {
                    try {
                        jsonstr = json.getString("response");
                   } catch (JSONException e) {
                        jsonstr=e.getMessage();
                    }
                }
                try {
                        if (json.getBoolean("res")) {

                            String token = json.getString("token");
                            String grav = json.getString("grav");
                            String name = json.getString("name");
                            String branch = json.getString("branch");
                            String email = json.getString("email");
                            SharedPreferences.Editor edit = pref.edit();
                            edit.putString("token", token);
                            edit.putString("grav", grav);
                            edit.putString("email", email);
                            edit.putString("name", name);
                            edit.putString("branch", branch);
                            edit.putString("owner", k);
                            edit.commit();
                            progressDialog.dismiss();
                            Intent profactivity = new Intent(LoginActivity.this, ProfileActivity.class);
                            startActivity(profactivity);
                            finish();
                        }


                } catch (Exception e) {

                    jsonstr+=" "+e.getMessage();
                }


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(getApplication(),  jsonstr, Toast.LENGTH_SHORT).show();
email.setText("");
                        password.setText("");
                    }
                });


            }
        };
        t.setDaemon(true);
        t.start();



    }


}
