package ca.owenpeterson.rssreader.feeders;

/**
 * Created by Owen on 2/18/2015.
 *
 * This interface is used to create a callback for fragments that need to parse an RSS feed.
 * It allows the handler class to trigger the onFeedLoaded event of the fragment that displays the
 * results in a list.
 */
public interface OnFeedLoaded {
    public void onFeedLoaded();
}
