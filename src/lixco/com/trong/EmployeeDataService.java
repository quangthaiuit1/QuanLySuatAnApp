package lixco.com.trong;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class EmployeeDataService {
	public static final String NAME = "nhanvien";
	static Gson gson;
	static {
		GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
		gsonBuilder.registerTypeAdapter(Date.class, new DateJsonSerializer());
		gsonBuilder.registerTypeAdapter(Date.class, new DateJsonDeserializer());
		gson = gsonBuilder.create();
	}

	/**
	 * Tat ca nhan vien
	 * 
	 * @param method pbtatca
	 * @param param  Khong co tham so truyen vao
	 * @return danh sach phong ban
	 */
	public static EmployeeData[] tatcanhanvien() {
		try {
			String link = "?cm=tatcanhanvien&dt=";
			String data = process(link);
			DataResponseAPI ketqua = gson.fromJson(data, DataResponseAPI.class);
			EmployeeData[] employeeDatas = gson.fromJson(ketqua.getDt(), EmployeeData[].class);
			return employeeDatas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * tim nhan vien theo phong ban
	 * 
	 * @param method nvtheophongban
	 * @param param  ma phong ban (VD: 200034,200024)
	 * @return danh sach nhan vien
	 */
	public static EmployeeData[] timtheophongban(String param) {
		try {
			String link = "?cm=nvtheophongban&dt=" + param;
			String data = process(link);
			DataResponseAPI ketqua = gson.fromJson(data, DataResponseAPI.class);
			EmployeeData[] departmentDatas = gson.fromJson(ketqua.getDt(), EmployeeData[].class);
			return departmentDatas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * tim nhan vien theo phong ban
	 * 
	 * @param method nvtheophongban
	 * @param param  ma phong ban ma (VD: 200034,200024)
	 * @return danh sach nhan vien
	 */
	public static EmployeeData[] timtheodsphongban(String param) {
		try {
			String link = "?cm=nvtheophongban&dt=" + param;
			String data = process(link);
			DataResponseAPI ketqua = gson.fromJson(data, DataResponseAPI.class);
			EmployeeData[] departmentDatas = gson.fromJson(ketqua.getDt(), EmployeeData[].class);
			return departmentDatas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Tim theo chuc danh
	 * 
	 * @param method nvtheochucdanh
	 * @param param  ma chuc danh[] (VD: 000234,123413)
	 * @return danh sach nhan vien
	 */
	public static EmployeeData[] timtheochucdanh(String param) {
		try {
			String link = "?cm=nvtheochucdanh&dt=" + param;
			String data = process(link);
			DataResponseAPI ketqua = gson.fromJson(data, DataResponseAPI.class);
			EmployeeData[] employeeDatas = gson.fromJson(ketqua.getDt(), EmployeeData[].class);
			return employeeDatas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Tim nhan vien theo ma
	 * 
	 * @param method nvtheoma
	 * @param param  ma nhan vien
	 * @return Nhan vien
	 */
	public static EmployeeData timtheoma(String param) {
		try {

			String link = "?cm=nvtheoma&dt=" + param;
			String data = process(link);
			DataResponseAPI ketqua = gson.fromJson(data, DataResponseAPI.class);
			EmployeeData employeeData = gson.fromJson(ketqua.getDt(), EmployeeData.class);
			return employeeData;
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
}
