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

public class Schedule extends JFrame{
	//should probably make the connection global so do not need to keep establishing connection?
	JTabbedPane mainPane;
	JPanel addPanel, listPanel, schPanel;
	JButton modBtn, exportBtn, addEmpBtn, delBtn, editBtn, updateBtn;
	JLabel sch1Lbl, avail2Lbl, fNameLbl, lNameLbl, emailLbl, idLbl, phoneLbl, listLbl, hoursLbl;
	JTextArea trial, trial2;
	JTextField fNameTxt, lNameTxt, emailTxt, idTxt, hoursTxt;
	JFormattedTextField phoneTxt;
	JTable listTable;
	MaskFormatter phoneFormatter = createFormatter();
	
	DefaultTableModel model = new DefaultTableModel() { 
		//need to override the bottom function to sort base of the type of data (Int,Double,String) of the column
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


	public static void main(String[] args) {
		Schedule tp = new Schedule();
		tp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tp.setTitle("GPA Schedule");
		tp.setSize(1000, 770);
		tp.setResizable(false);
		tp.setVisible(true);
	}

	//drawing the windows for the application
	public Schedule (){
		mainPane = new JTabbedPane();
		//scheduled panel components
		schPanel = new JPanel();
		schPanel.setLayout(new GridBagLayout());

		GridBagConstraints gcS = new GridBagConstraints();
		gcS.gridx = 0;
		gcS.gridy = 0;
		gcS.weightx = 1;
		gcS.weighty = 1;
		gcS.gridwidth = 1;
		gcS.gridheight = 1;
		gcS.anchor = GridBagConstraints.PAGE_START;

		sch1Lbl = new JLabel("Current Schedule");
		schPanel.add(sch1Lbl, gcS);

		trial = new JTextArea("This spot is used to fill in the JTABLE for the schedule",30,60);
		gcS.gridx = 0;
		gcS.gridy = 1;
		gcS.weightx = 5;
		gcS.weighty = 10;
		gcS.gridwidth = 5;
		gcS.gridheight = 5;
		gcS.insets = new Insets(0,0,0,0);

		schPanel.add(trial, gcS);

		modBtn = new JButton ("Modify");
		gcS.gridx = 0;
		gcS.gridy = 6;
		gcS.weightx = 1;
		gcS.weighty = 1;
		gcS.gridwidth = 1;
		gcS.gridheight = 1;
		gcS.anchor = GridBagConstraints.LAST_LINE_START;

		schPanel.add(modBtn,gcS);

		exportBtn = new JButton("Export");
		gcS.anchor = GridBagConstraints.LAST_LINE_END;

		schPanel.add(exportBtn,gcS);

		//addPanel components
		addPanel = new JPanel();
		addPanel.setLayout(new GridBagLayout());

		GridBagConstraints gcA = new GridBagConstraints();
		gcA.gridx = 0;
		gcA.gridy = 0;
		gcA.weightx = 0.5;
		gcA.weighty = 1;
		gcA.gridwidth = 1;
		gcA.gridheight = 1;
		gcA.insets = new Insets(10,10,0,0);
		gcA.anchor = GridBagConstraints.FIRST_LINE_START;

		fNameLbl = new JLabel("First Name");
		addPanel.add(fNameLbl,gcA);		

		fNameTxt = new JTextField("",20);
		gcA.gridx = 1;
		gcA.weightx = 1;
		gcA.gridwidth = 2;
		gcA.insets = new Insets(5,5,0,0);
		addPanel.add(fNameTxt, gcA);

		lNameLbl = new JLabel("Last Name");
		gcA.gridx = 4;
		gcA.weightx = 0.5;
		gcA.gridwidth = 1;
		gcA.insets = new Insets(10,10,0,0);
		addPanel.add(lNameLbl, gcA);		

		lNameTxt = new JTextField("",20);
		gcA.gridx = 5;
		gcA.weightx = 1;
		gcA.gridwidth = 2;
		gcA.insets = new Insets(5,5,0,0);
		addPanel.add(lNameTxt, gcA);

		emailLbl = new JLabel("Email");
		gcA.gridx = 0;
		gcA.gridy = 1;
		gcA.weightx = 0.5;
		gcA.gridwidth = 1;
		gcA.insets = new Insets(10,10,0,0);
		addPanel.add(emailLbl, gcA);

		emailTxt = new JTextField("",30);
		gcA.gridx = 1;
		gcA.weightx = 1;
		gcA.gridwidth = 6;
		gcA.insets = new Insets(5,5,0,0);
		addPanel.add(emailTxt, gcA);

		phoneLbl = new JLabel("Phone #");
		gcA.gridx = 4;
		gcA.weightx = 0.5;
		gcA.gridwidth = 1;
		gcA.insets = new Insets(10,10,0,0);
		addPanel.add(phoneLbl, gcA);

		phoneTxt = new JFormattedTextField(phoneFormatter);
		phoneTxt.setColumns(10);
		gcA.gridx = 5;
		gcA.weightx = 1;
		gcA.gridwidth = 1;
		gcA.insets = new Insets(5,5,0,0);
		addPanel.add(phoneTxt, gcA);

		idLbl = new JLabel("ID");
		gcA.gridx = 0;
		gcA.gridy = 2;
		gcA.weightx = 0.5;
		gcA.gridwidth = 1;
		gcA.insets = new Insets(10,10,0,0);
		addPanel.add(idLbl, gcA);

		idTxt = new JTextField("",5);
		gcA.gridx = 1;
		gcA.weightx = 1;
		gcA.gridwidth = 1;
		gcA.insets = new Insets(5,5,0,0);
		addPanel.add(idTxt, gcA);

		hoursLbl = new JLabel("Hours");
		gcA.gridx = 4;
		gcA.weightx = 0.5;
		gcA.gridwidth = 1;
		gcA.insets = new Insets(10,10,0,0);
		addPanel.add(hoursLbl, gcA);

		hoursTxt = new JTextField("",5);
		gcA.gridx = 5;
		gcA.weightx = 1;
		gcA.gridwidth = 2;
		gcA.insets = new Insets(5,5,0,0);
		addPanel.add(hoursTxt, gcA);

		avail2Lbl = new JLabel("Availability");
		gcA.gridx = 3;
		gcA.gridy = 4;
		gcA.weightx = 0.5;
		gcA.gridwidth = 1;
		gcA.anchor = GridBagConstraints.PAGE_START;
		addPanel.add(avail2Lbl, gcA);

		addEmpBtn = new JButton("Add Employee");
		gcA.gridx = 6;
		gcA.gridy = 6;
		gcA.weightx = 1;
		gcA.anchor = GridBagConstraints.LAST_LINE_END;

		addEmpActionListener lForButton = new addEmpActionListener();
		addEmpBtn.addActionListener(lForButton);

		addPanel.add(addEmpBtn, gcA);


		trial2 = new JTextArea("Space for Availability Table",20,60);
		gcA.gridx = 0;
		gcA.gridy = 5;
		gcA.weighty = 30;
		gcA.gridwidth = 10;
		gcA.gridheight = 13;
		gcA.anchor = GridBagConstraints.PAGE_START;
		addPanel.add(trial2, gcA);

		//add components on the availability panel;
		listPanel = new JPanel();
		listPanel.setLayout(new GridBagLayout());

		listLbl = new JLabel("List of Employees");
		GridBagConstraints gcL = new GridBagConstraints();
		gcL.gridx = 0;
		gcL.gridy = 0;
		gcL.weightx = 1;
		gcL.weighty = 1;
		gcL.anchor = GridBagConstraints.PAGE_START;
		listPanel.add(listLbl, gcL);

		//listTable customizations
		Object[] columnNames = {"ID","First Name","Last Name","Email","Phone","Hours"};
		model.setColumnIdentifiers(columnNames);
		listTable = new JTable() {
			public boolean isCellEditable(int row,int column) {
				if (column > 0) {
					return true;
				}
				else return false;
			}
		};
		listTable.setModel(model); //setting the table to the custom defaultTableModel();
		show_Employee();
		//imports the layout from model which is the default table model
		leftRenderer.setHorizontalAlignment(JLabel.LEFT); //changes default settings to align all values in the column to the left
		listTable.setAutoCreateRowSorter(true);  		//enable automatic sorting based on the data type of the column
		listTable.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);  //changes the ID column settings
		listTable.getColumnModel().getColumn(4).setCellRenderer(leftRenderer);  //change the phone column settings
		listTable.getColumnModel().getColumn(5).setCellRenderer(leftRenderer);  //change the hours column settings
		listTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		listTable.getColumnModel().getColumn(0).setPreferredWidth(50);
		listTable.getColumnModel().getColumn(1).setPreferredWidth(180);
		listTable.getColumnModel().getColumn(2).setPreferredWidth(180);
		listTable.getColumnModel().getColumn(3).setPreferredWidth(350);
		listTable.getColumnModel().getColumn(4).setPreferredWidth(150);
		listTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

		listTableActionListener lForTable = new listTableActionListener();
		listTable.getModel().addTableModelListener(lForTable);


		//adds the  scroll bar to the listTable
		JScrollPane pane = new JScrollPane(listTable);  
		gcL.gridy = 1;
		gcL.fill = GridBagConstraints.HORIZONTAL;
		listPanel.add(pane, gcL);

		delBtn = new JButton("Delete");
		gcL.gridy = 2;
		gcL.fill = GridBagConstraints.NONE;
		gcL.anchor = GridBagConstraints.LAST_LINE_START;
		delBtnActionListener lForDelBtn = new delBtnActionListener();
		delBtn.addActionListener(lForDelBtn);
		listPanel.add(delBtn, gcL);

		editBtn = new JButton("Edit");
		gcL.anchor = GridBagConstraints.PAGE_END;
		editBtnActionListener lForEdtBtn = new editBtnActionListener();
		editBtn.addActionListener(lForEdtBtn);
		listPanel.add(editBtn, gcL);

		updateBtn = new JButton ("Update");
		gcL.anchor = GridBagConstraints.LAST_LINE_END;
		listPanel.add(updateBtn, gcL);


		//adding Panels to the tabbedPane
		mainPane.add("Schedule",schPanel);
		mainPane.add("Add Employee",addPanel);
		mainPane.add("List",listPanel);

		//adding tabbedPane to the JFrame
		this.add(mainPane);
	}

