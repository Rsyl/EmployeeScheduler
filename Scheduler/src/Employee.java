
public class Employee {
	private int id, hours;
	private long phone;
	private String fName, lName, email;
	//private byte[][] table = new byte[7][29];
	
	public Employee(int id, String fName, String lName, String email, long phone, int hours) {
		this.id = id;
		this.fName = fName;
		this.lName = lName;
		this.email = email;
		this.phone = phone;
		this.hours = hours;
	}
	
	public int getID() {
		return this.id;
	}
	
	public String getFirst() {
		return this.fName;
	}
	public String getLast() {
		return this.lName;
	}
	public String getEmail() {
		return this.email;
	}
	public long getPhone() {
		return this.phone;
	}
	public int getHours() {
		return this.hours;
	}
}

