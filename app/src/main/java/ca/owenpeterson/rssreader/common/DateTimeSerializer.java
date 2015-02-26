package ca.owenpeterson.rssreader.common;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.lang.reflect.Type;

/**
 * Created by Owen on 2/20/2015.
 *
 * I got this class from StackOverflow so I'm not entirely certain how it works.
 * I know that it is used to serialize a JodaTime DateTime object within a GSON conversion.
 *
 */
public class DateTimeSerializer implements JsonDeserializer<DateTime>, JsonSerializer<DateTime>
{

    private static final DateTimeFormatter TIME_FORMAT = ISODateTimeFormat.timeNoMillis();

    /**
     * Converts a JSON element into a DateTime object.
     *
     * @param je
     * @param type
     * @param jdc
     * @return
     * @throws JsonParseException
     */
    @Override
    public DateTime deserialize(final JsonElement je, final Type type,
                                 final JsonDeserializationContext jdc) throws JsonParseException
    {
            final String dateAsString = je.getAsString();
            if (dateAsString.length() == 0) {
                return null;
            } else {
                return TIME_FORMAT.parseDateTime(dateAsString);
            }
        }

    /**
     * Serializes a DateTime object into a Json Element.
     *
     * @param src
     * @param typeOfSrc
     * @param context
     * @return
     */
    @Override
    public JsonElement serialize(final DateTime src, final Type typeOfSrc,
                                 final JsonSerializationContext context)
    {
        String retVal;
        if (src == null)
        {
            retVal = "";
        }
        else
        {
            retVal = TIME_FORMAT.print(src);
        }
        return new JsonPrimitive(retVal);
    }

}
