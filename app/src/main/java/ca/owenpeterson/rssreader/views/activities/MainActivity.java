package ca.owenpeterson.rssreader.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import ca.owenpeterson.rssreader.R;
import ca.owenpeterson.rssreader.common.AppConstants;
import ca.owenpeterson.rssreader.views.fragments.BaseRSSFragment;
import ca.owenpeterson.rssreader.views.fragments.CBCFragment;
import ca.owenpeterson.rssreader.views.fragments.NavigationDrawerFragment;
import ca.owenpeterson.rssreader.views.fragments.RedditFragment;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private BaseRSSFragment currentFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int themeId = getSharedPreferences(AppConstants.SETTINGS_PREFERENCES_ID,MODE_PRIVATE).getInt(AppConstants.THEME, -1);
        if (themeId > 0) {
            setTheme(themeId);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    /**
     * This method handles what occurs when a navigation item is selected
     *
     * @param position
     */
    @Override
    public void onNavigationDrawerItemSelected(int position) {

        //create a fragment object placeholder
        BaseRSSFragment objFragment = null;

        //define a name for the fragment.
        String fragmentName= "CUSTOM_FRAGMENT";

        //instantiates a different fragment depending on what the user has selected
        switch (position) {
            case 0:
                objFragment = new RedditFragment();
                setCurrentFragment(objFragment);
                break;
            case 1:
                objFragment = new CBCFragment();
                setCurrentFragment(objFragment);
                break;
        }

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, objFragment, fragmentName)
                .commit();
    }

    /**
     * Attaches a title to the item in the drawer when the item object is created.
     *
     * @param number
     */
    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_reddit);
                break;
            case 2:
                mTitle = getString(R.string.title_cbc);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_refresh:
                refreshFragment();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    /**
     * This method is used to refresh the fragment by calling the refresh method of the currently
     * displayed fragment.
     */
    private void refreshFragment() {
        currentFragment.refresh();
    }

    public BaseRSSFragment getCurrentFragment() {
        return currentFragment;
    }

    public void setCurrentFragment(BaseRSSFragment currentFragment) {
        this.currentFragment = currentFragment;
    }
}
