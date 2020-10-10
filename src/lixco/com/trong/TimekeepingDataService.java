package lixco.com.trong;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TimekeepingDataService {
	public static final String NAME = "timekeeping";
	static Gson gson;
	static {
		GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
		gsonBuilder.registerTypeAdapter(Date.class, new DateJsonSerializer());
		gsonBuilder.registerTypeAdapter(Date.class, new DateJsonDeserializer());
		gson = gsonBuilder.create();
	}

	/**
	 * Tim timekeeping theo date
	 * 
	 * @param method findByTimeKp
	 * @param param  date
	 * @return timekeeping
	 */
	public static TimekeepingData[] timtheongay(String param) {
		try {

			String link = "?cm=findByTimeKp&dt=" + param;
			String data = process(link);
			DataResponseAPI ketqua = gson.fromJson(data, DataResponseAPI.class);
			TimekeepingData[] employeeData = gson.fromJson(ketqua.getDt(), TimekeepingData[].class);
			return employeeData;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	public static TimekeepingData[] searchByDateAndWorkTemp(String strDate, String ca) {
		try {
			String link = "";
			if (ca.equals("8C")) {
				link = "?cm=findByTimeKpWorkShif8c&dt=" + strDate;
			}
			if (ca.equals("CD")) {
				link = "?cm=findByTimeKpWorkShifCD&dt=" + strDate;
			}
			String data = process(link);
			DataResponseAPI ketqua = gson.fromJson(data, DataResponseAPI.class);
			TimekeepingData[] employeeData = gson.fromJson(ketqua.getDt(), TimekeepingData[].class);
			return employeeData;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private static String process(String link) throws Exception {
		URL url = new URL("http://192.168.0.5:9290" + "/chamcong/api/" + NAME + link);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("GET");
		conn.setRequestProperty("token", "c894b18f-6e51-4bf3-9a3b-0a1c2d7d4211");
		conn.setRequestProperty("Content-type", "application/json");
		conn.setUseCaches(false);
		conn.setDoInput(true);
		try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
			StringBuilder response = new StringBuilder();
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			return response.toString();
		}
	}
}
