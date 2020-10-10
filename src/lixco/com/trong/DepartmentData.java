package lixco.com.trong;

public class DepartmentData{
	private long id;
	private String code;
	private String name;
	private String codeEmp;
	private String codeDepart;
	private int level;
	private boolean disable;
	
	
	public DepartmentData(long id,String code, String name, String codeEmp, String codeDepart, int level,boolean disable) {
		this.id=id;
		this.code = code;
		this.name = name;
		this.codeEmp = codeEmp;
		this.codeDepart = codeDepart;
		this.level = level;
		this.disable=disable;
	}
	public DepartmentData(long id,String code, String name, String codeEmp, String codeDepart, int level) {
		this.id=id;
		this.code = code;
		this.name = name;
		this.codeEmp = codeEmp;
		this.codeDepart = codeDepart;
		this.level = level;
	}
	public DepartmentData(long id,String code, String name) {
		this.id=id;
		this.code = code;
		this.name = name;
	}
	public DepartmentData() {
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCodeEmp() {
		return codeEmp;
	}
	public void setCodeEmp(String codeEmp) {
		this.codeEmp = codeEmp;
	}
	public String getCodeDepart() {
		return codeDepart;
	}
	public void setCodeDepart(String codeDepart) {
		this.codeDepart = codeDepart;
	}

	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public boolean isDisable() {
		return disable;
	}
	public void setDisable(boolean disable) {
		this.disable = disable;
	}

	

}
