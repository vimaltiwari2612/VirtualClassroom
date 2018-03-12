package com.virtualclassroom.main.virtualclassroom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.virtualclassroom.main.ServerRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ForumActivity extends AppCompatActivity {
    EditText text;
    Button upload, refresh;
    String emailtxt,result="",ques="";
    SharedPreferences pref;
    List<NameValuePair> params;
    HashMap<String, String> data = new HashMap<String, String>();
    ListView forumlist;
    ArrayAdapter<Forum> arrayAdapter;
    ProgressDialog progressDialog;
    boolean records=false;
    Forum[] forums;
    public static String totalAnswers="",totalUsers="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        pref = getSharedPreferences("AppPref", MODE_PRIVATE);
        emailtxt = pref.getString("email", "Anonymous");
        setSupportActionBar(toolbar);
        text = (EditText) findViewById(R.id.ques);
        upload = (Button) findViewById(R.id.upload);
        forumlist = (ListView) findViewById(R.id.list);
        forumlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor edit = pref.edit();
                Forum f = (Forum) parent.getItemAtPosition(parent.getCount() - 1 - position);
                edit.putString("emailFromForum", f.getQuesUser());
                edit.putString("quesFromForum", f.getQues());
                edit.putString("ansUserFromForum", f.getAnsUser());
                edit.putString("ansFromForum", f.getAns());
                edit.putString("email", emailtxt);
                edit.commit();
                Intent i = new Intent(ForumActivity.this, Question.class);
                startActivity(i);

            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text.getText().toString().trim().equals(""))
                    Toast.makeText(getApplication(), "Plaese write something...", Toast.LENGTH_LONG).show();
                else
                  uploadQuestion();
            }
        });

        refresh = (Button) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {

                  getQuestionsList(true);

                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
                }


        });


    }



    class Forum {
        String quesUser, ques, ansUser, ans;

        public String getQuesUser() {
            return quesUser;
        }

        public void setQuesUser(String quesUser) {
            this.quesUser = quesUser;
        }

        public String getQues() {
            return ques;
        }

        public void setQues(String ques) {
            this.ques = ques;
        }

        public String getAnsUser() {
            return ansUser;
        }

        public void setAnsUser(String ansUser) {
            this.ansUser = ansUser;
        }

        public String getAns() {
            return ans;
        }

        public void setAns(String ans) {
            this.ans = ans;
        }
    }

    class ForumAdapter extends ArrayAdapter<Forum> {
        ForumAdapter() {
            super(getApplicationContext(), R.layout.simple_list_view, forums);
        }

        @Override
        public int getCount() {
            return forums.length;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater layoutInflater = ForumActivity.this.getLayoutInflater();
                convertView = layoutInflater.inflate(R.layout.simple_list_view, parent, false);
            }
            if (position < getCount()) {
                TextView firstLine = (TextView) convertView.findViewById(R.id.email);
                TextView secondLine = (TextView) convertView.findViewById(R.id.contents);
                firstLine.setText(String.valueOf(forums[getCount() - 1 - position].getQuesUser()));
                secondLine.setText(forums[getCount() - 1 - position].getQues());
            }
            return convertView;
        }
    }


    public String[] getRefreshedList(String email ,String ques)
    {
        getQuestionsList(false);
        String[] response=new String[2];
        for(int i=0;i<forums.length;i++)
        {
            if(forums[i].getQues().equals(ques.trim()) && forums[i].getQuesUser().equals(email.trim()))
            {
                response[0]=forums[i].getAns();
                response[1]=forums[i].getAnsUser();
                return  response;
            }
        }
        return  null;
    }

   public void getQuestionsList(final boolean value) {
if(value==true) {
    progressDialog = new ProgressDialog(this);
    progressDialog.setMessage("Please Wait...");
    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    progressDialog.setIndeterminate(true);
    progressDialog.setCanceledOnTouchOutside(false);
    progressDialog.show();
}
        final Thread t = new Thread() {
        public void run() {


                params = new ArrayList<NameValuePair>();
                ServerRequest sr = new ServerRequest();
                //    JSONObject json = sr.getJSON("http://192.168.56.1:8080/api/chgpass",params);
                JSONObject json = sr.getJSON("http://192.168.43.101:8080/forumList", params);
                if (json != null) {
                    try {
                        int i = 0;
                        int len = json.length();
                        forums = new Forum[len];
                        Iterator<String> iterator = json.keys();
                        while (iterator.hasNext()) {
                            JSONObject jsonObject = json.getJSONObject(iterator.next());
                            Forum f = new Forum();
                            f.setQuesUser(jsonObject.getString("quesUser"));
                            f.setQues(jsonObject.getString("ques"));
                            f.setAnsUser(jsonObject.getString("ansUser"));
                            f.setAns(jsonObject.getString("ans"));
                            forums[i] = f;
                            i++;
                        }
                        result=len+" Records Fetched!!";
                        records=true;

                    } catch (JSONException e) {
                       result=e.getMessage();
                        records=false;

                    }

                } else {
                    result = "No Records!!";
                    records=false;

                }

if(value) {
    runOnUiThread(new Runnable() {
        @Override
        public void run() {
            Toast.makeText(getApplicationContext(), "Populating List...\n PLease Wait...", Toast.LENGTH_LONG);
            if (records) {
                setUpListView();
                records = false;

            }
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG);


        }
    });
}

        }


        };
       t.setDaemon(true);
       t.start();



   }

    public void setUpListView()
    {
        forumlist.setAdapter(new ForumAdapter());

    }

    public void uploadQuestion()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);

        progressDialog.show();

        final Thread t = new Thread() {

           public void run() {
                params = new ArrayList<NameValuePair>();
                pref = getSharedPreferences("AppPref", MODE_PRIVATE);
                emailtxt = pref.getString("email", "");
                params.add(new BasicNameValuePair("quesUser", emailtxt));
                params.add(new BasicNameValuePair("ques", text.getText().toString().trim()));
                ServerRequest sr = new ServerRequest();
                //    JSONObject json = sr.getJSON("http://192.168.56.1:8080/api/chgpass",params);
                JSONObject json = sr.getJSON("http://192.168.43.101:8080/registerForum", params);
                if (json != null) {
                    try {
                        String jsonstr = json.getString("response");
                        result=jsonstr;

                    } catch (JSONException e) {
                        result=e.getMessage();
                    }
                }

               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       progressDialog.dismiss();
                       text.setText("");
                       Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
                       getQuestionsList(true);
                   }
               });

               }
        };
        t.setDaemon(true);
        t.start();



    }




}