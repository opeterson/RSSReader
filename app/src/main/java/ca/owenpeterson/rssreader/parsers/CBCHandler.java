package ca.owenpeterson.rssreader.parsers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;

import ca.owenpeterson.rssreader.results.CBCResult;

/**
 * Created by Owen on 2/16/2015.
 *
 * This class extends the functionality of the BaseRSSHandler to add handling of an author and category element.
 *
 * For more specific comments see the BaseRSSHandler class.
 */
public class CBCHandler extends BaseRSSHandler<CBCResult> {

    private boolean parsingAuthor;
    private boolean parsingCategory;

    /**
     * Constructor that accepts a class as an argument and passes it to the superclass.
     * @param impl
     */
    public CBCHandler(Class<? extends CBCResult> impl) {
        super(impl);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        if (qName.equals("author")) {
            parsingAuthor = true;
        }

        if (qName.equals("category")) {
            parsingCategory = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        if (qName.equals("author")) {
            parsingAuthor = false;

            if (parsingItem) {
                String authorString = RSSItemBuilder.toString();
                currentResult.setAuthor(authorString);
            }
            RSSItemBuilder.setLength(0);
        }

        if (qName.equals("category")) {
            parsingCategory = false;

            if (parsingItem) {
                String categoryString = RSSItemBuilder.toString();
                currentResult.setCategory(categoryString);
            }
            RSSItemBuilder.setLength(0);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);

        //maybe make this protected in the superclass
        String chars = new String(ch, start, length);

        if (parsingItem && parsingAuthor) {
            RSSItemBuilder.append(chars);
        }

        if (parsingItem && parsingCategory) {
            RSSItemBuilder.append(chars);
        }
    }

    @Override
    public ArrayList<CBCResult> getRSSResults() {
        return super.getRSSResults();
    }
}
