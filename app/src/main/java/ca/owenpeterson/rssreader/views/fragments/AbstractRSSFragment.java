package ca.owenpeterson.rssreader.views.fragments;

import android.support.v4.app.Fragment;

/**
 * Created by Owen on 2/18/2015.
 *
 * Abstract class for an RSSFragment that forces the use of a refresh method.
 *
 * May be better as an interface.
 */
public abstract class AbstractRSSFragment extends Fragment {

    public abstract void refresh();
}
