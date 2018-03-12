package com.virtualclassroom.main.virtualclassroom;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.virtualclassroom.main.ServerRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Question extends AppCompatActivity {

    TextView quesEmail,ques,ansCount;
    List<NameValuePair> params;
  EditText ans;
    Button submit,clear,send,cancel;
    SharedPreferences pref;
    Dialog Answer;
    String response="",myemail,answ,ansUser,Global_Separator="1111111111",result="";
    int Global_Separator_count=10;
   List<String> totalAnswers,totalUsers;
  ListView responseList;
ProgressDialog progressDialog;
    String[] responses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);

        setContentView(R.layout.activity_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        quesEmail=(TextView)findViewById(R.id.questionUser);
        ques=(TextView)findViewById(R.id.question);
       ansCount=(TextView)findViewById(R.id.anscount);
        responseList=(ListView)findViewById(R.id.response_list);
       responseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               String item[]=parent.getItemAtPosition(parent.getCount() - 1 - position).toString().trim().split(Global_Separator);
               SharedPreferences.Editor edit = pref.edit();
               edit.putString("email",item[0]);
               edit.putString("contents", item[1]);
               edit.commit();
               Intent i=new Intent(Question.this.getBaseContext(),ResponseDetails.class);
               startActivity(i);
           }
       });
        submit=(Button)findViewById(R.id.submit_answer);
        pref = getSharedPreferences("AppPref", MODE_PRIVATE);
        quesEmail.setText(pref.getString("emailFromForum","Anonymus"));
        ques.setText(pref.getString("quesFromForum",""));
        myemail=pref.getString("email", "Anonymous");

        answ=pref.getString("ansFromForum","").trim();
        ansUser=pref.getString("ansUserFromForum","").trim();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Answer = new Dialog(Question.this);
                Answer.setTitle("Your Answer...");
                Answer.setContentView(R.layout.forum_answer);
                clear = (Button)Answer.findViewById(R.id.clearbtn);
                send = (Button)Answer.findViewById(R.id.submitbtn);
                ans=(EditText)Answer.findViewById(R.id.answer);
                ans.setText(response);
                cancel = (Button)Answer.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!ans.getText().toString().trim().equals(""))
                            response=ans.getText().toString().trim();
                        Answer.dismiss();
                    }
                });
                clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ans.setText("");
                        response="";
                    }
                });
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ans.getText().toString().trim().equals(""))
                            Toast.makeText(getApplicationContext(), "Write Someting...", Toast.LENGTH_LONG).show();
                        else {
                           submitResponse();
                            Answer.dismiss();
                        }
                    }
                });
        Answer.show();
            }
        });
        new CountResponses().execute();

    }

    public  void submitResponse()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        final Thread t=new Thread()
        {
         public void run()
          {
              answ += ans.getText().toString().trim()+Global_Separator ;
              ansUser += myemail+Global_Separator  ;
              params = new ArrayList<NameValuePair>();
              pref = getSharedPreferences("AppPref", MODE_PRIVATE);
              myemail = pref.getString("email", "");
              params.add(new BasicNameValuePair("quesUser", quesEmail.getText().toString()));
              params.add(new BasicNameValuePair("ques", ques.getText().toString()));
              params.add(new BasicNameValuePair("ans", answ));
              params.add(new BasicNameValuePair("ansUser", ansUser));
              ServerRequest sr = new ServerRequest();
              //    JSONObject json = sr.getJSON("http://192.168.56.1:8080/api/chgpass",params);
              JSONObject json = sr.getJSON("http://192.168.43.101:8080/registerAnswer", params);
              if (json != null) {
                  try {
                      String jsonstr = json.getString("response");
                      result=jsonstr;
                  } catch (JSONException e) {
                      result="Exception - "+e.getMessage();
                  }
              }
              runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                      progressDialog.dismiss();
                      Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG);
                     if( Answer.isShowing())
                         Answer.dismiss();
                      if(result.toLowerCase().contains("exception")==false) {
                      getAnswersList();
                      }

                  }
              });
          }
        };
        t.setDaemon(true);
        t.start();

    }



    public void getAnswersList() {

            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

        final Thread t = new Thread() {
            public void run() {


                params = new ArrayList<NameValuePair>();
                ServerRequest sr = new ServerRequest();
                //    JSONObject json = sr.getJSON("http://192.168.56.1:8080/api/chgpass",params);
                JSONObject json = sr.getJSON("http://192.168.43.101:8080/forumList", params);
                if (json != null) {
                    try {

                        int len = json.length();

                        Iterator<String> iterator = json.keys();
                        while (iterator.hasNext()) {
                            JSONObject jsonObject = json.getJSONObject(iterator.next());

                            if(jsonObject.getString("quesUser").equals(myemail) && jsonObject.getString("ques").equals(ques.getText().toString().trim()))
                            {
                                answ=jsonObject.getString("ans");
                                ansUser=jsonObject.getString("ansUser");
                            }

                           }

                    } catch (JSONException e) {

                    }

                }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Populating List...\n PLease Wait...", Toast.LENGTH_LONG);
                            progressDialog.dismiss();
                            progressDialog=null;
                            new CountResponses().execute();
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG);


                        }
                    });


            }


        };
        t.setDaemon(true);
        t.start();



    }
    class ResponseAdapter extends ArrayAdapter<String>
    {

        ResponseAdapter()
        {


            super(getApplicationContext(), R.layout.response_list_items, responses);
        }

        @Override
        public int getCount() {
            return responses.length;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater layoutInflater = Question.this.getLayoutInflater();
                convertView = layoutInflater.inflate(R.layout.simple_list_view, parent, false);
            }
            if (position < getCount()) {
                String string[]=responses[getCount() - 1 - position].trim().split(Global_Separator);
                TextView firstLine = (TextView) convertView.findViewById(R.id.email);
                TextView secondLine = (TextView) convertView.findViewById(R.id.contents);
                firstLine.setText(string[0].trim());
                secondLine.setText(string[1].trim());
            }
            return convertView;
        }
    }

    class CountResponses extends AsyncTask<Void,String[],String[]>
    {
        @Override
        protected void onPreExecute() {
            if(progressDialog==null || progressDialog.isShowing()==false) {
                progressDialog = new ProgressDialog(Question.this);
                progressDialog.setMessage("Please Wait...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setIndeterminate(true);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }

        }

        @Override
        protected void onPostExecute(String[] res) {
            if(res!=null) {
                ansCount.setText("Total Responses = " + totalUsers.size());
                responses = res;
                responseList.setAdapter(new ResponseAdapter());

            }
            else
           Toast.makeText(getApplicationContext(), 0 + " Records Available", Toast.LENGTH_LONG);
            progressDialog.dismiss();
        }

        @Override
        protected String[] doInBackground(Void... params) {


            if(answ.equals(""))
                return null;


            totalAnswers= (Arrays.asList(answ.trim().split(Global_Separator)));
            totalUsers=(Arrays.asList(ansUser.trim().split(Global_Separator)));

            String res[]=new String[totalAnswers.size()];
            try {
                for (int i = 0; i < totalAnswers.size(); i++)
                    res[i] = totalUsers.get(i) + Global_Separator + totalAnswers.get(i);

            }catch (Exception e)
            {
                Log.e("Error ",e.getMessage());
            }

          return res;
        }
    }
}



