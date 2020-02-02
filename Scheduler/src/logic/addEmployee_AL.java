package logic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import persistence.employeeIFace;

public class addEmployee_AL extends JFrame implements ActionListener{

	private employeeIFace employeeIFace;
	private JTextField nameTxt;
	
	public addEmployee_AL(JTextField name) {
		this.nameTxt = name;
	}
	
	@Override
    public void actionPerformed(ActionEvent ae) {
		String action = ae.getActionCommand();
		if (action.equals("Add Employee") && validTextFormat()) {
			employeeIFace.addEmpActionListener();
        }
    }
	
	private boolean validTextFormat() {
		boolean result = true;
		//check to make sure all text fields are filled in with their proper type
		if (nameTxt.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Please make sure that employee name field is filled out.", "Error: Empty text fields", JOptionPane.INFORMATION_MESSAGE);
			result = false;
		}
		else {
			//check to make sure that first and last names are only strings
			if (!isLetter(nameTxt)) {
				JOptionPane.showMessageDialog(null, "Names can only contain letters", "Error: Invalid name format", JOptionPane.INFORMATION_MESSAGE);
				result = false;
			}
		}

		return result;
	}
	
	//function to check that the names do not contain invalid characters
	private boolean isLetter(JTextField text) {
		String name = text.getText();
		return name.matches("[a-zA-Z]+");
	}
	
}
