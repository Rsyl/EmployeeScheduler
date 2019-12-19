import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; 
import java.util.*;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

import presentation.addEmployee_Panel;
import presentation.employeeList_Panel;
import presentation.schedule_Panel;

public class Schedule extends JFrame{
	//should probably make the connection global so do not need to keep establishing connection?
	private JTabbedPane mainPane;
	private schedule_Panel schPanel;
	private addEmployee_Panel addEmpPanel;
	private employeeList_Panel listPanel;
	JButton modBtn, saveBtn, addEmpBtn, delBtn, editBtn, updateBtn;
	JLabel sch1Lbl, avail2Lbl, fNameLbl, lNameLbl, emailLbl, idLbl, phoneLbl, listLbl, hoursLbl;
	JTextArea trial, trial2;
	JTextField fNameTxt, lNameTxt, emailTxt, idTxt, hoursTxt;
	JFormattedTextField phoneTxt;
	JTable listTable;
	MaskFormatter phoneFormatter = createFormatter();
	
	DefaultTableModel model = new DefaultTableModel() { 
		//need to override the bottom function to sort based on the type of data (Int,Double,String) of the column
		//if we dont override then the default treats the sorting as strings
		@Override
		public Class<?> getColumnClass(int column) {
			Class returnValue;
			if ((column >=0) && (column <getColumnCount())) {
				returnValue = getValueAt(0,column).getClass();
			}
			else {
				returnValue = Object.class;
			}
			return returnValue;
		}
	};

	DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer(); //to adjust the cell layout of the listTable
	Object[] row = new Object[6];  //array to insert the employees data into the database and show on the jTable;
	ArrayList<Employee> list = empList();


	public Schedule execute() {
		Schedule tp = new Schedule();
		tp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tp.setTitle("GPA Schedule");
		tp.setSize(1000, 770);
		tp.setResizable(false);
		tp.setVisible(true);
		return tp;
	}

	//drawing the windows for the application
	public Schedule (){
		this.mainPane = new JTabbedPane();
		init();
	}
	
	public void init() {
		//creating panels
		schPanel = new schedule_Panel();
		addEmpPanel = new addEmployee_Panel();
		listPanel = new employeeList_Panel();

		//adding Panels to the tabbedPane
		mainPane.add("Schedule",schPanel);
		mainPane.add("Add Employee",addEmpPanel);
		mainPane.add("List",listPanel);

		//adding tabbedPane to the JFrame
		this.add(mainPane);
	}


	private class delBtnActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == delBtn) { 
				list = empList(); //updates the list everytime the button is clicked
				int i = listTable.getSelectedRow();
				if (i >= 0) {				
					model.removeRow(i); //deleting data from the jTable
					delete(list.get(i).getID());
					list = empList(); //updates the list after removing
				}
				else {
					System.out.println("Delete Error");
				}
			}
		}
	}

	private class editBtnActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == editBtn) { 
				int i = listTable.getSelectedRow();
				if (i >= 0) {
					//enable editing.
					//
				}
				else {
					System.out.println("Delete Error");
				}
			}
		}
	}

	//enables editing of employees details from the listTable
	private class listTableActionListener implements TableModelListener{
		public void tableChanged(TableModelEvent e) {
			if (e.getType() == TableModelEvent.UPDATE) {  //if the cells were changed, then execute code below
				int row = e.getFirstRow();
				int column = e.getColumn();
				Object data = model.getValueAt(row, column);
				//update the new data at column for employee row
				update(data,list.get(row).getID(),column);
			}

		}
	}

	//deleting data from database
	public void delete(int id) {
		String sql = "DELETE FROM employees WHERE id = ?";

		try (Connection conn = DriverManager.getConnection("jdbc:sqlite:/Users/sly/schedule.db" );
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			//update
			pstmt.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}


	//inserting new data into the database
	public void insert(int id,String fName, String lName, String email, long phone, int hours) {

		String sql = "INSERT INTO employees(id, fName, lName, email, phone, hours) VALUES(?,?,?,?,?,?)";

		try (Connection conn = DriverManager.getConnection("jdbc:sqlite:/Users/sly/schedule.db" );
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			pstmt.setString(2, fName);
			pstmt.setString(3, lName);
			pstmt.setString(4, email);
			pstmt.setLong(5, phone);
			pstmt.setInt(6, hours);
			//update
			pstmt.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	//importing database into an array list
	public ArrayList<Employee> empList()  {
		ArrayList<Employee> empList = new ArrayList<>();
		try {
			Class.forName("org.sqlite.JDBC");
			Connection con = DriverManager.getConnection("jdbc:sqlite:/Users/sly/schedule.db ");
			String query1 = "SELECT * FROM employees ORDER BY id ASC";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query1);

			Employee employee;
			while(rs.next()) {
				employee = new Employee(rs.getInt("id"), rs.getString("fName"), rs.getString("lName"), rs.getString("email"), rs.getLong("phone"), rs.getInt("hours"));
				empList.add(employee);
			}
			con.close();
		}
		catch (Exception e){
			JOptionPane.showMessageDialog(null, e);
		}
		return empList;
	}
	
	//updating the database when values are modified
	public void update(Object data, int id, int column) {
		String [] header = {"id","fName","lName","email","phone","hours"};
		String col = header[column];
		String query1 = "UPDATE employees SET " + col + " = ? " + "WHERE id = ?";

		try (Connection conn = DriverManager.getConnection("jdbc:sqlite:/Users/sly/schedule.db ");
				PreparedStatement pstmt = conn.prepareStatement(query1)) {

			// set the corresponding param
			if (column >=1 && column < 4) {
				String sData = (String) data;
				pstmt.setString(1, sData);
			}
			else {
				int iData = (int) data;
				pstmt.setInt(1, iData);
			}

			pstmt.setInt(2, id);
			// update 
			pstmt.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	//method to show the arraylist on the listTable
	public void show_Employee() {
		for (int i=0; i<list.size();i++) {
			row[0]=list.get(i).getID();
			row[1]=list.get(i).getFirst();
			row[2]=list.get(i).getLast();
			row[3]=list.get(i).getEmail();
			row[4]=list.get(i).getPhone();
			row[5]=list.get(i).getHours();
			model.addRow(row);
		}	
	}
	// to format the input method for the phone number
	private MaskFormatter createFormatter() {
		MaskFormatter formatter = null;
		try {
			formatter = new MaskFormatter("(###)###-####");
			formatter.setPlaceholderCharacter(' ');
			formatter.setValueContainsLiteralCharacters(false);
		} catch (java.text.ParseException exc) {
			System.err.println("formatter is bad: " + exc.getMessage());
		}
		return formatter;
	}
}