	private class addEmpActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == addEmpBtn) {
				if (valid()) {
					row[0] = idTxt.getText();
					row[1] = fNameTxt.getText();
					row[2] = lNameTxt.getText();
					row[3] = emailTxt.getText();
					row[4] = phoneTxt.getValue();
					row[5] = hoursTxt.getText();

					model.addRow(row);
					insert(Integer.valueOf((String)row[0]),(String)row[1],(String)row[2],(String)row[3],Long.valueOf((String)row[4]),Integer.valueOf((String)row[5]));

					//reseting the text field
					idTxt.setText("");
					fNameTxt.setText("");
					lNameTxt.setText("");
					emailTxt.setText("");
					phoneTxt.setValue("");
					hoursTxt.setText("");
				}

			}
			list = empList();
		}
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
	
	//checks that the inputs are valid for employee fields
	public boolean valid() {
		boolean result = true;
		//check to make sure all text fields are filled in with their proper type
		if (idTxt.getText().isEmpty() || fNameTxt.getText().isEmpty() || lNameTxt.getText().isEmpty() || emailTxt.getText().isEmpty() || phoneTxt.getValue()==null || hoursTxt.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Please make sure that all fields are filled out.", "Error: Empty text fields", JOptionPane.INFORMATION_MESSAGE);
			result = false;
		}
		else {
			//check to make sure the idTxt,phoneTxt and hoursTxt are integers
			try {
				int id = Integer.valueOf((String)idTxt.getText());
				//detect if the same id has been used before
				for (int i = 0; i < list.size(); i++) {
					if (id == list.get(i).getID()) {
						JOptionPane.showMessageDialog(null, "This ID has been assigned to another employee. Please use a different ID number.", "Error: Duplicate ID", JOptionPane.INFORMATION_MESSAGE);
						result = false;
					}	
				}
				long phone = Long.valueOf((String)phoneTxt.getValue()); //temp data to test parsing
				int hours = Integer.valueOf((String)hoursTxt.getText()); //temp data to test parsin

			} catch (NumberFormatException nfe) {
				JOptionPane.showMessageDialog(null, "Invaid entry. ID, Phone and Hours should be numbers.", "Error: Invalid Entry(non-integers)", JOptionPane.INFORMATION_MESSAGE);
				System.out.println("NumberFormatException: " + nfe.getMessage());
				result = false;
			}	
			//also check to make sure that first and last names are only strings
			if (!isAlpha(fNameTxt) || !isAlpha(lNameTxt) ) {
				JOptionPane.showMessageDialog(null, "First and Last names can only contain letters", "Error: Invalid First or Last name", JOptionPane.INFORMATION_MESSAGE);
				result = false;
			}
		}

		return result;
	}

	//function to check that the names do not contain invalid characters
	public boolean isAlpha(JTextField text) {
		String name = (String)text.getText();
		return name.matches("[a-zA-Z]+");
	}
}
