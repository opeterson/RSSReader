package ca.owenpeterson.rssreader.results;

import org.joda.time.DateTime;

/**
 * Created by Owen on 2/16/2015.
 */
public class CBCResult extends BaseRSSResult{

    protected String author;
    protected String category;

    public CBCResult(String title, String description, String link, DateTime pubDate, String guid, String author, String category) {
        super(title, description, link, pubDate, guid);
        this.author = author;
        this.category = category;
    }

    public CBCResult() {
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
