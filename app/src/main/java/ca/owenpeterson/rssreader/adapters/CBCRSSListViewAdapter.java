package ca.owenpeterson.rssreader.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ca.owenpeterson.rssreader.R;
import ca.owenpeterson.rssreader.results.BaseRSSResult;
import ca.owenpeterson.rssreader.results.CBCResult;

/**
 * Created by Owen on 2/16/2015.
 *
 * This class is used to create a list view for CBC results.
 * The class takes an agrument that must extend the BaseRSSresult class. In this case it would be a
 * CBC Result item.
 */
public class CBCRSSListViewAdapter<T extends BaseRSSResult> extends ArrayAdapter<T> {

    //holds the arrayList of RSSResults that will be used for this view
    private ArrayList<T> rssResults;

    /**
     * Constructor for the adapter
     *
     * @param context -  The class that will be holding the list view.
     * @param resource - the ID of the list item resource
     * @param textViewResourceId - May not be necessary for my case.and ID for a textView.
     * @param objects - the array list of items to use for the adapter.
     */
    public CBCRSSListViewAdapter(Context context, int resource, int textViewResourceId, ArrayList<T> objects) {
        super(context, resource, textViewResourceId, objects);

        //place the objects list into a class level variable.
        this.rssResults = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        //if the view is null, inflate it
        if (null == view) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.cbc_list_item, null);
        }

        //get the current result from the RSSResults list.
         T result = rssResults.get(position);
         CBCResult cbcResult = (CBCResult) result;

        //if it is not null
        if (null != cbcResult) {
            //create the view objects and populate them
            TextView titleText = (TextView) view.findViewById(R.id.cbc_title);
            TextView authorText = (TextView) view.findViewById(R.id.cbc_author);

            if (null != titleText) {
                titleText.setText(cbcResult.getTitle());
            }

            if (null != authorText) {
                authorText.setText(cbcResult.getAuthor());
            }
        }
        return view;
    }
}
