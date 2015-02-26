package ca.owenpeterson.rssreader.results;

import java.util.Comparator;

/**
 * Created by Owen on 2/20/2015.
 *
 * This class is used to compare the title of an RSS Result item that extends BaseRSSResult to another
 * item of that type.
 *
 * Makes the comparison based on the title, and does so alphabetically.
 */
public class TitleComparator<T extends BaseRSSResult> implements Comparator<T> {

    @Override
    public int compare(T leftTitle, T rightTitle) {
        return leftTitle.getTitle().compareTo(rightTitle.getTitle());
    }

}
