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
import ca.owenpeterson.rssreader.adapters.RedditRSSListViewAdapter;
import ca.owenpeterson.rssreader.common.AppConstants;
import ca.owenpeterson.rssreader.feeders.OnFeedLoaded;
import ca.owenpeterson.rssreader.feeders.RSSFeeder;
import ca.owenpeterson.rssreader.parsers.RedditHandler;
import ca.owenpeterson.rssreader.results.BaseRSSResult;
import ca.owenpeterson.rssreader.results.RedditResult;
import ca.owenpeterson.rssreader.views.activities.DetailsActivity;

/**
 * Created by Owen on 2/16/2015.
 *
 * This class is used to extend the BaseRSSFragment class and display a list of RSS Items from reddit.
 */
public class RedditFragment extends BaseRSSFragment {

    private View rootView;
    private static final String feedURL = "http://www.reddit.com/r/java/.rss";
    private ListView listView;
    private RedditRSSListViewAdapter viewAdapter;
    private OnFeedLoadedListener listener;
    private ArrayList<? extends BaseRSSResult> rssResults;
    private RedditHandler handler;
    private RSSFeeder<RedditHandler> rssFeeder;
    private SharedPreferences sharedPreferences;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.reddit_layout, container, false);

        //intialize the view components
        initComponents();

        //start the secondary thread.
        rssFeeder.execute();

        return rootView;
    }


    /**
     * Returns the list of RSSResults that has been set on the Fragment.
     * @return
     */
    public ArrayList<? extends BaseRSSResult> getRssResults() {
        return rssResults;
    }

    /**
     * Sets the list of RSS results on this fragment
     *
     * @param rssResults
     */
    public void setRssResults(ArrayList<? extends BaseRSSResult> rssResults) {
        this.rssResults = rssResults;
    }

    /**
     * Refreshes the feed by reinitializing the components and then running the RSSFeeder again.
     */
    @Override
    public void refresh() {
        initComponents();
        rssFeeder.execute();
    }

    /**
     * Initializes all of the components for the form.
     */
    private void initComponents() {
        //create a new handler passing the required result class as a parameter
        handler = new RedditHandler(RedditResult.class);

        //instantiate the custom onFeedLoadedListener
        listener = new OnFeedLoadedListener();

        //create a new RSSFeeder that works with a RedditHandler type, passing along the handler
        //the Feed URL, the context, and the listener used for a callback when the feed is complete.
        rssFeeder = new RSSFeeder<RedditHandler>(handler, feedURL, getActivity(), listener);

        //set up the sharedpreferences object.
        sharedPreferences = getActivity().getSharedPreferences(AppConstants.SETTINGS_PREFERENCES_ID, getActivity().MODE_PRIVATE);
    }

    /**
     * Private innner class used to handle the OnFeedLoaded event.
     */
    private class OnFeedLoadedListener implements OnFeedLoaded {

        @Override
        public void onFeedLoaded() {
            //get the list of results from the handler.
            ArrayList<? extends BaseRSSResult> results;
            results = handler.getRSSResults();

            //set the list of results on this fragment.
            setRssResults(results);

            //create the view adapter passing along the list of results, as well as a reference to the list item layout.
            //the reddit_title object is irrelivant and I'm not sure why its included.
            viewAdapter = new RedditRSSListViewAdapter(getActivity(), R.layout.reddit_list_item, R.id.reddit_title, results);

            //instantiate the list view object
            listView = (ListView) rootView.findViewById(R.id.list_view);

            //and set its adapter.
            listView.setAdapter(viewAdapter);

            //set the onItemClickListener of the ListView to a new OnItemClickListener.
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //get a reference to the list of RSSResults.
                    ArrayList<? extends BaseRSSResult> results = getRssResults();

                    //get the result object from the cicked position
                    BaseRSSResult clickedResult = results.get(position);

                    //determine if the user has the Open Immediate setting enabled
                    boolean isOpenImmediate = sharedPreferences.getBoolean(AppConstants.OPEN_IMMEDIATE, false);

                    //if they do, open the link in a browser right away
                    if (isOpenImmediate) {
                        //open the link right away in a browser if the user has changed a setting to do so
                        String url = clickedResult.getLink();
                        Intent browser = new Intent (Intent.ACTION_VIEW, Uri.parse(url) );
                        startActivity(browser);
                    } else {
                        //otherwise display the details in the details activity.
                        Intent detailsIntent = new Intent(getActivity(), DetailsActivity.class);
                        detailsIntent.putExtra("result", clickedResult);
                        startActivity(detailsIntent);
                    }
                }
            });
        }
    };

}
