package presentation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import logic.addEmployee_AL;

public class addEmployee_Panel extends JPanel{
	private static final long serialVersionUID = 1L;
	private JPanel addEmp_Panel;
	private JLabel name_Lbl;
	private JTextField name_Txt;
	private JButton addEmp_Btn;
	
	public addEmployee_Panel() {
		this.addEmp_Panel = init();
	}
	
	private JPanel init() {
		addEmp_Panel = new JPanel();
		addEmp_Panel.setLayout(new GridBagLayout());

		GridBagConstraints gcA = new GridBagConstraints();
		gcA.gridx = 0;
		gcA.gridy = 0;
		gcA.weightx = 0.5;
		gcA.weighty = 1;
		gcA.gridwidth = 1;
		gcA.gridheight = 1;
		gcA.insets = new Insets(10,10,0,0);
		gcA.anchor = GridBagConstraints.FIRST_LINE_START;

		name_Lbl = new JLabel("Name");
		addEmp_Panel.add(name_Lbl,gcA);		

		name_Txt = new JTextField("",20);
		gcA.gridx = 1;
		gcA.weightx = 1;
		gcA.gridwidth = 2;
		gcA.insets = new Insets(5,5,0,0);
		addEmp_Panel.add(name_Txt, gcA);


		addEmp_Btn = new JButton("Add Employee");
		gcA.gridx = 6;
		gcA.gridy = 6;
		gcA.weightx = 1;
		gcA.anchor = GridBagConstraints.LAST_LINE_END;

		addEmployee_AL lForButton = new addEmployee_AL(name_Txt);
		addEmp_Btn.addActionListener(lForButton);
		addEmp_Btn.setActionCommand("Add Employee");

		addEmp_Panel.add(addEmp_Btn, gcA);
		
		return addEmp_Panel;
	}
	
}

