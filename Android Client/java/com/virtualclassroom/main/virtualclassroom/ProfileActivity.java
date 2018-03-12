package com.virtualclassroom.main.virtualclassroom;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import java.io.FileOutputStream;

import com.koushikdutta.ion.*;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.concurrent.Future;
import  com.koushikdutta.async.future.FutureCallback;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {


    SharedPreferences pref;

    String token, grav, oldpasstxt, newpasstxt, emailtxt, nametxt, branchtxt, passwordtxt,owner,file_to_download="";
    WebView web;
    Button chgpass, chgpassfr, cancel, logout, forum, fileupload,lecture,livelecture,recordlecture;
    Dialog dlg,filesDialog,lectuedlg,lecturesDialog;
    EditText oldpass, newpass;
    TextView nameTextView, branchTV, emailTV, filename;
    List<NameValuePair> params;
    List<String> filesOnServer;
    Object files[];
    ListView filesList;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
lecture=(Button)findViewById(R.id.lecture);
        lecture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, LectureActivity.class);
                startActivity(i);

            }
        });
        // web = (WebView)findViewById(R.id.webView);
        lecture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               lectuedlg=new Dialog(ProfileActivity.this);
                if(owner.trim().equals("faculty"))
                    lectuedlg.setTitle("Options...");
                    else
                lectuedlg.setTitle("You can Watch ...");
                lectuedlg.setContentView(R.layout.attend_lecture_option);
                livelecture=(Button)lectuedlg.findViewById(R.id.option1);
                recordlecture=(Button)lectuedlg.findViewById(R.id.option2);
                if(owner.trim().equals("faculty"))
                    livelecture.setText("Take Lecture");
                lectuedlg.show();
                recordlecture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        params = new ArrayList<NameValuePair>();

                        ServerRequest sr = new ServerRequest();
                        JSONObject json = sr.getJSON("http://192.168.43.101:8080/videoList", params);
                        if (json != null) {
                            try {
                                int len = json.length();
                                filesOnServer=new ArrayList<String>(len);
                                if(len==0)
                                    Toast.makeText(getApplication(), "No Vedios Available!", Toast.LENGTH_SHORT).show();
                                else {
                                    JSONArray jsonArray=json.getJSONArray("Files");
                                    for(int i=0;i<jsonArray.length();i++) {
                                        JSONObject filename=jsonArray.getJSONObject(i);
                                        filesOnServer.add(filename.getString("File").trim());
                                    }
                                    Toast.makeText(getApplication(), "Total videos = "+filesOnServer.size(), Toast.LENGTH_SHORT).show();
                                    files=filesOnServer.toArray();
                                }

                            } catch (Exception e) {
                                Toast.makeText(getApplication(), "Check Your Internet Connection - "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        filesDialog=new Dialog(ProfileActivity.this);
                        filesDialog.setContentView(R.layout.files_on_server);
                        filesList=(ListView)filesDialog.findViewById(R.id.list_files);
                        if(filesList!=null)
                            filesList.setAdapter(new FilesAdapter());
                        filesDialog.show();
                        filesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                String filename = (String) parent.getItemAtPosition(position);
                                SharedPreferences.Editor edit = pref.edit();
                                edit.putString("file", "http://192.168.43.101:8080/video?name="+filename);
                                edit.commit();
                                Intent i = new Intent(ProfileActivity.this, VideoShow.class);
                                startActivity(i);


                            }

                        });

                    }
                });

                livelecture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (owner.trim().equals("faculty")) {
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse("https://www.webrtc-experiment.com/webrtc-broadcasting/"));
                            startActivity(i);
                        }
                       else
                        {
                          SharedPreferences.Editor edit = pref.edit();
                            edit.putString("file", "https://www.webrtc-experiment.com/webrtc-broadcasting/");
                            edit.commit();
                            Intent i = new Intent(ProfileActivity.this, VideoShow.class);
                            startActivity(i);
                            //new JP().execute("https://www.webrtc-experiment.com/webrtc-broadcasting/");
                        }

                    }
                });

            }
        });
        chgpass = (Button) findViewById(R.id.chgbtn);
        logout = (Button) findViewById(R.id.logout);
        nameTextView = (TextView) findViewById(R.id.name);
        branchTV = (TextView) findViewById(R.id.branch);
        emailTV = (TextView) findViewById(R.id.email);
        filename = (TextView) findViewById(R.id.filename);
        forum = (Button) findViewById(R.id.forum);


        forum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = pref.edit();
                //Storing Data using SharedPreferences
                edit.putString("email", ProfileActivity.this.emailtxt);
                edit.commit();
                Intent loginactivity = new Intent(ProfileActivity.this, ForumActivity.class);
                startActivity(loginactivity);

            }
        });

        fileupload = (Button) findViewById(R.id.fileuplaod);
        fileupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (owner.trim().equals("faculty")) {
                    ProfileActivity.this.showFiles();
                } else {
                    params = new ArrayList<NameValuePair>();

                    ServerRequest sr = new ServerRequest();
                    JSONObject json = sr.getJSON("http://192.168.43.101:8080/filesList", params);
                    if (json != null) {
                        try {
                            int len = json.length();
                            filesOnServer=new ArrayList<String>(len);
                            if(len==0)
                                Toast.makeText(getApplication(), "No files Available For Download!!", Toast.LENGTH_SHORT).show();
                                else {
                                JSONArray jsonArray=json.getJSONArray("Files");
                                for(int i=0;i<jsonArray.length();i++) {
                                    JSONObject filename=jsonArray.getJSONObject(i);
                                    filesOnServer.add(filename.getString("File").trim());
                                }
                                Toast.makeText(getApplication(), "Total files = "+filesOnServer.size(), Toast.LENGTH_SHORT).show();
                            files=filesOnServer.toArray();
                            }

                        } catch (Exception e) {
                            Toast.makeText(getApplication(), "Check Your Internet Connection - "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                        filesDialog=new Dialog(ProfileActivity.this);
                    filesDialog.setContentView(R.layout.files_on_server);
                    filesList=(ListView)filesDialog.findViewById(R.id.list_files);
                    if(filesList!=null)
                    filesList.setAdapter(new FilesAdapter());
                    filesDialog.show();
                    filesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            String filename=(String)parent.getItemAtPosition(position);

                            String url = "http://192.168.43.101:8080/download?name="+filename.trim();
                            file_to_download=filename;
                            new DownloadFileAsync().execute(url);




                        }

                    });
                }

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor edit = pref.edit();
                //Storing Data using SharedPreferences
                edit.putString("token", "");
                edit.commit();
                Intent loginactivity = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(loginactivity);
                finish();

            }
        });

        pref = getSharedPreferences("AppPref", MODE_PRIVATE);
        token = pref.getString("token", "");
        grav = pref.getString("grav", "");
        emailtxt = pref.getString("email", "no email");
        nametxt = pref.getString("name", "No name");
        branchtxt = pref.getString("branch", "");
        owner=pref.getString("owner","");
        emailTV.setText(emailtxt);
        branchTV.setText(branchtxt);
        nameTextView.setText(nametxt);
       if(owner.trim().equals("student"))
           fileupload.setText("File Download");
        else
       fileupload.setText("File Upload");
        chgpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg = new Dialog(ProfileActivity.this);
                dlg.setContentView(R.layout.chgpassword_frag);
                dlg.setTitle("Change Password");
                chgpassfr = (Button) dlg.findViewById(R.id.chgbtn);

                chgpassfr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        oldpass = (EditText) dlg.findViewById(R.id.oldpass);
                        newpass = (EditText) dlg.findViewById(R.id.newpass);
                        oldpasstxt = oldpass.getText().toString();
                        newpasstxt = newpass.getText().toString();
                        params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("oldpass", oldpasstxt));
                        params.add(new BasicNameValuePair("newpass", newpasstxt));
                        params.add(new BasicNameValuePair("id", token));
                        ServerRequest sr = new ServerRequest();
                        //    JSONObject json = sr.getJSON("http://192.168.56.1:8080/api/chgpass",params);
                        JSONObject json = sr.getJSON("http://192.168.43.101:8080/api/chgpass", params);
                        if (json != null) {
                            try {
                                String jsonstr = json.getString("response");
                                if (json.getBoolean("res")) {

                                    dlg.dismiss();
                                    Toast.makeText(getApplication(), jsonstr, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplication(), jsonstr, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(getApplication(), "Check Your Internet Connection - "+e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }

                    }
                });
                cancel = (Button) dlg.findViewById(R.id.cancelbtn);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dlg.dismiss();
                    }
                });
                dlg.show();
            }
        });


    }

    public void showFiles() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose File"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            // action cancelled
        }
        if (resultCode == RESULT_OK) {

                new UploadTask().execute(data);
        }


    }



