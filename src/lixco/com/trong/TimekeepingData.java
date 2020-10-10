package lixco.com.trong;
public class TimekeepingData {

	private long id;
	private String code;
	private String codeOld;
	private boolean workShift;
	private String workTemp;

	public TimekeepingData() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCodeOld() {
		return codeOld;
	}

	public void setCodeOld(String codeOld) {
		this.codeOld = codeOld;
	}

	public boolean isWorkShift() {
		return workShift;
	}

	public void setWorkShift(boolean workShift) {
		this.workShift = workShift;
	}

	public String getWorkTemp() {
		return workTemp;
	}

	public void setWorkTemp(String workTemp) {
		this.workTemp = workTemp;
	}
}
