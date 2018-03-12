package com.virtualclassroom.main.virtualclassroom;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.MediaController;
import android.widget.VideoView;

import org.jsoup.Jsoup;
import org.w3c.dom.Document;

public class VideoShow extends AppCompatActivity {
    ProgressDialog pDialog;
    VideoView videoview;
    WebView webView;
    SharedPreferences pref;
    ProgressDialog progressDialog;
    String VideoURL = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_video_show);
        webView = (WebView) findViewById(R.id.VideoView);
        pref = getSharedPreferences("AppPref", MODE_PRIVATE);
        VideoURL=VideoURL+pref.getString("file","");
        startWebView(VideoURL);
    }




        private void startWebView(String url) {
            webView.setWebViewClient(new WebViewClient() {

                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                public void onLoadResource(WebView view, String url) {
                    if (progressDialog == null) {
                        progressDialog = new ProgressDialog(VideoShow.this);
                        progressDialog.setMessage("Loading...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                    }
                }

                public void onPageFinished(WebView view, String url) {
                    try {

                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }

            });

            webView.setWebChromeClient(new WebChromeClient());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            webView.setScrollbarFadingEnabled(false);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
            webView.loadUrl(url);


        }

    class MyWebChromeClient extends WebChromeClient
    {

    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        startWebView(VideoURL);
    }

    @Override
    public void onBackPressed() {

        webView.clearSslPreferences();
        webView.clearCache(true);
        webView.removeAllViews();
        webView.destroy();
        finish();
    }
}