class UploadTask extends AsyncTask<Intent,Object,String>
{
     String msg="";
    @Override
    protected String doInBackground(Intent... params) {

        Intent data=params[0];
        try {

            Uri selectedimg = data.getData();

            Ion.getDefault(ProfileActivity.this).setLogging("ion-logging", Log.DEBUG);
            File f = new File(selectedimg.getEncodedPath());
            Future uploading = Ion.with(ProfileActivity.this)
                    .load("http://192.168.43.101:8080/upload")
                    .setMultipartFile("image", f)
                    .asString()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<String>>() {
                        @Override
                        public void onCompleted(Exception e, Response<String> result) {

                            try {
                                JSONObject jobj = new JSONObject(result.getResult());
                               UploadTask.this.msg = jobj.getString("response");
                            }
                            catch (Exception e1)
                            {
                                UploadTask.this.msg= "Check Your Internet Connection - "+e1.getMessage();
                            }

                        }
                    });
        } catch (Exception e1) {

            msg= "Check Your Internet Connection - "+e1.getMessage();

        }
        return msg;
    }

    @Override
    protected void onPreExecute() {

        progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setMessage("Uploading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(String aVoid) {
        progressDialog.dismiss();

        Toast.makeText(getApplication(),aVoid, Toast.LENGTH_SHORT).show();

    }
}

 class FilesAdapter extends ArrayAdapter<Object>
    {
        FilesAdapter()
        {
            super(getApplicationContext(), R.layout.files_name,files);
        }
        @Override
        public int getCount() {
            return files.length;
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater layoutInflater = ProfileActivity.this.getLayoutInflater();
                convertView = layoutInflater.inflate(R.layout.files_name, parent, false);
            }
            if(position<getCount()) {
                TextView firstLine = (TextView) convertView.findViewById(R.id.filesOnServerName);
                firstLine.setText((String)files[position]);
            }
            return convertView;
        }
    }



    class DownloadFileAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ProfileActivity.this);
            progressDialog.setMessage("Downloading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... aurl) {
            int count;

            try {

                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lenghtOfFile = conexion.getContentLength();
                Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

                InputStream input = new BufferedInputStream(url.openStream());
               OutputStream output = new FileOutputStream("/sdcard/download/"+file_to_download);

                byte data[] = new byte[128*1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress(""+(int)((total*100)/lenghtOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                return e.getMessage();
            }
            return null;

        }
        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC",progress[0]);
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {

      progressDialog.dismiss();
            if(unused==null)
            Toast.makeText(getApplication(), "downloaded", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplication(), unused, Toast.LENGTH_SHORT).show();
        }
    }


    class JP extends AsyncTask<String,String,String>
    {
        String res="";
        @Override
        protected String doInBackground(String... params) {
            try{

                Document doc=Jsoup.connect("https://www.webrtc-experiment.com/webrtc-broadcasting/").get();
                res=doc.getElementById("room-list").data();
            }catch(Exception e)
            {
                res=e.getMessage();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getApplication(),s,Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ProfileActivity.this);
            progressDialog.setMessage("Extracting Details...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

        }
    }


}
