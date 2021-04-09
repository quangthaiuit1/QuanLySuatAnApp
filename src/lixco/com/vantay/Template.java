package lixco.com.vantay;

public class Template {
	private int maChamCong;
	private int fingerID;
	private int flag;
	private String fingerTemplate;
	private String maNhanVien;
	private String tenNhanVien;
	private String maThe;

	public Template() {
		super();
	}

	public Template(int maChamCong, int fingerID, int flag, String fingerTemplate) {
		super();
		this.maChamCong = maChamCong;
		this.fingerID = fingerID;
		this.flag = flag;
		this.fingerTemplate = fingerTemplate;
	}

	public Template(int maChamCong, int fingerID, int flag, String fingerTemplate, String maNhanVien,
			String tenNhanVien, String maThe) {
		super();
		this.maChamCong = maChamCong;
		this.fingerID = fingerID;
		this.flag = flag;
		this.fingerTemplate = fingerTemplate;
		this.maNhanVien = maNhanVien;
		this.tenNhanVien = tenNhanVien;
		this.maThe = maThe;
	}

	public int getMaChamCong() {
		return maChamCong;
	}

	public void setMaChamCong(int maChamCong) {
		this.maChamCong = maChamCong;
	}

	public int getFingerID() {
		return fingerID;
	}

	public void setFingerID(int fingerID) {
		this.fingerID = fingerID;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getFingerTemplate() {
		return fingerTemplate;
	}

	public void setFingerTemplate(String fingerTemplate) {
		this.fingerTemplate = fingerTemplate;
	}

	public String getMaNhanVien() {
		return maNhanVien;
	}

	public void setMaNhanVien(String maNhanVien) {
		this.maNhanVien = maNhanVien;
	}

	public String getTenNhanVien() {
		return tenNhanVien;
	}

	public void setTenNhanVien(String tenNhanVien) {
		this.tenNhanVien = tenNhanVien;
	}

	public String getMaThe() {
		return maThe;
	}

	public void setMaThe(String maThe) {
		this.maThe = maThe;
	}
}
