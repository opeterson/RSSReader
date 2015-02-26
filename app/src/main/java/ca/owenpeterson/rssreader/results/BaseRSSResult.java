package ca.owenpeterson.rssreader.results;

import org.joda.time.DateTime;

import java.io.Serializable;

import ca.owenpeterson.rssreader.common.Detailable;

/**
 * Created by Owen on 2/13/2015.
 *
 * An object that is used to store the basic content of an RSS feed.
 */
public class BaseRSSResult implements Serializable, Detailable {
    protected String title;
    protected String description;
    protected String link; //maybe URL
    protected DateTime pubDate;
    protected String guid;

    /**
     * Constructor used to create an RSSResult item with arguments
     *
     * @param title - the title of the RSS Item
     * @param description - the description of the RSS Item
     * @param link - the link provided by the RSS item
     * @param pubDate - the publication date of the RSS Item
     * @param guid - the GUID of the RSS item.
     */
    public BaseRSSResult(String title, String description, String link, DateTime pubDate, String guid) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.pubDate = pubDate;
        this.guid = guid;
    }

    /**
     * No Arg constructor
     */
    public BaseRSSResult() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public DateTime getPubDate() {
        return pubDate;
    }

    public void setPubDate(DateTime pubDate) {
        this.pubDate = pubDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    @Override
    public String getAuthor() {
        return "";
    }

    @Override
    public String getCategory() {
        return "";
    }
}
