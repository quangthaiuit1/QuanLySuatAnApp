package lixco.com.entity;

import java.util.Date;

public class OrderFood {
	
	private Long id;
	private String employeeName;
	private String employeeCode;
	private String employeeId;
	private String departmentName;
	private String departmentCode;
	private String foodName;
	private  byte[] image;
	private Date food_date;
	private int category_food_id;
	private int shifts_id;
	
	public int getShifts_id() {
		return shifts_id;
	}
	public void setShifts_id(int shifts_id) {
		this.shifts_id = shifts_id;
	}
	public int getCategory_food_id() {
		return category_food_id;
	}
	public void setCategory_food_id(int category_food_id) {
		this.category_food_id = category_food_id;
	}
	public Date getFood_date() {
		return food_date;
	}
	public void setFood_date(Date food_date) {
		this.food_date = food_date;
	}
	public String getDepartmentCode() {
		return departmentCode;
	}
	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getFoodName() {
		return foodName;
	}
	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
}
