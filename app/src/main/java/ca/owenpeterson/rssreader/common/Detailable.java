package ca.owenpeterson.rssreader.common;

import org.joda.time.DateTime;

/**
 * Created by Owen on 2/24/2015.
 *
 * Interface used to define the contract for an item that is going to be displayed in the DetailActivity.
 *
 * Adding this coment to test the Git Integration.
 */
public interface Detailable {

    public String getTitle();

    public String getDescription();

    public String getLink();

    public DateTime getPubDate();

    public String getGuid();

    public String getAuthor();

    public String getCategory();

}
