package lixco.com.trong;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class DateJsonDeserializer implements JsonDeserializer<Date> {
    static SimpleDateFormat formatter;
    static{
        formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }
    @Override
    public Date deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
        String date = element.getAsString();
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }


}
