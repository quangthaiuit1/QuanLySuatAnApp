package lixco.com.trong;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DepartmentDataService {
	public static final String NAME = "phongban";
	static Gson gson;
	static {
		GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
		gsonBuilder.registerTypeAdapter(Date.class, new DateJsonSerializer());
		gsonBuilder.registerTypeAdapter(Date.class, new DateJsonDeserializer());
		gson = gsonBuilder.create();
	}

	/**
	 * Lay du lieu phong ban
	 * 
	 * @param method pbtatca
	 * @param param  Khong co tham so truyen vao
	 * @return danh sach phong ban
	 */
	public static DepartmentData[] tatcaphongban(String param) {
		try {
			String link = "?cm=pbtatca&dt=" + param;
			String data = process(link);
			DataResponseAPI ketqua = gson.fromJson(data, DataResponseAPI.class);
			DepartmentData[] departmentDatas = gson.fromJson(ketqua.getDt(), DepartmentData[].class);
			return departmentDatas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * tim phong ban theo cap
	 * 
	 * @param method pbtheocap
	 * @param param  cap phong ban
	 * @return danh sach phong ban
	 */
	public static DepartmentData[] timtheocap(String param) {
		try {
			String link = "?cm=pbtheocap&dt=" + param;
			String data = process(link);
			DataResponseAPI ketqua = gson.fromJson(data, DataResponseAPI.class);
			DepartmentData[] departmentDatas = gson.fromJson(ketqua.getDt(), DepartmentData[].class);
			return departmentDatas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Lay danh sach phong ban theo phong quan ly
	 * 
	 * @param method pbtheoquanly
	 * @param param  ma phong ban quan ly
	 * @return danh sach phong ban
	 */
	public static DepartmentData[] timtheophongquanly(String param) {
		try {
			String link = "?cm=pbtheoquanly&dt=" + param;
			String data = process(link);
			DataResponseAPI ketqua = gson.fromJson(data, DataResponseAPI.class);
			DepartmentData[] departmentDatas = gson.fromJson(ketqua.getDt(), DepartmentData[].class);
			return departmentDatas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Tim phong ban theo ma phong
	 * 
	 * @param method pbtheoma
	 * @param param  (tham so truyen vao) ma phong ban
	 * @return phong ban
	 */
	public static DepartmentData timtheoma(String param) {
		try {
			String link = "?cm=pbtheo&dt=" + param;
			String data = process(link);
			DataResponseAPI ketqua = gson.fromJson(data, DataResponseAPI.class);
			DepartmentData departmentDatas = gson.fromJson(ketqua.getDt(), DepartmentData.class);
			return departmentDatas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private static String process(String link) throws Exception {
		URL url = new URL("http://192.168.0.5" + "/api/" + NAME + link);
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

	public static void main(String[] args) {
		try {
			DepartmentData[] result = DepartmentDataService.tatcaphongban("");
			System.out.println(result.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
