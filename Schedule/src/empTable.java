public class empTable {
	private int id;
	private int day = 7;	//days
	private int time = 29;	//columns
	private byte[][]table;

	public empTable(int id) {
		this.id = id;
		this.table = new byte[day][time];
	}
	//returns the id 
	public int getId() {
		return this.id;
	}
	
	//return the entire table
	public byte[][] getTable() {
		return this.table;
	}

	//sets the state of availability to true or false
	public void setAvl(int d, int t, byte state) {
		if (d > day || d < 0 || t > 29 || t < 0 ) {
			System.out.println("row and or column selection for the table of employee # " + this.id + "is out of bounds.");
		}
		else {
			if (this.table[day][time] != state)
				this.table[day][time] = state;
		}
	}

	//get the state of the availability at that [][] locations
	public byte getAvl(int day, int time) {
		return this.table[day][time];
	}
}
