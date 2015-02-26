package ca.owenpeterson.rssreader.views.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.util.Locale;

import ca.owenpeterson.rssreader.R;
import ca.owenpeterson.rssreader.common.AppConstants;
import ca.owenpeterson.rssreader.common.DateTimeSerializer;
import ca.owenpeterson.rssreader.common.Detailable;
import ca.owenpeterson.rssreader.results.BaseRSSResult;

/**
 * This class is used to display the details for a clicked RSS Item.
 */
public class DetailsActivity extends ActionBarActivity {
    protected TextView linkText;
    protected TextView titleText;
    protected TextView descriptionText;
    protected TextView dateText;
    protected TextView authorText;
    protected TextView categoryText;
    protected Button openButton;
    protected Button openInBrowserButton;
    protected Detailable result;
    protected Gson gson;
    protected GsonBuilder builder;
    protected SharedPreferences sharedPreferences;
    private final String PREFERENCES_ID = this.getClass().getName();
    private SharedPreferences.Editor editor;
    private final String NO_DESC = "No description available.";
    private final String NO_DATE = "No date available.";
    private final String NO_AUTHOR = "No author available";
    private final String NO_CATEGORY = "No category Available";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //get the theme that the user has set, and apply it.
        int themeId = getSharedPreferences(AppConstants.SETTINGS_PREFERENCES_ID,MODE_PRIVATE).getInt(AppConstants.THEME, -1);
        if (themeId > 0) {
            setTheme(themeId);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //create sharedpreferences objects
        sharedPreferences = getSharedPreferences(PREFERENCES_ID, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //greate a gson builder and gson object used to serialize the result object to a string.
        builder = new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeSerializer());
        gson = builder.create();

        //create the clickeventhandler
        ClickEventHandler clickEventHandler = new ClickEventHandler();

        //set up view components
        linkText = (TextView) findViewById(R.id.details_link);
        titleText = (TextView) findViewById(R.id.details_title);
        descriptionText = (TextView) findViewById(R.id.details_description);
        dateText = (TextView) findViewById(R.id.details_date);
        authorText = (TextView) findViewById(R.id.details_author);
        categoryText = (TextView) findViewById(R.id.details_category);
        openButton = (Button) findViewById(R.id.open_button);
        openInBrowserButton = (Button) findViewById(R.id.open_browser_button);

        //apply listeners
        openButton.setOnClickListener(clickEventHandler);
        openInBrowserButton.setOnClickListener(clickEventHandler);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
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
                //open the settings activity
                Intent intent = new Intent(DetailsActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.home:
                //act as if the back button was pressed.
                super.onBackPressed();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handles the click events for buttons on this view
     */
    private class ClickEventHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.open_button:
                    handleOpenButtonClick();
                    break;
                case R.id.open_browser_button:
                    handleOpenBrowserButtonClick();
            }
        }

        //open the link for the clicked result in the web view
        private void handleOpenButtonClick() {
            Intent webViewIntent = new Intent(DetailsActivity.this, WebViewActivity.class);
            webViewIntent.putExtra("link", result.getLink());
            startActivity(webViewIntent);
        }

        //open the link for the clicked result in the external browser
        private void handleOpenBrowserButtonClick() {
            String url = result.getLink();
            Intent browser = new Intent (Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browser);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        //when the activity is stopped, store the result into shared preferences using the Gson
        //library. The result object is turned into a JSON object and stored as a string.
        String resultAsJson = gson.toJson(result);
        editor.putString("result", resultAsJson);
        editor.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //get a serializeable extra from the intent and place it into the result.
        result = (Detailable) getIntent().getSerializableExtra("result");

        //but if that object is null, take it from shared preferences instead.
        if (null == result) {
            String resultAsJson = sharedPreferences.getString("result", "");
            result = gson.fromJson(resultAsJson, BaseRSSResult.class);
        }

        //set the title on the view to the value taken from the result object.
        titleText.setText(result.getTitle());

        //get the description from the result object
        String description = result.getDescription();

        //if the description value is empty, indicate that there is no description.
        if (StringUtils.isEmpty(description)) {
            descriptionText.setText(NO_DESC);
        } else {
            //check this for image tags first and remove them.
            //this works for my case, but it would probably be better to use regex pattern matching instead.

            //if the description contains an image tag, remove it.
            if (StringUtils.contains(description, "<img")) {
                String imageTag = StringUtils.substringBetween(description, "<img", "/>");
                description = StringUtils.remove(description, "<img" + imageTag + "/>");
            }

            //set the description using an HTML object. This handles the case when there is CDATA in
            //the description
            descriptionText.setText(Html.fromHtml(description));
        }

        //get the date from the result object
        DateTime date = result.getPubDate();

        //if the date is null, indicate that there was no date. Otherwise display it.
        if (null == date) {
            dateText.setText(NO_DATE);
        } else {
            dateText.setText(date.toString(AppConstants.DISPLAY_DATE_FORMAT, Locale.CANADA));
        }

        //get the author from the result object
        String author = result.getAuthor();

        //if there is no author text, indicate that there was no author available
        if (StringUtils.isNotBlank(author)) {
            authorText.setText(author);
        } else {
            authorText.setText(NO_AUTHOR);
        }

        //get the category from the result
        String category = result.getCategory();

        //if the category is empty, indicate that is is empty
        if (StringUtils.isNotBlank(category)) {
            categoryText.setText(category);
        } else {
            categoryText.setText(NO_CATEGORY);
        }

        //set the link text to the value extracted from the result object.
        linkText.setText(result.getLink());
    }

}
