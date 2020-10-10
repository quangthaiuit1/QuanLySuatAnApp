package lixco.com.trong;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DateJsonSerializer implements JsonSerializer<Date> {
	static SimpleDateFormat formatter;
	static {
		formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	}

	@Override
	public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
		try {
			return new JsonPrimitive(formatter.format(src));
		} catch (Exception e) {
			return null;
		}
	}
}
