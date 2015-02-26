package ca.owenpeterson.rssreader.parsers;

import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

import ca.owenpeterson.rssreader.results.BaseRSSResult;

/**
 * Created by Owen on 2/14/2015.
 *
 * Abstract class that defines a method that is required by all RSSHandler objects.
 */
public abstract class AbstractRSSHandler extends DefaultHandler {

    public abstract ArrayList<? extends BaseRSSResult> getRSSResults();

}
