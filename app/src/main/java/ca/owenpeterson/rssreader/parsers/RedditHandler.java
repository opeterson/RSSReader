package ca.owenpeterson.rssreader.parsers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;

import ca.owenpeterson.rssreader.results.RedditResult;

/**
 * Created by Owen on 2/16/2015.
 *
 * This class is used to extend the functionality of the BaseRSSHandler object specifically for a Reddit
 * RSS feed. Currently does not perform any new functions.
 *
 * Even overriding the methods is not necessary but here for an example of what might be necessary
 * when extending the class in the future.
 */
public class RedditHandler extends BaseRSSHandler<RedditResult> {

    public RedditHandler(Class<? extends RedditResult> impl) {
        super(impl);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
    }

    @Override
    public ArrayList<RedditResult> getRSSResults() {
        return super.getRSSResults();
    }
}
