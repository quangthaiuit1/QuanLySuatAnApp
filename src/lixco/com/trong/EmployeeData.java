package lixco.com.trong;
import java.util.Date;


public class EmployeeData {

	private long id;
	private String code;
	private String codeOld;
	private String name;
	private String nameDepart;
	private String codeDepart;
	private String email;
	private boolean layOff;

	private Date timeFixed;
	private boolean workShift = false;// di ca

	private Date dayAtWork;
	private int numberDayOff;
	private String cateJobCode;
	private String madv;
	private Date dateEndContract;//ngày kết thúc hợp đồng

	public EmployeeData(long id, String code, String name, String nameDepart) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.nameDepart = nameDepart;
	}
	
	public EmployeeData(long id, String code, String name, String nameDepart, String madv, Date dateEndContract) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.nameDepart = nameDepart;
		this.madv = madv;
		this.dateEndContract = dateEndContract;
	}

	public EmployeeData() {
	}

	public EmployeeData(long id, String code, String codeOld, String name, String codeDepart, String nameDepart) {
		this.id = id;
		this.code = code;
		this.codeOld = codeOld;
		this.name = name;
		this.codeDepart = codeDepart;
		this.nameDepart = nameDepart;
	}

	public EmployeeData(String code) {
		this.code = code;
	}

	public EmployeeData(long id, String code, String name, String nameDepart, String codeDepart, String email,
			boolean layOff) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.nameDepart = nameDepart;
		this.codeDepart = codeDepart;
		this.email = email;
		this.layOff = layOff;
	}

	public EmployeeData(long id, String code, String codeOld, String name, String nameDepart, String codeDepart,
			String email, boolean layOff, Date timeFixed, boolean workShift) {
		this.id = id;
		this.code = code;
		this.codeOld = codeOld;
		this.name = name;
		this.nameDepart = nameDepart;
		this.codeDepart = codeDepart;
		this.email = email;
		this.layOff = layOff;
		this.timeFixed = timeFixed;
		this.workShift = workShift;
	}

	public EmployeeData(long id, String code, String codeOld, String name, String nameDepart, String codeDepart,
			String email, boolean layOff, Date timeFixed, boolean workShift, Date dayAtWork, int numberDayOff,String cateJobCode) {
		this.id = id;
		this.code = code;
		this.codeOld = codeOld;
		this.name = name;
		this.nameDepart = nameDepart;
		this.codeDepart = codeDepart;
		this.email = email;
		this.layOff = layOff;
		this.timeFixed = timeFixed;
		this.workShift = workShift;
		this.dayAtWork = dayAtWork;
		this.numberDayOff = numberDayOff;
		this.cateJobCode=cateJobCode;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameDepart() {
		return nameDepart;
	}

	public void setNameDepart(String nameDepart) {
		this.nameDepart = nameDepart;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isLayOff() {
		return layOff;
	}

	public void setLayOff(boolean layOff) {
		this.layOff = layOff;
	}

	public String getCodeDepart() {
		return codeDepart;
	}

	public void setCodeDepart(String codeDepart) {
		this.codeDepart = codeDepart;
	}

	public String getCodeOld() {
		return codeOld;
	}

	public void setCodeOld(String codeOld) {
		this.codeOld = codeOld;
	}

	public Date getTimeFixed() {
		return timeFixed;
	}

	public void setTimeFixed(Date timeFixed) {
		this.timeFixed = timeFixed;
	}

	public boolean isWorkShift() {
		return workShift;
	}

	public void setWorkShift(boolean workShift) {
		this.workShift = workShift;
	}

	public Date getDayAtWork() {
		return dayAtWork;
	}

	public void setDayAtWork(Date dayAtWork) {
		this.dayAtWork = dayAtWork;
	}

	public int getNumberDayOff() {
		return numberDayOff;
	}

	public void setNumberDayOff(int numberDayOff) {
		this.numberDayOff = numberDayOff;
	}

	public String getCateJobCode() {
		return cateJobCode;
	}

	public void setCateJobCode(String cateJobCode) {
		this.cateJobCode = cateJobCode;
	}

	public String getMadv() {
		return madv;
	}

	public void setMadv(String madv) {
		this.madv = madv;
	}

	public Date getDateEndContract() {
		return dateEndContract;
	}

	public void setDateEndContract(Date dateEndContract) {
		this.dateEndContract = dateEndContract;
	}

}
