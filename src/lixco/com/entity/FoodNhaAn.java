package lixco.com.entity;

public class FoodNhaAn {
	private String employeeName;
	private  byte[] imageFood;
	private String employeeIdOld;
	private String foodName;
	
	public String getFoodName() {
		return foodName;
	}
	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public byte[] getImageFood() {
		return imageFood;
	}
	public void setImageFood(byte[] imageFood) {
		this.imageFood = imageFood;
	}
	public String getEmployeeIdOld() {
		return employeeIdOld;
	}
	public void setEmployeeIdOld(String employeeIdOld) {
		this.employeeIdOld = employeeIdOld;
	}
}	
