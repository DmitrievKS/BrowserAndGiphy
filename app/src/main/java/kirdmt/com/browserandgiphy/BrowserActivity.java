package kirdmt.com.browserandgiphy;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


public class BrowserActivity extends AppCompatActivity {

    private static final String HTTPS = "https://";
    private static final String HTTP = "http://";

    EditText urlBox;
    WebView webView;
    Button back, forward, go, cancel;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        urlBox = (EditText) findViewById(R.id.urlBox);
        webView = (WebView) findViewById(R.id.webView);
        back = (Button) findViewById(R.id.back);
        forward = (Button) findViewById(R.id.forward);
        go = (Button) findViewById(R.id.go);
        cancel = (Button) findViewById(R.id.stop);
        progress = (ProgressBar) findViewById(R.id.progress);


        webView.setWebViewClient(new CustomWebViewClient());
        webView.setWebChromeClient(new CustomWebChromeClient());

        urlBox.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //when enter is pressed in edittext, start loading the page
                if (keyCode == KeyEvent.KEYCODE_ENTER) {

                    openUrl();

                    return true;
                }
                return false;
            }
        });

        //go to previous page
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();
                }
            }
        });

        //go to next page
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoForward()) {
                    webView.goForward();
                }
            }
        });

        //reload page
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //webView.reload();

                openUrl();
            }
        });

        //cancel loading page
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.stopLoading();
            }
        });


    }


    public void openUrl() {

        String url = urlBox.getText().toString();

        if (!url.startsWith(HTTP) && !url.startsWith(HTTPS)) {
            url = HTTP + url;
        }
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        webView.loadUrl(url);
    }


    public class CustomWebViewClient extends WebViewClient {

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(BrowserActivity.this, description, Toast.LENGTH_SHORT).show();
        }

    }

    public class CustomWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progress.setProgress(newProgress);
            urlBox.setText(view.getUrl());

            if (newProgress == 100) {
                //  cancel.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);


            } else {
                // cancel.setVisibility(View.VISIBLE);
                progress.setVisibility(View.VISIBLE);

            }
        }
    }


}
