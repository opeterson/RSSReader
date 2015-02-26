package ca.owenpeterson.rssreader.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import ca.owenpeterson.rssreader.R;
import ca.owenpeterson.rssreader.common.AppConstants;

/**
 * This class is used to display the article inside the application using a browser, rather than an external browser.
 */
public class WebViewActivity extends ActionBarActivity {

    protected WebView webView;
    protected WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //get the theme settings from shared preferences.
        int themeId = getSharedPreferences(AppConstants.SETTINGS_PREFERENCES_ID,MODE_PRIVATE).getInt(AppConstants.THEME, -1);

        //if it is not the default theme, set the theme.
        if (themeId > 0) {
            setTheme(themeId);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        //get the URL that was passed to the web view
        String url = getIntent().getStringExtra("link");

        //create a webview
        webView = (WebView) findViewById(R.id.webview);

        //set the web view up and load the URL.
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportZoom(true);
        webSettings.setDisplayZoomControls(true);
        webView.loadUrl(url);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_web_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id) {
            case R.id.action_settings:
                //start the settings activity
                Intent intent = new Intent(WebViewActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.home:
                //if the user presses the home arrow on the action bar, act as if the back buttonw as
                //pressed
                super.onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
