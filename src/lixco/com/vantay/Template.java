package lixco.com.vantay;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Template {
	private int maChamCong;
	private int fingerID;
	private int flag;
	private String fingerTemplate;
	private String maNhanVien;
	private String tenNhanVien;
	private String maThe;

	public Template(int maChamCong, int fingerID, int flag, String fingerTemplate) {
		super();
		this.maChamCong = maChamCong;
		this.fingerID = fingerID;
		this.flag = flag;
		this.fingerTemplate = fingerTemplate;
	}
	
	
}
