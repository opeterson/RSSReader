package ca.owenpeterson.rssreader.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Locale;

import ca.owenpeterson.rssreader.R;
import ca.owenpeterson.rssreader.common.AppConstants;
import ca.owenpeterson.rssreader.results.BaseRSSResult;
import ca.owenpeterson.rssreader.results.RedditResult;

/**
 * Created by Owen on 2/14/2015.
 *
 * This class is used to create the list view for Reddit RSS Results.
 * Pretty similar to the CBCRSSListViewAdapter, so see that class for comments.
 *
 */
public class RedditRSSListViewAdapter<T extends BaseRSSResult> extends ArrayAdapter<T> {

    private ArrayList<T> rssResults;

    public RedditRSSListViewAdapter(Context context, int resource, int textViewResourceId, ArrayList<T> objects) {
        super(context, resource, textViewResourceId, objects);
        this.rssResults = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (null == view) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.reddit_list_item, null);
        }

        T result = rssResults.get(position);
        RedditResult redditResult = (RedditResult) result;

        if (null != redditResult) {
            TextView titleText = (TextView) view.findViewById(R.id.reddit_title);
            TextView dateText = (TextView) view.findViewById(R.id.reddit_pubdate);

            if (null != titleText) {
                titleText.setText(redditResult.getTitle());
            }

            if (null != dateText) {
                DateTime date = redditResult.getPubDate();
                dateText.setText(date.toString(AppConstants.DISPLAY_DATE_FORMAT, Locale.CANADA));
            }
        }
        return view;
    }
}
