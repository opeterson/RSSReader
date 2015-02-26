package ca.owenpeterson.rssreader.views.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import org.apache.commons.lang3.StringUtils;

import ca.owenpeterson.rssreader.R;
import ca.owenpeterson.rssreader.common.AppConstants;

/**
 * This view allows the user to change settings for the application.
 */
public class SettingsActivity extends ActionBarActivity {
    private Spinner maxResults;
    private Spinner orderBy;
    private Spinner colorTheme;
    private Switch listClick;
    private SwitchListener switchListener;
    private SpinnerListener spinnerListener;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private  ArrayAdapter<CharSequence> maxResultsAdapter;
    private ArrayAdapter<CharSequence> orderByAdapter;
    private ArrayAdapter<CharSequence> colorThemeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //set the theme of the window.
        sharedPreferences = getSharedPreferences(AppConstants.SETTINGS_PREFERENCES_ID, MODE_PRIVATE);
        int themeId = sharedPreferences.getInt(AppConstants.THEME, -1);
        if (themeId > 0) {
            setTheme(themeId);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editor = sharedPreferences.edit();

        //create listeners for the switch and spinners
        switchListener = new SwitchListener();
        spinnerListener = new SpinnerListener();

        //get references to the spinner and switch items
        maxResults = (Spinner) findViewById(R.id.max_results_spinner);
        orderBy = (Spinner) findViewById(R.id.order_by_spinner);
        colorTheme = (Spinner) findViewById(R.id.theme_spinner);
        listClick = (Switch) findViewById(R.id.switch_immediate);


    }

    @Override
    protected void onStart() {
        super.onStart();
        //populate the max results spinner with data from the strings.xml file
        maxResultsAdapter = ArrayAdapter.createFromResource(this, R.array.max_results_array, android.R.layout.simple_spinner_item);
        maxResultsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        maxResults.setAdapter(maxResultsAdapter);
        maxResults.setOnItemSelectedListener(spinnerListener);

        //Extracts the users setting value for the Maxinum Results returned from shared preferences.
        int currentSetting = 0;
        currentSetting = sharedPreferences.getInt(AppConstants.MAX_RESULTS, -1);

        //load the correct setting value
        int index;

        //if the current setting is not currently zero
        if (currentSetting > 0) {

            //get the index of the item in the spinner that matches the current setting.
            index = maxResultsAdapter.getPosition(String.valueOf(currentSetting));

            //set that item as the selected item, as this is what the user has the value set to.
            maxResults.setSelection(index);
        } else {
            //if no value had been stored in shared preferences previously, set the default to unlimited.
            index = maxResultsAdapter.getPosition(AppConstants.UNLIMITED);
            maxResults.setSelection(index);
        }

        //set up the orderBy spinner using an array from the string.xml file. Set its listener.
        orderByAdapter = ArrayAdapter.createFromResource(this, R.array.order_by_array, android.R.layout.simple_spinner_item);
        orderByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderBy.setAdapter(orderByAdapter);
        orderBy.setOnItemSelectedListener(spinnerListener);

        //get the previous sortOrder index value, with a default return of -1
        int sortOrderIndex = sharedPreferences.getInt(AppConstants.ORDER_BY, -1);

        //if its -1, which indicates that the value had not been set previously
        if (sortOrderIndex == -1) {
            //set it to the default value which will be no order
            orderBy.setSelection(0);
        } else {
            //otherwise set the selection to be the last selected index.
            orderBy.setSelection(sortOrderIndex);
        }

        //set the color theme spinner content to values of an array in strings.xml.
        //also set up listeners.
        colorThemeAdapter = ArrayAdapter.createFromResource(this, R.array.color_theme, android.R.layout.simple_spinner_item);
        colorThemeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorTheme.setAdapter(colorThemeAdapter);
        colorTheme.setOnItemSelectedListener(spinnerListener);

        //get the previously selected index from shared preferences. If not available, return -1
        int colorThemeIndex = sharedPreferences.getInt(AppConstants.THEME_INDEX, -1);

        //if not available, set the selection to default.
        if (colorThemeIndex == -1) {
            colorTheme.setSelection(0);
        } else {
            //otherwise set the index to the last selected index.
            colorTheme.setSelection(colorThemeIndex);
        }

        //set the on check changed listener for the switch.
        listClick.setOnCheckedChangeListener(switchListener);

        //get the saved state of the open immediate setting and set that value on the switch.
        boolean isOpenInBrowserDefault = sharedPreferences.getBoolean(AppConstants.OPEN_IMMEDIATE, false);
        listClick.setChecked(isOpenInBrowserDefault);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        //commented out because the settings screen does not need a menu, at present.
        //getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.home:
                //if the home button is pressed, act as if the back button was pressed instead.
                super.onBackPressed();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * This class is used to handle the onItemSelectedListener event of the spinner views.
     */
    private class SpinnerListener implements Spinner.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int spinnerId = parent.getId();

            switch (spinnerId) {
                case R.id.max_results_spinner:
                    String value = maxResultsAdapter.getItem(position).toString();

                    //if unlimited was selected, set the max results value to 0
                    if (StringUtils.equals(value, AppConstants.UNLIMITED)) {
                        editor.putInt(AppConstants.MAX_RESULTS, 0);
                        editor.commit();
                    } else {
                        //otherwise take the selected value from the spinner and
                        //parse it to an int, then set it in the shared preferences.
                        int maximum = Integer.parseInt(value);
                        editor.putInt(AppConstants.MAX_RESULTS, maximum);
                        editor.commit();
                    }
                    break;
                case R.id.order_by_spinner:
                    //put the selected position into the setting.
                    editor.putInt(AppConstants.ORDER_BY, position);
                    editor.commit();
                    break;
                case R.id.theme_spinner:
                    //place the selection into storage so it can be restored later.
                    editor.putInt(AppConstants.THEME_INDEX, position);
                    editor.commit();

                    //the integer placed into shared storage for the theme selection is the id of the theme
                    //itself.
                    switch (position) {
                        case 0:
                            //default selected
                            editor.putInt(AppConstants.THEME, R.style.AppTheme);
                            editor.commit();
                            break;
                        case 1:
                            //light selected
                            editor.putInt(AppConstants.THEME, R.style.Light);
                            editor.commit();
                            break;
                        case 2:
                            //dark selected
                            editor.putInt(AppConstants.THEME, R.style.Dark);
                            editor.commit();
                            break;
                    }
                    break;
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    /**
     * Handles the onCheeckChangeListener event of the Switch on the view.
     */
    private class SwitchListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int id = buttonView.getId();

            //Just place a true or false into shared preferences depending on what the user had selected.
            switch (id) {
                case R.id.switch_immediate:
                    if (isChecked) {
                        editor.putBoolean(AppConstants.OPEN_IMMEDIATE, true);
                        editor.commit();
                    } else {
                        editor.putBoolean(AppConstants.OPEN_IMMEDIATE, false);
                        editor.commit();
                    }
            }
        }
    }
}
