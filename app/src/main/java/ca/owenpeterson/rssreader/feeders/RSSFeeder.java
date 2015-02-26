package ca.owenpeterson.rssreader.feeders;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import ca.owenpeterson.rssreader.common.AppConstants;
import ca.owenpeterson.rssreader.parsers.AbstractRSSHandler;
import ca.owenpeterson.rssreader.results.BaseRSSResult;
import ca.owenpeterson.rssreader.results.DateComparator;
import ca.owenpeterson.rssreader.results.TitleComparator;

/**
 * Created by Owen on 2/13/2015.
 *
 * This class is used to open an RSS feed from a web source and then pass the stream to a SAXParser
 * for processing.
 *
 * The class itself takes a type argument that has to extend the AbstractRSSHandler class. This is so that
 * the class can handle different parser class configurations.
 *
 * Looking back, I wouldn't do it this way again. I would make one parsing class that parses pretty much everything
 * from the feed rather than creating specific components.
 *
 * However, it as a good excercise because I learned a lot about how to pass parameters to classes and how to handle
 * abstraction, interfaces, etc.
 */


public class RSSFeeder<T extends AbstractRSSHandler> extends AsyncTask<Void, Void, ArrayList<? extends BaseRSSResult>> {

    private T handler;
    private String feedURL;
    private ProgressDialog dialog;
    private Context context;
    private OnFeedLoaded listener;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    /**
     * Constructor used to instantiate the RSSFeeder object
     *
     * @param handler - The handler that will be used to parse the feed
     * @param feedURL - the URL of the feed to parse
     * @param context - used to indicate which activity will be the parent of the loading dialog box
     * @param listener - used to make a callback to the activity that started the async task
     */
    public RSSFeeder(T handler, String feedURL, Context context, OnFeedLoaded listener) {
        this.handler = handler;
        this.feedURL = feedURL;
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading Feed");
        dialog.show();
    }


    @Override
    protected ArrayList<? extends BaseRSSResult> doInBackground(Void... params) {
        ArrayList<? extends BaseRSSResult> results = null;
        URL feedLocation = null;
        try {

            //start by getting some values for settings
            sharedPreferences = context.getSharedPreferences(AppConstants.SETTINGS_PREFERENCES_ID, Context.MODE_PRIVATE);
            int maxResults = sharedPreferences.getInt(AppConstants.MAX_RESULTS, -1);
            int orderBy = sharedPreferences.getInt(AppConstants.ORDER_BY, 0);

            //open the stream from the RSS feed
            feedLocation = new URL(feedURL);
            BufferedReader in = new BufferedReader(new InputStreamReader(feedLocation.openStream()));

            //create a new sax parser
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();

            //parse the feed using the handler that was passed into the class
            sp.parse(new InputSource(in), handler);

            //when finished, get the results from the handler into a new array list
            results = handler.getRSSResults();

            //set the result size to the value taken from shared preferences
            int resultCount = results.size();
            if (maxResults > 0 ) {
                //creates a sublist of results from the maximum value downward and clears it.
                results.subList(maxResults, resultCount).clear();
            }

            //determine how the list should be sorted
            if (orderBy > 0) {
                switch (orderBy) {
                    case 1:
                        //Order by Date
                        DateComparator<BaseRSSResult> dateComparator = new DateComparator<>();
                        Collections.sort(results, dateComparator);
                        //swap to show newest first. This could be another setting someday.
                        Collections.reverse(results);
                        break;
                    case 2:
                        //Order by title alphabetically
                        TitleComparator<BaseRSSResult> titleComparator = new TitleComparator<>();
                        Collections.sort(results, titleComparator);
                        break;
                }
            }

        } catch (MalformedURLException murlex) {
            Log.d(this.getClass().getName(), "The supplied URL is not valid " + murlex.getMessage());
        } catch (IOException iox) {
            Log.d(this.getClass().getName(), "Could not read data from the supplied URL: " + feedLocation.toString() + " " + iox.getMessage());
        } catch (ParserConfigurationException pcex) {
            Log.d(this.getClass().getName(), "Could not configure new parser. " + pcex.getMessage());
        } catch (SAXException saxex) {
            Log.d(this.getClass().getName(), "Could not create new sax parser. " + saxex.getMessage());
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "Exception Caught.");
            e.printStackTrace();
        }

        //return the results from the secondary thread
        return results;
    }

    @Override
    protected void onPostExecute(ArrayList<? extends BaseRSSResult> results) {
        super.onPostExecute(results);

        //if there is a dialog showing, dismiss it
        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        //trigger the on feed loaded event that will use the value returned by the secondary thread.
        listener.onFeedLoaded();

    }

}
