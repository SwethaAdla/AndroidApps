package com.app.thegamesdb;

/* Homework 05
   WebViewActivity.java
   Group 02- Lakshmi Sridhar, Swetha Adla */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.w3c.dom.Text;

public class WebViewActivity extends AppCompatActivity {
    String yurl ="";
    String gameTitle = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        if(getIntent().getExtras() != null){
            yurl = (String)getIntent().getExtras().get(GetGameActivity.TRAILER_LINK_KEY);
            gameTitle = (String)getIntent().getExtras().get(GetGameActivity.GAME_NAME_Key);
            TextView tv = (TextView) findViewById(R.id.textViewTitle);
            tv.setText(gameTitle+" Trailer");
            final WebView webView = (WebView) findViewById(R.id.webView);
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    webView.loadUrl(url);
                    return true;
                }
            });
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setLoadsImagesAutomatically(true);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            String[] S = yurl.split("\\?");
            String s = S[1];
            s = s.replace("v=","");
            String url = "http://www.youtube.com/embed/"+ s;
            webView.loadUrl(url);


        }


        findViewById(R.id.buttonExit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
