package org.richit.nctb.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.richit.nctb.AdUtils.MyAdsListener;
import org.richit.nctb.AdUtils.MyAllAdsUtil;
import org.richit.nctb.Config;
import org.richit.nctb.Models.Book;
import org.richit.nctb.R;

public class BookWebViewActivity extends AppCompatActivity {

    //Views
    WebView webView_book;
    ProgressBar progressBar;
    Toolbar toolbar;
    LinearLayout linearLayoutAds;

    //Adapter Class
    Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Trying to load web view
        try {
            setContentView(R.layout.activity_book_web_view);

            //Initializing views
            toolbar = findViewById(R.id.toolbar);
            webView_book = findViewById(R.id.webView_book);
            progressBar = findViewById(R.id.pBar);
            linearLayoutAds = findViewById(R.id.adsLL);

            //Setting toolbar as action bar and home button of toolbar functionality
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            //setting webView invisible at the starting of the app
            webView_book.setVisibility(View.INVISIBLE);

            //Getting data from Book adapter and set it to book class
            book = getIntent().getParcelableExtra("bookFromAdapter");

            //WebView is a view that display web pages inside your application
            //Here we set those ingredient that needs for viewing content from an web page
            WebSettings webSettings = webView_book.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setAppCacheEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setDatabaseEnabled(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(true);

            //Loading data from server side and set it to client side
            webView_book.setWebViewClient(new WebViewClient() {


                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    //Setting toolbar subtitle as loading before student class
                    // and their books are loaded
                    toolbar.setSubtitle("Loading...");
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);

                    //if page loaded successfully then showing the content via webView
                    progressBar.setVisibility(View.GONE);
                    webView_book.setVisibility(View.VISIBLE);
                    String nameStr[] = book.getPdf().split("-");

                    //setting class of the student
                    toolbar.setSubtitle("Class " + nameStr[nameStr.length - 1].replace(".pdf", "").replace("OPT", ""));
                }
            });

            toolbar.setSubtitle("Loading...");

            //load the books that user want to see
            webView_book.loadUrl(book.getBanglaView());

            //Showing banner ads
            adsAdder();

        } catch (Exception e) {

            //if there is no web view pre-installed in your device then you need to
            //install web view first
            e.printStackTrace();
            Toast.makeText(this, "Please install webview first", Toast.LENGTH_SHORT).show();
            finish();

            //showing the site of installing web view
            String url = Config.webViewUrl;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
    }

    @Override

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            //if home button selected then finish the activity
            finish();
        }
        return super.onOptionsItemSelected(item);
    }



    private void adsAdder() {

        MyAllAdsUtil.addAnyBannerAd(true, this, true, linearLayoutAds, new MyAdsListener());
    }

}
