package lixco.com.vantay;

public class template {
	int MaChamCong;
	int FingerID;
	int Flag;
	String FingerTemplate;
	String MaNhanVien;
	String TenNhanVien;
	public String getMaNhanVien() {
		return MaNhanVien;
	}
	public void setMaNhanVien(String MaNhanVien) {
		this.MaNhanVien = MaNhanVien;
	}
	public String getTenNhanVien() {
		return TenNhanVien;
	}
	public void setTenNhanVien(String TenNhanVien) {
		this.TenNhanVien = TenNhanVien;
	}
	public template(int maChamCong, int fingerID, int flag, String fingerTemplate) {
		super();
		MaChamCong = maChamCong;
		FingerID = fingerID;
		Flag = flag;
		FingerTemplate = fingerTemplate;
	}
public template(int maChamCong, int fingerID, int flag, String fingerTemplate, String MaNhanVien, String TenNhanVien) {
	super();
	MaChamCong = maChamCong;
	FingerID = fingerID;
	Flag = flag;
	FingerTemplate = fingerTemplate;
	this.MaNhanVien = MaNhanVien;
	this.TenNhanVien = TenNhanVien;
}
	public int getMaChamCong() {
		return MaChamCong;
	}
	public void setMaChamCong(int maChamCong) {
		MaChamCong = maChamCong;
	}
	public int getFingerID() {
		return FingerID;
	}
	public void setFingerID(int fingerID) {
		FingerID = fingerID;
	}
	public int getFlag() {
		return Flag;
	}
	public void setFlag(int flag) {
		Flag = flag;
	}
	public String getFingerTemplate() {
		return FingerTemplate;
	}
	public void setFingerTemplate(String fingerTemplate) {
		FingerTemplate = fingerTemplate;
	}
	
}
