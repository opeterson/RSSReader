package ca.owenpeterson.rssreader.results;

import java.util.Comparator;

/**
 * Created by Owen on 2/20/2015.
 *
 * Used to compare two JodaTime DateTime objects, which is used in sorting the ArrayList by date.
 *
 * The argument passed into the class is any type that extends the BaseRSSResult class.
 * This is required so that the getPubDate method can be called.
 */
public class DateComparator<T extends BaseRSSResult> implements Comparator<T> {
    @Override
    public int compare(T leftDate, T rightDate) {
        return leftDate.getPubDate().compareTo(rightDate.getPubDate());
    }

}
