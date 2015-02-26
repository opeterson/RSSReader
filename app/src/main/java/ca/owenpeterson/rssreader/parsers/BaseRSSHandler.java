package ca.owenpeterson.rssreader.parsers;

import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Locale;

import ca.owenpeterson.rssreader.results.BaseRSSResult;

/**
 * Created by Owen on 2/13/2015.
 *
 * This class acts as a base for other RSSHandler types. It contains the common functionality required for an RSSHandler.
 * To create and customize a different handler, this class can be extended.
 *
 * The class accepts an RSSResult type that extends BaseRSSResult as a parameter.
 */
public class BaseRSSHandler<T extends BaseRSSResult> extends AbstractRSSHandler {

    protected boolean parsingTitle;
    protected boolean parsingItem;
    protected boolean parsingLink;
    protected boolean parsingPubDate;
    protected boolean parsingDescription;
    protected boolean parsingGuid;
    protected T currentResult;
    private ArrayList<T> rssResults = new ArrayList<>();
    private Constructor<? extends T> constructor = null;
    private String className = this.getClass().getName();
    protected StringBuilder RSSItemBuilder = new StringBuilder();

    //this is an array of DateTimeFormats that can be used to parse a Date.
    private DateTimeParser[] parsers = {
            DateTimeFormat.forPattern("E, d MMM yyyy HH:mm:ss zzz").withLocale(Locale.CANADA).getParser(),
            DateTimeFormat.forPattern("E, d MMM yyyy HH:mm:ss Z").withLocale(Locale.CANADA).getParser()};
    private DateTimeFormatter dateFormatter = new DateTimeFormatterBuilder().append(null, parsers).toFormatter();

    /**
     * Constructor for the RSSHandler. It accepts a class as a parameter, which is required to instantiate
     * and object of that type.
     * @param impl
     */
    public BaseRSSHandler(Class<? extends T> impl) {
        try {
            //get the constructor of the class that was passed in and set it as an variable in this class.
            this.constructor = impl.getConstructor();
        } catch (NoSuchMethodException nsmex) {
            Log.e(this.getClass().getName(), "Current type argument does not contain a constructor!");
        }
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    /**
     * Takes specific actions depending on the name of the element that is being parsed.
     *
     * @param uri
     * @param localName
     * @param qName
     * @param attributes
     * @throws SAXException
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        //if parsing an item, create a new rssResult object of the correct type, using the constructor of the class
        //that was passed in as an argument.
        if (qName.equals("item")) {
            //set a boolean indicating that an item is being parsed.
            parsingItem = true;
            try {
                //instantiates the correct type, matching the class that was passed in as an argument.
                currentResult = constructor.newInstance();
            } catch (IllegalAccessException iaex) {
                Log.e(className, iaex.getMessage());
            } catch (InstantiationException iex ) {
                Log.e(className, iex.getMessage());
            } catch (InvocationTargetException itex) {
                Log.e(className, itex.getMessage());
            }
        }

        //set booleans indicating that an item of that type is being parsed.
        if (qName.equals("title")) {
            parsingTitle = true;
        }

        if (qName.equals("link")) {
            parsingLink = true;
        }

        if (qName.equals("pubDate")) {
            parsingPubDate = true;
        }

        if (qName.equals("description")) {
            parsingDescription = true;

        }

        if (qName.equals("guid")) {
            parsingGuid = true;
        }
    }

    /**
     * Takes specific ations depending on the name of the element being parsed.
     *
     * @param uri
     * @param localName
     * @param qName
     * @throws SAXException
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        //if finished parsing an item, set the boolean and add the result object to the list of
        //rss results.
        if (qName.equals("item")) {
            parsingItem = false;
            rssResults.add(currentResult);
        }

        //if finished parsing a title
        if (qName.equals("title")) {
            parsingTitle = false;

            //and currently parsing an item
            if (parsingItem) {
                //get the value of the RSSItemBuilder stringbuilder object
                String titleString = RSSItemBuilder.toString();

                //set the current title to the extracted value
                currentResult.setTitle(titleString);
            }

            //set the length of the stringbuilder to zero so another item can start fresh.
            RSSItemBuilder.setLength(0);
        }

        //same idea as title
        if (qName.equals("link")) {
            parsingLink = false;

            if (parsingItem) {
                String linkString = RSSItemBuilder.toString();
                currentResult.setLink(linkString);
            } //else add to description object
            RSSItemBuilder.setLength(0);
        }

        if (qName.equals("pubDate")) {
            parsingPubDate = false;

            if (parsingItem) {
                String dateString = RSSItemBuilder.toString();
                DateTime date = dateFormatter.parseDateTime(dateString);
                currentResult.setPubDate(date);
            }
            RSSItemBuilder.setLength(0);
        }

        if (qName.equals("description")) {
            parsingDescription = false;

            if (parsingItem) {
                String descriptionString = RSSItemBuilder.toString();
                currentResult.setDescription(descriptionString);
            }
            RSSItemBuilder.setLength(0);
        }

        if (qName.equals("guid")) {
            parsingGuid = false;

            if (parsingItem) {
                String guidString = RSSItemBuilder.toString();
                currentResult.setDescription(guidString);
            }
            RSSItemBuilder.setLength(0);
        }
    }

    /**
     * Extracts data from the element that is currently being parsed.
     *
     * @param ch
     * @param start
     * @param length
     * @throws SAXException
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);

        //create a string that contains the data in the element. This may not always be complete,
        //so a StringBuilder object is used.
        String chars = new String(ch, start, length);

        //appends the characters to the StringBuilder if currently processing one of the required
        //elements.
        if (parsingItem && parsingTitle) {
            RSSItemBuilder.append(chars);
        }

        if (parsingItem && parsingLink) {
            RSSItemBuilder.append(chars);
        }

        if (parsingItem && parsingPubDate) {
            RSSItemBuilder.append(chars);
        }

        if (parsingItem && parsingDescription) {
            RSSItemBuilder.append(chars);
        }
    }

    //gets the finished RSSResults list from the handler. This also allows any subclasses to have this
    //same method.
    @Override
    public ArrayList<T> getRSSResults() {
        return rssResults;
    }
}
