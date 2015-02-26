package ca.owenpeterson.rssreader.views.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import ca.owenpeterson.rssreader.R;
import ca.owenpeterson.rssreader.adapters.CBCRSSListViewAdapter;
import ca.owenpeterson.rssreader.common.AppConstants;
import ca.owenpeterson.rssreader.feeders.OnFeedLoaded;
import ca.owenpeterson.rssreader.feeders.RSSFeeder;
import ca.owenpeterson.rssreader.parsers.CBCHandler;
import ca.owenpeterson.rssreader.results.BaseRSSResult;
import ca.owenpeterson.rssreader.results.CBCResult;
import ca.owenpeterson.rssreader.views.activities.DetailsActivity;

/**
 * Created by Owen on 2/16/2015.
 *
 * This is a fragment that is used to display a list of results for the CBC news feed.
 */
public class CBCFragment extends BaseRSSFragment {

    private View rootView;
    private final String feedURL = "http://www.cbc.ca/cmlink/rss-canada-manitoba";
    private ListView listView;
    private CBCRSSListViewAdapter viewAdapter;
    private ArrayList<? extends BaseRSSResult> rssResults;
    private CBCHandler handler;
    private OnFeedLoadedListener listener;
    private RSSFeeder<CBCHandler> rssFeeder;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //inflate the view.
        //for some reason I have different list view layouts for each list. For my case this doesn't seem
        //necessary.
        rootView = inflater.inflate(R.layout.reddit_layout, container, false);

        //initialize components for the view
        initComponents();

        //trigger the secondary thread that will parse the results.
        rssFeeder.execute();

        return rootView;
    }

    /**
     * This method is used to initialize components on the view.
     */
    private void initComponents() {
        handler = new CBCHandler(CBCResult.class);
        listener = new OnFeedLoadedListener();
        rssFeeder = new RSSFeeder<CBCHandler>(handler, feedURL, getActivity(), listener);
        sharedPreferences = getActivity().getSharedPreferences(AppConstants.SETTINGS_PREFERENCES_ID, getActivity().MODE_PRIVATE);
    }

    /**
     * Returns the list of RSS results
     * @return
     */
    public ArrayList<? extends BaseRSSResult> getRssResults() {
        return rssResults;
    }

    /**
     * Sets the list of rss results for this view
     * @param rssResults
     */
    public void setRssResults(ArrayList<? extends BaseRSSResult> rssResults) {
        this.rssResults = rssResults;
    }

    @Override
    public void refresh() {
        //when the user wants to refresh the feed, reinitialize the components for the view and then
        //execute the rssfeeder async task again
        initComponents();
        rssFeeder.execute();
    }

    /**
     * Nested class used as a callback from the RSSFeeder when the async task to populate the feed is
     * completed.
     */
    private class OnFeedLoadedListener implements OnFeedLoaded {


        /**
         * Retrieves the results from the handler class when the async task has been completed.
         */
        @Override
        public void onFeedLoaded() {

            ArrayList<? extends BaseRSSResult> results;

            //get the results form the handler
            results = handler.getRSSResults();

            //set the rss results in the view class
            setRssResults(results);

            //set up the view adapter and create the list view.
            viewAdapter = new CBCRSSListViewAdapter(getActivity(), R.layout.cbc_list_item, R.id.cbc_title, results);
            listView = (ListView) rootView.findViewById(R.id.list_view);
            listView.setAdapter(viewAdapter);


            //set the onItemClickListener for the listView.
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ArrayList<? extends BaseRSSResult> results = getRssResults();

                    //get the item that was clicked from the results array list.
                    BaseRSSResult clickedResult = results.get(position);

                    //determine if the user has a setting enabled to open the article in a web browser immediately
                    boolean isOpenImmediate = sharedPreferences.getBoolean(AppConstants.OPEN_IMMEDIATE, false);

                    //if the settting is enabled
                    if (isOpenImmediate) {
                        //open the link right away in a browser if the user has changed a setting to do so
                        String url = clickedResult.getLink();
                        Intent browser = new Intent (Intent.ACTION_VIEW, Uri.parse(url) );
                        startActivity(browser);
                    } else {
                        //open the item in a new window
                        //using the details activity
                        Intent detailsIntent = new Intent(getActivity(), DetailsActivity.class);

                        //put the result object that was retrieved into the intent as an extra
                        detailsIntent.putExtra("result", clickedResult);

                        //start the DetailsActivity
                        startActivity(detailsIntent);
                    }
                }
            });

        }
    }


}
