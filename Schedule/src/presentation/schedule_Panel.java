package presentation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class schedule_Panel extends JPanel {
	private JPanel schedule_Panel;
	private JLabel schedule_Lbl;
	private JButton newSchedule_Btn, saveBtn;

	public schedule_Panel() {
		this.schedule_Panel = init();
	}

	private JPanel init() {
		schedule_Panel = new JPanel();
		schedule_Panel.setLayout(new GridBagLayout());

		GridBagConstraints gcS = new GridBagConstraints();
		gcS.gridx = 1;
		gcS.gridy = 0;
		gcS.weightx = 1;
		gcS.weighty = 1;
		gcS.gridwidth = 1;
		gcS.gridheight = 1;
		gcS.anchor = GridBagConstraints.PAGE_START;
		
		schedule_Lbl = new JLabel("Current Schedule");
		schedule_Panel.add(schedule_Lbl, gcS);
		
		newSchedule_Btn = new JButton ("New Schedule");
		gcS.gridx = 0;
		gcS.gridy = 1;
		gcS.weightx = 1;
		gcS.weighty = 1;
		gcS.gridwidth = 1;
		gcS.gridheight = 1;
		gcS.anchor = GridBagConstraints.CENTER;

		schedule_Panel.add(newSchedule_Btn,gcS);

		saveBtn = new JButton ("Save");
		gcS.gridx = 2;
		gcS.gridy = 1;
		gcS.weightx = 1;
		gcS.weighty = 1;
		gcS.gridwidth = 1;
		gcS.gridheight = 1;
		gcS.anchor = GridBagConstraints.CENTER;

		schedule_Panel.add(saveBtn,gcS);
		return schedule_Panel;
	}
}
