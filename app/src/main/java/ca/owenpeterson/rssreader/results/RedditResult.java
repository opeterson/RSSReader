package ca.owenpeterson.rssreader.results;

import org.joda.time.DateTime;

/**
 * Created by Owen on 2/14/2015.
 *
 * This class is used to extend the BaseRSSResult if there is any new functionality required
 * in the future. Currently does not offer anything more than the BaseRSSResult class.
 */
public class RedditResult extends BaseRSSResult {

    /**
     * Constructor used to create a new reddit result.
     *
     * @param title
     * @param description
     * @param link
     * @param pubDate
     * @param guid
     */
    public RedditResult(String title, String description, String link, DateTime pubDate, String guid) {
        super(title, description, link, pubDate, guid);
    }

    /**
     * No argument constructor.
     */
    public RedditResult() {
    }

    //these methods are required for using the Detailable interface.
    @Override
    public String getAuthor() {
        return "";
    }

    @Override
    public String getCategory() {
        return "";
    }
}
